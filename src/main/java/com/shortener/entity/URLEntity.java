package com.shortener.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "url_entity")
public class URLEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "shorted_code", unique = true)
    private String shorted;

    @Column(name = "new_url")
    private String newUrl;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    public URLEntity() {

    }

    public URLEntity(Integer id, Date creationDate, String url, String shorted) {
        this.id = id;
        this.creationDate = creationDate;
        this.url = url;
        this.shorted = shorted;
    }

    public URLEntity(Date creationDate, String url) {
        this.creationDate = creationDate;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShorted() {
        return shorted;
    }

    public void setShorted(String shorted) {
        this.shorted = shorted;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
