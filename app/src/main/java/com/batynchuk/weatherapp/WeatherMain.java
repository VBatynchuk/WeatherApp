package com.batynchuk.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.batynchuk.weatherapp.weather.Weather;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


public class WeatherMain extends AppCompatActivity {

    private TextView tvCityName;
    private TextView tvDescription;
    private TextView tvTemperature;
    private TextView tvPressure;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private TextView tvDayTime;
    private ImageView ivWeather;

    WeatherHTTPGet weatherHTTPGet = new WeatherHTTPGet();
    private String city = "Kiev";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        tvCityName = (TextView) findViewById(R.id.city_name);
        tvDescription = (TextView) findViewById(R.id.description);
        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvPressure = (TextView) findViewById(R.id.pressure);
        tvHumidity = (TextView) findViewById(R.id.humidity);
        tvWindSpeed = (TextView) findViewById(R.id.wind_speed);
        tvDayTime = (TextView) findViewById(R.id.day_time);
        ivWeather = (ImageView) findViewById(R.id.image_weather);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(city);

    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            Weather weather = new Weather();

            try {
                weather = JSONWeatherParser.getWeather(weatherHTTPGet.getWeatherData(city));

                weather.iconData = ((new WeatherHTTPGet()).getImage(weather.weatherCondition.getIcon()));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            tvCityName.setText(String.format(Locale.GERMAN, "%s, %s",
                    weather.otherWeatherInfo.getName(), weather.sys.getCountry()));

            tvDescription.setText(String.format(Locale.GERMAN, "%s, %s",
                    weather.weatherCondition.getMain(),
                    weather.weatherCondition.getDescription()));

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0,
                        weather.iconData.length);
                ivWeather.setImageBitmap(img);
            }

            tvTemperature.setText(String.format(Locale.GERMAN, "%d %s",
                    Math.round(weather.weatherMain.getTemperature() - 273.15), "\u2103"));

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
