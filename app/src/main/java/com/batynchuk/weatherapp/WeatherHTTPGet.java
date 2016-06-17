package com.batynchuk.weatherapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Батинчук on 14.06.2016.
 */
public class WeatherHTTPGet {
    private static String URL_BBASE = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&APPID=2ad13af02d3ec1ce877fc6d8b3308686";

    public String getWeatherData(String location) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(URL_BBASE + location + API_KEY)).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        try {
            InputStream in;
            StringBuffer buffer = new StringBuffer();
            in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null)
                buffer.append(line + "rn");
            in.close();
            return buffer.toString();
        } finally {
            connection.disconnect();
        }

    }
}
