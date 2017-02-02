package com.example.koen.wineretry.Objects;

/**
 * Created by Koen on 14-1-2017.
 *
 * Wine bottle object that consists of a title, the region the wine comes from, the year,
 * additional information about the bottle (story), the id of the user that sells the bottle
 * (sellerid), an unique ID for the bottle and a tag (red/white etc). This object is created at
 * NewSellActivity and used for AllsellsActivity and BuyActivity. Furthermore the info of the
 * objects is given to BuyfullinfoActivity and SellfullinfoActivity
 */

public class WineObject {

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

    public WineObject(String title, String region, String year, String story, String sellerid,
                      String bottleid, String tag) {
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
    public String getBottleid(){
        return bottleid;
    }
    public String getTag (){
        return tag;
    }
}


