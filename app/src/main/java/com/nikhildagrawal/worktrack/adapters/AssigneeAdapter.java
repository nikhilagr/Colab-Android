package com.nikhildagrawal.worktrack.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Contact;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssigneeAdapter extends RecyclerView.Adapter<AssigneeAdapter.AssigneeViewHolder> {

    List<Contact> assigneeList;
    Context mContext;

    public AssigneeAdapter(Context context){
        mContext = context;
    }


    @NonNull
    @Override
    public AssigneeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_assignee_list_item,parent,false);
        AssigneeViewHolder holder = new AssigneeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AssigneeViewHolder holder, final int position) {

        holder.mTextViewName.setText(assigneeList.get(position).getName());

        holder.mCheckbox.setChecked(assigneeList.get(position).isSelected());

        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    assigneeList.get(position).setSelected(true);

                }else{
                    assigneeList.get(position).setSelected(false);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(assigneeList == null){
            return 0;
        }
        return assigneeList.size();

    }

    public class AssigneeViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckbox;
        private TextView mTextViewName;

        public AssigneeViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckbox = itemView.findViewById(R.id.assignee_checkbox);
            mTextViewName = itemView.findViewById(R.id.assignee_name);
        }

    }

    public void setAssigneeList(List<Contact> list){
        assigneeList = list;
        notifyDataSetChanged();
    }

}
