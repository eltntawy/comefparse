package com.ef.model;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by mohamedrefaat on 10/14/17.
 */
public class Argument {

    // duration argument types
    public enum Duration {
        hourly, daily,
    }

    private Date startDate;
    private Duration duration;
    private int threshold;
    private String filePath;


    public Argument() throws ParseException {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "startDate=" + startDate +
                ", duration=" + duration +
                ", threshold=" + threshold +
                '}';
    }
}
