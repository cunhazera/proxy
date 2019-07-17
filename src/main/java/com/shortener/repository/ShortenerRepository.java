package com.shortener.repository;

import com.shortener.entity.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortenerRepository extends JpaRepository<URLEntity, Integer> {

    URLEntity findByShorted(String shorted);

}
