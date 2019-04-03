package com.nikhildagrawal.worktrack.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.NotesAdapter;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.viewmodels.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NotesFragment extends Fragment {

    public static final String TAG = "NotesFragment";

    private RecyclerView mNotesRecyclerView;
    private FloatingActionButton mFabAddNotes;
    private View mView;
    private RecyclerView.Adapter mAdapter;

    private NoteViewModel mNoteViewModel;

    public NotesFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView");


        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        mNotesRecyclerView = view.findViewById(R.id.rv_notes);
        mFabAddNotes = view.findViewById(R.id.fab_add_notes);


        mNoteViewModel = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
        mNoteViewModel.init();





        //List<Note> list = getFakeNotes();
        mAdapter = new NotesAdapter(getActivity(),mNoteViewModel.getNotes().getValue());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mNotesRecyclerView.setLayoutManager(layoutManager);
        mNotesRecyclerView.setAdapter(mAdapter);
        mFabAddNotes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toAddNewNoteFragment));

        mNoteViewModel.getNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> noteList) {
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"onViewCreated");

    }


//    private List<Note> getFakeNotes(){
//        List<Note> noteList = new ArrayList<>();
//
//        noteList.add(new Note("Test Note 1","This is note 1 test description"));
//        noteList.add(new Note("Test Note 2","This is note 2 test description"));
//        noteList.add(new Note("Test Note 3","This is note 3 test description"));
//        noteList.add(new Note("Test Note 4","This is note 4 test description"));
//        noteList.add(new Note("Test Note 5","This is note 5 test description"));
//        noteList.add(new Note("Test Note 6","This is note 6 test description"));
//        noteList.add(new Note("Test Note 7","This is note 7 test description"));
//        noteList.add(new Note("Test Note 8","This is note 8 test description"));
//        noteList.add(new Note("Test Note 9","This is note 9 test description"));
//        noteList.add(new Note("Test Note 10","This is note 10 test description"));
//
//
//        return noteList;
//
//    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");




    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }

}
