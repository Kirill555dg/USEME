package com.example.useme.app.group;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.useme.R;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class CreateHomeworkFragment extends Fragment {

    MaterialButton pickDeadlineButton;
    LocalDate deadline;

    public CreateHomeworkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_homework, container, false);
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        pickDeadlineButton = view.findViewById(R.id.pickDeadlineButton);
        pickDeadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeadlinePickerDialog();
            }
        });

        Button addTasksButton = view.findViewById(R.id.add_tasks_button);
        addTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_createHomeworkFragment_to_addTaskFragment);
            }
        });

        return view;
    }

    private void openDeadlinePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                deadline = LocalDate.of(year, month+1, dayOfMonth);
                pickDeadlineButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                pickDeadlineButton.setStrokeColorResource(R.color.colorPrimary);
                pickDeadlineButton.setText("Указать дедлайн");
                Toast.makeText(getContext(), "Выбранный дедлайн: " + deadline.toString(), Toast.LENGTH_LONG).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Выберите дедлайн");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "сохранить", dialog);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "выйти", dialog);
        dialog.show();
    }
}