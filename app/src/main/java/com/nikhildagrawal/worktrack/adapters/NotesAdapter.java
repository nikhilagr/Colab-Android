package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.interfaces.NoteClickListner;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.repository.NotesRepository;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context mContext;
    private List<Note> noteList;

    private NoteClickListner mListner;


    public NotesAdapter(Context context , NoteClickListner listner){
            mContext = context;
            mListner = listner;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_notes_list_item,parent,false);

        NotesViewHolder holder = new NotesViewHolder(view,mListner);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, final int position) {

        holder.tv_title.setText(noteList.get(position).getNote_title());
        holder.tv_desc.setText(noteList.get(position).getNote_desc());


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

                dialog.setTitle(mContext.getString(R.string.delete));
                dialog.setMessage(mContext.getString(R.string.delete_note_message));

                dialog.setPositiveButton(mContext.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        
                        NotesRepository.getInstance().delteNoteFromFireStore(noteList.get(position).getNote_id());
                        noteList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                dialog.setNegativeButton(mContext.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {

        if(noteList!=null){
            return noteList.size();
        }

        return 0;

    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tv_title;
        private TextView tv_desc;
        private ImageView btnDelete;


        public NotesViewHolder(@NonNull View itemView, NoteClickListner listner) {

            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_item_notes_title);
            tv_desc = itemView.findViewById(R.id.tv_item_notes_description);
            btnDelete = itemView.findViewById(R.id.btn_delete_cell_notes);
            mListner = listner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListner.onNoteClick(getAdapterPosition());
        }
    }

    public void setNoteList(List<Note> list){
        noteList = list;
        notifyDataSetChanged();
    }


}
