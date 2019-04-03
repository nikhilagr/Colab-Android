package com.nikhildagrawal.worktrack.fragments;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.Utilties;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.viewmodels.NoteViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewNoteFragment extends Fragment {

    private TextInputEditText mETAddTitle;
    private TextInputEditText mETAddDesc;
    private MaterialButton btnAdd;

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


        btnAdd = view.findViewById(R.id.btn_add);


        mViewModel = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO: Check if both the fields are non empty)
                String title = mETAddTitle.getText().toString();
                String desc  = mETAddDesc.getText().toString();


                if(!Utilties.isEmpty(title)&&!Utilties.isEmpty(desc)){
                    mViewModel.addNewNote(new Note(title,desc));

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference ref = db.collection("notes").document();

                    Note newNote = new Note(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            ref.getId(),title,desc);

                    ref.set(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("AddNewNoteFragment","note inserted successfully");
                            }else{
                                Log.d("AddNewNoteFragment","error while inserting");
                            }
                        }
                    });


                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    getFragmentManager().popBackStackImmediate();
                }else{


                }
            }
        });

        return view;
    }



}
