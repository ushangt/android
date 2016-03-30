package com.masmutual.newssearch;

import java.util.Date;

/**
 * Created by Ushang-PC on 3/29/2016.
 */
public class Article {

    private String headline;
    private String posted;
    private String content;
    private String imageURL;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
