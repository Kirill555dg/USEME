package com.example.useme.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.data.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {

    private List<Student> students = new ArrayList<>();

    @NonNull
    @Override
    public StudentAdapter.StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);



        return new StudentAdapter.StudentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.StudentHolder holder, int position) {

        Student student = students.get(position);

        Boolean gender = student.getMale();
        holder.genderTV.setText(gender ? "Ученик" : "Ученица");
        holder.id = student.getId();
        if ((student.getLastName() == null || student.getLastName().isEmpty()) && (student.getFirstName() == null || student.getFirstName().isEmpty())){
            holder.fullnameTV.setVisibility(View.GONE);
        }
        holder.fullnameTV.setText(student.getLastName() + " " + student.getFirstName());
        holder.emailTV.setText(student.getEmail());
        holder.ageTV.setText("" + student.getAge());
        holder.idTV.setText("#" + holder.id);
        holder.completeHomeworksTV.setText(student.getCountCompleteHomeworks() +"/"+ student.getCountHomeworks());
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public class StudentHolder extends RecyclerView.ViewHolder {

        private Long id;
        private TextView genderTV;
        private TextView idTV;
        private TextView fullnameTV;
        private TextView emailTV;
        private TextView ageTV;
        private TextView completeHomeworksTV;

        public StudentHolder(@NonNull View itemView) {
            super(itemView);

            genderTV = itemView.findViewById(R.id.student_item_gender);
            idTV = itemView.findViewById(R.id.student_item_id);

            fullnameTV = itemView.findViewById(R.id.student_item_fullname);

            emailTV = itemView.findViewById(R.id.student_item_email);
            ageTV = itemView.findViewById(R.id.student_item_age);
            completeHomeworksTV = itemView.findViewById(R.id.student_item_complete_homeworks);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("ID", id);
                    Navigation.findNavController(itemView).navigate(R.id.action_groupPageFragment_to_statisticFragment2, bundle);
                }
            });
        }
    }

}
