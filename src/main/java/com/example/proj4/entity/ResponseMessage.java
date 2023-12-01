package com.example.proj4.entity;

public class ResponseMessage {
    private final String from;
    private final String text;
    private final String time;

    public ResponseMessage(String from, String text, String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }


    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }


    public String getTime() {
        return time;
    }
}
