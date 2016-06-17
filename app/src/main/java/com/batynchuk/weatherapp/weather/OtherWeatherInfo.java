package com.batynchuk.weatherapp.weather;

/**
 * Created by Батинчук on 17.06.2016.
 */
public class OtherWeatherInfo {
    private String base;
    private int rainTime;
    private long dt;
    private int id;
    private String name;
    private int cod;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getRainTime() {
        return rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
