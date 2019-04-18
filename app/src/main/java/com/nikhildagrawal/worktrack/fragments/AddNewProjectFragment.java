package com.nikhildagrawal.worktrack.fragments;



import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.MembersAdapter;
import com.nikhildagrawal.worktrack.adapters.TasksAdapter;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.repository.ColabRepository;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ContactViewModel;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewProjectFragment extends Fragment {



    private static final String TAG = "AddNewProjectFragment";
    private MaterialButton mButtonAddMemebers;
    private MaterialButton mButtonAddTask;
    private MaterialButton mButtonAddProject;
    private TextInputEditText mETTitle;
    private TextInputEditText mETDesc;
    private TextInputEditText mETStartDate;
    private TextInputEditText mETEndDate;
    private View mView;
    private TextView tvMessageMembersList;
    Calendar calendar;
    RecyclerView  mMembersrecyclerview;
    RecyclerView mTasksRecyclerview;
    MembersAdapter mMembersAdapter;
    TasksAdapter mTasksAdpater;

    private ColabRepository mColabRepository;
    private ColabViewModel mColabViewModel;
    private String currentUserId;
    Project mProject;
    private ContactViewModel contactViewModel;
    private List<User> membersList;


    public AddNewProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_add_new_project, container, false);
        calendar = Calendar.getInstance();
        mButtonAddMemebers = mView.findViewById(R.id.btn_add_project_members);
        mButtonAddTask = mView.findViewById(R.id.btn_add_project_tasks);
        mButtonAddProject = mView.findViewById(R.id.btn_add_project);
        mETTitle = mView.findViewById(R.id.et_add_project_title);
        mETDesc = mView.findViewById(R.id.et_add_project_desc);
        mETStartDate = mView.findViewById(R.id.et_add_project_start_date);
        mETEndDate = mView.findViewById(R.id.et_add_project_end_date);
        membersList = new ArrayList<>();
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);
        contactViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);

        mColabRepository = ColabRepository.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mProject = new Project();

        mTasksRecyclerview = mView.findViewById(R.id.tasks_recyclerview);

        mMembersrecyclerview = mView.findViewById(R.id.members_recyclerview);

        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mMembersrecyclerview.setLayoutManager(layoutManager);

        tvMessageMembersList = mView.findViewById(R.id.empty_task_list_message);


        mMembersAdapter = new MembersAdapter(getActivity());

        mMembersrecyclerview.setAdapter(mMembersAdapter);


        contactViewModel.getContactList().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                Log.d("CONTACT LIST UPDATING", contacts.toString());
                for (Contact contact: contacts) {

                    if(contact.isSelected()){
                        List<String> members = mProject.getMembers();

                        if(members == null){
                            members = new ArrayList<>();
                        }
                        members.add(contact.getAuth_id());

                        DocumentReference ref = FirebaseFirestore.getInstance().collection("users").document(contact.getAuth_id());
                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){
                                    User user = task.getResult().toObject(User.class);
                                    mMembersAdapter.updateUserInMembersAdpater(user);
                                    tvMessageMembersList.setVisibility(View.GONE);
                                  //  mMembersAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }


            mMembersAdapter.setUserList(membersList);
            }

        });

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


        mETStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mETEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi2, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addNewTaskFragment);
            }
        });


        mButtonAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mETTitle.getText().toString();
                String desc = mETDesc.getText().toString();
                String startDate = mETStartDate.getText().toString();
                String endDate = mETEndDate.getText().toString();
                String creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                List<String> stringListTaskIds = new ArrayList<>();
                List<String> stringListMemberIds = new ArrayList<>();
                stringListMemberIds.add(currentUserId);
                Project pro = new Project(title,desc,creatorId,startDate,endDate,stringListMemberIds,stringListTaskIds);
                mColabRepository.insertProjectInFirestoreDb(pro);
                getFragmentManager().popBackStackImmediate();

            }
        });

        mButtonAddMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addMembersFragment);
            }
        });


        mColabViewModel.getProjects().observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {



            }
        });



        return mView;
    }


    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mETStartDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mETEndDate.setText(sdf.format(calendar.getTime()));
    }
}
