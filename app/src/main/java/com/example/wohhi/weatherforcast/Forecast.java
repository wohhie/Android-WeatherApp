package com.example.wohhi.weatherforcast;

/**
 * Created by wohhi on 2/24/2017.
 */
public class Forecast {

    private String code;
    private String date;
    private String day;

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    private String high;
    private String low;
    private String text;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {

        switch(day){
            case "Sat":
                return "Saturday";
            case "Sun":
                return "Sunday";
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thu":
                return "Thursday";
            case "Fri":
                return "Friday";
            default:
                return "";
        }
    }

    public void setDay(String day) {
        this.day = day;
    }



    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
