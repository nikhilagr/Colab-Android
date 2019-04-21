package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.Reminder;
import com.nikhildagrawal.worktrack.repository.ColabRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColabAdapter extends RecyclerView.Adapter<ColabAdapter.ColabViewHolder> {



    List<Project> projectList;
    Context mContext;

    public ColabAdapter(Context context){
        mContext = context;
    }


    @NonNull
    @Override
    public ColabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_colab_list_item,parent,false);
        ColabViewHolder holder = new ColabViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ColabViewHolder holder, final int position) {

        if(holder!=null && projectList!=null){
            holder.mTitle.setText(projectList.get(position).getTitle());
            holder.mDesc.setText(projectList.get(position).getDescription());
            holder.mDeadline.setText(projectList.get(position).getEnd_date());

            if(projectList.get(position).getMembers()!=null){
                holder.mTotalMembers.setText(String.valueOf(projectList.get(position).getMembers().size()) );
            }


            holder.mOptionsMenuImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO: Bottom Pane

                    //TODO: Insert on delete action in bottom pane.
//                    ColabRepository.getInstance().deleteProjectFromFirestoreDb(projectList.get(position).getProject_id());
//                    projectList.remove(projectList.get(position));
//                    notifyDataSetChanged();
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        if(projectList!=null){
            return projectList.size();
        }
       return 0 ;
    }

    public class ColabViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle,mDesc,mDeadline,mTotalMembers;
        ImageView mOptionsMenuImage;


        public ColabViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.colab_list_item_title);
            mDesc = itemView.findViewById(R.id.colab_list_item_desc);
            mDeadline = itemView.findViewById(R.id.colab_list_item_end_date);
            mTotalMembers = itemView.findViewById(R.id.colab_list_item_total_members);
            mOptionsMenuImage = itemView.findViewById(R.id.colab_list_item_menu_image);

        }
    }

    public void setProjectList(List<Project> list){
        projectList = list;
        notifyDataSetChanged();
    }
}
