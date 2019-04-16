package com.nikhildagrawal.worktrack.fragments;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.adapters.ContactsAdapter;
import com.nikhildagrawal.worktrack.models.Contact;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMembersFragment extends Fragment {



    RecyclerView mMembersRecyclerview;
    ContactsAdapter mAdapter;



    public AddMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_members, container, false);

        mMembersRecyclerview = view.findViewById(R.id.rcv_members);

        List<Contact> list = addContacts();

       // mAdapter = new ContactsAdapter(getActivity(),list);




        return view;
    }

    public List<Contact> addContacts(){

       final List<Contact> contactList = new ArrayList<>();
        try {

            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);

            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                final String email = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                final Contact contact = new Contact(name,email,phoneNumber);
                Log.d("HEYYYYYYYY::" , contact.toString());

                Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",email);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot snap = task.getResult();
                        if(snap.isEmpty()){

                            Log.d("Email::" ,email+ "Not registered");

                        }else{
                            Log.d("Email::***********" ,email+ "  registered");
                            contactList.add(contact);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });


            }
            phones.close();

            mAdapter = new ContactsAdapter(getActivity(),contactList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mMembersRecyclerview.setLayoutManager(layoutManager);
            mMembersRecyclerview.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("exception::" ,e.getLocalizedMessage());
        }

        return contactList;
    }



}
