package com.example.useme.app.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.app.teacher.TeacherActivity;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.Teacher;
import com.example.useme.data.model.invite.Application;
import com.example.useme.data.model.invite.ApplicationPK;
import com.example.useme.retrofit.MyErrorMessage;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.ApplicationApi;
import com.example.useme.retrofit.api.GroupApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendApplicationFragment extends Fragment {

    private ApplicationApi applicationApi;


    private TextInputEditText groupIdTIET;
    private TextInputLayout groupIdTIL;
    private TextInputEditText passwordTIET;
    private TextInputLayout passwordTIL;

    private Button sendButton;

    public SendApplicationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        applicationApi = retrofitService.getRetrofit().create(ApplicationApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_application, container, false);

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        groupIdTIET = view.findViewById(R.id.TIET_GroupId);
        groupIdTIL = view.findViewById(R.id.TIL_GroupId);

        passwordTIET = view.findViewById(R.id.TIET_Password);
        passwordTIL = view.findViewById(R.id.TIL_Password);

        sendButton = view.findViewById(R.id.sendApplicationButton);
        sendButton.setEnabled(false);

        addTextListener(groupIdTIL, groupIdTIET);
        addTextListener(passwordTIL, passwordTIET);

        return view;
    }

    private void addTextListener(TextInputLayout TIL, TextInputEditText ET) {
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!ET.getText().toString().isEmpty()) {
                    TIL.setHelperText(null);
                } else {
                    TIL.setHelperText("Нужно заполнить");
                }
                enableSendButtonIfReady();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSendButtonIfReady();
            }
        });
    }

    private boolean isIDCorrect() {
        String id = groupIdTIET.getText().toString();
        if (!id.matches("[0-9]+")) {
            if (id.isEmpty()) {
                groupIdTIL.setHelperText("Нужно заполнить");
            } else {
                groupIdTIL.setHelperText("ID может состоять только из цифр");
            }
            return false;
        } else {
            groupIdTIL.setHelperText(null);
            return true;
        }
    }

    private void enableSendButtonIfReady() {
        if (isIDCorrect() &&
            !passwordTIET.getText().toString().isEmpty())
        {
            sendButton.setEnabled(true);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String groupId = groupIdTIET.getText().toString();
                    String password = passwordTIET.getText().toString();

                    Group group = new Group();
                    group.setId(Long.valueOf(groupId));
                    group.setPassword(password);

                    ApplicationPK pk = new ApplicationPK();
                    pk.setGroup(group);
                    pk.setStudent(new Student(StudentActivity.id));

                    Call<Application> callSendApplication = applicationApi.sendApplication(pk);
                    callSendApplication.enqueue(new Callback<Application>() {
                        @Override
                        public void onResponse(Call<Application> call, Response<Application> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getLayoutInflater().getContext(), "Заявка успешно отправлена", Toast.LENGTH_SHORT).show();
                                Log.d("RESPONSE", response.toString());
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                groupIdTIET.setText("");
                                passwordTIET.setText("");
                                sendButton.setEnabled(false);
                                Log.d("ERROR_RESPONSE", response.toString());
                                if (response.code() == 409 || response.code() == 404 || response.code() == 401) {
                                    Gson gson = new GsonBuilder().create();
                                    MyErrorMessage message;
                                    try {
                                        message = gson.fromJson(response.errorBody().string(), MyErrorMessage.class);
                                        Toast.makeText(getLayoutInflater().getContext(), message.getError(), Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        Toast.makeText(getLayoutInflater().getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Application> call, Throwable t) {
                            Log.d("CALL", t.toString());
                            Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            sendButton.setEnabled(false);
        }
    }
}