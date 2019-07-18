package com.shortener.service;

import com.shortener.entity.URLEntity;
import com.shortener.exception.ExpiredCodeException;
import com.shortener.exception.ShortedURLNotFoundException;
import com.shortener.repository.ShortenerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class ShortenerService {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private ShortenerRepository repository;

    private static final int MIN = 5;

    @Transactional(propagation = Propagation.REQUIRED)
    public URLEntity shortURL(String url) {
        int[] array = {2, 3, 4};
        int pos = new Random().nextInt(array.length);
        int randomValue = array[pos];
        int max = 32;
        if (url.length() < max) {
            max = url.length();
        }
        if (max >= url.length() / 2) {
            max /= randomValue;
        }
        if (MIN > max) {
            max += MIN;
        }
        String shortedCode = RandomStringUtils.randomAlphanumeric(MIN, max);
        URLEntity urlEntity = new URLEntity(new Date(), url);
        urlEntity.setShorted(shortedCode);
        urlEntity.setNewUrl(String.format("http://%s:%s/%s", System.getenv("INSTANCE_IP"), serverPort, shortedCode));
        urlEntity.setExpirationDate(addOneYearToDate(urlEntity.getCreationDate()));
        return repository.save(urlEntity);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public RedirectView redirect(String shorted) throws IOException {
        URLEntity urlEntity = repository.findByShorted(shorted);

        Optional.ofNullable(urlEntity).orElseThrow(() -> new ShortedURLNotFoundException(shorted));

        if (new Date().after(urlEntity.getExpirationDate())) {
            repository.deleteById(urlEntity.getId());
            throw new ExpiredCodeException(shorted);
        }

        return new RedirectView(urlEntity.getUrl());
    }

    private Date addOneYearToDate(Date creationDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(creationDate);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

}
