package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.repository.TaskRepository;
import com.nikhildagrawal.worktrack.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectDetailsAdapter extends RecyclerView.Adapter<ProjectDetailsAdapter.ProjectViewHolder> {

    Context mContext;
    List<Task> mList;


    public ProjectDetailsAdapter(Context context){
        mContext = context;
    }




    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_project_details,parent,false);
        ProjectViewHolder holder = new ProjectViewHolder(view);
        return holder;

    }



    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectViewHolder holder, final int position) {

        if(holder!=null){

            List<String> assigneeList = mList.get(position).getAssigned_to();

            //TODO: grab name and create chip dynamically

            for (String assignee : assigneeList) {

                DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(assignee);

                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {

                        User user = task.getResult().toObject(User.class);
                        Chip ch = new Chip(mContext);
                        ch.setText(user.getFirtst_name());
                        ch.setTextColor(mContext.getResources().getColor(R.color.colorwhite));
                        ch.setChipBackgroundColorResource(R.color.colorchip);
                        holder.mChipLayout.addView(ch);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ch.getLayoutParams();
                        params.setMarginEnd(10);


                    }
                });

            }




            holder.mTitle.setText(mList.get(position).getName());
            holder.mEndDate.setText(mList.get(position).getEnd_date());
            holder.mStatus.setText(mList.get(position).getStatus());
            holder.mSeekBar.setProgress(Integer.valueOf(mList.get(position).getStatus()));



            if(isAssignee(FirebaseAuth.getInstance().getCurrentUser().getUid(),assigneeList)){

                //holder.mSeekBar.setActivated(true);
                holder.mSeekBar.setEnabled(true);
                holder.mMessage.setVisibility(View.VISIBLE);


            }else{
                holder.mSeekBar.setEnabled(false);
            }

            holder.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    holder.mStatus.setText(String.valueOf(progress));


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {



                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    //TODO: Update Status of particular task

                    Map<String,Object> map = new HashMap<>();
                    map.put("status",holder.mStatus.getText().toString());

                    TaskRepository.getInstance().updateTaskInFirestoreDb(map,mList.get(position).getTask_id());
                }
            });
        }

    }



    private boolean isAssignee(String uid, List<String> assigneeList) {

        for(String id: assigneeList){
            if(uid.equals(id)){
                return true;
            }
        }

        return false;

    }

    @Override
    public int getItemCount() {

        if(mList!=null){
            return mList.size();
        }
        return 0;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mEndDate,mStatus,mMessage;
        SeekBar mSeekBar;
        LinearLayout mChipLayout;
        ChipGroup mChipGroup;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.proj_detail_task_title);
            mEndDate = itemView.findViewById(R.id.proj_detail_due_date);
            mStatus = itemView.findViewById(R.id.proj_detail_status_percent);
            mSeekBar = itemView.findViewById(R.id.proj_detail_seekbar);
            mMessage = itemView.findViewById(R.id.progress_message);
            mChipLayout = itemView.findViewById(R.id.member_chip_layout);
       //     mChipGroup = itemView.findViewById(R.id.chipGrp);

        }
    }

    public void setTasksList(List<Task> taskList){
        mList = taskList;
        notifyDataSetChanged();

    }


}
