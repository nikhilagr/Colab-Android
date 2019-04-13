package com.nikhildagrawal.worktrack.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.TabActivity;
import com.nikhildagrawal.worktrack.Utilties;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.repository.NotesRepository;
import com.nikhildagrawal.worktrack.repository.ReminderRepository;
import com.nikhildagrawal.worktrack.viewmodels.NoteViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;



public class AddNewNoteFragment extends Fragment {

    private TextInputEditText mETAddTitle;
    private TextInputEditText mETAddDesc;
    private MaterialButton btnAdd, btnSave;
    private int position;

    private NoteViewModel mViewModel;

    public AddNewNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_note, container, false);

        mETAddTitle = view.findViewById(R.id.et_add_title);
        mETAddDesc = view.findViewById(R.id.et_add_desc);
        btnSave = view.findViewById(R.id.btn_save_note);


        ActionBar actionBar = ((TabActivity)getActivity()).getBar();
        String str = "";
        position = -1;

        if(getArguments()!= null){
           str = getArguments().getString("from");
           position = getArguments().getInt("position");
        }

        btnAdd = view.findViewById(R.id.btn_add_new_note);

        if(str.contentEquals("NoteClick")){
            btnAdd.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        }

        mViewModel = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);

        final List<Note> notes = mViewModel.getNotes().getValue();
        if(position != -1){
            mETAddTitle.setText(notes.get(position).getNote_title());
            mETAddDesc.setText(notes.get(position).getNote_desc());
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO: Check if both the fields are non empty)
                String title = mETAddTitle.getText().toString();
                String desc  = mETAddDesc.getText().toString();


                if(!Utilties.isEmpty(title)&&!Utilties.isEmpty(desc)){

                    NotesRepository.getInstance().insertNoteInFirestore(title,desc);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    getFragmentManager().popBackStackImmediate();

                }else{


                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<>();

                if(!mETAddTitle.getText().toString().contentEquals(notes.get(position).getNote_title())){
                    map.put("note_title",mETAddTitle.getText().toString());
                }

                if(!mETAddDesc.getText().toString().contentEquals(notes.get(position).getNote_desc())){
                    map.put("note_desc",mETAddDesc.getText().toString());
                }

                if(map.size()!=0){
                    NotesRepository.getInstance().updateNoteInFirestore(map,notes.get(position).getNote_id());
                    Snackbar.make(v,"Changes saved successfully",Snackbar.LENGTH_LONG).show();
                    getFragmentManager().popBackStackImmediate();
                }else{
                    getFragmentManager().popBackStackImmediate();
                }

            }
        });

        return view;
    }

}
