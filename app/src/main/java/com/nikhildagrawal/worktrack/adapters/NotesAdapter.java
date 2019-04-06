package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Note;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context mContext;
    private List<Note> noteList;

    public NotesAdapter(Context context ){
            mContext = context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_notes_list_item,parent,false);

        NotesViewHolder holder = new NotesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.tv_title.setText(noteList.get(position).getNote_title());
        holder.tv_desc.setText(noteList.get(position).getNote_desc());

    }

    @Override
    public int getItemCount() {

        if(noteList!=null){
            return noteList.size();
        }

        return 0;

    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_desc;

        public NotesViewHolder(@NonNull View itemView) {

            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_item_notes_title);
            tv_desc = itemView.findViewById(R.id.tv_item_notes_description);
        }
    }

    public void setNoteList(List<Note> list){
        noteList = list;
        notifyDataSetChanged();
    }


}
