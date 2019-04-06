package com.nikhildagrawal.worktrack.viewmodels;

import com.nikhildagrawal.worktrack.models.Checklist;
import com.nikhildagrawal.worktrack.repository.ChecklistRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ChecklistViewModel extends ViewModel {

    private ChecklistRepository mRepo;

    public ChecklistViewModel(){
        mRepo = ChecklistRepository.getInstance();
    }

    public LiveData<List<Checklist>> getChecklists(){
        return mRepo.getChecklists();
    }

}
