package com.example.demochat;

import com.google.gson.annotations.SerializedName;

public class Post {
    private String user;
    private String date;



    private String message;
    //@SerializedName("body")
    private String text;

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
    public String getMessage() {
        return message;
    }
}
