package com.nikhildagrawal.worktrack.repository;



import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.models.Reminder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ReminderRepository {

    private static final String TAG = "ReminderRepository";


    private static ReminderRepository instance;
    private MutableLiveData<List<Reminder>> mList;


    /**
     * Singleton class
     * @return
     */
    public static ReminderRepository getInstance(){
        if(instance == null){
            instance = new ReminderRepository();
        }

        return instance;
    }


    public ReminderRepository(){
        mList = new MutableLiveData<>();
    }


    public LiveData<List<Reminder>> getReminderList(){
        readReminderListFromFireStore();
        return mList;
    }



    public void readReminderListFromFireStore(){

        final List<Reminder> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reminders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot document: task.getResult()) {
                        Reminder reminder = document.toObject(Reminder.class);
                        list.add(reminder);

                    }
                    mList.postValue(list);

                }else{
                     if(list.isEmpty()){
                         mList.postValue(null);
                     }
                }
            }
        });

    }


    public void insertReminderInFireStore(String title,String desc,String date,String time){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("reminders").document();

        Reminder reminder = new Reminder(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                ref.getId(),title,desc,date,time);

        List<Reminder> list = mList.getValue();
        list.add(reminder);
        mList.setValue(list);

        ref.set(reminder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Log.d(TAG,"insertion in reminder db successful");
                }else{

                    Log.d(TAG,"insertion in reminder db unsuccessful");
                }
            }
        });
    }

}
