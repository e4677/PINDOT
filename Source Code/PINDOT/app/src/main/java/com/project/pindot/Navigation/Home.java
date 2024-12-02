package com.project.pindot.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.project.pindot.R;
import com.project.pindot.Rooms.ComputerLab;
import com.project.pindot.Rooms.FacultyOffice;
import com.project.pindot.Rooms.LectureRoom;
import com.project.pindot.Rooms.SmartClassroom;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Home extends AppCompatActivity {

    ImageView home, profile;
    CardView lecture, smart, comlab, faculty;
    TextView monthText, dayText, yearText, temperatureText, locationText;
    LinearLayout about;

    FusedLocationProviderClient fusedLocationClient;
    String TAG = "Home";
    String WEATHER_API_KEY = "bc4a6e5323a441bb2e9bef190d8c8119";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Find TextViews by their IDs
        monthText = findViewById(R.id.month);
        dayText = findViewById(R.id.day);
        yearText = findViewById(R.id.year);

        lecture = findViewById(R.id.lecture);
        smart = findViewById(R.id.smart);
        comlab = findViewById(R.id.comlab);
        faculty = findViewById(R.id.faculty);
        about = findViewById(R.id.about);


        // Get the current date
        Calendar calendar = Calendar.getInstance();
        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime());
        String day = new SimpleDateFormat("dd", Locale.getDefault()).format(calendar.getTime());
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.getTime());

        // Update the TextViews
        monthText.setText(month);
        dayText.setText(day);
        yearText.setText(year);

        // Initialize weather TextViews
        temperatureText = findViewById(R.id.temperature);
        locationText = findViewById(R.id.location);

        // Fetch and display weather information
        fetchLocationAndWeather();

        about.setOnClickListener(v -> startActivity(new Intent(Home.this, About.class)));
        lecture.setOnClickListener(v -> startActivity(new Intent(Home.this, LectureRoom.class)));
        smart.setOnClickListener(v -> startActivity(new Intent(Home.this, SmartClassroom.class)));
        comlab.setOnClickListener(v -> {
            // Create and show the AlertDialog when the 'comlab' card is clicked
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View customView = inflater.inflate(R.layout.alertdialog, null);
            builder.setView(customView);

            AlertDialog alertDialog = builder.create();

            // Set the background of the AlertDialog window to transparent
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            Button okButton = customView.findViewById(R.id.btn_ok);
            okButton.setOnClickListener(dialog -> {
                // Dismiss the dialog when the 'OK' button is clicked
                alertDialog.dismiss();
            });

            alertDialog.setCancelable(false); // Optional: prevent outside dismissal
            alertDialog.show();
        });
        faculty.setOnClickListener(v -> {
            // Create and show the AlertDialog when the 'comlab' card is clicked
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View customView = inflater.inflate(R.layout.alertdialog, null);
            builder.setView(customView);

            AlertDialog alertDialog = builder.create();

            // Set the background of the AlertDialog window to transparent
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            Button okButton = customView.findViewById(R.id.btn_ok);
            okButton.setOnClickListener(dialog -> {
                // Dismiss the dialog when the 'OK' button is clicked
                alertDialog.dismiss();
            });

            alertDialog.setCancelable(false); // Optional: prevent outside dismissal
            alertDialog.show();
        });

    }

    @SuppressLint("MissingPermission")
    private void fetchLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Get current location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        fetchWeather(location);
                    } else {
                        Log.e(TAG, "Location is null.");
                    }
                });
    }

    private void fetchWeather(Location location) {
        new Thread(() -> {
            try {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String apiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s",
                        latitude, longitude, WEATHER_API_KEY);

                HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();

                parseAndDisplayWeather(response.toString());
            } catch (Exception e) {
                Log.e(TAG, "Error fetching weather data", e);
            }
        }).start();
    }

    private void parseAndDisplayWeather(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String temperature = jsonObject.getJSONObject("main").getString("temp") + "°C";
            String locationName = jsonObject.getString("name");
            String iconCode = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            runOnUiThread(() -> {
                temperatureText.setText(temperature);
                locationText.setText(locationName);
                setWeatherIcon(iconCode);
            });
        } catch (Exception e) {
            Log.e(TAG, "Error parsing weather data", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location and weather
                fetchLocationAndWeather();
            } else {
                Log.e(TAG, "Location permission denied");
                // Optionally show a message or handle the case when permission is denied
            }
        }
    }

    private void setWeatherIcon(String iconCode) {
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        ImageView weatherIcon = findViewById(R.id.weatherIcon);

        Glide.with(this)
                .load(iconUrl)
                .apply(new RequestOptions().error(R.drawable.clouds)) // Fallback icon if load fails
                .into(weatherIcon);
    }

}

