package com.nikhildagrawal.worktrack.repository;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Checklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ChecklistRepository {

    private static ChecklistRepository instance;
    private MutableLiveData<List<Checklist>> mList;
    private static final String TAG = "ChecklistRepository";



    public static ChecklistRepository getInstance(){

        if(instance == null){
            instance = new ChecklistRepository();
        }

        return instance;
    }


    public LiveData<List<Checklist>> getChecklists(){

        readChecklistFromFireStore();
        return mList;
    }

    public ChecklistRepository(){
        mList = new MutableLiveData<>();
    }


    public void readChecklistFromFireStore(){

        final List<Checklist> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("checklists").whereEqualTo("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {

                        Checklist checklist = documentSnapshot.toObject(Checklist.class);
                        list.add(checklist);
                    }

                    Collections.sort(list);
                    mList.postValue(list);


                }else{
                    //TODO:
                    Log.d(TAG,"read from checklists db Unsuccessful");
                    //mList.postValue(null);
                }
            }
        });

    }


    public void insertChecklistInFireStore(String item, String status){

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference ref = db.collection("checklists").document();

        Checklist checklistItem = new Checklist(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                ref.getId(),status,item);
        List<Checklist> list = mList.getValue();
        list.add(checklistItem);
        mList.setValue(list);
        Log.d(TAG,checklistItem.toString());

        ref.set(checklistItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Log.d(TAG,"insertion in checklists db successful");

                }else{
                    Log.d(TAG,"insertion in checklists db Unsuccessful");
                }
            }
        });

        //TODO: How can we return boolean
    }



    private boolean updateChecklistInFireStore(String checklist_id){

        //TODO: Complete the function
        return true;
    }



    public void deleteChecklisFromFireStore(String checkListItemId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection("checklists").document(checkListItemId);


        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //TODO: Check if need to remove from local list

                }
            }
        });
    }


    public void updateChecklistInFireStore(String checklistItemId, String status){

        DocumentReference ref = FirebaseFirestore.getInstance().collection("checklists").document(checklistItemId);

        ref.update("status",status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }



}
