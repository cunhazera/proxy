package com.shortener.rest;

import com.shortener.entity.URLEntity;
import com.shortener.service.ShortenerService;
import com.shortener.vo.RestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping
public class ShortenerRest {

    @Autowired
    private ShortenerService service;

    @PostMapping(value = "short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity shortURL(@RequestBody String url) {
        URLEntity urlEntity = service.shortURL(url);
        return new ResponseEntity(new RestVO(urlEntity.getNewUrl(), urlEntity.getExpirationDate().getTime()), HttpStatus.OK);
    }

    @GetMapping(value = "/{shorted}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView redirectToShortedURL(@PathVariable String shorted) throws IOException {
        return service.redirect(shorted);
    }

}
