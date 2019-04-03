package com.nikhildagrawal.worktrack;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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

public class TabActivity extends AppCompatActivity {


    public static final String TAG = "TabActivity";
    private TextView mTextMessage;
    private Toolbar toolbar;
    private NavController mNavController;

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
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
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

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }


}
