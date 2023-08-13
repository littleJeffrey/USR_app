package com.Natuo;

public class uploads {

    private double longitude,latitude, degree, humidity;
    private String fileName;
    private String mImageUrl;

    public uploads(){

    }

    public uploads(double longitude, double latitude, double degree, double humidity){
        this.longitude = longitude;
        this.latitude = latitude;
        this.degree = degree;
        this.humidity = humidity;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setDegree(double degree){
        this.degree = degree;
    }

    public double getDegree(){
        return degree;
    }
    public void setHumidity(double humidity){
        this.humidity = humidity;
    }

    public double getHumidity(){
        return humidity;
    }

}
