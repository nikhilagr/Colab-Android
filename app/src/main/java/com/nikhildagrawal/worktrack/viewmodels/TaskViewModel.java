package com.nikhildagrawal.worktrack.viewmodels;

import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.repository.TaskRepository;

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

    public void addTask(Task task){
        mRepo.addTask(task);
    }


    public void clearTask(){
        mRepo.clearList();
    }



}
