package com.nikhildagrawal.worktrack.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.interfaces.CheckListItemClickListner;
import com.nikhildagrawal.worktrack.models.Checklist;
import com.nikhildagrawal.worktrack.repository.ChecklistRepository;
import com.nikhildagrawal.worktrack.repository.NotesRepository;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {


    private Context mContext;
    private List<Checklist> mChecklist;
    CheckListItemClickListner mListner;
    private static final String TAG = "ChecklistAdapter";

    public ChecklistAdapter(Context context, CheckListItemClickListner listner) {
        this.mContext = context;
        this.mListner = listner;
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_checklist_list_item,parent,false);
        ChecklistViewHolder holder = new ChecklistViewHolder(view,mListner);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistViewHolder holder, final int position) {

        if(holder!=null){
            holder.tvTitle.setText(mChecklist.get(position).getTitle());


            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

                    dialog.setTitle("DELETE");
                    dialog.setMessage("Are you sure you want to delete TODO Item?");

                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.d(TAG,mChecklist.get(position).getChecklist_id());
                            Toast.makeText(mContext,"Call Delete here",Toast.LENGTH_SHORT).show();
                            ChecklistRepository.getInstance().deleteChecklisFromFireStore(mChecklist.get(position).getChecklist_id());
                            mChecklist.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });

                    dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dialog.create().show();
                }
            });





            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if(mChecklist.get(position).getStatus().equals("Complete")){
                        holder.checkBox.setChecked(true);
                    }else if(mChecklist.get(position).getStatus().equals("Incomplete")){
                        holder.checkBox.setChecked(false);
                    }


                    if(isChecked){
                        //Update DB set status Complete
                    }else{
                        //Update DB set status Incomplete
                    }
                }
            });



        }
    }

    @Override
    public int getItemCount() {
        if(mChecklist == null){
            return 0;
        }
        return mChecklist.size();
    }



    public class ChecklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CheckBox checkBox;
        private TextView tvTitle;
        private ImageView btnDelete;
        public ChecklistViewHolder(@NonNull View itemView, CheckListItemClickListner itemClickListener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_checklist_title);
            checkBox = itemView.findViewById(R.id.cb_item_checklist);
            btnDelete = itemView.findViewById(R.id.btn_delete_cell_checklist);
            mListner = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mListner.onCheckListItemClick(getAdapterPosition());
        }
    }


    public void setmChecklist(List<Checklist> list){
        mChecklist = list;
        notifyDataSetChanged();
    }


}
