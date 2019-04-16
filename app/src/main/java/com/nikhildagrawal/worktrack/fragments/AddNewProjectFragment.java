package com.nikhildagrawal.worktrack.fragments;



import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Project;
import com.nikhildagrawal.worktrack.models.Task;
import com.nikhildagrawal.worktrack.repository.ColabRepository;
import com.nikhildagrawal.worktrack.viewmodels.ColabViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewProjectFragment extends Fragment {



    private static final String TAG = "AddNewProjectFragment";
    private MaterialButton mButtonAddMemebers;
    private MaterialButton mButtonAddTask;
    private MaterialButton mButtonAddProject;
    private TextInputEditText mETTitle;
    private TextInputEditText mETDesc;
    private TextInputEditText mETStartDate;
    private TextInputEditText mETEndDate;
    private View mView;
    Calendar calendar;
    private ColabRepository mColabRepository;

    private ColabViewModel mColabViewModel;
    private List<Project> projectList;
    private List<Task> taskList;
    private String currentUserId;


    public AddNewProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_add_new_project, container, false);
        calendar = Calendar.getInstance();
        mButtonAddMemebers = mView.findViewById(R.id.btn_add_project_members);
        mButtonAddTask = mView.findViewById(R.id.btn_add_project_tasks);
        mButtonAddProject = mView.findViewById(R.id.btn_add_project);
        mETTitle = mView.findViewById(R.id.et_add_project_title);
        mETDesc = mView.findViewById(R.id.et_add_project_desc);
        mETStartDate = mView.findViewById(R.id.et_add_project_start_date);
        mETEndDate = mView.findViewById(R.id.et_add_project_end_date);
        mColabViewModel = ViewModelProviders.of(getActivity()).get(ColabViewModel.class);
        mColabRepository = ColabRepository.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final DatePickerDialog.OnDateSetListener dateDi = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateLabelStart();
            }
        };

        final DatePickerDialog.OnDateSetListener dateDi2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR , year);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateLabelEnd();
            }
        };


        mETStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mETEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDi2, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addNewTaskFragment);
            }
        });


        mButtonAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mETTitle.getText().toString();
                String desc = mETDesc.getText().toString();
                String startDate = mETStartDate.getText().toString();
                String endDate = mETEndDate.getText().toString();
                String creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                List<String> stringListTaskIds = new ArrayList<>();
                List<String> stringListMemberIds = new ArrayList<>();
                stringListMemberIds.add(currentUserId);
                Project pro = new Project(title,desc,creatorId,startDate,endDate,stringListMemberIds,stringListTaskIds);
                mColabRepository.insertProjectInFirestoreDb(pro);
                getFragmentManager().popBackStackImmediate();

            }
        });

        mButtonAddMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.fragment).navigate(R.id.action_addNewProjectFragment_to_addMembersFragment);
            }
        });

        return mView;
    }


    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mETStartDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mETEndDate.setText(sdf.format(calendar.getTime()));
    }
}
