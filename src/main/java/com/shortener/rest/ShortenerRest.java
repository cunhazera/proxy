package com.shortener.rest;

import com.shortener.entity.URLEntity;
import com.shortener.service.ShortenerService;
import com.shortener.vo.RestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping
public class ShortenerRest {

    @Autowired
    private ShortenerService service;

    @GetMapping(value = "short", produces = "application/json")
    public ResponseEntity shortURL(@RequestParam("url") String url) {
        URLEntity urlEntity = service.shortURL(url);
        return new ResponseEntity(new RestVO(urlEntity.getNewUrl(), urlEntity.getExpirationDate().getTime()), HttpStatus.OK);
    }

    @GetMapping(value = "/{shorted}", produces = "application/json")
    public void redirectToShortedURL(@PathVariable String shorted, HttpServletResponse httpServletResponse) throws IOException {
        service.redirect(shorted, httpServletResponse);
    }

}
