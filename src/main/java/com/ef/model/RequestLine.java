package com.ef.model;

import java.util.Date;

/**
 * Created by mohamedrefaat on 10/14/17.
 */
public class RequestLine {

    private Date date;
    private String ip;
    private String request;
    private String status;
    private String userAgent;


    public RequestLine(Date date, String ip, String request, String status, String userAgent) {
        this.date = date;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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
