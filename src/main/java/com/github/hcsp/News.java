package com.github.hcsp;

import java.time.Instant;

/**
 * @Author: GoFocus
 * @Date: 2020-04-15 15:08
 * @Description:
 */
public class News {

    private Integer id;
    private String title;
    private String content;
    private String url;
    private Instant createdAt;
    private Instant modifiedAt;

    public News(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
