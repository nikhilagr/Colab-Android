package com.nikhildagrawal.worktrack.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ProjectDetailsAdapter;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;
import com.nikhildagrawal.worktrack.viewmodels.TaskViewModel;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ProjectDetailsFragment extends Fragment {


    private View mView;
    private RecyclerView mRecyclerView;
    private ProjectDetailsAdapter mAdapter;
    private TaskViewModel mViewModel;
    private Integer position;
    private ColabViewModel mColabViewModel;



    public ProjectDetailsFragment() {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView =  inflater.inflate(R.layout.fragment_project_details, container, false);
        mRecyclerView = mView.findViewById(R.id.rcv_proj_details);
        mViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);

        setupTasksRecyclerView();

        if(getArguments()!=null){
            position = getArguments().getInt("position",-1);
        }


        List<Project> pros = mColabViewModel.getProjects(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue();

        String projectId = pros.get(position).getProject_id();



        mViewModel.getTasks(projectId).observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mAdapter.setTasksList(tasks);
            }
        });


        return mView;
    }

    private void setupTasksRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProjectDetailsAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.clearTask();
    }
}
