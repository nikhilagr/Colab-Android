package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.Integer.parseInt;

public class ProjectDetailsAdapter extends RecyclerView.Adapter<ProjectDetailsAdapter.ProjectViewHolder> {

    Context mContext;
    List<Task> mList;
    PieData data;
    PieDataSet dataSet;


    public ProjectDetailsAdapter(Context context){
        mContext = context;
    }

     ArrayList NoOfEmp;


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
                        ch.setText(user.getFirst_name());
                        ch.setTextColor(mContext.getResources().getColor(R.color.colorwhite));
                        ch.setChipBackgroundColorResource(R.color.colorchip);
                        holder.mChipLayout.addView(ch);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ch.getLayoutParams();
                        params.setMarginEnd(10);
                    }
                });
            }

            NoOfEmp = new ArrayList();
            int perCom = Integer.parseInt(mList.get(position).getStatus());
            int perUnComp = 100 - perCom;
            NoOfEmp.add(new PieEntry(perCom, 0));
            NoOfEmp.add(new PieEntry(perUnComp, 1));
            dataSet = new PieDataSet(NoOfEmp, "% completed");
            dataSet.setDrawValues(false);
            data = new PieData(dataSet);
            holder.mPieChart.setData(data);

            Log.d("****sdjfhj***",String.valueOf(dataSet.getEntryCount()));

            final int[] MY_COLORS = {
                    Color.rgb(89,222,120),
                    Color.rgb(224,224,224)
            };

            final ArrayList<Integer> colors = new ArrayList<>();

            for(int c: MY_COLORS) colors.add(c);

            dataSet.setColors(colors);
            holder.mPieChart.getDescription().setEnabled(false);
            holder.mPieChart.setDrawEntryLabels(false);
            holder.mPieChart.setCenterTextSize(20);
            holder.mPieChart.setCenterText(mList.get(position).getStatus()+ "%");
            holder.mPieChart.animateXY(500, 500);

            holder.mPieChart.getLegend().setEnabled(false);
            holder.mTitle.setText(mList.get(position).getName());
            holder.mEndDate.setText(mList.get(position).getEnd_date());
            holder.mSeekBar.setProgress(parseInt(mList.get(position).getStatus()));
            if(isAssignee(FirebaseAuth.getInstance().getCurrentUser().getUid(),assigneeList)){

                holder.mSeekBar.setEnabled(true);
                holder.mMessage.setVisibility(View.VISIBLE);


            }else{
                holder.mSeekBar.setEnabled(false);
            }

            holder.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    holder.mPieChart.setCenterText(progress +"%");

                    int perCom = progress;
                    int perUnComp = 100 - perCom;

                    data.getDataSet().removeFirst();
                    data.getDataSet().removeFirst();

                    data.getDataSet().addEntry(new PieEntry(perCom,0));
                    data.getDataSet().addEntry(new PieEntry(perUnComp,1));

                    data.notifyDataChanged();
                    holder.mPieChart.setData(data);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {



                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    //TODO: Update Status of particular task

                    Map<String,Object> map = new HashMap<>();
                    String text = holder.mPieChart.getCenterText().toString();

                    map.put("status", text.substring(0,text.length()-1));

                    TaskRepository.getInstance().updateTaskInFirestoreDb(map,mList.get(position).getTask_id());
                }
            });
        }


        String end_date = mList.get(position).getEnd_date();

        Calendar calCurrent = Calendar.getInstance();
        Calendar day = Calendar.getInstance();

        try {
            day.setTime(new SimpleDateFormat("EEE, MMM d, yyyy").parse(end_date));

            if(day.after(calCurrent)){
                int diff = day.get(Calendar.DAY_OF_MONTH) - calCurrent.get(Calendar.DAY_OF_MONTH);
                //TODO: set in TV
                holder.mDaysLeft.setText(String.valueOf(diff)+" "+ "days to complete the task!");

            }

        } catch (ParseException e) {
            e.printStackTrace();
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

        TextView mTitle,mEndDate,mStatus,mMessage,mDaysLeft;
        SeekBar mSeekBar;
        LinearLayout mChipLayout;
        ChipGroup mChipGroup;
        PieChart mPieChart;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.proj_detail_task_title);
            mEndDate = itemView.findViewById(R.id.proj_detail_due_date);
            mSeekBar = itemView.findViewById(R.id.proj_detail_seekbar);
            mMessage = itemView.findViewById(R.id.progress_message);
            mChipLayout = itemView.findViewById(R.id.member_chip_layout);
            mPieChart = itemView.findViewById(R.id.pieChart);
            mDaysLeft = itemView.findViewById(R.id.days_left);

        }
    }

    public void setTasksList(List<Task> taskList){
        mList = taskList;
        notifyDataSetChanged();

    }


}
