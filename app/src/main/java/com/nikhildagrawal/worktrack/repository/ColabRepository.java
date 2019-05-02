package com.nikhildagrawal.worktrack.repository;


import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class ColabRepository {

    private static final String TAG = "ColabRepository";

    private static ColabRepository instance;
    private MutableLiveData<List<Project>> mList;
    List<String> stringList;
    Map<String, Object> map ;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();




    public ColabRepository(){

        mList = new MutableLiveData<>();
        map = new HashMap<>();

    }

    public static ColabRepository getInstance(){

        if(instance == null){
            instance = new ColabRepository();
        }

        return instance;
    }


    public void insertProjectInFirestoreDb(final Project project, DocumentReference ref){

        final DocumentReference userRef =FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final String projectId = project.getProject_id();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User user =task.getResult().toObject(User.class);

                    stringList  = user.getProjects();
                    stringList.add(projectId);
                    map.put("projects",stringList);

                    userRef.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG,"Successfully added project id in user");
                            if(mList.getValue() != null){
                                List<Project>  temp = mList.getValue();
                                temp.add(project);
                                mList.postValue(temp);
                            }else{
                                List<Project>  temp = new ArrayList<>();
                                temp.add(project);
                                mList.postValue(temp);
                            }

                        }
                    });
            }
        });



        ref.set(project).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"New Project inserted successfully!!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Failed to insert new project!!");
            }
        });
    }


    public LiveData<List<Project>> getProjects(String userId){

        readProjectsFromFirestoreDb(userId);
        return mList;
    }




    /**
     * To read projects for a logged in user.
     * @param userId
     * @return
     */

    public void readProjectsFromFirestoreDb(String userId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(userId);


        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    List<String> projects = new ArrayList<>();

                    User user = task.getResult().toObject(User.class);

                    projects = user.getProjects();

                    final List<Project> listPro = new ArrayList<>();
                    for (String projectId: projects) {

                        DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constants.PROJECT_COLLECTION).document(projectId);

                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                Project project = task.getResult().toObject(Project.class);

                                listPro.add(project);
                                mList.setValue(listPro);

                            }
                        });
                    }


                }
                else{

                }
            }
        });




    }

    /**
     * To update project info based on project id.
     */
    public void updateProjectInFirestoreDb(Map<String,Object> map,String projectId){

        DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.PROJECT_COLLECTION).document(projectId);

        ref.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Successfully Updated Project");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Failed to update project: "+ e.getLocalizedMessage() );
            }
        });

    }


    public void deleteProjectFromFirestoreDb(final String projectId){

        //remove project from users project list.
        final DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);

                    List<String> projectIds = user.getProjects();
                    projectIds.remove(projectId);

                    Map<String, Object> map = new HashMap<>();
                    map.put("projects",projectIds);

                    ref.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG,"Project removed from users db Successfully!!");
                            }else{
                                Log.d(TAG,"UnSuccessful!");
                            }
                        }
                    });

                }
            }
        });

        //delete tasks from project.

        CollectionReference colRef = FirebaseFirestore.getInstance().collection(Constants.TASK_COLLECTION);

        Query query = colRef.whereEqualTo("project_id",projectId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<com.nikhildagrawal.worktrack.models.Task> projectList = task.getResult().toObjects(com.nikhildagrawal.worktrack.models.Task.class);

                for (com.nikhildagrawal.worktrack.models.Task pro : projectList) {

                    DocumentReference ref = FirebaseFirestore.getInstance().collection("tasks").document(pro.getTask_id());
                    ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG,"Deleting task...");
                        }
                    });
                }
            }
        });

        //delete project from project collection.
        DocumentReference proRef = FirebaseFirestore.getInstance().collection(Constants.PROJECT_COLLECTION).document(projectId);

        List<Project> proList = mList.getValue();

        for (Project pro: proList) {

            if(pro!=null){

                if(pro.getProject_id().equals(projectId)){
                    proList.remove(pro);
                }
            }
        }

        mList.setValue(proList);

        proRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Log.d(TAG,"Project successfully Deleted!!!!");
            }
        });

    }


    public void addProjectToLiveData(Project project){

        List<Project> list = mList.getValue();

        if(list != null){
            list.add(project);
        }else{
            list = new ArrayList<>();
            list.add(project);
        }

        mList.setValue(list);
    }

    public void deleteProjectToLiveData(String projectID){

        List<Project> list = mList.getValue();
        List<Project> newList = new ArrayList<>();

        if(list != null){

            for (Project pro: list) {
                if(pro!=null){
                    if(!pro.getProject_id().equals(projectID)){
                       newList.add(pro);
                }

                }
            }
        }else{
          return;
        }
        mList.setValue(newList);
    }

    public void updateProjectInLiveData(Project project){
        List<Project> list = mList.getValue();
        if(list != null){
            List<Project> tempList = list;

            for (Project pro : list) {

                if(pro.getProject_id().equals(project.getProject_id())){
                    list.remove(pro);
                }

            }
            for (Project pro : tempList) {

                if(pro.getProject_id().equals(project.getProject_id())){
                    list.add(project);

                }

            }
        }else{
            return;
        }

        mList.setValue(list);

    }

    public void addMemberToProject(String member, String projectId){

        List<Project> list = mList.getValue();

        if(list != null){
            List<Project> tempList = list;

            for (Project pro : list) {

                if(pro.getProject_id().equals(projectId)){
                    if(pro.getMembers() == null){
                        List<String> members = new ArrayList<>();
                        members.add(member);
                        pro.setMembers(members);
                    }else{
                        List<String> members = pro.getMembers();
                        members.add(member);
                        pro.setMembers(members);
                    }
                }
            }
        }else{
            return;
        }

        mList.setValue(list);
    }

    public void clearProject(){

        List<Project> list = mList.getValue();

        if(list!=null && list.size()>0){
            list.clear();
        }

        mList.setValue(list);
    }

}
