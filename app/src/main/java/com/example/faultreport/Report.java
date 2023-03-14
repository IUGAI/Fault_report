package com.example.faultreport;

public class Report {
    String id;
    String img;
    String location;
    String date;
    String title;
    String report_type;
    String result;


    public Report(String id,String img, String location, String date, String title, String report_type, String result) {
        this.img = img;
        this.id = id;
        this.location = location;
        this.date = date;
        this.title = title;
        this.report_type = report_type;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
