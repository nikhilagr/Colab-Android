package com.nikhildagrawal.worktrack.viewmodels;

import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.repository.NotesRepository;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {


    private NotesRepository mRepo;


    public  NoteViewModel() {
        mRepo = NotesRepository.getInstance();
    }


    public LiveData<List<Note>> getNotes() {
        return mRepo.getNotesData();
    }

}
