package com.example.useme.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.app.student.StudentActivity;
import com.example.useme.app.teacher.TeacherActivity;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder>  {
    private List<Group> groups = new ArrayList<>();

    @NonNull
    @Override
    public GroupAdapter.GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);

        return new GroupAdapter.GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupHolder holder, int position) {

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
                    Bundle bundle = new Bundle();
                    bundle.putLong("ID", id);
                    Navigation.findNavController(itemView).navigate(R.id.action_teacherGroupsFragment_to_groupActivity, bundle);
                }
            });
        }
    }
}
