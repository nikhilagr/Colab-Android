package com.nikhildagrawal.worktrack.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ContactsAdapter;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;
import com.nikhildagrawal.worktrack.viewmodels.ContactViewModel;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMembersFragment extends Fragment {



    RecyclerView mMembersRecyclerview;
    ContactsAdapter mAdapter;
    ContactViewModel mViewModel;
    ColabViewModel mColabViewModel;
    List<Project> currentprojectList;
    String projectId;
    Project mCurrentProject;
    List<Project> projectList;


    public AddMembersFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_members, container, false);

        if(getArguments()!=null){
            projectId = getArguments().getString("projectId");
        }
        mMembersRecyclerview = view.findViewById(R.id.rcv_members);
        mViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);
        
        mAdapter = new ContactsAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMembersRecyclerview.setLayoutManager(layoutManager);
        mMembersRecyclerview.setAdapter(mAdapter);
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);

        projectList = mColabViewModel.getProjects().getValue();
        for (Project pro: projectList) {

            if (pro.getProject_id().equals(projectId)) {
                mCurrentProject = pro;
            }
        }

        mColabViewModel.getProjects().observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projectList) {

            }
        });



        addContacts();

        mViewModel.getContactList().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {

                mAdapter.setContactList(contacts);

            }


        });

        return view;
    }

    public void addContacts(){


        try {


            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);

            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                final String email = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                final Contact contact = new Contact(name,email,phoneNumber,false);
                Log.d("HEYYYYYYYY::" , contact.toString());

                Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",email);



                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot snap = task.getResult();
                        if(snap.isEmpty()){

                            Log.d("Email::" ,email+ " Not registered");

                        }else{
                            Log.d("Email::***********" ,email+ "  registered");
                           
                                 List<DocumentSnapshot> docs = snap.getDocuments();
                            for (DocumentSnapshot sn: docs) {
                                   User user  = sn.toObject(User.class);

                                   user.getUser_id();
                                   contact.setAuth_id(user.getUser_id());
                                    mViewModel.addContactToLiveData(contact);

                            }

                        }

                    }
                });


            }
            phones.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("exception::" ,e.getLocalizedMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        List<Contact> list = mViewModel.getContactList().getValue();

        for (Contact con :
                list){

            if(con.isSelected()){
                List<String> members = mCurrentProject.getMembers();
                if(members == null){
                    members = new ArrayList<>();
                }
                members.add(con.getAuth_id());
                mCurrentProject.setMembers(members);
                Log.d("ObSERving +-----", mCurrentProject.toString());

            }else{
                if(mCurrentProject != null && mCurrentProject.getMembers() != null){
                    List<String> members = mCurrentProject.getMembers();
                    members.remove(con.getAuth_id());
                    mCurrentProject.setMembers(members);
                }

            }

        }

        mColabViewModel.updateProjectInLiveData(mCurrentProject);
    }
}
