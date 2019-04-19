package com.nikhildagrawal.worktrack.viewmodels;

import android.content.Context;

import com.nikhildagrawal.worktrack.models.Contact;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssigneeViewModel extends ViewModel {

    MutableLiveData<List<Contact>> assigneeList;
    Context mContext;

    public AssigneeViewModel(){
        assigneeList = new MutableLiveData<>();
    }


    public LiveData<List<Contact>> getAssigneeList(){
        return assigneeList;
    }

    public void addContactToLiveData(Contact contact){

        List<Contact> list = assigneeList.getValue();

        if(list != null){

            list.add(contact);
            assigneeList.setValue(list);

        }else{

                List<Contact> list1 = new ArrayList<>();
                list1.add(contact);
                assigneeList.setValue(list1);

        }
    }


    public void setAssigneeListViewModel(List<Contact> list){

        assigneeList.setValue(list);
    }

}
