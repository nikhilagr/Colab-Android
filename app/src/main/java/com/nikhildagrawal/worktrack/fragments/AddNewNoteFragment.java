package com.nikhildagrawal.worktrack.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.Utilties;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.repository.NotesRepository;
import com.nikhildagrawal.worktrack.viewmodels.NoteViewModel;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;



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

                    NotesRepository.getInstance().insertNoteInFirestore(title,desc);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    getFragmentManager().popBackStackImmediate();

                }else{


                }
            }
        });

        return view;
    }

}
