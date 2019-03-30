package com.nikhildagrawal.worktrack;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;


    public SliderAdapter(Context context){

        mContext = context;
    }

    public int[] slideImages = {
            R.drawable.checklist,R.drawable.imagework,R.drawable.checklist,R.drawable.imagework
    };

    public String[] slideTitles = {
            "Colab",
            "Reminders",
            "Checklist",
            "Notes"
    };

    public String[] slideDescrition = {
            "hjasbdfhjkasfdhgasjfhd kjh jkagshjdfgkljgasdf jagsljfd ljghahjsgdf",
            "aksjhdfgjasflk jhaskjdhfkjaf lkjhasdkfjh kasf lkajhsdfkjhkjahsdf jka",
            "jhasdfgjkagsfdjkga hjkagshjdfghasdgfg hjgakjsgdfjhasfdk ahsgdfhjg ahgsf",
            "jhasdfgjkagsfdjkga hjkagshjdfghasdgfg hjgakjsgdfjhasfdk ahsgdfhjg ahgsf"
    };

    public String[] slideCardColors = {

            "#ffaf49","#8bc34a","#c62828","#7b1fa2"
    };


    @Override
    public int getCount() {
        return slideTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        mLayoutInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.onboardpage_layout,container,false);

        ImageView imageView = view.findViewById(R.id.onboard_image);
        TextView textViewTitle = view.findViewById(R.id.onboard_title);
        TextView textViewDesc = view.findViewById(R.id.onboard_description);
        MaterialCardView cardView = view.findViewById(R.id.onboard_card);

        imageView.setImageResource(slideImages[position]);
        textViewTitle.setText(slideTitles[position]);
        textViewDesc.setText(slideDescrition[position]);
        cardView.setCardBackgroundColor(Color.parseColor(slideCardColors[position]));

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
