package com.example.useme.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.app.group.GroupActivity;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.invite.Application;
import com.example.useme.data.model.invite.ApplicationPK;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.ApplicationApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationHolder> {

    private List<Student> students = new ArrayList<>();
    private Context context;

    public ApplicationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ApplicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_item, parent, false);

        return new ApplicationAdapter.ApplicationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationHolder holder, int position) {

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
    }

    public void setApplications(List<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public class ApplicationHolder extends RecyclerView.ViewHolder {

        private Long id;
        private TextView genderTV;
        private TextView idTV;
        private TextView fullnameTV;
        private TextView emailTV;
        private TextView ageTV;

        public ApplicationHolder(@NonNull View itemView) {
            super(itemView);

            genderTV = itemView.findViewById(R.id.student_item_gender);
            idTV = itemView.findViewById(R.id.student_item_id);

            fullnameTV = itemView.findViewById(R.id.student_item_fullname);

            emailTV = itemView.findViewById(R.id.student_item_email);
            ageTV = itemView.findViewById(R.id.student_item_age);

            Button acceptButton = itemView.findViewById(R.id.acceptButton);
            Button declineButton = itemView.findViewById(R.id.declineButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetrofitService retrofitService = new RetrofitService();
                    ApplicationApi applicationApi = retrofitService.getRetrofit().create(ApplicationApi.class);

                    Group group = new Group(GroupActivity.id);
                    Student student = new Student(id);
                    ApplicationPK pk = new ApplicationPK(student, group);
                    Call<Application> callAccept = applicationApi.acceptApplication(pk);
                    callAccept.enqueue(new Callback<Application>() {
                        @Override
                        public void onResponse(Call<Application> call, Response<Application> response) {
                            Toast.makeText(context, "Заявка отклонена", Toast.LENGTH_SHORT);
                            students.remove(getAbsoluteAdapterPosition());
                            notifyItemRemoved(getAbsoluteAdapterPosition());
                        }

                        @Override
                        public void onFailure(Call<Application> call, Throwable t) {
                            Toast.makeText(context, "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT);
                            Log.d("FAIL", t.toString());
                        }
                    });
                }
            });

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetrofitService retrofitService = new RetrofitService();
                    ApplicationApi applicationApi = retrofitService.getRetrofit().create(ApplicationApi.class);
                    Group group = new Group(GroupActivity.id);
                    Student student = new Student(id);
                    ApplicationPK pk = new ApplicationPK(student, group);
                    Log.d("DEBUG", pk.toString());

                    Call<Void> callDecline = applicationApi.deleteApplication(pk);
                    callDecline.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(context, "Заявка принята", Toast.LENGTH_SHORT);
                            students.remove(getAbsoluteAdapterPosition());
                            notifyItemRemoved(getAbsoluteAdapterPosition());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT);
                            Log.d("FAIL", t.toString());
                        }
                    });
                }
            });
        }
    }

}