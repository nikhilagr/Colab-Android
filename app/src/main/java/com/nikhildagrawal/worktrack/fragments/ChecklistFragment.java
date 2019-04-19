package com.nikhildagrawal.worktrack.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ChecklistAdapter;
import com.nikhildagrawal.worktrack.dialogs.AddChecklistItemDialog;
import com.nikhildagrawal.worktrack.interfaces.CheckListItemClickListner;
import com.nikhildagrawal.worktrack.models.Checklist;
import com.nikhildagrawal.worktrack.viewmodels.ChecklistViewModel;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistFragment extends Fragment implements CheckListItemClickListner {

    private RecyclerView mChecklistRecyclerView;
    private FloatingActionButton mFabAddChecklistItem;
    private ChecklistAdapter mAdapter;
    ChecklistViewModel mCheckListViewModel;
    private LinearLayout mNoChecklistLayout;


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
        mNoChecklistLayout = view.findViewById(R.id.no_checklist_layout);

        mCheckListViewModel = ViewModelProviders.of(getActivity()).get(ChecklistViewModel.class);
        initRecyclerView();

            mCheckListViewModel.getChecklists().observe(getViewLifecycleOwner(), new Observer<List<Checklist>>() {
                @Override
                public void onChanged(List<Checklist> checklists) {

                    if(checklists.isEmpty()){
                        mNoChecklistLayout.setVisibility(View.VISIBLE);
                    }else{
                        mNoChecklistLayout.setVisibility(View.GONE);
                    }

                    mAdapter.setmChecklist(checklists);
                }
            });



        mFabAddChecklistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddChecklistItemDialog dialog = new AddChecklistItemDialog();
                dialog.show(getFragmentManager(),"checklist_dialog");
            }
        });

    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mChecklistRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChecklistAdapter(getActivity(),this);
        mChecklistRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public void onCheckListItemClick(int position) {

    }
}
