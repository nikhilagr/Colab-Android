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
import android.widget.LinearLayout;
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
import com.nikhildagrawal.worktrack.interfaces.FCM;
import com.nikhildagrawal.worktrack.interfaces.TaskClickListner;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.FirebaseCloudMessage;
import com.nikhildagrawal.worktrack.models.NotificationData;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.repository.ColabRepository;
import com.nikhildagrawal.worktrack.utils.Constants;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ContactViewModel;
import com.nikhildagrawal.worktrack.viewmodels.TaskViewModel;
import okhttp3.ResponseBody;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewProjectFragment extends Fragment implements TaskClickListner {



    private static final String TAG = "AddNewProjectFragment";

    private TextInputEditText mETTitle;
    private TextInputEditText mETDesc;
    private TextInputEditText mETStartDate;
    private TextInputEditText mETEndDate;
    private View mView;
    private TextView tvMessageTaskList;
    private Calendar calendar;
    private MembersAdapter mMembersAdapter;
    private TasksAdapter mTasksAdpater;
    private DocumentReference reference;
    private ColabRepository mColabRepository;
    private String currentUserId;
    private ContactViewModel contactViewModel;
    private static String projectId;
    private List<String> members;
    private List<String> tasksList;
    private List<String> memberTokenIds;
    private SharedPreferences pref;
    private String inMode;
    private Integer position;
    private List<Project> projectList;
    private List<Contact> contacts;
    private ImageButton mButtonAddMemebers;
    private ImageButton mButtonAddTask;
    private MaterialButton mButtonAddProject;
    private MaterialButton mButtonSaveProject;
    private MaterialButton mButtonDeleteProject;
    private TaskViewModel mTaskViewModel;
    private RecyclerView mTasksRecyclerview;
    private RecyclerView mMembersRecyclerview;
    private ColabViewModel mColabViewModel;
    private  boolean contactsProcessed;
    private String currentUserName;
    private LinearLayout buttonLayout;

    public AddNewProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_add_new_project, container, false);

        initUiComponents();


        members = new ArrayList<>(); // List to store ids of members
        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);
        contactViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);

        mColabRepository = ColabRepository.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);

        setupMembersRecyclerView();

        setupTasksRecyclerView();


        inMode = pref.getString("Mode",null);
        position = pref.getInt("pos",-1);
        contactsProcessed = pref.getBoolean("contactProcessed", false);
        memberTokenIds = new ArrayList<>();

        checkMode();

        setupAddMode();


        if(inMode.equals("Edit")){

            buttonLayout.setVisibility(View.VISIBLE);
            mButtonAddProject.setVisibility(View.GONE);

            projectId = pref.getString(projectId,null);

             projectList = mColabViewModel.getProjects(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue();
            if(projectId == null){

                // Project Id will be initially passed from colab fragment
                if(getArguments()!=null){
                    Integer pos = getArguments().getInt("position");
                    String proj = projectList.get(pos).getProject_id();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("projectId", proj); // Storing string
                    editor.putInt("position",pos);
                    editor.commit();
                    projectId = proj;
                    position = pos;

                }
            }


            mETTitle.setText(projectList.get(position).getTitle());
            mETDesc.setText(projectList.get(position).getDescription());
            mETStartDate.setText(projectList.get(position).getStart_date());
            mETEndDate.setText(projectList.get(position).getEnd_date());

            List<String> currentProjMembers = projectList.get(position).getMembers();

            if(!contactsProcessed){


                //TODO: ADD Preferences
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("contactProcessed", true); // Storing string
                editor.commit();

                for (String memberId: currentProjMembers) {

                    if(memberId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        continue;
                    }

                    DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(memberId);
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.isSuccessful()){
                                User user = task.getResult().toObject(User.class);

                                members.add(user.getUser_id());
                                memberTokenIds.add(user.getFcm_instance_token());
                                mMembersAdapter.updateUserInMembersAdpater(user);
                                Contact contact = new Contact(user.getFirst_name()+" "+user.getLast_name(),user.getEmail(),
                                        "",true,user.getUser_id());
                                contactViewModel.addContactToLiveData(contact);
                            }
                        }
                    });

                }
            }


        }


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

        // In add mode we show user which is logged in as a member in members list.
        DocumentReference ref1 = FirebaseFirestore.getInstance().collection("users").document(currentUserId);

        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                User user = task.getResult().toObject(User.class);

                currentUserName = user.getFirst_name()+ " " +user.getLast_name();
                mMembersAdapter.updateUserInMembersAdpater(user);
            }
        });

        mButtonAddMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                if(inMode.equals("Add")){
                    bundle.putString("projectId",reference.getId());
                }
                if(inMode.equals("Edit")){
                    bundle.putString("projectId",projectId);
                }

                bundle.putString("mode",inMode);

                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addMembersFragment,bundle);
            }
        });


        mButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                if(inMode.equals("Add")){
                    bundle.putString("projectId",reference.getId());
                }
                if(inMode.equals("Edit")){
                    bundle.putString("projectId",projectId);
                }
                bundle.putString("mode",inMode);
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addNewTaskFragment,bundle);
            }
        });

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




        List<User> membersList = new ArrayList<>();
        // To fill members recycler view using data from contactviewmodel.
        contacts = contactViewModel.getContactList().getValue();
        if(contacts != null){

            for (Contact contact : contacts) {

                if(contact.isSelected()){
                    if(members == null){
                        members = new ArrayList<>();
                    }
                    if(members.contains(contact)){
                        continue;
                    }
                    members.add(contact.getAuth_id());


                    DocumentReference ref = FirebaseFirestore.getInstance().collection("users").document(contact.getAuth_id());

                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.isSuccessful()){
                                User user = task.getResult().toObject(User.class);
                                memberTokenIds.add(user.getFcm_instance_token());
                                mMembersAdapter.updateUserInMembersAdpater(user);
                            }
                        }
                    });
                    mMembersAdapter.setUserList(membersList);
                }
            }
        }


        mButtonAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = mETTitle.getText().toString();
                String desc = mETDesc.getText().toString();
                String startDate = mETStartDate.getText().toString();
                String endDate = mETEndDate.getText().toString();
                String creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                List<String> stringListTaskIds = tasksList;

                if(!title.equals("")){

                    List<String> stringListMemberIds;

                    if(members!=null){

                        stringListMemberIds = members;

                        // update and add project to each users db
                        for (String memberId: stringListMemberIds) {

                            updateProjectInUserDb(memberId,projectId,1); // Id 1 to add project.

                        }
                    }
                    else{
                        stringListMemberIds = new ArrayList<>();
                    }

                    stringListMemberIds.add(currentUserId);
                    Project pro = new Project(reference.getId(),title,desc,creatorId,startDate,endDate,stringListMemberIds,stringListTaskIds);

                    // Insert project in projects collection
                    mColabRepository.insertProjectInFirestoreDb(pro,reference);


                    //TODO: Pass TokenIDS, title, message
                    Log.d("***Notificati Test ***",memberTokenIds.toString());

                    notifyMembers(memberTokenIds,"Added To New Project","You have been added to new project: "+mETTitle.getText().toString() + " by "+currentUserName);

                    getFragmentManager().popBackStackImmediate();
                }
                else{

                    Snackbar.make(mView,"Title is mandatory",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        mButtonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<String> stringListTaskIds = tasksList;

                Map<String,Object> map = new HashMap<>();
                map.put("project_id",projectId);
                map.put("title", mETTitle.getText().toString());
                map.put("description",mETDesc.getText().toString());
                map.put("creator_id",projectList.get(position).getCreator_id());
                map.put("start_date",mETStartDate.getText().toString());
                map.put("end_date",mETEndDate.getText().toString());
                map.put("tasks",stringListTaskIds);
                members.add(FirebaseAuth.getInstance().getCurrentUser().getUid());


                /**
                 * 1) Get old members list, find the members who added new to members list
                 * 2) find the deleted members , for deleted members we will need to
                 * update there user db and remove project entry
                 * 3) If member is same no need to change the
                 */

                List<String> oldMembers = projectList.get(position).getMembers();
                List<String> stringListMemberIds = members;

                for(String oldMember: oldMembers){

                    if(isStillMember(oldMember,members)){
                        continue;
                    }else{

                        //TODO: update the user remove current project id from projects array of the user (COMPLETED)
                        updateProjectInUserDb(oldMember,projectId,2); // Id 2 to remove proj
                    }
                }

                for(String newMember: members){

                    if(isStillMember(newMember,oldMembers)){
                        continue;
                    }else{
                        //TODO:Add current project to project list (COMPLETED)
                        updateProjectInUserDb(newMember,projectId,1); // Id 1 to add proj

                    }
                }


                map.put("members",stringListMemberIds);
                ColabRepository.getInstance().updateProjectInFirestoreDb(map,projectId);
                getFragmentManager().popBackStackImmediate();
        }
        });

        return mView;
    }





    private void notifyMembers(List<String> tokens,String title, String message){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FCM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FCM fcmAPI = retrofit.create(FCM.class);

        // attach the headers
        HashMap<String, String> headers = new HashMap<>();

        headers.put("Content-Type","application/json");
        headers.put("Authorization","key=" + Constants.SERVER_KEY);

        for(String token: tokens){
            Log.d(TAG,"NOTIFY USERS: Sending to token: "+ token);

            NotificationData data = new NotificationData();
            data.setTitle(title);
            data.setMessage(message);

            FirebaseCloudMessage firebaseCloudMessage = new FirebaseCloudMessage();
            firebaseCloudMessage.setTo(token);
            firebaseCloudMessage.setData(data);

            Call<ResponseBody> call = fcmAPI.send(headers,firebaseCloudMessage);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "ON Response: "+response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "ON Failure: "+t.getMessage());
                }
            });
        }

    }
    private void initUiComponents() {
        calendar = Calendar.getInstance();
        mButtonAddMemebers = mView.findViewById(R.id.btn_add_project_members);
        mButtonAddTask = mView.findViewById(R.id.btn_add_project_tasks);
        mButtonAddProject = mView.findViewById(R.id.btn_add_project);
        mButtonSaveProject = mView.findViewById(R.id.btn_save_project);
        mETTitle = mView.findViewById(R.id.et_add_project_title);
        mETDesc = mView.findViewById(R.id.et_add_project_desc);
        mETStartDate = mView.findViewById(R.id.et_add_project_start_date);
        mETEndDate = mView.findViewById(R.id.et_add_project_end_date);
        mTasksRecyclerview = mView.findViewById(R.id.tasks_recyclerview);
        mMembersRecyclerview = mView.findViewById(R.id.members_recyclerview);
        TextView tvMessageMembersList = mView.findViewById(R.id.empty_mem_list_message);
        tvMessageTaskList = mView.findViewById(R.id.empty_tas_list_message);
        buttonLayout = mView.findViewById(R.id.button_layout);
        mButtonDeleteProject = mView.findViewById(R.id.btn_delete_project);

    }



    private void setupTasksRecyclerView() {
        LinearLayoutManager layoutMana = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mTasksRecyclerview.setLayoutManager(layoutMana);
        mTasksAdpater = new TasksAdapter(getActivity(),this);
        mTasksRecyclerview.setAdapter(mTasksAdpater);
    }

    private void setupMembersRecyclerView() {
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mMembersRecyclerview.setLayoutManager(layoutManager);
        mMembersAdapter = new MembersAdapter(getActivity());
        mMembersRecyclerview.setAdapter(mMembersAdapter);
    }

    private void checkMode() {

        if(inMode == null) {

            if (getArguments() != null) {

                SharedPreferences.Editor editor = pref.edit();

                String mode = getArguments().getString("from colab");

                if (mode.equals("Add")) {

                    inMode = "Add";
                    editor.putString("Mode", "Add");

                } else {
                    inMode = "Edit";
                    editor.putString("Mode", "Edit");
                }

                editor.commit();
            }

        }
    }


    private void setupAddMode() {
        // Store reference in preference for add mode of projects
        if(inMode.equals("Add")){
            mButtonAddProject.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.GONE);

            projectId = pref.getString("projectId",null);

            if(projectId == null){
                reference = FirebaseFirestore.getInstance().collection("projects").document();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("projectId", reference.getId()); // Storing string
                editor.commit();

            }else{
                reference = FirebaseFirestore.getInstance().collection("projects").document(projectId);
            }
        }
    }




    private DatePickerDialog.OnDateSetListener setDataPicker(final TextInputEditText mET){

        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);

                String myFormat = "EEE, MMM d, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mET.setText(sdf.format(calendar.getTime()));
            }
        };

        return dateDi;
    }


    @Override
    public void onTaskClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addNewTaskFragment,bundle);

    }

    private boolean isStillMember(String memberId,List<String> memberIds){

        if(memberIds == null || memberIds.size() == 0 ){
            return false;
        }
        for(String mem: memberIds){
            if(mem.equals(memberId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 1) Get User from user db
     * 2) Add project to projects array and update User
     */
    private void updateProjectInUserDb(String userId, final String projId, final Integer operation){


        final DocumentReference re = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(userId);

        re.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                User user = task.getResult().toObject(User.class);

                List<String> pros = user.getProjects();

                //Add project from projectlist of user
                if(operation == 1){
                    pros.add(projId);
                }


                // Remove Project from projectlist of user
                if(operation == 2){

                    for(String pro: pros){
                        if(pro.equals(projectId)){
                            pros.remove(pro);
                            break;
                        }
                    }
                }

                Map<String,Object> map = new HashMap<>();
                map.put("projects",pros);

                re.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        // remove data from shared preference.

        contactViewModel.removeData();
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("projectId");
        editor.remove("Mode");
        editor.remove("position");
        editor.remove("contactProcessed");
        editor.commit();
        mTaskViewModel.clearTask();
        mTasksAdpater.clearTaskList();
        mColabViewModel.clearProject();



    }



}

