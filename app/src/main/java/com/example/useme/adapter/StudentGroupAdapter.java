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
import com.example.useme.app.group.GroupActivity;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Teacher;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentGroupAdapter extends RecyclerView.Adapter<StudentGroupAdapter.GroupHolder>  {
    private List<Group> groups = new ArrayList<>();

    @NonNull
    @Override
    public StudentGroupAdapter.GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_group_item, parent, false);

        return new StudentGroupAdapter.GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentGroupAdapter.GroupHolder holder, int position) {

        Group group = groups.get(position);
        holder.id = group.getId();
        holder.nameTV.setText(group.getName());
        holder.idTV.setText("#"+group.getId());
        holder.targetSubjectTV.setText(group.getTargetSubject());
        holder.descriptionTV.setText(group.getDescription());
        Teacher t = group.getTeacher();
        if ((t.getLastName() == null || t.getLastName().isEmpty()) &&
            (t.getFirstName() == null || t.getFirstName().isEmpty()) &&
            (t.getMiddleName() == null || t.getMiddleName().isEmpty())) {
            holder.fullNameTeacherTV.setText("#"+t.getId());
        } else {
            holder.fullNameTeacherTV.setText(t.getLastName() + " " + t.getFirstName() + " " + t.getLastName());
        }
        holder.emailTeacherTV.setText(t.getEmail());
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public class GroupHolder extends RecyclerView.ViewHolder {

        private Long id;
        private TextView nameTV;
        private TextView idTV;
        private TextView targetSubjectTV;
        private TextView descriptionTV;
        private TextView fullNameTeacherTV;
        private TextView emailTeacherTV;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.student_group_item_name);
            idTV = itemView.findViewById(R.id.student_group_item_id);
            targetSubjectTV = itemView.findViewById(R.id.student_group_item_target_subject);
            descriptionTV = itemView.findViewById(R.id.student_group_item_description);
            fullNameTeacherTV = itemView.findViewById(R.id.student_group_full_teacher);
            emailTeacherTV = itemView.findViewById(R.id.student_group_email_teacher);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetrofitService retrofitService = new RetrofitService();
                    GroupApi groupApi = retrofitService.getRetrofit().create(GroupApi.class);
                    Call<Group> callGetGroup = groupApi.findGroup(id);
                    callGetGroup.enqueue(new Callback<Group>() {
                        @Override
                        public void onResponse(Call<Group> call, Response<Group> response) {
                            Group group = response.body();
                            Log.d("RESPONSE", group.toString());
                            GroupActivity.countHomeworks = group.getCountHomeworks();
                            GroupActivity.countMembers = group.getCountMembers();

                            Bundle bundle = new Bundle();
                            bundle.putLong(GroupActivity.KEY_ID, id);
                            bundle.putInt(GroupActivity.KEY_COUNT_MEMBERS, group.getCountMembers());
                            bundle.putInt(GroupActivity.KEY_COUNT_HOMEWORKS, group.getCountHomeworks());

                            Navigation.findNavController(itemView).navigate(R.id.action_studentGroupsFragment_to_homeworkFragment, bundle);
                        }

                        @Override
                        public void onFailure(Call<Group> call, Throwable t) {
                        }
                    });
                }
            });
        }
    }
}
