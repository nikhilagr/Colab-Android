package com.nikhildagrawal.worktrack.viewmodels;

import com.nikhildagrawal.worktrack.models.Note;
import com.nikhildagrawal.worktrack.repository.NotesRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {

    private MutableLiveData<List<Note>> mNotes;
    private NotesRepository mRepo;


    public void init(){
        if(mNotes!=null){
            return;
        }

        mRepo = NotesRepository.getInstance();
        mNotes = mRepo.getNotesData();
    }


    public LiveData<List<Note>> getNotes(){
        return mNotes;
    }

    public void addNewNote(final Note note){

        List<Note> noteList = mNotes.getValue();
        noteList.add(note);
        mNotes.postValue(noteList);

    }

}
