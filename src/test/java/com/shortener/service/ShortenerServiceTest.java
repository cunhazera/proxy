package com.shortener.service;

import com.shortener.entity.URLEntity;
import com.shortener.exception.ExpiredCodeException;
import com.shortener.exception.ShortedURLNotFoundException;
import com.shortener.repository.ShortenerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ShortenerServiceTest {

    @InjectMocks
    private ShortenerService service;

    @Mock
    private ShortenerRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ShortedURLNotFoundException.class)
    public void testShortedCodeNotFoundThrowsException() throws IOException {
        String code = "code";
        Mockito.when(repository.findByShorted(code)).thenReturn(null);
        service.redirect(code);
    }

    @Test(expected = ExpiredCodeException.class)
    public void testExpiredCodeThrowsException() throws IOException {
        URLEntity entity = new URLEntity();
        entity.setExpirationDate(lastYear(new Date()));
        Mockito.when(repository.findByShorted(entity.getShorted())).thenReturn(entity);
        service.redirect(entity.getShorted());
    }

    private Date lastYear(Date creationDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(creationDate);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

}
