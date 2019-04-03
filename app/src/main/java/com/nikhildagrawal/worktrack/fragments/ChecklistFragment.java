package com.nikhildagrawal.worktrack.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ChecklistAdapter;
import com.nikhildagrawal.worktrack.models.Checklist;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistFragment extends Fragment {

    private RecyclerView mChecklistRecyclerView;
    private FloatingActionButton mFabAddChecklistItem;
    private View mView;
    private RecyclerView.Adapter mAdapter;


    public ChecklistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChecklistRecyclerView = view.findViewById(R.id.rv_checklist);
        mFabAddChecklistItem = view.findViewById(R.id.fab_add_checklist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mChecklistRecyclerView.setLayoutManager(layoutManager);

        List<Checklist> list = getFakeChecklist();
        mAdapter = new ChecklistAdapter(getActivity(),list);
        mChecklistRecyclerView.setAdapter(mAdapter);


    }


    private List<Checklist> getFakeChecklist(){
        List<Checklist> checklist = new ArrayList<>();

        checklist.add(new Checklist("Incomplete","Gym"));
        checklist.add(new Checklist("Incomplete","Study"));
        checklist.add(new Checklist("Incomplete","Grocery"));
        checklist.add(new Checklist("Incomplete","Play"));
        checklist.add(new Checklist("Incomplete","Cook"));
        checklist.add(new Checklist("Incomplete","Swim"));
        checklist.add(new Checklist("Incomplete","Cycling"));
        return checklist;

    }
}
