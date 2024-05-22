package com.example.useme.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.useme.R;
import com.example.useme.app.student.StudentActivity;
import com.example.useme.data.model.Student;
import com.example.useme.retrofit.MyErrorMessage;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.StudentApi;
import com.example.useme.retrofit.api.TaskApi;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterTaskFragment extends DialogFragment {

    private TaskApi taskApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_filter_task, container, false);


        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setGravity(Gravity.TOP);
    }
}

