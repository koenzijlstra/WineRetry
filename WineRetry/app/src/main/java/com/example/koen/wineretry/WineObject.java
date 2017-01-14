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

    public WineObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public WineObject(String title, String region, String year, String story) {
        this.title = title;
        this.region = region;
        this.year = year;
        this.story = story;
    }
}


