package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Checklist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private Context mContext;
    private List<Checklist> list;

    public ChecklistAdapter(Context context, List<Checklist> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_checklist_list_item,parent,false);
        ChecklistViewHolder holder = new ChecklistViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {

        if(holder!=null){
            holder.tvTitle.setText(list.get(position).getTitle());

            if(list.get(position).getStatus() == "Complete"){
                holder.checkBox.setEnabled(true);
            }else{
                holder.checkBox.setEnabled(false);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder{


        private CheckBox checkBox;
        private TextView tvTitle;
        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_checklist_title);
            checkBox = itemView.findViewById(R.id.cb_item_checklist);


        }
    }
}
