package com.nikhildagrawal.worktrack.repository;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class TaskRepository {

    private static final String TAG = "TaskRepository";
    private static TaskRepository instance;

    private MutableLiveData<List<Task>> mList;

    public TaskRepository(){
        mList = new MutableLiveData<>();
    }

    public static TaskRepository getInstance(){
        if(instance == null){
            instance = new TaskRepository();
        }

        return instance;
    }

    public MutableLiveData<List<Task>> getTasks(String projectId){

        readTaskFromDb(projectId);
        return mList;
    }

    public void insertTaskInFirestoreDb(Task task, String taskId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.TASK_COLLECTION).document(taskId);

        ref.set(task).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    /**
     * To update task info based on task id.
     */
    public void updateTaskInFirestoreDb(Map<String,Object> map, String taskId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.TASK_COLLECTION).document(taskId);

        ref.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Successfully Updated Task");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Failed to update task: "+ e.getLocalizedMessage() );
            }
        });

    }


    public void deleteTaskFromFirestoreDb(String taskId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.TASK_COLLECTION).document(taskId);

        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Successfully deleted Task");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Failed to delete task: "+ e.getLocalizedMessage() );
            }
        });
    }



    public void readTaskFromDb(String projectId){

        Query query = FirebaseFirestore.getInstance().collection(Constants.TASK_COLLECTION).whereEqualTo("project_id",projectId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<Task> taskList = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()) {

                        Task task1 = document.toObject(Task.class);
                        taskList.add(task1);
                    }

                    mList.setValue(taskList);
                }
            }
        });
    }

    public void addTask(Task task){

        List<Task> list = mList.getValue();

        if(list!=null){
            list.add(task);
        }else{
            list = new ArrayList<>();
            list.add(task);
        }

        mList.setValue(list);
    }


    public void clearList(){
        List<Task> list = mList.getValue();

        if(list!=null){
            list.clear();
        }
        mList.setValue(list);
    }

}
