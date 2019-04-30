package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.interfaces.EditProjectClickListner;
import com.nikhildagrawal.worktrack.interfaces.ProjectClickListner;
import com.nikhildagrawal.worktrack.models.Project;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColabAdapter extends RecyclerView.Adapter<ColabAdapter.ColabViewHolder> {



    List<Project> projectList;
    Context mContext;

    private ProjectClickListner mListener;
    private EditProjectClickListner mEditListner;


    public ColabAdapter(Context context, ProjectClickListner listner, EditProjectClickListner editListner ){
        mContext = context;
        mListener = listner;
        mEditListner = editListner;
    }


    @NonNull
    @Override
    public ColabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_colab_list_item,parent,false);
        ColabViewHolder holder = new ColabViewHolder(view,mListener,mEditListner);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ColabViewHolder holder, final int position) {

        if(holder!=null && projectList!=null && projectList.get(position)!=null){


            holder.mTitle.setText(projectList.get(position).getTitle());
            holder.mDesc.setText(projectList.get(position).getDescription());
            holder.mDeadline.setText(projectList.get(position).getEnd_date());

            if(projectList.get(position).getTasks()!=null){
                holder.mTotalTasks.setText(String.valueOf(projectList.get(position).getTasks().size()) );
            }

            if(projectList.get(position).getMembers()!=null){
                holder.mTotalMembers.setText(String.valueOf(projectList.get(position).getMembers().size()) );
            }


            //TODO: Insert on delete action in bottom pane.
//                    ColabRepository.getInstance().deleteProjectFromFirestoreDb(projectList.get(position).getProject_id());
//                    projectList.remove(projectList.get(position));
//                    notifyDataSetChanged();

        }


    }

    @Override
    public int getItemCount() {
        if(projectList!=null){
            return projectList.size();
        }
       return 0 ;
    }

    public class ColabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTitle,mDesc,mDeadline,mTotalMembers,mTotalTasks;

        View view;
        MaterialButton mButtonDetails;
        MaterialButton mButtonEdit;


        public ColabViewHolder(@NonNull View itemView, ProjectClickListner listner , EditProjectClickListner editlistner) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.colab_list_item_title);
            mDesc = itemView.findViewById(R.id.colab_list_item_desc);
            mDeadline = itemView.findViewById(R.id.colab_list_item_end_date);
            mTotalMembers = itemView.findViewById(R.id.colab_list_item_total_members);
            mTotalTasks = itemView.findViewById(R.id.colab_list_item_total_taskss);

            mButtonDetails = itemView.findViewById(R.id.button_colab_detail);
            mButtonEdit = itemView.findViewById(R.id.button_colab_edit);
            mListener = listner;
            mEditListner = editlistner;

            mButtonEdit.setOnClickListener(this);
            mButtonDetails.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            switch (v.getId()){

                case R.id.button_colab_detail:
                    mEditListner.onEditProjectClick(getAdapterPosition());
                    break;

                case R.id.button_colab_edit:
                    mListener.onProjectClick(getAdapterPosition());
                    break;

                default:
                    break;
            }

        }
    }

    public void setProjectList(List<Project> list){
        projectList = list;
        notifyDataSetChanged();
    }
}
