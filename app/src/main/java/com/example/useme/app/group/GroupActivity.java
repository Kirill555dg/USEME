package com.example.useme.app.group;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.useme.R;
import com.example.useme.data.model.Group;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {

    public static final String KEY_ID = "ID";
    public static final String KEY_COUNT_MEMBERS = "COUNT_MEMBERS";
    public static final String KEY_COUNT_HOMEWORKS = "COUNT_HOMEWORKS";

    public static Long id;
    public static Integer countHomeworks;
    public static Integer countMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getIntent().getLongExtra(KEY_ID, -1L);
        countHomeworks = getIntent().getIntExtra(KEY_COUNT_HOMEWORKS, -1);
        countMembers = getIntent().getIntExtra(KEY_COUNT_MEMBERS, -1);


        Log.d("DEBUUUG", String.valueOf(id));

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group);

        BottomNavigationView bottomNavigationView = findViewById(R.id.group_bottom_nav_menu);
        NavController navController = Navigation.findNavController(this, R.id.group_nav_graph);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(navController.getGraph())
                .build();
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_group);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}