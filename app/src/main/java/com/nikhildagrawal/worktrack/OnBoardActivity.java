package com.nikhildagrawal.worktrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.nikhildagrawal.worktrack.adapters.SliderAdapter;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private SliderAdapter mSliderAdapter;
    private MaterialButton mBtnSignUp;
    private MaterialButton mBtnSignIn;
    private TextView[] mDots;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        mViewPager = findViewById(R.id.viewpager);
        dotsLayout = findViewById(R.id.dotsLayout);
        mSliderAdapter = new SliderAdapter(this);
        mViewPager.setAdapter(mSliderAdapter);
        mBtnSignIn = findViewById(R.id.btn_sigin);
        mBtnSignUp = findViewById(R.id.btn_signup);

        addDots(0);

        mViewPager.addOnPageChangeListener(viewListner);

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OnBoardActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });


        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OnBoardActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    private void addDots(int position){
        mDots = new TextView[4];
        dotsLayout.removeAllViews();

        for(int i=0; i < mDots.length ;i++ ){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(25);
            mDots[i].setTextColor(getResources().getColor(R.color.colorwhite));
            dotsLayout.addView(mDots[i]);

        }
            if(mDots.length > 0){
                mDots[position].setTextColor(getResources().getColor(R.color.colordots));
            }

    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}
