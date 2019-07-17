package com.shortener.vo;

public class RestVO {

    private String newUrl;
    private Long expiresAt;

    public RestVO() {

    }

    public RestVO(String newUrl, Long expiresAt) {
        this.newUrl = newUrl;
        this.expiresAt = expiresAt;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
