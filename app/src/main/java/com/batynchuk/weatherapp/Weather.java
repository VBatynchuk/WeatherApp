package com.batynchuk.weatherapp;

public class Weather {

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

    public WeatherCondition weatherCondition = new WeatherCondition();
    public WeatherMain weatherMain = new WeatherMain();
    public Wind wind = new Wind();
    public Sys sys = new Sys();
    public Coord coord = new Coord();
    public Cloud cloud = new Cloud();

    public class Cloud{

        private int cloudsAll;

        public int getCloudsAll() {
            return cloudsAll;
        }

        public void setCloudsAll(int cloudsAll) {
            this.cloudsAll = cloudsAll;
        }
    }

    public class Coord{

        private double longitude;
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitute) {
            this.longitude = longitute;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
    public class Sys{

        private int type;
        private int sysId;
        private double message;
        private String country;
        private long sunrise;
        private long sunset;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSysId() {
            return sysId;
        }

        public void setSysId(int sysId) {
            this.sysId = sysId;
        }

        public double getMessage() {
            return message;
        }

        public void setMessage(double message) {
            this.message = message;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }
    }
    public class Wind{

        private double windSpeed;
        private int windDeg;

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public int getWindDeg() {
            return windDeg;
        }

        public void setWindDeg(int windDeg) {
            this.windDeg = windDeg;
        }
    }
    public class WeatherMain{
        private double temperature;
        private int pressure;

        private int humidity;
        private double minTemperature;
        private double maxTemperature;

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }


        public double getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(double minTemperature) {
            this.minTemperature = minTemperature;
        }

        public double getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(double maxTemperature) {
            this.maxTemperature = maxTemperature;
        }
    }
    public class WeatherCondition{

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





}
