package com.hub.anyspot.utils;

/**
 * Created by LYS on 2017-11-27.
 */

public class cList {
    private Float rating;
    private Double sLat, sLon;
    private String name;
    private Float color;
    private int icon;

    cList(Float rating, Double sLat, Double sLon, String name, Float color, int icon) {
        this.rating = rating;
        this.sLat = sLat;
        this.sLon = sLon;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public Double getLat() {
        return sLat;
    }

    public Double getLon() {
        return sLon;
    }

    public int getIcon() {
        return icon;
    }

    public String getname() {
        return name;
    }

    public Float getcolor() {
        return color;
    }
}
