package com.batynchuk.weatherapp.weather;

/**
 * Created by Батинчук on 17.06.2016.
 */
public class WeatherCondition {

    private int wId;
    private String description;
    private String main;

    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}