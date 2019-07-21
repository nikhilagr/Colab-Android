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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ContactsAdapter;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.User;
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

    private RecyclerView mMembersRecyclerview;
    private ContactsAdapter mContactsAdapter;
    private ContactViewModel mVContactViewModel;
    List<Contact> contactList;


    public AddMembersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_members, container, false);

        mMembersRecyclerview = view.findViewById(R.id.rcv_members);
        mVContactViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);
        mContactsAdapter = new ContactsAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMembersRecyclerview.setLayoutManager(layoutManager);
        mMembersRecyclerview.setAdapter(mContactsAdapter);
        contactList = new ArrayList<>();

        addContacts();

        mVContactViewModel.getContactList().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {


                mContactsAdapter.setContactList(contacts);
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

                if(email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    continue;
                }



                Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",email);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot snap = task.getResult();
                        if(snap.isEmpty()){
                            // It means User is not registered
                        }else{
                           //User is registered
                            List<DocumentSnapshot> docs = snap.getDocuments();
                            for (DocumentSnapshot sn: docs) {
                                   User user  = sn.toObject(User.class);

                                   user.getUser_id();
                                   Contact contact = new Contact(user.getFirst_name() + " "+ user.getLast_name(),user.getEmail(),"",false,user.getUser_id());
                                   mVContactViewModel.addContactToLiveData(contact);
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


    }

}
