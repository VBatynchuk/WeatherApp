package com.batynchuk.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.batynchuk.weatherapp.weather.Weather;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


public class WeatherMain extends AppCompatActivity {

    private TextView tvDescription;
    private TextView tvTemperature;
    private TextView tvPressure;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private TextView tvDayTime;

    WeatherHTTPGet weatherHTTPGet = new WeatherHTTPGet();
    private String city = "Kiev";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        tvDescription = (TextView) findViewById(R.id.description);
        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvPressure = (TextView) findViewById(R.id.pressure);
        tvHumidity = (TextView) findViewById(R.id.humidity);
        tvWindSpeed = (TextView) findViewById(R.id.wind_speed);
        tvDayTime = (TextView) findViewById(R.id.day_time);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(city);
        
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            Weather weather = new Weather();

            try {
                weather = JSONWeatherParser.getWeather(weatherHTTPGet.getWeatherData(city));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            tvDescription.setText(weather.weatherCondition.getDescription());

            tvTemperature.setText(String.format(Locale.GERMAN, "%s %d", "Temperature",
                    Math.round(weather.weatherMain.getTemperature() - 273.15)));

            tvPressure.setText(String.format(Locale.GERMAN, "%s %d", "Pressure",
                    weather.weatherMain.getPressure()));

            tvHumidity.setText(String.format(Locale.GERMAN, "%s %d", "Humidity",
                    weather.weatherMain.getHumidity()));

            tvWindSpeed.setText(String.format(Locale.GERMAN, "%s %f", "Wind speed",
                    weather.wind.getWindSpeed()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(weather.otherWeatherInfo.getDt() * 1000);
            tvDayTime.setText(calendar.getTime().toString());
        }
    }
}
