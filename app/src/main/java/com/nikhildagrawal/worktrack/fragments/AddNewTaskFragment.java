package com.nikhildagrawal.worktrack.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nikhildagrawal.worktrack.R;
import androidx.fragment.app.Fragment;


public class AddNewTaskFragment extends Fragment {

    public AddNewTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_task, container, false);
    }

}
