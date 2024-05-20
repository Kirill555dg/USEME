package com.example.useme.authorization;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.useme.R;

public class AuthorizationActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText passwordET;
    private RadioButton teacherRB;
    private RadioButton studentRB;
    private Button signIn;
    private Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);

        emailET = findViewById(R.id.ET_email);
        passwordET = findViewById(R.id.ET_password);

        teacherRB = findViewById(R.id.RBT);
        studentRB = findViewById(R.id.RBS);

        signIn = findViewById(R.id.button_sign_in);
        registration = findViewById(R.id.button_registration);

        signIn.setEnabled(false);
        registration.setEnabled(false);

        addRadioButtonListener(teacherRB);
        addRadioButtonListener(studentRB);

        addTextListener(emailET);
        addTextListener(passwordET);
    }

    private void addRadioButtonListener(RadioButton RB) {
        RB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableRegistration();
                enableSignInIfReady();
            }
        });
    }

    private void addTextListener(EditText ET) {
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSignInIfReady();
            }
        });
    }

    private void enableRegistration() {
        registration.setEnabled(true);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teacherRB.isChecked()) {
                    RegistrationTeacherFragment fragment = new RegistrationTeacherFragment();
                    fragment.show(getSupportFragmentManager(), "Teacher registration");
                } else if (studentRB.isChecked()) {
                    RegistrationStudentFragment fragment = new RegistrationStudentFragment();
                    fragment.show(getSupportFragmentManager(), "Student registration");
                }
            }
        });
    }

    private void enableSignInIfReady() {
        if (!emailET.getText().toString().isEmpty() &&
            !passwordET.getText().toString().isEmpty() &&
            (teacherRB.isChecked() || studentRB.isChecked()))
        {
            signIn.setEnabled(true);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}