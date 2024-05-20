package com.example.useme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.useme.authorization.AuthorizationActivity;
import com.example.useme.student.StudentActivity;
import com.example.useme.teacher.TeacherActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String role = sharedPreferences.getString(KEY_ROLE, null);

        if (email == null && role == null) {
            Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        } else if (email != null && role != null) {
            if (role.equals("TEACHER")) {
                Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                startActivity(intent);
            } else if (role.equals("STUDENT")) {
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        }
    }
}