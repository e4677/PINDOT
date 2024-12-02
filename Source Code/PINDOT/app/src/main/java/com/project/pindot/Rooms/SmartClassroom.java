package com.project.pindot.Rooms;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
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
// */
import com.project.pindot.R;
import android.util.Log;

public class SmartClassroom extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch swLight2, swFan2;
    TextView tempRoom2, humRoom2, tempRoom2Disp;
    private DatabaseReference roomRef;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.room_smart);

        ConstraintLayout backButton = findViewById(R.id.back_btn);
        swLight2 = findViewById(R.id.sw_light2);
        swFan2 = findViewById(R.id.sw_fan2);
        tempRoom2 = findViewById(R.id.tempRoom);
        tempRoom2Disp = findViewById(R.id.tempRoomDisp);
        humRoom2 = findViewById(R.id.humRoom);
        progressBar = findViewById(R.id.progressbar);

        // Retrieve switch states as strings from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("RoomPrefs", MODE_PRIVATE);
        String light2State = prefs.getString("swLight2State", "off"); // Default value is "off"
        String fan2State = prefs.getString("swFan2State", "off");    // Default value is "off"

        // Set the switches to their saved states based on the string values
        swLight2.setChecked("on".equals(light2State));
        swFan2.setChecked("on".equals(fan2State));

        backButton.setOnClickListener(view -> {
            saveSwitchState(); // Save the state before exiting
            finish();
        });

        /*=====================================================================*/
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Firebase reference to the rooms data
        roomRef = database.getReference();

        // Set up the listener for room 1 data
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract data for room 1
                    Double room2Temp = snapshot.child("room2").child("temp").getValue(Double.class);
                    Double room2Hum = snapshot.child("room2").child("hum").getValue(Double.class);

                    // Update the UI with temperature and humidity values
                    if (room2Temp != null && room2Hum != null) {
                        tempRoom2.setText(room2Temp + "\u00B0C");
                        tempRoom2Disp.setText(room2Temp + "°C");
                        humRoom2.setText(room2Hum + "%");
                    } else {
                        tempRoom2.setText("--");
                        tempRoom2Disp.setText("--");
                        humRoom2.setText("--");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any errors in reading from Firebase
                tempRoom2.setText("Error loading data");
                humRoom2.setText("Error loading data");
            }
        });

        // Set listeners for the light switch
        swLight2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DatabaseReference light2Ref = database.getReference("light2");
            String value = isChecked ? "on" : "off"; // Convert boolean to "on"/"off"
            light2Ref.setValue(value)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "light2 set to: " + value))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to update light2", e));
        });

        // Set listeners for the fan switch
        swFan2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DatabaseReference fan2Ref = database.getReference("fan2");
            String value = isChecked ? "on" : "off"; // Convert boolean to "on"/"off"
            fan2Ref.setValue(value)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "fan2 set to: " + value))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to update fan2", e));
        });


        // Handle insets (system UI for Edge-to-Edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.smart), (v, insets) -> {
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
        editor.putString("swLight2State", swLight2.isChecked() ? "on" : "off");
        editor.putString("swFan2State", swFan2.isChecked() ? "on" : "off");
        editor.apply();
    }

}
