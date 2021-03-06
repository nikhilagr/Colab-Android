package com.nikhildagrawal.worktrack.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.nikhildagrawal.worktrack.utils.ReminderNotificationReceiver;
import com.nikhildagrawal.worktrack.utils.Constants;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Reminder;
import com.nikhildagrawal.worktrack.repository.ReminderRepository;
import com.nikhildagrawal.worktrack.viewmodels.ReminderViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class AddReminderFragment extends Fragment implements OnItemSelectedListener {


    private EditText mTitle;
    private Spinner mDescription;
    private EditText mDate;
    private EditText mTime;
    private Button mBtnAddReminder, mBtnSave;
    private Calendar calendar;
    private ReminderViewModel mViewModel;
    private Integer mPosition;
    private Calendar mCalendar;
    private long startTime;
    private String mSpinnerSelection;
    private ArrayAdapter<String> dataAdapter;


    public AddReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        mTitle = view.findViewById(R.id.et_add_reminder_title);
        mDate = view.findViewById(R.id.et_add_reminder_date);
        mTime = view.findViewById(R.id.et_add_reminder_time);
        mBtnAddReminder = view.findViewById(R.id.btn_add_reminder);
        mBtnSave = view.findViewById(R.id.btn_save_reminder);

        mDescription = view.findViewById(R.id.et_add_reminder_desc);

        mDescription.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("e.g. Travel");
        categories.add("Travel");
        categories.add("Health");
        categories.add("Car");
        categories.add("Education");
        categories.add("Food");
        categories.add("Get Together");
        categories.add("Finance");

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDescription.setAdapter(dataAdapter);



        calendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();


        String str = "";
        mPosition = -1;


        createNotificationChannel();

        if(getArguments()!= null){
            str = getArguments().getString("from");
            mPosition = getArguments().getInt("position");
        }

        mViewModel = ViewModelProviders.of(getActivity()).get(ReminderViewModel.class);
        final List<Reminder> reminders =  mViewModel.getReminderList().getValue();

        if(str.contentEquals("ReminderClick")){
            int spinnerPosition = dataAdapter.getPosition(reminders.get(mPosition).getDesc());
            mSpinnerSelection = reminders.get(mPosition).getDesc();
            mBtnAddReminder.setVisibility(View.GONE);
            mBtnSave.setVisibility(View.VISIBLE);
            mTitle.setText(reminders.get(mPosition).getTitle());
            mDescription.setSelection(spinnerPosition);
            mDate.setText(reminders.get(mPosition).getDate());
            mTime.setText(reminders.get(mPosition).getTime());
        }


        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String curTitle = mTitle.getText().toString();
                String curDesc = mSpinnerSelection;
                String curDate = mDate.getText().toString();
                String curTime = mTime.getText().toString();

                Map<String,Object> map = new HashMap<>();

                if(!curTitle.equals(reminders.get(mPosition).getTitle())){
                    map.put(Constants.REMINDER_TITLE,curTitle);
                }

                if(!curDesc.equals(reminders.get(mPosition).getDesc())){

                    map.put(Constants.REMINDER_DESC,curDesc);
                }

                if(!curDate.equals(reminders.get(mPosition).getDate())){
                    map.put(Constants.REMINDER_DATE,curDate);
                }

                if(!curTime.equals(reminders.get(mPosition).getTime())){
                    map.put(Constants.REMINDER_TIME,curTime);
                }

                if(map.size()!=0){

                    ReminderRepository.getInstance().updateReminderFromFirestore(map,reminders.get(mPosition).getReminder_id());
                    setNotificationForReminder(getActivity());
                    Snackbar.make(v,"Changes saved successfully",Snackbar.LENGTH_LONG).show();
                    getFragmentManager().popBackStackImmediate();
                }else{
                    setNotificationForReminder(getActivity());
                    getFragmentManager().popBackStackImmediate();
                }


            }
        });




        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateLabel();
            }
        };

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mTime.setOnClickListener(new View.OnClickListener() {

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
                        mTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        mBtnAddReminder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setNotificationForReminder(getActivity());

                if(mSpinnerSelection == null){
                    mSpinnerSelection = "e.g. Travel";
                }
                ReminderRepository.getInstance().insertReminderInFireStore(mTitle.getText().toString(),mSpinnerSelection
                        ,mDate.getText().toString(),mTime.getText().toString());
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getFragmentManager().popBackStackImmediate();

            }
        });
        return view;
    }




    private void setNotificationForReminder(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm");
        String dateInString = mDate.getText().toString() + " " + mTime.getText().toString();
        try {
            Date date = sdf.parse(dateInString);
            startTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int id= (int) System.currentTimeMillis();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent( getContext() , ReminderNotificationReceiver.class);
        myIntent.putExtra("reminder_title", mTitle.getText().toString());
//        myIntent.putExtra("reminder_description", mDescription.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),id,myIntent,0);

        manager.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDate.setText(sdf.format(calendar.getTime()));
    }

    private void createNotificationChannel() {
        final String CHANNEL_ID = "REMINDER_CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.reminder_channel_name);
            String description = getString(R.string.reminder_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0){
            mSpinnerSelection  = parent.getItemAtPosition(position).toString();
        }else{
            Toast.makeText(getContext(), "Select the Reminder Type", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
