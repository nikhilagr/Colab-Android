package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Reminder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder> {

    private Context mContext;
    private List<Reminder> list;

    public RemindersAdapter(Context context,List<Reminder> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_reminder_list_item,parent,false);
        ReminderViewHolder holder = new ReminderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDesc.setText(list.get(position).getDesc());
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvTime.setText(list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvDesc;
        TextView tvDate;
        TextView tvTime;
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_reminder_title);
            tvDesc = itemView.findViewById(R.id.tv_item_reminder_description);
            tvDate = itemView.findViewById(R.id.tv_item_reminder_date);
            tvTime = itemView.findViewById(R.id.tv_item_reminder_time);
        }
    }


}
