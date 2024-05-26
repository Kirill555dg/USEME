package com.example.useme.app.student;

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
import com.example.useme.data.model.Student;
import com.example.useme.retrofit.MyErrorMessage;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.StudentApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentProfileFragment extends Fragment {

    private Button exitButton;
    private Button changeInfoButton;
    private Button saveInfoButton;


    private EditText firstnameET;
    private EditText lastnameET;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private RadioGroup RG;
    private DatePicker dateOfBirthDP;

    private TextView firstnameTV;
    private TextView lastnameTV;
    private TextView genderTV;
    private TextView dateOfBirthTV;

    private StudentApi studentApi;
    private SharedPreferences sharedPreferences;


    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        firstnameTV = view.findViewById(R.id.student_profile_firstname);
        lastnameTV = view.findViewById(R.id.student_profile_lastname);
        genderTV = view.findViewById(R.id.student_profile_gender);
        dateOfBirthTV = view.findViewById(R.id.student_profile_date_of_birth);

        firstnameET = view.findViewById(R.id.student_profile_change_firstname);
        lastnameET = view.findViewById(R.id.student_profile_change_lastname);
        maleRB = view.findViewById(R.id.student_profile_change_gender_male);
        femaleRB = view.findViewById(R.id.student_profile_change_gender_female);
        RG = view.findViewById(R.id.student_change_RG);
        dateOfBirthDP = view.findViewById(R.id.student_profile_change_date_of_birth);

        changeInfoButton = view.findViewById(R.id.student_profile_change_info_button);
        exitButton = view.findViewById(R.id.student_profile_exit_button);
        saveInfoButton = view.findViewById(R.id.student_profile_save_info_button);
        saveInfoButton.setEnabled(false);

        RetrofitService retrofitService = new RetrofitService();
        studentApi = retrofitService.getRetrofit().create(StudentApi.class);

        onUpdateView();

        changeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceViews(firstnameET, firstnameTV);
                replaceViews(lastnameET, lastnameTV);

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
                LocalDate dateOfBirth = LocalDate.of(dateOfBirthDP.getYear(), dateOfBirthDP.getMonth() + 1, dateOfBirthDP.getDayOfMonth());
                Boolean isMale = maleRB.isChecked();

                String email = sharedPreferences.getString(MainActivity.KEY_EMAIL, null);
                String password = sharedPreferences.getString(MainActivity.KEY_PASSWORD, null);

                Student student = new Student();
                student.setFirstName(firstname);
                student.setLastName(lastname);
                student.setDateOfBirth(dateOfBirth.toString());
                student.setMale(isMale);
                student.setEmail(email);
                student.setPassword(password);
                Log.d("STUDENT", student.toString());
                Call<Student> callChangeInfo = studentApi.changeInfo(student);

                callChangeInfo.enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        if (response.isSuccessful()) {
                            sharedPreferences = getContext()
                                    .getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            Student realStudent = response.body();
                            editor.putString(MainActivity.KEY_FIRSTNAME, realStudent.getFirstName());
                            editor.putString(MainActivity.KEY_LASTNAME, realStudent.getLastName());
                            editor.putString(MainActivity.KEY_DATE_OF_BIRTH, realStudent.getDateOfBirth());
                            editor.putBoolean(MainActivity.KEY_GENDER, realStudent.getMale());
                            editor.apply();

                            Toast.makeText(getLayoutInflater().getContext(), "Данные успешно изменены", Toast.LENGTH_SHORT).show();
                            Log.d("RESPONSE", response.toString());

                            onUpdateView();
                            reverseReplaceViews(firstnameET, firstnameTV);
                            reverseReplaceViews(lastnameET, lastnameTV);

                            RG.setVisibility(View.GONE);
                            genderTV.setVisibility(View.VISIBLE);

                            dateOfBirthDP.setVisibility(View.GONE);
                            dateOfBirthTV.setVisibility(View.VISIBLE);

                            changeInfoButton.setEnabled(true);
                            changeInfoButton.setVisibility(View.VISIBLE);
                            saveInfoButton.setEnabled(false);
                            saveInfoButton.setVisibility(View.INVISIBLE);
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
                    public void onFailure(Call<Student> call, Throwable t) {
                        Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_LONG).show();
                        Log.d("FAIL", t.toString());
                    }
                });
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
        Boolean isMale = sharedPreferences.getBoolean(MainActivity.KEY_GENDER, true);
        genderTV.setText(isMale ? "мужской" : "женский");
        maleRB.setChecked(isMale);
        femaleRB.setChecked(!isMale);
        String dateOfBirth = sharedPreferences.getString(MainActivity.KEY_DATE_OF_BIRTH, "отсутствует");
        dateOfBirthTV.setText(dateOfBirth);
        dateOfBirthDP.setMaxDate(new Date().getTime());
    }
}