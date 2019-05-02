package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.interfaces.EditProjectClickListner;
import com.nikhildagrawal.worktrack.interfaces.ProjectClickListner;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.utils.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColabAdapter extends RecyclerView.Adapter<ColabAdapter.ColabViewHolder> {



    List<Project> projectList;
    Context mContext;
    String creatorName = "";

    private ProjectClickListner mListener;
    private EditProjectClickListner mEditListner;

    public int[] slideImages = {
            R.drawable.projim1,
            R.drawable.projim2,
            R.drawable.projim3,
            R.drawable.projim4,
            R.drawable.projim5,
            R.drawable.projim6,
            R.drawable.projim7,
            R.drawable.projim8,
            R.drawable.projim9,
            R.drawable.projim10,
            R.drawable.projim11,
            R.drawable.projim12,
            R.drawable.projim13,
            R.drawable.projim14,
            R.drawable.projim15,


    };

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

            String creatorID = projectList.get(position).getCreator_id();

            DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(creatorID);

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);
                        creatorName = "Owner: " + user.getFirtst_name()+ " "+user.getLast_name();

                }
            });

            holder.mTitle.setText(projectList.get(position).getTitle());
            holder.mDesc.setText(projectList.get(position).getDescription());
            holder.mDeadline.setText(projectList.get(position).getEnd_date());

            holder.mOwner.setText(creatorName);

            if(projectList.get(position).getTasks()!=null){
                holder.mTotalTasks.setText(String.valueOf(projectList.get(position).getTasks().size()) );
            }

            if(projectList.get(position).getMembers()!=null){
                holder.mTotalMembers.setText(String.valueOf(projectList.get(position).getMembers().size()) );
            }


            holder.mProjectDp.setImageResource(slideImages[position]);


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

        TextView mTitle,mDesc,mDeadline,mTotalMembers,mTotalTasks,mOwner;
        ImageView mEditImage;

        View view;
        MaterialButton mButtonDetails;
        MaterialButton mButtonEdit;
        ImageView mProjectDp;
        LinearLayout layout;


        public ColabViewHolder(@NonNull View itemView, ProjectClickListner listner , EditProjectClickListner editlistner) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.colab_list_item_title);
            mDesc = itemView.findViewById(R.id.colab_list_item_desc);
            mDeadline = itemView.findViewById(R.id.colab_list_item_end_date);
            mTotalMembers = itemView.findViewById(R.id.colab_list_item_total_members);
            mTotalTasks = itemView.findViewById(R.id.colab_list_item_total_taskss);
            mProjectDp = itemView.findViewById(R.id.colab_list_item_project_dp);
            layout = itemView.findViewById(R.id.layout_cell_colab);
            mButtonDetails = itemView.findViewById(R.id.button_colab_detail);
            mButtonEdit = itemView.findViewById(R.id.button_colab_edit);
            mEditImage = itemView.findViewById(R.id.image_edit);
            mOwner = itemView.findViewById(R.id.colab_list_item_creator);

            mListener = listner;
            mEditListner = editlistner;

            mEditImage.setOnClickListener(this);
            layout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            switch (v.getId()){

                case R.id.layout_cell_colab:
                    mEditListner.onEditProjectClick(getAdapterPosition());
                    break;

                case R.id.image_edit:
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

    public void removeProjectFromColabAdapter(){
        if(projectList!=null){
            projectList.clear();
        }

    }
}
