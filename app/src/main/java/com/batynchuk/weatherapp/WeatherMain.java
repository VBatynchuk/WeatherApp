package com.batynchuk.weatherapp;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.*;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.batynchuk.weatherapp.weather.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


public class WeatherMain extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView tvCityName;
    private TextView tvDescription;
    private TextView tvTemperature;
    private TextView tvPressure;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private TextView tvDayTime;
    private ImageView ivWeather;
    private TextView tvLatitude;
    private TextView tvLongitude;

    private GoogleApiClient mGoogleApiClient;
    private WeatherHTTPGet weatherHTTPGet = new WeatherHTTPGet();
    private String city = "Kiev";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1234;

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
        tvLatitude = (TextView) findViewById(R.id.latitude);
        tvLongitude = (TextView) findViewById(R.id.longitude);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(city);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            tvLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
            tvLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    tvLatitude.setText("Granted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    tvLatitude.setText("NotGranted");
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    tvLongitude.setText("Granted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    tvLongitude.setText("NotGranted");
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
