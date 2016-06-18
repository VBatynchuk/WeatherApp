package com.batynchuk.weatherapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherHTTPGet {
    private static String URL_BBASE = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&APPID=2ad13af02d3ec1ce877fc6d8b3308686";
    private static String IMG_URL = "http://openweathermap.org/img/w/";

    public String getWeatherData(String location) throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) (new URL(URL_BBASE + location + API_KEY)).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        try {
            InputStream inputStream;
            StringBuilder buffer = new StringBuilder();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null)
                buffer.append(line).append("rn");
            inputStream.close();
            return buffer.toString();
        } finally {
            connection.disconnect();
        }

    }

    public byte[] getImage(String code) throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) (new URL(IMG_URL + code + ".png")).openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while (inputStream.read(buffer) != -1) {
                byteArrayOutputStream.write(buffer);
            }

            inputStream.close();
            return byteArrayOutputStream.toByteArray();
        } finally {
            try {
                connection.disconnect();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
