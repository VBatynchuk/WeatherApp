package com.batynchuk.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WeatherApp extends AppCompatActivity implements View.OnClickListener {

    private TextView tvWeather;

    WeatherHTTPGet weatherHTTPGet = new WeatherHTTPGet();
    private String city = "Kiev&APPID=2ad13af02d3ec1ce877fc6d8b3308686";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);

        tvWeather = (TextView) findViewById(R.id.weather_show);
        tvWeather.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_show:
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{city});
                break;
        }
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            Weather weather = new Weather();

            try {
                weather = JSONWeatherParser.getWeather(weatherHTTPGet.getWeatherData(city));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(weather.sys.getSunrise());
            //tvWeather.setText(String.format(Locale.GERMAN, "%d %s %s", weather.weatherCondition.getwId(), weather.weatherCondition.getDescription(), weather.weatherCondition.getMain()));
        tvWeather.setText(String.format(Locale.GERMAN,"%d %s", Math.round(weather.weatherMain.getTemperature() - 273.15), calendar.getTime().toString()));
        }
    }
}
