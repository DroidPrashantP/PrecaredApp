package com.app.precared.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    public static void setJSONProperty(JSONObject jsonObject, String key,
                                       Object value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {

        }
    }

    public static Object getJSONProperty(JSONObject jsonObject, String key) {
        try {
            return jsonObject.get(key);
        } catch (JSONException e) {

        }
        return null;
    }

    public static String getJSONString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                if (StringUtils.isNotEmpty(jsonObject.getString(key))) {
                    return jsonObject.getString(key);
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (JSONException e) {
            return "";
        }
    }

    public static boolean getJSONBoolean(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                    return jsonObject.getBoolean(key);
            } else {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }
    }
    public static int getJSONIntValue(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                if (StringUtils.isNotEmpty(jsonObject.getString(key))) {
                    return jsonObject.getInt(key);
                }
            } else {
                return 0;
            }
        } catch (JSONException e) {
            return 0;
        }
        return 0;
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static Object get(JSONArray jsonArray, int index) {
        try {
            return jsonArray.get(index);
        } catch (JSONException e) {
            return null;
        }
    }

    public static boolean isValid(JSONObject jsonObject) {
        return ((jsonObject != null) && jsonObject.length() > 0) ? true : false;
    }

    public static boolean isValid(JSONArray jsonArray) {
        return ((jsonArray != null) && jsonArray.length() > 0) ? true : false;
    }

    public static Object removeJSONProperty(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.remove(key);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static long getJSONLong(JSONObject jsonObject, String key) {
        try {
            if (StringUtils.isNotEmpty(jsonObject.getString(key))) {
                return jsonObject.getLong(key);
            } else {
                return 0;
            }
        } catch (JSONException e) {
            return 0;
        }
    }
}
