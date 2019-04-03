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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ReminderFragment extends Fragment {



    private RecyclerView mReminderRecyclerView;
    private FloatingActionButton mFabAddReminder;
    private View mView;
    private RecyclerView.Adapter mAdapter;


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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mReminderRecyclerView.setLayoutManager(layoutManager);

        List<Reminder> list = getFakeReminders();
        mAdapter = new RemindersAdapter(getActivity(),list);
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
