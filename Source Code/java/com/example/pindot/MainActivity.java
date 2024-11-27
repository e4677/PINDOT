package com.example.pindot;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button onLight1, offLight1, onFan1, offFan1, onLight2, offLight2, onFan2, offFan2;
    TextView tempRoom1, humRoom1, tempRoom2, humRoom2;
    DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize buttons and TextViews
        onLight1 = findViewById(R.id.onLight1);
        offLight1 = findViewById(R.id.offLight1);
        onFan1 = findViewById(R.id.onFan1);
        offFan1 = findViewById(R.id.offFan1);
        onLight2 = findViewById(R.id.onLight2);
        offLight2 = findViewById(R.id.offLight2);
        onFan2 = findViewById(R.id.onFan2);
        offFan2 = findViewById(R.id.offFan2);

        tempRoom1 = findViewById(R.id.tempRoom1);
        humRoom1 = findViewById(R.id.humRoom1);
        tempRoom2 = findViewById(R.id.tempRoom2);
        humRoom2 = findViewById(R.id.humRoom2);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Firebase reference to the rooms data
        roomRef = database.getReference();

        // Set up the listener for room 1 and room 2 data
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract data for room 1 and room 2
                    Double room1Temp = snapshot.child("room1").child("temp").getValue(Double.class);
                    Double room1Hum = snapshot.child("room1").child("hum").getValue(Double.class);
                    Double room2Temp = snapshot.child("room2").child("temp").getValue(Double.class);
                    Double room2Hum = snapshot.child("room2").child("hum").getValue(Double.class);

                    // Update the UI with temperature and humidity values
                    if (room1Temp != null && room1Hum != null) {
                        tempRoom1.setText("Room 1 Temp: " + room1Temp + "°C");
                        humRoom1.setText("Room 1 Humidity: " + room1Hum + "%");
                    } else {
                        tempRoom1.setText("Room 1 Temp: N/A");
                        humRoom1.setText("Room 1 Humidity: N/A");
                    }

                    if (room2Temp != null && room2Hum != null) {
                        tempRoom2.setText("Room 2 Temp: " + room2Temp + "°C");
                        humRoom2.setText("Room 2 Humidity: " + room2Hum + "%");
                    } else {
                        tempRoom2.setText("Room 2 Temp: N/A");
                        humRoom2.setText("Room 2 Humidity: N/A");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any errors in reading from Firebase
                tempRoom1.setText("Error loading data");
                humRoom1.setText("Error loading data");
                tempRoom2.setText("Error loading data");
                humRoom2.setText("Error loading data");
            }
        });

        // Button click listeners for light and fan controls
        onLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference light1 = database.getReference("light1");
                light1.setValue("on");
            }
        });

        offLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference light1 = database.getReference("light1");
                light1.setValue("off");
            }
        });

        onFan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference fan1 = database.getReference("fan1");
                fan1.setValue("on");
            }
        });

        offFan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference fan1 = database.getReference("fan1");
                fan1.setValue("off");
            }
        });

        onLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference light2 = database.getReference("light2");
                light2.setValue("on");
            }
        });

        offLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference light2 = database.getReference("light2");
                light2.setValue("off");
            }
        });

        onFan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference fan2 = database.getReference("fan2");
                fan2.setValue("on");
            }
        });

        offFan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference fan2 = database.getReference("fan2");
                fan2.setValue("off");
            }
        });

        // Handle insets (system UI for Edge-to-Edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
