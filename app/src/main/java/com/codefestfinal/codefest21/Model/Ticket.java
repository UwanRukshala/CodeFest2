package com.codefestfinal.codefest21.Model;

public class Ticket {
    String title;
    String Body;
    String type;

    public Ticket() {
    }

    public Ticket(String title, String body, String type) {
        this.title = title;
        Body = body;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
