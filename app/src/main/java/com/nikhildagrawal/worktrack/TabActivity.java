package com.nikhildagrawal.worktrack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nikhildagrawal.worktrack.models.User;
import com.nikhildagrawal.worktrack.utils.Constants;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabActivity extends AppCompatActivity {


    public static final String TAG = "TabActivity";
    private TextView mTextMessage;
    private Toolbar toolbar;
    private NavController mNavController;
    private String currentUserId = "";
    private List<String> userLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_tab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextMessage =  findViewById(R.id.message);
        mNavController = Navigation.findNavController(this,R.id.fragment);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        NavigationUI.setupWithNavController(navigation,mNavController);
        NavigationUI.setupActionBarWithNavController(this,mNavController);
        intiFCM();





    }


    private void intiFCM(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
               final String token = instanceIdResult.getToken();
                Log.d(TAG,"FCM token successfully received: "+ token);
                addFcmTokenToUserCollection(token);


            }
        });
    }

    private void addFcmTokenToUserCollection(final String token){


        Query query = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).whereEqualTo("user_auth_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot doc:task.getResult()) {

                    currentUserId = doc.getId();
                    DocumentReference ref = FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION).document(currentUserId);

                    Map<String,Object> map = new HashMap<>();
                    map.put("fcm_instance_token",token);
                    ref.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Log.d(TAG,"Successfully Updated FCM TOKEN");
                            }else{
                                Log.d(TAG,"Unable to Update FCM TOKEN");
                            }

                        }
                    });

                }
            }
        });

    }



    public ActionBar getBar(){
        return getSupportActionBar();
    }
    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(mNavController, (DrawerLayout) null);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TabActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        checkAuthenticationState();
    }

    private void checkAuthenticationState(){

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){

            Intent intent = new Intent(TabActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }else{
            Log.d("TabActivity","Successfully Authenticated:");
        }
    }


}
