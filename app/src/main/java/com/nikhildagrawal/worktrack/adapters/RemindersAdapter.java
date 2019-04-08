package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.interfaces.ReminderClickListner;
import com.nikhildagrawal.worktrack.models.Reminder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder> {

    private Context mContext;
    private List<Reminder> mReminderList;
    private ReminderClickListner mReminderClickListner;


    public RemindersAdapter(Context context , ReminderClickListner clickListner){
        this.mContext = context;
        mReminderClickListner = clickListner;

    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_reminder_list_item,parent,false);
        ReminderViewHolder holder = new ReminderViewHolder(view,mReminderClickListner);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        holder.tvTitle.setText(mReminderList.get(position).getTitle());
        holder.tvDesc.setText(mReminderList.get(position).getDesc());
        holder.tvDate.setText(mReminderList.get(position).getDate());
        holder.tvTime.setText(mReminderList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        if(mReminderList != null){
            return mReminderList.size();
        }
        return 0;
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvDesc;
        TextView tvDate;
        TextView tvTime;
        ReminderClickListner reminderClickListner;


        public ReminderViewHolder(@NonNull View itemView, ReminderClickListner clickListner) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_reminder_title);
            tvDesc = itemView.findViewById(R.id.tv_item_reminder_description);
            tvDate = itemView.findViewById(R.id.tv_item_reminder_date);
            tvTime = itemView.findViewById(R.id.tv_item_reminder_time);
            reminderClickListner = clickListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            reminderClickListner.onReminderClick(getAdapterPosition());
        }
    }

    public void setReminderList(List<Reminder> list){
        mReminderList = list;
        notifyDataSetChanged();
    }



}
