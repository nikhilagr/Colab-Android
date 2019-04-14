package com.nikhildagrawal.worktrack.repository;

import com.nikhildagrawal.worktrack.models.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {

    private TaskRepository mRepo;

    public TaskViewModel(){
        mRepo = TaskRepository.getInstance();
    }

    public LiveData<List<Task>> getTasks(String projectId){

        return  mRepo.getTasks(projectId);
    }
}
