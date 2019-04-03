package com.nikhildagrawal.worktrack.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Note;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


/**
 * Singleton Pattern
 */
public class NotesRepository {

    private static NotesRepository instance;
    private List<Note> noteList = new ArrayList<>();

    public static NotesRepository getInstance(){
        if(instance == null){
            instance = new NotesRepository();
        }
        return instance;
    }


    public MutableLiveData<List<Note>> getNotesData(){

       // addFakeData();

        readDataFromFireStore();

        MutableLiveData<List<Note>> data = new MutableLiveData<>();
        data.setValue(noteList);
        return data;
    }


    private void addFakeData(){

        noteList.add(new Note("Test Note 1","This is note 1 test description"));
        noteList.add(new Note("Test Note 2","This is note 2 test description"));
        noteList.add(new Note("Test Note 3","This is note 3 test description"));
        noteList.add(new Note("Test Note 4","This is note 4 test description"));
        noteList.add(new Note("Test Note 5","This is note 5 test description"));
        noteList.add(new Note("Test Note 6","This is note 6 test description"));
        noteList.add(new Note("Test Note 7","This is note 7 test description"));
        noteList.add(new Note("Test Note 8","This is note 8 test description"));
        noteList.add(new Note("Test Note 9","This is note 9 test description"));
        noteList.add(new Note("Test Note 10","This is note 10 test description"));
    }

    private void readDataFromFireStore(){

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot document: task.getResult()) {

                        Note note = document.toObject(Note.class);
                        noteList.add(note);
                    }

                }else{

                }

            }
        });
    }


}
