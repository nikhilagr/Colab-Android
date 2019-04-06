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
    private NotesAdapter mAdapter;

    private NoteViewModel mNoteViewModel;

    public NotesFragment() {

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



        mAdapter = new NotesAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mNotesRecyclerView.setLayoutManager(layoutManager);
        mNotesRecyclerView.setAdapter(mAdapter);

        mFabAddNotes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toAddNewNoteFragment));

        mNoteViewModel.getNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> noteList) {

                mAdapter.setNoteList(noteList);
            }
        });

        return view;

    }


}



