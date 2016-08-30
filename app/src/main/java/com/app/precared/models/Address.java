package com.app.precared.models;

import com.app.precared.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prashant on 8/22/16.
 */
public class Address {
    public int id;
    public String line1;
    public String line2;
    public String city;
    public String state;
    public String pincode;
    public String mobile_no;
    public boolean defaultSelection;
    public boolean isSelected;

    public Address(JSONObject jsonObject, boolean isselected) {
        id = JSONUtil.getJSONIntValue(jsonObject, "id");
        line1 = JSONUtil.getJSONString(jsonObject, "line1");
        line2 = JSONUtil.getJSONString(jsonObject, "line2");
        city = JSONUtil.getJSONString(jsonObject, "city");
        state = JSONUtil.getJSONString(jsonObject, "state");
        pincode = JSONUtil.getJSONString(jsonObject, "pincode");
        mobile_no = JSONUtil.getJSONString(jsonObject, "mobile_no");
        try {
            defaultSelection = jsonObject.getBoolean("default");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isSelected = isselected;
    }
}
