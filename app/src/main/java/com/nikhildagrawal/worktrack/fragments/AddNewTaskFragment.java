package com.nikhildagrawal.worktrack.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.AssigneeAdapter;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.viewmodels.AssigneeViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ContactViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddNewTaskFragment extends Fragment {


    RecyclerView mAssigneeRecyclerView;
    AssigneeAdapter mAdapter;
    AssigneeViewModel mAssigneeViewModel;
    ColabViewModel mColabViewModel;
    private String projectId;
    private MaterialButton mBtnAddTask;
    List<Contact> list;
    private TextInputEditText mTitle,mDesc,mStartDate,mEndDate;
    private Calendar calendar;
    List<Contact> assigneeList;
    Project mCurrentProject;
    List<Project> projectList;

    public AddNewTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_task, container, false);


        if(getArguments()!=null){
            projectId = getArguments().getString("projectId");
        }

        mAssigneeViewModel = ViewModelProviders.of(getActivity()).get(AssigneeViewModel.class);
        mAssigneeRecyclerView = view.findViewById(R.id.assign_recyclerview);
        mAdapter = new AssigneeAdapter(getActivity());
        mBtnAddTask = view.findViewById(R.id.btn_add_task);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAssigneeRecyclerView.setLayoutManager(layoutManager);
        mAssigneeRecyclerView.setAdapter(mAdapter);
        mTitle = view.findViewById(R.id.et_add_task_title);
        mDesc =view.findViewById(R.id.et_add_task_desc);
        mStartDate =view.findViewById(R.id.et_task_start_date);
        mEndDate =view.findViewById(R.id.et_task_end_date);
        calendar = Calendar.getInstance();
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);


        projectList = mColabViewModel.getProjects().getValue();
        for (Project pro: projectList) {

            if (pro.getProject_id().equals(projectId)) {
                mCurrentProject = pro;
            }
        }


        List<String> members = mCurrentProject.getMembers();
        Log.d("IN TASK FRAGMENT ****",members.toString());


        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateLabelStart();
            }
        };

        final DatePickerDialog.OnDateSetListener dateDi2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateLabelEnd();
            }
        };

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi2, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mBtnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = FirebaseFirestore.getInstance().collection("tasks").document();
                Task task = new Task();
                List<String> assignees = new ArrayList<>();
                for (Contact con: list) {
                    if(con.isSelected()){
                        assignees.add(con.getAuth_id());
                    }
                }
                task.setAssigned_to(assignees);
                task.setProject_id(projectId);
                task.setName(mTitle.getText().toString());
                task.setDesc(mDesc.getText().toString());
                task.setStart_date(mStartDate.getText().toString());
                task.setEnd_date(mEndDate.getText().toString());
                task.setStatus("0");
                task.setTask_id(ref.getId());

                ref.set(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                    }
                });
            }
        });

        return view;
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mStartDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mEndDate.setText(sdf.format(calendar.getTime()));
    }

}
