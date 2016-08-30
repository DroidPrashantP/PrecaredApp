package com.app.precared.models;

/**
 * Created by prashant on 8/22/16.
 */
public class NotificationRow {
    public String title;
    public String desc;
    public String subType;
    public int id ;
    public String dateText;

    public NotificationRow(String titlestring, String description, String sub_type, String datetext) {
        title = titlestring;
        desc = description;
        subType = sub_type;
        dateText = datetext;
    }
}
