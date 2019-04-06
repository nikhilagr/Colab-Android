package com.nikhildagrawal.worktrack.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.RemindersAdapter;
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


public class ReminderFragment extends Fragment {



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

       // List<Reminder> list = getFakeReminders();
        mAdapter = new RemindersAdapter(getActivity());

        mReminderRecyclerView.setAdapter(mAdapter);


    }

    private List<Reminder> getFakeReminders(){
        List<Reminder> reminderList = new ArrayList<>();

        reminderList.add(new Reminder("Reminder 1","Description about reminder 1","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 2","Description about reminder 2","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 3","Description about reminder 3","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 4","Description about reminder 4","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 5","Description about reminder 5","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 6","Description about reminder 6","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 7","Description about reminder 7","03/04/2019","6:00 PM"));
        reminderList.add(new Reminder("Reminder 8","Description about reminder 8","03/04/2019","6:00 PM"));

        return reminderList;
    }

}
