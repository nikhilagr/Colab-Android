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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.AssigneeAdapter;
import com.nikhildagrawal.worktrack.models.Assignee;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.repository.TaskRepository;
import com.nikhildagrawal.worktrack.viewmodels.AssigneeViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ContactViewModel;
import com.nikhildagrawal.worktrack.viewmodels.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private String projectId;
    private MaterialButton mBtnAddTask;
    private MaterialButton mBtnSaveTask;


    private TextInputEditText mTitle,mDesc,mStartDate,mEndDate;
    private Calendar calendar;
    List<Assignee> assigneeList;
    private ContactViewModel mContactViewModel;

    private Assignee assignee;
    private String mode;
    private int position;
    View view;

    TaskViewModel mTaskViewModel;

    public AddNewTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_new_task, container, false);


        if(getArguments()!=null){
            projectId = getArguments().getString("projectId");
            position = getArguments().getInt("position",-1);
        }

        if(position == -1){
            mode = "add";
        }else{
            mode = "edit";
        }

        mContactViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);
        mAssigneeViewModel = ViewModelProviders.of(getActivity()).get(AssigneeViewModel.class);
        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);

        mAssigneeRecyclerView = view.findViewById(R.id.assign_recyclerview);
        mAdapter = new AssigneeAdapter(getActivity());
        mBtnAddTask = view.findViewById(R.id.btn_add_task);
        mBtnSaveTask = view.findViewById(R.id.btn_save_task);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAssigneeRecyclerView.setLayoutManager(layoutManager);
        mAssigneeRecyclerView.setAdapter(mAdapter);

        mTitle = view.findViewById(R.id.et_add_task_title);
        mDesc =view.findViewById(R.id.et_add_task_desc);
        mStartDate =view.findViewById(R.id.et_task_start_date);
        mEndDate =view.findViewById(R.id.et_task_end_date);
        calendar = Calendar.getInstance();

        final List<Task> tasks = mTaskViewModel.getTasks(projectId).getValue();

        if(mode.equals("edit")){
            mBtnSaveTask.setVisibility(View.VISIBLE);
            mBtnAddTask.setVisibility(View.GONE);
            Task task = tasks.get(position);
            mTitle.setText(task.getName());
            mDesc.setText(task.getDesc());
            mStartDate.setText(task.getStart_date());
            mEndDate.setText(task.getEnd_date());

        }else{
            mBtnSaveTask.setVisibility(View.GONE);
            mBtnAddTask.setVisibility(View.VISIBLE);
        }

        mTaskViewModel.getTasks(projectId).observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {


            }
        });



        final boolean selected;
        if(mode.equals("add")){
            selected = true;
        }else{
            Task task = tasks.get(position);
            List<String> assigneeIds = task.getAssigned_to();

            selected = isAssignee(FirebaseAuth.getInstance().getCurrentUser().getUid(),assigneeIds);
        }

        DocumentReference ref1 = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    User user = task.getResult().toObject(User.class);

                    assignee = new Assignee(user.getFirtst_name()+" "+user.getLast_name(),user.getEmail(),selected,user.getUser_id());
                }

                assigneeList.add(assignee);
                mAdapter.notifyDataSetChanged();
            }
        });


        if(mode.equals("add")){

            assigneeList = new ArrayList<>();
            List<Contact> list = mContactViewModel.getUnmutableList();
            for (Contact contact: list) {

                if(contact.isSelected() && !contact.getAuth_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Assignee assignee = new Assignee(contact.getName(),contact.getEmail(),true,contact.getAuth_id());
                    assigneeList.add(assignee);
                }
            }
            mAdapter.setAssigneeList(assigneeList);

        }else if(mode.equals("edit")){

            Task task = tasks.get(position);

                List<String> assigneeIds = task.getAssigned_to();


            assigneeList = new ArrayList<>();
            List<Contact> list = mContactViewModel.getUnmutableList();
            for (Contact contact: list) {
                boolean deci = isAssignee(contact.getAuth_id(),assigneeIds);

                if(contact.isSelected() && !contact.getAuth_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Assignee assignee = new Assignee(contact.getName(),contact.getEmail(),deci,contact.getAuth_id());
                    assigneeList.add(assignee);
                }
            }
            mAdapter.setAssigneeList(assigneeList);

        }




        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), setDataPicker(mStartDate), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), setDataPicker(mEndDate), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        mBtnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!mTitle.getText().toString().equals("")){

                    DocumentReference ref = FirebaseFirestore.getInstance().collection("tasks").document();
                    Task task = new Task();
                    List<String> assignees = new ArrayList<>();
                    for (Assignee con: assigneeList) {
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
                    TaskRepository.getInstance().insertTaskInFirestoreDb(task,ref.getId());
                    getFragmentManager().popBackStackImmediate();

                }
                else{
                    Snackbar.make(view,"Title is mandatory.",Snackbar.LENGTH_SHORT).show();
                }

            }


        });

        mBtnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = tasks.get(position);
                DocumentReference ref = FirebaseFirestore.getInstance().collection("tasks").document(task.getTask_id());

                Map<String,Object> map = new HashMap<>();
                map.put("task_id",task.getTask_id());
                map.put("project_id",task.getProject_id());
                map.put("status",task.getStatus());

                map.put("name",mTitle.getText().toString());
                map.put("desc",mDesc.getText().toString());
                map.put("start_date",mStartDate.getText().toString());
                map.put("end_date",mEndDate.getText().toString());


                List<String> assignees = new ArrayList<>();
                for (Assignee con: assigneeList) {
                    if(con.isSelected()){
                        assignees.add(con.getAuth_id());
                    }
                }

                map.put("assigned_to",assignees);


                ref.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                    }
                });
                getFragmentManager().popBackStackImmediate();


            }
        });

        return view;
    }


    private DatePickerDialog.OnDateSetListener setDataPicker(final TextInputEditText mET){

        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);

                String myFormat = "MM-dd-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mET.setText(sdf.format(calendar.getTime()));
            }
        };

        return dateDi;
    }

    private boolean isAssignee(String id,List<String> list){

    if(list == null || list.size() == 0){
        return false;
    }
        for(String str: list){
            if(str.equals(id)){
                return true;
            }
        }

        return false;
    }
}
