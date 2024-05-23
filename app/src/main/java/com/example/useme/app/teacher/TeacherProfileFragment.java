package com.example.useme.app.teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.useme.R;
import com.example.useme.app.MainActivity;
import com.example.useme.app.authorization.AuthorizationActivity;
import com.example.useme.data.model.Teacher;
import com.example.useme.retrofit.MyErrorMessage;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.TeacherApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherProfileFragment extends Fragment {

    
    private Button exitButton;
    private Button changeInfoButton;
    private Button saveInfoButton;


    private EditText firstnameET;
    private EditText lastnameET;
    private EditText middlenameET;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private RadioGroup RG;
    private DatePicker dateOfBirthDP;

    private TextView firstnameTV;
    private TextView lastnameTV;
    private TextView middlenameTV;
    private TextView genderTV;
    private TextView dateOfBirthTV;

    private TeacherApi teacherApi;
    private SharedPreferences sharedPreferences;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public TeacherProfileFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        firstnameTV = view.findViewById(R.id.teacher_profile_firstname);
        lastnameTV = view.findViewById(R.id.teacher_profile_lastname);
        middlenameTV = view.findViewById(R.id.teacher_profile_middlename);
        genderTV = view.findViewById(R.id.teacher_profile_gender);
        dateOfBirthTV = view.findViewById(R.id.teacher_profile_date_of_birth);

        firstnameET = view.findViewById(R.id.teacher_profile_change_firstname);
        lastnameET = view.findViewById(R.id.teacher_profile_change_lastname);
        middlenameET = view.findViewById(R.id.teacher_profile_change_middlename);
        maleRB = view.findViewById(R.id.teacher_profile_change_gender_male);
        femaleRB = view.findViewById(R.id.teacher_profile_change_gender_female);
        RG = view.findViewById(R.id.teacher_change_RG);
        dateOfBirthDP = view.findViewById(R.id.teacher_profile_change_date_of_birth);

        changeInfoButton = view.findViewById(R.id.teacher_profile_change_info_button);
        exitButton = view.findViewById(R.id.teacher_profile_exit_button);
        saveInfoButton = view.findViewById(R.id.teacher_profile_save_info_button);
        saveInfoButton.setEnabled(false);

        RetrofitService retrofitService = new RetrofitService();
        teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);

        onUpdateView();

        addTextListener(firstnameET);
        addTextListener(lastnameET);
        addTextListener(middlenameET);
        changeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceViews(firstnameET, firstnameTV);
                replaceViews(lastnameET, lastnameTV);
                replaceViews(middlenameET, middlenameTV);

                genderTV.setVisibility(View.GONE);
                RG.setVisibility(View.VISIBLE);

                dateOfBirthTV.setVisibility(View.GONE);
                dateOfBirthDP.setVisibility(View.VISIBLE);

                saveInfoButton.setEnabled(true);
                saveInfoButton.setVisibility(View.VISIBLE);
                changeInfoButton.setEnabled(false);
                changeInfoButton.setVisibility(View.INVISIBLE);
            }
        });

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstnameET.getText().toString();
                String lastname = lastnameET.getText().toString();
                String middlename = middlenameET.getText().toString();
                LocalDate dateOfBirth = LocalDate.of(dateOfBirthDP.getYear(), dateOfBirthDP.getMonth() + 1, dateOfBirthDP.getDayOfMonth());
                Boolean isMale = maleRB.isChecked();

                String email = sharedPreferences.getString(MainActivity.KEY_EMAIL, null);
                String password = sharedPreferences.getString(MainActivity.KEY_PASSWORD, null);

                Teacher teacher = new Teacher();
                teacher.setFirstName(firstname);
                teacher.setLastName(lastname);
                teacher.setMiddleName(middlename);
                teacher.setDateOfBirth(dateOfBirth.toString());
                teacher.setMale(isMale);
                teacher.setEmail(email);
                teacher.setPassword(password);
                Log.d("TEACHER", teacher.toString());
                Call<Teacher> callRegisterTeacher = teacherApi.changeInfo(teacher);

                callRegisterTeacher.enqueue(new Callback<Teacher>() {
                    @Override
                    public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                        if (response.isSuccessful()) {
                            sharedPreferences = getContext()
                                    .getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            Teacher realTeacher = response.body();
                            editor.putString(MainActivity.KEY_FIRSTNAME, realTeacher.getFirstName());
                            editor.putString(MainActivity.KEY_LASTNAME, realTeacher.getLastName());
                            editor.putString(MainActivity.KEY_MIDDLENAME, realTeacher.getMiddleName());
                            editor.putString(MainActivity.KEY_DATE_OF_BIRTH, realTeacher.getDateOfBirth());
                            editor.putBoolean(MainActivity.KEY_GENDER, realTeacher.getMale());
                            editor.apply();

                            Toast.makeText(getLayoutInflater().getContext(), "Данные успешно изменены", Toast.LENGTH_SHORT).show();
                            Log.d("RESPONSE", response.toString());
                        } else {
                            Log.d("ERROR_RESPONSE", response.toString());
                            if (response.code() == 409) {
                                Gson gson = new GsonBuilder().create();
                                MyErrorMessage message;
                                try {
                                    message = gson.fromJson(response.errorBody().string(), MyErrorMessage.class);
                                    Toast.makeText(getLayoutInflater().getContext(), message.getError(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    Toast.makeText(getLayoutInflater().getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getLayoutInflater().getContext(), response.message(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Teacher> call, Throwable t) {
                        Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                        Log.d("FAIL", t.toString());
                    }
                });

                reverseReplaceViews(firstnameET, firstnameTV);
                reverseReplaceViews(lastnameET, lastnameTV);
                reverseReplaceViews(middlenameET, middlenameTV);

                RG.setVisibility(View.GONE);
                genderTV.setVisibility(View.VISIBLE);

                dateOfBirthDP.setVisibility(View.GONE);
                dateOfBirthTV.setVisibility(View.VISIBLE);

                changeInfoButton.setEnabled(true);
                changeInfoButton.setVisibility(View.VISIBLE);
                saveInfoButton.setEnabled(false);
                saveInfoButton.setVisibility(View.INVISIBLE);

                onUpdateView();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
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

            }
        });
    }

    private void replaceViews(EditText ET, TextView TV) {
        ET.setText(TV.getText());
        TV.setVisibility(View.GONE);
        ET.setVisibility(View.VISIBLE);
    }

    private void reverseReplaceViews(EditText ET, TextView TV) {
        TV.setText(ET.getText());
        ET.setVisibility(View.GONE);
        TV.setVisibility(View.VISIBLE);
    }



    private void onUpdateView() {
        sharedPreferences = getContext().getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        firstnameTV.setText(sharedPreferences.getString(MainActivity.KEY_FIRSTNAME, "отсутствует"));
        lastnameTV.setText(sharedPreferences.getString(MainActivity.KEY_LASTNAME, "отсутствует"));
        middlenameTV.setText(sharedPreferences.getString(MainActivity.KEY_MIDDLENAME, "отсутствует"));
        Boolean isMale = sharedPreferences.getBoolean(MainActivity.KEY_GENDER, true);
        genderTV.setText(isMale ? "мужской" : "женский");
        maleRB.setChecked(isMale);
        femaleRB.setChecked(!isMale);
        String dateOfBirth = sharedPreferences.getString(MainActivity.KEY_DATE_OF_BIRTH, "отсутствует");
        dateOfBirthTV.setText(dateOfBirth);
        dateOfBirthDP.setMaxDate(new Date().getTime());
    }
}