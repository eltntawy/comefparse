package com.ef.parse.model;

import java.util.Date;

/**
 * Created by mohamedrefaat on 10/14/17.
 */
public class RequestLine {

    private Date date;
    private String ip;

    public RequestLine(Date date, String ip) {
        this.date = date;
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestLine requestLine = (RequestLine) o;

        if (date != null ? !date.equals(requestLine.date) : requestLine.date != null) return false;
        return ip != null ? ip.equals(requestLine.ip) : requestLine.ip == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        return result;
    }


}
