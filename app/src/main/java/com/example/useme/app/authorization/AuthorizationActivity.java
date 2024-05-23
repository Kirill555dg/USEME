package com.example.useme.app.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.useme.app.MainActivity;
import com.example.useme.R;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.Teacher;
import com.example.useme.retrofit.MyErrorMessage;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.StudentApi;
import com.example.useme.retrofit.api.TeacherApi;
import com.example.useme.app.student.StudentActivity;
import com.example.useme.app.teacher.TeacherActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private TeacherApi teacherApi;
    private StudentApi studentApi;

    private TextView roleTV;
    private EditText emailET;
    private TextInputLayout emailTIL;
    private EditText passwordET;
    private TextInputLayout passwordTIL;
    private RadioButton teacherRB;
    private RadioButton studentRB;
    private Button signIn;
    private Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);

        roleTV = findViewById(R.id.roleTV);
        teacherRB = findViewById(R.id.RBT);
        studentRB = findViewById(R.id.RBS);

        emailET = findViewById(R.id.auth_Email);
        emailTIL = findViewById(R.id.containerEmail);
        passwordET = findViewById(R.id.auth_Password);
        passwordTIL = findViewById(R.id.containerPassword);

        signIn = findViewById(R.id.button_sign_in);
        registration = findViewById(R.id.button_registration);

        signIn.setEnabled(false);
        registration.setEnabled(false);

        RetrofitService retrofitService = new RetrofitService();
        teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);
        studentApi = retrofitService.getRetrofit().create(StudentApi.class);

        addRadioButtonListener(teacherRB);
        addRadioButtonListener(studentRB);

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmailCorrect();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEmailCorrect()) {
                    enableSignInIfReady();
                }
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSignInIfReady();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSignInIfReady();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isEmailCorrect() {
        String email = emailET.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTIL.setHelperText("Неверный формат почты");
            return false;
        } else {
            emailTIL.setHelperText(null);
            return true;
        }
    }

    private void addRadioButtonListener(RadioButton RB) {
        RB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roleTV.setTextColor(Color.GRAY);
                roleTV.setText("Тип акканута");
                enableRegistration();
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
                    fragment.show(getSupportFragmentManager(), fragment.getClass().getName());
                } else if (studentRB.isChecked()) {
                    RegistrationStudentFragment fragment = new RegistrationStudentFragment();
                    fragment.show(getSupportFragmentManager(), fragment.getClass().getName());
                }
            }
        });
    }

    private void enableSignInIfReady() {
        if (!isEmailCorrect() &&
            !passwordET.getText().toString().isEmpty() &&
            (teacherRB.isChecked() || studentRB.isChecked()))
        {
            signIn.setEnabled(true);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (studentRB.isChecked()) {
                        signInStudent();
                    } else {
                        signInTeacher();
                    }

                }
            });
        } else {
            signIn.setEnabled(false);
        }
    }

    private void signInTeacher() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        Teacher teacher = new Teacher();
        teacher.setEmail(email);
        teacher.setPassword(password);
        Call<Teacher> callAuthTeacher = teacherApi.auth(teacher);
        callAuthTeacher.enqueue(new Callback<Teacher>() {
            @Override
            public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                if (response.isSuccessful()) {
                    sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Teacher realTeacher = response.body();
                    editor.putString(MainActivity.KEY_FIRSTNAME, realTeacher.getFirstName());
                    editor.putString(MainActivity.KEY_LASTNAME, realTeacher.getLastName());
                    editor.putString(MainActivity.KEY_MIDDLENAME, realTeacher.getMiddleName());
                    editor.putString(MainActivity.KEY_DATE_OF_BIRTH, realTeacher.getDateOfBirth());
                    editor.putBoolean(MainActivity.KEY_GENDER, realTeacher.getMale());
                    editor.putString(MainActivity.KEY_EMAIL, email);
                    editor.putString(MainActivity.KEY_PASSWORD, password);
                    editor.putString(MainActivity.KEY_ROLE, MainActivity.TEACHER_ROLE);
                    editor.apply();

                    Toast.makeText(getLayoutInflater().getContext(), "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE", response.toString());

                    Intent intent = new Intent(AuthorizationActivity.this, TeacherActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("ERROR_RESPONSE", response.toString());
                    if (response.code() == 401 || response.code() == 404) {
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
    }

    private void signInStudent() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        Student student = new Student();
        student.setEmail(email);
        student.setPassword(password);
        Call<Student> callAuthStudent = studentApi.auth(student);
        callAuthStudent.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Student realStudent = response.body();
                    editor.putString(MainActivity.KEY_FIRSTNAME, realStudent.getFirstName());
                    editor.putString(MainActivity.KEY_LASTNAME, realStudent.getLastName());
                    editor.putString(MainActivity.KEY_DATE_OF_BIRTH, realStudent.getDateOfBirth());
                    editor.putBoolean(MainActivity.KEY_GENDER, realStudent.getMale());
                    editor.putString(MainActivity.KEY_EMAIL, email);
                    editor.putString(MainActivity.KEY_PASSWORD, password);
                    editor.putString(MainActivity.KEY_ROLE, MainActivity.STUDENT_ROLE);
                    editor.apply();

                    Toast.makeText(getLayoutInflater().getContext(), "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE", response.toString());

                    Intent intent = new Intent(AuthorizationActivity.this, StudentActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("ERROR_RESPONSE", response.toString());
                    if (response.code() == 401 || response.code() == 404) {
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
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });
    }
}