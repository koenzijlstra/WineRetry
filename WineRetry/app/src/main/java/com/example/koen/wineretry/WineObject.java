package com.example.koen.wineretry;

/**
 * Created by Koen on 14-1-2017.
 */

public class WineObject {

    // public String een of ander uniek id (push id?)
    public String title;
    public String region;
    public String year;
    public String story;
    public String sellerid;
    public String bottleid;
    public String tag;

    public WineObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public WineObject(String title, String region, String year, String story, String sellerid, String bottleid, String tag) {
        this.title = title;
        this.region = region;
        this.year = year;
        this.story = story;
        this.sellerid = sellerid;
        this.bottleid = bottleid;
        this.tag = tag;

    }

    public String getRegion(){
        return region;
    }

    public String getTitle(){
        return title;
    }

    public String getYear(){
        return year;
    }

    public String getStory(){
        return story;
    }
    public String getSellerid(){
        return sellerid;
    }

    // gedaan om te kunnen deleten
    public String getBottleid(){
        return bottleid;
    }

    public String getTag (){
        return tag;
    }
}


