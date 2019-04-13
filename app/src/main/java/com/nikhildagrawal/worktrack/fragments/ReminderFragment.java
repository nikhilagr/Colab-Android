package com.nikhildagrawal.worktrack.fragments;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.RemindersAdapter;
import com.nikhildagrawal.worktrack.interfaces.ReminderClickListner;
import com.nikhildagrawal.worktrack.models.Reminder;
import com.nikhildagrawal.worktrack.viewmodels.ReminderViewModel;

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


public class ReminderFragment extends Fragment implements ReminderClickListner {



    private RecyclerView mReminderRecyclerView;
    private FloatingActionButton mFabAddReminder;
    private View mView;
    private RemindersAdapter mAdapter;
    private ReminderViewModel mReminderViewModel;


    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReminderRecyclerView = view.findViewById(R.id.rv_reminder);
        mFabAddReminder = view.findViewById(R.id.fab_add_reminder);
        mReminderViewModel = ViewModelProviders.of(getActivity()).get(ReminderViewModel.class);

        mReminderViewModel.getReminderList().observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {

                mAdapter.setReminderList(reminders);
            }
        });


        mFabAddReminder.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_reminderFragment_to_addNewReminderFragment));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mReminderRecyclerView.setLayoutManager(layoutManager);


        mAdapter = new RemindersAdapter(getActivity(),this);

        mReminderRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onReminderClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("from","ReminderClick");
        bundle.putInt("position",position);
        Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_reminderFragment_to_addNewReminderFragment,bundle);

    }
}
