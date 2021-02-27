package com.codefestfinal.codefest21.Model;

public class Add {
    String title;
    String body;
    double lat;
    double longt;

    public Add(String title, String body, double lat, double longt) {
        this.title = title;
        this.body = body;
        this.lat = lat;
        this.longt = longt;
    }

    public Add() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }
}
