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
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherGroupAdapter extends RecyclerView.Adapter<TeacherGroupAdapter.GroupHolder>  {
    private List<Group> groups = new ArrayList<>();

    @NonNull
    @Override
    public TeacherGroupAdapter.GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_group_item, parent, false);

        return new TeacherGroupAdapter.GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherGroupAdapter.GroupHolder holder, int position) {

        Group group = groups.get(position);
        holder.id = group.getId();
        holder.nameTV.setText(group.getName());
        holder.idTV.setText("#"+group.getId());
        holder.targetSubjectTV.setText(group.getTargetSubject());
        holder.descriptionTV.setText(group.getDescription());
        holder.countMembersTV.setText(String.valueOf(group.getCountMembers()));
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
        private TextView countMembersTV;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.group_item_name);
            idTV = itemView.findViewById(R.id.group_item_id);
            targetSubjectTV = itemView.findViewById(R.id.group_item_target_subject);
            descriptionTV = itemView.findViewById(R.id.group_item_description);
            countMembersTV = itemView.findViewById(R.id.group_count_members);

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
                            bundle.putString(GroupActivity.KEY_NAME, group.getName());
                            bundle.putString(GroupActivity.KEY_TARGET_SUBJECT, group.getTargetSubject());
                            bundle.putString(GroupActivity.KEY_DESCRIPTION, group.getDescription());
                            Navigation.findNavController(itemView).navigate(R.id.action_teacherGroupsFragment_to_groupActivity, bundle);
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
