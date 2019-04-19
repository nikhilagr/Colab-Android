package com.nikhildagrawal.worktrack.viewmodels;

import com.nikhildagrawal.worktrack.models.Contact;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactViewModel extends ViewModel {

    MutableLiveData<List<Contact>> mList;

    public ContactViewModel(){
        mList = new MutableLiveData<>();
    }

    public LiveData<List<Contact>> getContactList(){
        return mList;
    }

    public List<Contact> getUnmutableList(){
        List<Contact> list = new ArrayList<>();
        list = mList.getValue();
        return list;
    }

    public void addContactToLiveData(Contact contact){

        List<Contact> list = mList.getValue();

        if(list != null){

             list = mList.getValue();
             if(!containsEmail(contact.getEmail())){
                 list.add(contact);
                 mList.setValue(list);
             }
        }else{
            if(!containsEmail(contact.getEmail())){
                List<Contact> list1 = new ArrayList<>();
                list1.add(contact);
                mList.setValue(list1);
            }
        }
    }

    public boolean containsEmail(String email){

        List<Contact> list = mList.getValue();
        if(list == null){
            return false;
        }else{
            for (Contact con: list) {
               if(con.getEmail().equals(email)){
                   return true;
               }
            }
            return false;
        }
    }


}
