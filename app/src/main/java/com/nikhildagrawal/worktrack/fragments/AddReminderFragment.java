package com.nikhildagrawal.worktrack.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.RegisterActivity;
import com.nikhildagrawal.worktrack.repository.ReminderRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.fragment.app.Fragment;


public class AddReminderFragment extends Fragment {


    EditText etAddTitle;
    EditText etAddDesc;
    EditText etAddDate;
    EditText etAddTime;
    Button btnAddReminder;
    Calendar calendar;


    public AddReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        etAddTitle = view.findViewById(R.id.et_add_reminder_title);
        etAddDesc= view.findViewById(R.id.et_add_reminder_desc);
        etAddDate = view.findViewById(R.id.et_add_reminder_date);
        etAddTime = view.findViewById(R.id.et_add_reminder_time);
        btnAddReminder = view.findViewById(R.id.btn_add_reminder);
        calendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateLabel();
            }
        };

        etAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


            etAddTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etAddTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnAddReminder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ReminderRepository.getInstance().insertReminderInFireStore(etAddTitle.getText().toString(),etAddDesc.getText().toString()
                        ,etAddDate.getText().toString(),etAddTime.getText().toString());
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getFragmentManager().popBackStackImmediate();

            }
        });
        return view;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etAddDate.setText(sdf.format(calendar.getTime()));

    }


}
