package com.nikhildagrawal.worktrack.viewmodels;

import com.google.firebase.auth.FirebaseAuth;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.repository.ColabRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ColabViewModel extends ViewModel {



    private ColabRepository mRepo;

    public ColabViewModel(){
        mRepo = ColabRepository.getInstance();

    }

    public LiveData<List<Project>> getProjects(String userId){
       return mRepo.getProjects(userId);
    }

    public void addProjectToLiveData(Project pro){
        mRepo.addProjectToLiveData(pro);
    }

    public void deleteProjectToLiveData(Project pro){
        mRepo.deleteProjectToLiveData(pro);
    }

    public void updateProjectInLiveData(Project pro){
        mRepo.updateProjectInLiveData(pro);
    }

    public void addMemberToProject(String member,String projId){
        mRepo.addMemberToProject(member,projId);
    }





}
