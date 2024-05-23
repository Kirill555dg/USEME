package com.example.useme.app.teacher;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.useme.R;
import com.example.useme.app.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherActivity extends AppCompatActivity {

    public static Long id;
    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_NAME, MODE_PRIVATE);
        id = sharedPreferences.getLong(MainActivity.KEY_ID, -1);
        email = sharedPreferences.getString(MainActivity.KEY_EMAIL, null);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher);

        BottomNavigationView bottomNavigationView = findViewById(R.id.teacher_bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.teacher_nav_graph);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(navController.getGraph())
                .build();
    }
}