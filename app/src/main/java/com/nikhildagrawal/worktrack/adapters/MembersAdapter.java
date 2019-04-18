package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {



    List<User> userList;
    Context mContext;

    public MembersAdapter(Context context){
        mContext = context;
        //userList = list;
    }


    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_members_list_item,parent,false);
        MembersViewHolder holder = new MembersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {


        holder.mFName.setText(userList.get(position).getFirtst_name());
        holder.mLName.setText(userList.get(position).getLast_name());

    }

    @Override
    public int getItemCount() {

        if(userList == null)
        {
            return 0;
        }
        return userList.size();
    }

    public class MembersViewHolder extends RecyclerView.ViewHolder {

        TextView mFName,mLName;
        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
            mFName = itemView.findViewById(R.id.first_name);
            mLName = itemView.findViewById(R.id.last_name);
        }
    }



    public void updateUserInMembersAdpater(User user){

        userList.add(user);
        notifyDataSetChanged();
    }

    public void setUserList(List<User> list){
        userList = list;
        notifyDataSetChanged();
    }
}
