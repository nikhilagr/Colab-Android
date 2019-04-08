package com.nikhildagrawal.worktrack.repository;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * Singleton Pattern
 */

public class NotesRepository {

    private static NotesRepository instance;
    private MutableLiveData<List<Note>> mNoteList;



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

}









