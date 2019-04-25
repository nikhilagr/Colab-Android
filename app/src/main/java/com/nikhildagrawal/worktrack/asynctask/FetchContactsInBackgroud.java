package com.nikhildagrawal.worktrack.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Contact;
import com.nikhildagrawal.worktrack.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class FetchContactsInBackgroud extends AsyncTask<Void,Void,List<Contact>>{

    Context mContext;
    private OnContactFetchListener mListner;

    public FetchContactsInBackgroud(Context context, OnContactFetchListener listener){
        mContext = context;
        mListner = listener;
    }
    @Override
    protected List<Contact> doInBackground(Void... voids) {

        final List<Contact> contactList = new ArrayList<>();


        try {


            Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);

            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                final String email = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));


                Contact contactObj = new Contact(name,email,email,false);
                contactList.add(contactObj);
            }

            phones.close();
        }

        catch (Exception e){
            e.printStackTrace();
            Log.d("exception::" ,e.getLocalizedMessage());
        }

        return contactList;
    }

    public interface OnContactFetchListener {
        void onContactFetch(List<Contact>  list);
    }

    @Override
    protected void onPostExecute(List<Contact> list) {
        super.onPostExecute(list);
        if(mListner!=null){
            mListner.onContactFetch(list);
        }
    }

}
