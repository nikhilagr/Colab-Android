package com.nikhildagrawal.worktrack.fragments;



import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
import com.nikhildagrawal.worktrack.viewmodels.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
    private ImageButton mButtonAddMemebers;
    private ImageButton mButtonAddTask;
    private MaterialButton mButtonAddProject;
    private TextInputEditText mETTitle;
    private TextInputEditText mETDesc;
    private TextInputEditText mETStartDate;
    private TextInputEditText mETEndDate;
    private View mView;
    private TextView tvMessageMembersList;
    private TextView tvMessageTaskList;
    private Calendar calendar;

    private RecyclerView  mMembersrecyclerview;
    private RecyclerView mTasksRecyclerview;
    private MembersAdapter mMembersAdapter;
    private TasksAdapter mTasksAdpater;
    private DocumentReference reference;
    private ColabRepository mColabRepository;
    private ColabViewModel mColabViewModel;
    private TaskViewModel mTaskViewModel;
    private String currentUserId;
    private ContactViewModel contactViewModel;
    private List<User> membersList;
    private static String projectId;
    List<String> members;
    List<String> tasksList;
    SharedPreferences pref;

    public AddNewProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_add_new_project, container, false);
        calendar = Calendar.getInstance();
        mButtonAddMemebers = mView.findViewById(R.id.btn_add_project_members);
        mButtonAddTask = mView.findViewById(R.id.btn_add_project_tasks);
        mButtonAddProject = mView.findViewById(R.id.btn_add_project);
        mETTitle = mView.findViewById(R.id.et_add_project_title);
        mETDesc = mView.findViewById(R.id.et_add_project_desc);
        mETStartDate = mView.findViewById(R.id.et_add_project_start_date);
        mETEndDate = mView.findViewById(R.id.et_add_project_end_date);
        mTasksRecyclerview = mView.findViewById(R.id.tasks_recyclerview);
        tvMessageMembersList = mView.findViewById(R.id.empty_mem_list_message);
        tvMessageTaskList = mView.findViewById(R.id.empty_tas_list_message);



        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);
        contactViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);


        mColabRepository = ColabRepository.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // Store reference in preference for add mode of projects
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        projectId = pref.getString("projectId",null);

        if(projectId == null){
            reference = FirebaseFirestore.getInstance().collection("projects").document();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("projectId", reference.getId()); // Storing string
            editor.commit();


        }else{
            reference = FirebaseFirestore.getInstance().collection("projects").document(projectId);
        }

        mMembersrecyclerview = mView.findViewById(R.id.members_recyclerview);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mMembersrecyclerview.setLayoutManager(layoutManager);
        mMembersAdapter = new MembersAdapter(getActivity());
        mMembersrecyclerview.setAdapter(mMembersAdapter);


        LinearLayoutManager layoutMana = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mTasksRecyclerview.setLayoutManager(layoutMana);
        mTasksAdpater = new TasksAdapter(getActivity());
        mTasksRecyclerview.setAdapter(mTasksAdpater);



        mTaskViewModel.getTasks(projectId).observe(getViewLifecycleOwner(), new Observer<List<com.nikhildagrawal.worktrack.models.Task>>() {
            @Override
            public void onChanged(List<com.nikhildagrawal.worktrack.models.Task> tasks) {
                tasksList = new ArrayList<>();

                if(!tasks.isEmpty()){
                    tvMessageTaskList.setVisibility(View.GONE);

                }else{
                    tvMessageTaskList.setVisibility(View.VISIBLE);
                }
                    mTasksAdpater.setTaskList(tasks);

                for (com.nikhildagrawal.worktrack.models.Task tas: tasks) {
                    tasksList.add(tas.getTask_id());
                }
            }

        });


        DocumentReference ref1 = FirebaseFirestore.getInstance().collection("users").document(currentUserId);

        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                User user = task.getResult().toObject(User.class);
                mMembersAdapter.updateUserInMembersAdpater(user);
            }
        });

        members = new ArrayList<>(); // List to store ids of members
        membersList = new ArrayList<>(); // List for members adapter

        // To fill members recycler view using data from contactviewmodel.
        List<Contact> contacts = contactViewModel.getContactList().getValue();

        if(contacts != null){
            for (Contact contact : contacts) {

                if(contact.isSelected()){

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
                            }
                        }
                    });
                    mMembersAdapter.setUserList(membersList);
                }
            }
        }

        mETStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), setDataPicker(mETStartDate), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mETEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), setDataPicker(mETEndDate), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        mButtonAddMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("projectId",reference.getId());
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addMembersFragment,bundle);
            }
        });

        mButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("projectId",reference.getId());
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addNewTaskFragment,bundle);
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


                List<String> stringListTaskIds = tasksList;

                List<String> stringListMemberIds;
                if(members!=null){
                    stringListMemberIds = members;
                }else{
                    stringListMemberIds = new ArrayList<>();
                }

                if(!title.equals("")){
                    stringListMemberIds.add(currentUserId);
                    Project pro = new Project(reference.getId(),title,desc,creatorId,startDate,endDate,members,stringListTaskIds);
                    // Insert data in db
                    mColabRepository.insertProjectInFirestoreDb(pro,reference);
                    contactViewModel.removeData();
                    getFragmentManager().popBackStackImmediate();
                }else{
                    Snackbar.make(mView,"Title is mandatory",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // remove data from shared preference.
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("projectId");
        editor.commit();

    }

    private DatePickerDialog.OnDateSetListener setDataPicker(final TextInputEditText mET){

        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mET.setText(sdf.format(calendar.getTime()));
            }
        };

        return dateDi;
    }


}
