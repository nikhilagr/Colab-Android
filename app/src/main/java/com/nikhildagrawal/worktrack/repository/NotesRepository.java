package com.nikhildagrawal.worktrack.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Note;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * Singleton Pattern
 */

public class NotesRepository {

    private static NotesRepository instance;
    private MutableLiveData<List<Note>> mNoteList;
    private Context mContext;
    private static final String TAG = "NotesRepository";



    public static NotesRepository getInstance(){


        if(instance == null){
            instance = new NotesRepository();
        }
        return instance;

    }

    public NotesRepository(){
        mNoteList = new MutableLiveData<>();

    }



    public LiveData<List<Note>> getNotesData(){
        readNoteFromFireStore();
        return mNoteList;
    }

    private void readNoteFromFireStore(){


        Query query = FirebaseFirestore.getInstance().collection("notes").whereEqualTo("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<Note> noteList = new ArrayList<>();

                    for (QueryDocumentSnapshot document: task.getResult()) {

                        Note note = document.toObject(Note.class);
                        noteList.add(note);
                    }

                    mNoteList.postValue(noteList);
                }else{
                    mNoteList.postValue(null);
                }

            }
        });
    }

    public void insertNoteInFirestore(String title,String desc){


        DocumentReference ref = FirebaseFirestore.getInstance().collection("notes").document();
        Note newNote = new Note(FirebaseAuth.getInstance().getCurrentUser().getUid(),ref.getId(),title,desc);

        List<Note> list = mNoteList.getValue();
        list.add(newNote);
        mNoteList.setValue(list);

        ref.set(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("AddNewNoteFragment","note inserted successfully");
                }else{
                    Log.d("AddNewNoteFragment","error while inserting");
                }
            }
        });
    }


    public void delteNoteFromFireStore(String noteId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection("notes").document(noteId);


        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //TODO: Check if need to remove from local list

                }
            }
        });
    }



    public void updateNoteInFirestore(Map<String,Object> map, String noteId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection("notes").document(noteId);


        ref.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Log.d(TAG,"Update successful");
                }else{
                    Log.d(TAG,"Update unsuccessful");
                }
            }
        });

    }

}









