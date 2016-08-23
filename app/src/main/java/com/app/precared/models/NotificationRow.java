package com.app.precared.models;

/**
 * Created by prashant on 8/22/16.
 */
public class NotificationRow {
    public String title;
    public String desc;
    public String subType;
    public int id ;

    public NotificationRow(String titlestring, String description, String sub_type) {
        title = titlestring;
        desc = description;
        subType = sub_type;
    }
}
