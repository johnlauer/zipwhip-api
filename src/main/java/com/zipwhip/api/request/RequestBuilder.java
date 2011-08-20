package com.zipwhip.api.request;

import com.zipwhip.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: Jul 18, 2009
 * Time: 10:42:41 AM
 *
 * Simplifies the work of creating a parameter list.
 *
 */
public class RequestBuilder {

    private StringBuilder sb = new StringBuilder("?");

    public RequestBuilder params(Map<String, Object> values){
        if (values == null){
            return this;
        }
        for(String key : values.keySet()){
            Object value = values.get(key);
            if (value == null){
                // dont put nulls in there
                continue;
            }
            param(key, String.valueOf(value));
        }
        return this;
    }

    public RequestBuilder param(String key, String value) {

        if (StringUtil.isNullOrEmpty(key)) {
            return this;
        }

        String string;
        try {
            string = java.net.URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            string = value;
        }

        sb.append(key);
        sb.append("=");
        if (string != null) {
            sb.append(string);
        }
        sb.append("&");

        return this;
    }

    public String build() {
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
//        String result = "?";
//        for (String param : params) {
//            result += param + "&";
//        }
//        return result.substring(0, result.length() - 1);
    }

}
