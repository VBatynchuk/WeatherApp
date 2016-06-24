package com.batynchuk.weatherapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Build;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


public class WeatherMain extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback {

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
    private TextView tvMaxTemp;
    private TextView tvMinTemp;

    private GoogleApiClient mGoogleApiClient;
    private WeatherHTTPGet weatherHTTPGet = new WeatherHTTPGet();

    private String latitude;
    private String longitude;

    private String[] PERMISSIONS_LOCATION = new String[]{android
            .Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;

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
        tvMaxTemp = (TextView) findViewById(R.id.max_temperature);
        tvMinTemp = (TextView) findViewById(R.id.min_temperature);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION,
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();
            return;
        }
        android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());

            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(latitude, longitude);

            tvLatitude.setText(String.format(Locale.GERMAN, "%s %s", "Latitude:", latitude));
            tvLongitude.setText(String.format(Locale.GERMAN, "%s %s", "Longitude:", longitude));

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    tvLatitude.setText("Granted");
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    tvLatitude.setText("NotGranted");
//                }
//                return;
//            }
//
//        }
//    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(Double.parseDouble(latitude),
                Double.parseDouble(longitude));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            Weather weather = new Weather();

            try {
                weather = JSONWeatherParser.getWeather(weatherHTTPGet.getWeatherData(latitude,
                        longitude));

                weather.iconData = ((new WeatherHTTPGet()).getImage(weather.weatherCondition
                        .getIcon()));
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

            tvMaxTemp.setText(String.format(Locale.GERMAN, "%s %d %s", "Max:",
                    Math.round(weather.weatherMain.getMaxTemperature() - 273.15), "\u2103"));

            tvMinTemp.setText(String.format(Locale.GERMAN, "%s %d %s", "Min:",
                    Math.round(weather.weatherMain.getMinTemperature() - 273.15), "\u2103"));

            tvPressure.setText(String.format(Locale.GERMAN, "%s %d", "Pressure:",
                    weather.weatherMain.getPressure()));

            tvHumidity.setText(String.format(Locale.GERMAN, "%s %d", "Humidity:",
                    weather.weatherMain.getHumidity()));

            tvWindSpeed.setText(String.format(Locale.GERMAN, "%s %.2f", "Wind speed:",
                    weather.wind.getWindSpeed()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(weather.otherWeatherInfo.getDt() * 1000);
            tvDayTime.setText(calendar.getTime().toString());
        }
    }
}
