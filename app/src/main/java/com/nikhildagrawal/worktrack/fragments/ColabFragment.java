package com.nikhildagrawal.worktrack.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ColabAdapter;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ColabFragment extends Fragment {

    public static final String TAG = "ColabFragment";

    private RecyclerView mRecyclerView;
    private ColabAdapter mAdapter;
    private View mView;
    private FloatingActionButton mFabAddProject;
    ColabViewModel mViewModel;
    private List<Project> projectList;


    public ColabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView");


        mView  = inflater.inflate(R.layout.fragment_colab, container, false);

        mRecyclerView = mView.findViewById(R.id.rv_colab);


        mViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);

        mViewModel.getProjects().observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {

                mAdapter.setProjectList(projects);
            }
        });

        mFabAddProject =  mView.findViewById(R.id.fab_add_project);
        mFabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_colabFragment_to_addNewProjectFragment);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ColabAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
    }


}
