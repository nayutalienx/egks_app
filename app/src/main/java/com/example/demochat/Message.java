package com.example.demochat;

public class Message {
    private String user,text,date;
    public Message(String user, String text, String date){
        this.user = user;
        this.text = text;
        this.date = date;
    }
    public String getUser(){
        return this.user;
    }
    public String getText(){
        return this.text;
    }
    public String getDate(){
        return this.date;
    }

    public String toString(){
        return this.user+": "+this.text+" // "+this.date;
    }
}

