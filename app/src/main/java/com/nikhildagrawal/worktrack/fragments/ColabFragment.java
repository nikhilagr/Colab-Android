package com.nikhildagrawal.worktrack.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ColabAdapter;
import com.nikhildagrawal.worktrack.interfaces.EditProjectClickListner;
import com.nikhildagrawal.worktrack.interfaces.ProjectClickListner;
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
public class ColabFragment extends Fragment implements ProjectClickListner, EditProjectClickListner {

    public static final String TAG = "ColabFragment";

    private RecyclerView mRecyclerView;
    private ColabAdapter mAdapter;
    private View mView;
    private FloatingActionButton mFabAddProject;
    private ColabViewModel mViewModel;
    private LinearLayout mNoProjectLayout;
    private String currentUserId;


    public ColabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"onCreateView");


        mView  = inflater.inflate(R.layout.fragment_colab, container, false);

        mFabAddProject =  mView.findViewById(R.id.fab_add_project);
        mRecyclerView = mView.findViewById(R.id.rv_colab);
        mNoProjectLayout = mView.findViewById(R.id.no_colab_layout);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);


        setupRecyclerView();

        /**
         * Show projects based on user who is logged in.
         */
        mViewModel.getProjects(currentUserId).observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {

                if(projects.isEmpty()){
                    mNoProjectLayout.setVisibility(View.VISIBLE);
                }else{
                    mNoProjectLayout.setVisibility(View.GONE);
                }

                mAdapter.setProjectList(projects);
            }
        });


        mFabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("from colab","Add");
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_colabFragment_to_addNewProjectFragment,bundle);
            }
        });



        return mView;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ColabAdapter(getActivity(),this,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
    }


    @Override
    public void onProjectClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("from colab","Add Edit mode");
        bundle.putInt("position",position);
        Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_colabFragment_to_addNewProjectFragment,bundle);


    }

    @Override
    public void onEditProjectClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.toProjectDetails,bundle);

    }

}
