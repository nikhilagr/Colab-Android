package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Contact;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class    ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    Context mContext;
    List<Contact> contactList;




    public ContactsAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_contacts_list_fragment,parent,false);
        ContactsViewHolder holder = new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position) {

        holder.mName.setText(contactList.get(position).getName());

        Character label = contactList.get(position).getName().charAt(0);

        holder.mLabel.setText(label.toString());

        holder.mCheckbox.setChecked(contactList.get(position).isSelected());

        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    contactList.get(position).setSelected(true);

                }else{
                    contactList.get(position).setSelected(false);
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        if(contactList!=null){
            return contactList.size();
        }

        return 0;

    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckbox;
        TextView mName;
        TextView mLabel;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);


            mName = itemView.findViewById(R.id.contact_list_tv_name);
            mCheckbox = itemView.findViewById(R.id.select_contact);
            mLabel = itemView.findViewById(R.id.contact_name_label);
        }
    }

    public void setContactList(List<Contact> list){
        contactList = list;
        notifyDataSetChanged();
    }
}
