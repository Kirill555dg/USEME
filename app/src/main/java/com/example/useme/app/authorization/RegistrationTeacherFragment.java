package com.example.useme.app.authorization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
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

import com.example.useme.app.MainActivity;
import com.example.useme.R;
import com.example.useme.data.model.Teacher;
import com.example.useme.retrofit.MyErrorMessage;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.TeacherApi;
import com.example.useme.app.teacher.TeacherActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationTeacherFragment extends DialogFragment {

    SharedPreferences sharedPreferences;

    private TeacherApi teacherApi;
    private EditText firstnameET;
    private EditText lastnameET;
    private EditText middlenameET;
    private EditText emailET;
    private TextInputLayout emailTIL;
    private EditText passwordET;
    private TextInputLayout passwordTIL;
    private EditText passwordCheckET;
    private TextInputLayout passwordCheckTIL;

    private DatePicker dateOfBirthDP;
    private TextView genderTV;
    private RadioButton maleRB;
    private RadioButton femaleRB;

    private Button registerButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_registration_teacher, container, false);

        firstnameET = view.findViewById(R.id.teacher_registration_form_Firstname);
        lastnameET = view.findViewById(R.id.teacher_registration_form_Lastname);
        middlenameET = view.findViewById(R.id.teacher_registration_form_MiddleName);

        emailET = view.findViewById(R.id.teacher_registration_form_Email);
        emailTIL = view.findViewById(R.id.teacher_containerEmail);

        passwordET = view.findViewById(R.id.teacher_registration_form_Password);
        passwordTIL = view.findViewById(R.id.teacher_containerPassword);

        passwordCheckET = view.findViewById(R.id.teacher_registration_form_PasswordCheck);
        passwordCheckTIL = view.findViewById(R.id.teacher_containerPasswordCheck);

        dateOfBirthDP = view.findViewById(R.id.teacher_DP);

        genderTV = view.findViewById(R.id.teacher_genderTV);
        maleRB = view.findViewById(R.id.teacher_RB_male);
        femaleRB = view.findViewById(R.id.teacher_RB_female);

        registerButton = view.findViewById(R.id.teacher_button_register);
        registerButton.setEnabled(false);

        RetrofitService retrofitService = new RetrofitService();
        teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);

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
                    enableRegisterButtonIfReady();
                }
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPasswordCorrect();
                isPasswordCheckCorrect();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isPasswordCorrect() &&  isPasswordCheckCorrect()) {
                    enableRegisterButtonIfReady();
                }
            }
        });

        passwordCheckET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPasswordCheckCorrect();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isPasswordCheckCorrect()) {
                    enableRegisterButtonIfReady();
                }
            }
        });

        addRadioButtonListener(maleRB);
        addRadioButtonListener(femaleRB);

        dateOfBirthDP.setMaxDate(new Date().getTime());

        return view;
    }

    private boolean isPasswordCorrect() {
        String password = passwordET.getText().toString();
        if (password.length() < 6) {
            passwordTIL.setHelperText("Минимальная длина пароля - 6 символов");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            passwordTIL.setHelperText("Пароль должен содержать прописной латинский символ");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            passwordTIL.setHelperText("Пароль должен содержать строчный латинский символ");
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            passwordTIL.setHelperText("Пароль должен содержать цифру");
            return false;
        }
        if (!password.matches(".*[~!@#$%^&*()+`'\";:<>/\\\\|].*")) {
            passwordTIL.setHelperText("Пароль должен содержать специальный символ");
            return false;
        }
        passwordTIL.setHelperText(null);
        return true;
    }

    private boolean isPasswordCheckCorrect() {
        String password = passwordET.getText().toString();
        String passwordCheck = passwordCheckET.getText().toString();
        if (!password.equals(passwordCheck)) {
            passwordCheckTIL.setHelperText("Пароли не совпадают");
            return false;
        } else {
            passwordCheckTIL.setHelperText(null);
            return true;
        }
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
                genderTV.setTextColor(Color.GRAY);
                genderTV.setText("Пол");
                enableRegisterButtonIfReady();
            }
        });
    }


    private void enableRegisterButtonIfReady() {

        if (isPasswordCorrect() && isPasswordCheckCorrect() && isEmailCorrect()
                && (maleRB.isChecked() || femaleRB.isChecked())) {
            registerButton.setEnabled(true);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String firstname = firstnameET.getText().toString();
                    String lastname = lastnameET.getText().toString();
                    String middlename = middlenameET.getText().toString();
                    LocalDate dateOfBirth = LocalDate.of(dateOfBirthDP.getYear(), dateOfBirthDP.getMonth() + 1, dateOfBirthDP.getDayOfMonth());
                    Boolean isMale = maleRB.isChecked();
                    String email = emailET.getText().toString();
                    String password = passwordET.getText().toString();

                    Teacher teacher = new Teacher();
                    teacher.setFirstName(firstname);
                    teacher.setLastName(lastname);
                    teacher.setMiddleName(middlename);
                    teacher.setDateOfBirth(dateOfBirth.toString());
                    teacher.setMale(isMale);
                    teacher.setEmail(email);
                    teacher.setPassword(password);
                    Log.d("TEACHER", teacher.toString());
                    Call<Teacher> callRegisterTeacher = teacherApi.register(teacher);

                    callRegisterTeacher.enqueue(new Callback<Teacher>() {
                        @Override
                        public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                            if (response.isSuccessful()) {
                                sharedPreferences = getContext()
                                        .getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(MainActivity.KEY_EMAIL, email);
                                editor.putString(MainActivity.KEY_PASSWORD, password);
                                editor.putString(MainActivity.KEY_ROLE, MainActivity.TEACHER_ROLE);
                                editor.apply();

                                Toast.makeText(getLayoutInflater().getContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                                Log.d("RESPONSE", response.toString());

                                Intent intent = new Intent(getActivity(), TeacherActivity.class);
                                startActivity(intent);
                                getActivity().finish();
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
                }
            });
        } else {
            registerButton.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics newDisplayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(newDisplayMetrics);
        getDialog().getWindow().setLayout(newDisplayMetrics.widthPixels, (int)(newDisplayMetrics.heightPixels*0.7));
    }
}