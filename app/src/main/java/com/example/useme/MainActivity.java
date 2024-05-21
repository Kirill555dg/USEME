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
    public static final String TEACHER_ROLE = "TEACHER";
    public static final String STUDENT_ROLE = "STUDENT";
    public static final String SHARED_PREF_NAME = "user_pref";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROLE = "role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goNext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        goNext();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void goNext() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String role = sharedPreferences.getString(KEY_ROLE, null);

        if (email == null && role == null) {
            Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        } else if (email != null && role != null) {
            if (role.equals(TEACHER_ROLE)) {
                Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                startActivity(intent);
            } else if (role.equals(STUDENT_ROLE)) {
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        }
    }
}