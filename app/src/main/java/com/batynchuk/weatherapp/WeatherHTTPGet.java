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

    public String getWeatherData(String location) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(URL_BBASE + location)).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        try {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            StringBuffer buffer = new StringBuffer();
            in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ( (line = br.readLine()) != null )
                buffer.append(line + "rn");
            in.close();
            return buffer.toString();
        }
            finally{
                connection.disconnect();
            }

    }
}
