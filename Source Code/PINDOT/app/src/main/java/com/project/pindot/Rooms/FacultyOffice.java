package com.project.pindot.Rooms;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.project.pindot.R;

public class FacultyOffice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.room_faculty);

        ConstraintLayout backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(view -> {finish(); });
    }
}
