package com.szdd.qianxun.advertise.tools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LLX on 2015/11/11.
 */
public class HttpUtils {
    public static InputStream getStreamFromURL(String imageURL) {
        InputStream in = null;
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            in = connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }
}
