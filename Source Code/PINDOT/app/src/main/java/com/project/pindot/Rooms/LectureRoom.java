package com.project.pindot.Rooms;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*===========Comment For now=============================*/
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
 //*/
import com.project.pindot.R;

public class LectureRoom extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch swLight1, swFan1;
    TextView tempRoom1, humRoom1, tempRoom1Disp;
    private DatabaseReference roomRef;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.room_lecture);

        ConstraintLayout backButton = findViewById(R.id.back_btn);
        swLight1 = findViewById(R.id.sw_light1);
        swFan1 = findViewById(R.id.sw_fan1);
        tempRoom1 = findViewById(R.id.tempRoom);
        tempRoom1Disp = findViewById(R.id.tempRoomDisp);
        humRoom1 = findViewById(R.id.humRoom);
        progressBar = findViewById(R.id.progressbar);


        // Retrieve switch states as strings from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("RoomPrefs", MODE_PRIVATE);
        String light1State = prefs.getString("swLight1State", "off"); // Default value is "off"
        String fan1State = prefs.getString("swFan1State", "off");    // Default value is "off"

        // Set the switches to their saved states based on the string values
        swLight1.setChecked("on".equals(light1State));
        swFan1.setChecked("on".equals(fan1State));

        backButton.setOnClickListener(view -> {
            saveSwitchState(); // Save the state before exiting
            finish();
        });

        /*===============================================================================================*/
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Firebase reference to the rooms data
        roomRef = database.getReference();

        // Set up the listener for room 1 data
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract data for room 1
                    Double room1Temp = snapshot.child("room1").child("temp").getValue(Double.class);
                    Double room1Hum = snapshot.child("room1").child("hum").getValue(Double.class);

                    // Update the UI with temperature and humidity values
                    if (room1Temp != null && room1Hum != null) {
                        tempRoom1.setText(room1Temp + "°C");
                        tempRoom1Disp.setText(room1Temp + "°C");
                        humRoom1.setText(room1Hum + "%");
                    } else {
                        tempRoom1.setText("--");
                        tempRoom1Disp.setText("--");
                        humRoom1.setText("--");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any errors in reading from Firebase
                tempRoom1.setText("Error loading data");
                humRoom1.setText("Error loading data");
            }
        });

        // Set listeners for the light switch
        swLight1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DatabaseReference light1Ref = database.getReference("light1");
            String value = isChecked ? "on" : "off"; // Convert boolean to "on"/"off"
            light1Ref.setValue(value)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "light1 set to: " + value))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to update light1", e));
        });

        // Set listeners for the fan switch
        swFan1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DatabaseReference fan1Ref = database.getReference("fan1");
            String value = isChecked ? "on" : "off"; // Convert boolean to "on"/"off"
            fan1Ref.setValue(value)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "fan1 set to: " + value))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to update fan1", e));
        });

        // Handle insets (system UI for Edge-to-Edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lecture), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //===================================================================================*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSwitchState(); // Save the state when the activity is paused
    }

    // Method to save the switch state as a string in SharedPreferences
    private void saveSwitchState() {
        SharedPreferences prefs = getSharedPreferences("RoomPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save the current states of the switches as "on" or "off"
        editor.putString("swLight1State", swLight1.isChecked() ? "on" : "off");
        editor.putString("swFan1State", swFan1.isChecked() ? "on" : "off");
        editor.apply();
    }

}
