package com.nikhildagrawal.worktrack.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.repository.ChecklistRepository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddChecklistItemDialog extends DialogFragment {

    EditText mEditTextItem;
    Button mBtnCancle;
    Button mBtnAdd;
    private static final String TAG = "AddChecklistItemDialog";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_checklist_item,container,false);

        mEditTextItem = view.findViewById(R.id.et_dialog_checklist_item);
        mBtnAdd = view.findViewById(R.id.btn_dialog_checklist_add);
        mBtnCancle = view.findViewById(R.id.btn_dialog_checklist_cancle);


        final String status = "Incomplete";


        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChecklistRepository.getInstance().insertChecklistInFireStore(mEditTextItem.getText().toString(),status);
                dismiss();
            }
        });

        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
