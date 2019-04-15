package com.nikhildagrawal.worktrack.viewmodels;

import com.nikhildagrawal.worktrack.models.Reminder;
import com.nikhildagrawal.worktrack.repository.ReminderRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ReminderViewModel extends ViewModel {

    private ReminderRepository mRepo;

    public ReminderViewModel(){
        mRepo = ReminderRepository.getInstance();
    }

    public LiveData<List<Reminder>> getReminderList(){

        return mRepo.getReminderList();
    }
}
