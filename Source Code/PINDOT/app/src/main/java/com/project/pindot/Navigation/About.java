package com.project.pindot.Navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.project.pindot.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.about);

        ConstraintLayout backButton = findViewById(R.id.back_btn);
        TextView terms = findViewById(R.id.terms);
        backButton.setOnClickListener(view -> {finish(); });
        terms.setOnClickListener(this::showTermsOfService);
    }

    public void showTermsOfService(View view) {
        // Create and show the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms of Service");
        builder.setMessage("Welcome to PINDOT. By using our application, you agree to comply with and be bound by the following terms and conditions. Please read them carefully before using PINDOT.\n\n" +
                "1. Acceptance of Terms\nBy accessing or using the PINDOT app, you acknowledge that you have read, understood, and agree to be bound by these Terms of Service and our Privacy Policy.\n\n" +
                "2. Use of the App\nPINDOT is designed for [describe main functions of the app, e.g., controlling smart home devices, accessing data, etc.]. You may only use the app for its intended purposes and agree not to misuse or attempt to interfere with the proper functioning of the app.\n\n" +
                "3. User Content\nSince PINDOT does not require user registration, any data or content you interact with will not be tied to a personal profile. You retain ownership of any content or data you manage within the app, but by using PINDOT, you grant us a license to use, store, and process that data as needed to provide the service.\n\n" +
                "4. Restrictions\nYou agree not to:\n- Modify, reverse-engineer, or decompile the app.\n- Use the app for any unlawful purposes or activities.\n- Attempt to disrupt the service or gain unauthorized access to any aspect of the app.\n\n" +
                "5. Updates and Changes\nPINDOT may update the app periodically and reserves the right to change or discontinue any feature or service without notice. We may also update these Terms of Service and will notify you of any significant changes.\n\n" +
                "6. Limitation of Liability\nPINDOT is not liable for any damages resulting from your use of the app. We provide the app on an 'as-is' basis, and while we strive for the highest quality, we cannot guarantee that the app will be error-free or available at all times.\n\n" +
                "7. Governing Law\nThese Terms of Service are governed by the laws of [your jurisdiction], without regard to its conflict of law principles.\n\n" +
                "8. Acceptance\nBy using PINDOT, you agree to these Terms of Service. If you do not agree, please refrain from using the app.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }


}
