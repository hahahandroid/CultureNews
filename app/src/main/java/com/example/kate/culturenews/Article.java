package com.example.kate.culturenews;

import java.util.Date;

public class Article {
    private String mTitle;
    private String mAuthor;
    private String mSection;
    private Date mDate;
    private String mUrl;

    public Article(String title, String author, String section, Date date, String url) {
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSection() {
        return mSection;
    }

    public Date getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
