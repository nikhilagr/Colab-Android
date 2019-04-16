package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Contact;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    Context mContext;
    List<Contact> contactList;


    public ContactsAdapter(Context context, List<Contact> contactList){
        mContext = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_contacts_list_fragment,parent,false);
        ContactsViewHolder holder = new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {

        holder.mName.setText(contactList.get(position).getName());


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
        Button mBtnInvite;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            mBtnInvite = itemView.findViewById(R.id.contact_list_button_invite);
            mName = itemView.findViewById(R.id.contact_list_tv_name);
            mCheckbox = itemView.findViewById(R.id.select_contact);
        }
    }
}
