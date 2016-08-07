package com.app.precared.utils;

import java.util.ArrayList;

/**
 * Created by Prashant on 11/8/15.
 */
public class StringUtils {

    /**
     * Check whether a string is not NULL, empty or "NULL", "null", "Null"
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        boolean flag = true;
        if (str != null) {
            str = str.trim();
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static String join(ArrayList<String> list, String s) {
        String newString = "";
        if (list!= null && list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String str : list) {
                builder.append(str);
                builder.append(s);
            }
            newString = builder.toString();
            if (StringUtils.isNotEmpty(newString)){
                newString = newString.substring(0,newString.length()-1);
            }
            return newString;
        }
        return newString;
    }
}
