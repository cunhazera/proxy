package com.shortener.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.app.AppApplication;
import com.shortener.entity.URLEntity;
import com.shortener.exception.ErrorDetails;
import com.shortener.repository.ShortenerRepository;
import com.shortener.vo.RestVO;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ShortenerRestIT {

    public static final String URL_TEST = "https://www.google.com/search?q=teste&oq=teste&aqs=chrome..69i57j69i60j0l4.1808j0j1&sourceid=chrome&ie=UTF-8";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortenerRepository repository;

    @Test
    public void shortURL() throws Exception {
        RequestBuilder requestBuilder = buildGetRequest(URL_TEST);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        RestVO shortedResult = new ObjectMapper().readValue(response.getContentAsString(), RestVO.class);
        LocalDate nextYear = oneYearLater(new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate expirationDate = Instant.ofEpochMilli(shortedResult.getExpiresAt()).atZone(ZoneId.systemDefault()).toLocalDate();
        assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
        assertThat(shortedResult.getNewUrl(), Matchers.notNullValue());
        assertThat(nextYear.getYear(), equalTo(expirationDate.getYear()));
        assertThat(nextYear.getDayOfYear(), equalTo(expirationDate.getDayOfYear()));
        assertThat(nextYear.getMonth(), equalTo(expirationDate.getMonth()));
    }

    @Test
    public void testRedicrectShortedCode() throws Exception {
        URLEntity url = new URLEntity();
        url.setUrl(URL_TEST);
        url.setCreationDate(new Date());
        url.setExpirationDate(oneYearLater(new Date()));
        url.setShorted("shorted");
        repository.save(url);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/" + url.getShorted())).andReturn().getResponse();
        assertThat(response.getStatus(), equalTo(HttpStatus.FOUND.value()));
        assertThat(response.getHeader("Location"), equalTo(URL_TEST));
    }

    @Test
    public void testRedicrectExpiredShortedCode() throws Exception {
        URLEntity url = new URLEntity();
        url.setUrl(URL_TEST);
        url.setCreationDate(new Date(1500002194000l));
        url.setExpirationDate(new Date(1534216594000l));
        url.setShorted("shorted");
        repository.save(url);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/" + url.getShorted())).andReturn().getResponse();
        ErrorDetails errorDetails = new ObjectMapper().readValue(response.getContentAsString(), ErrorDetails.class);
        assertThat(errorDetails.getMessage(), equalTo(String.format("The code %s is expired", url.getShorted())));
        assertThat(response.getStatus(), equalTo(HttpStatus.GONE.value()));
    }

    @Test
    public void testRedicrectCodeNotFound() throws Exception {
        String inexistentCode = "CodeDoestExist";
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/" + inexistentCode)).andReturn().getResponse();
        ErrorDetails errorDetails = new ObjectMapper().readValue(response.getContentAsString(), ErrorDetails.class);
        assertThat(errorDetails.getMessage(), equalTo(String.format("The code %s doesn't exist", inexistentCode)));
        assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND.value()));
    }

    private MockHttpServletRequestBuilder buildGetRequest(String url) {
        return MockMvcRequestBuilders
                .post("/short")
                .content(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private Date oneYearLater(Date creationDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(creationDate);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

}
