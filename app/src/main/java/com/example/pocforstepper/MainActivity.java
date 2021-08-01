package com.example.pocforstepper;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvStepperStatusOne, tvStepperStatusTwo;
    private ImageView ivStepperStatusOne;
    private ProgressBar pbStatusBar;
    private Button btnNext, btnPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    private void initializeViews() {
        tvStepperStatusOne = findViewById(R.id.tv_stepper_1);
        tvStepperStatusTwo = findViewById(R.id.tv_stepper_2);
        ivStepperStatusOne = findViewById(R.id.iv_stepper_1);
        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        pbStatusBar = findViewById(R.id.progressBar);


        setProgressBarColor();

        //onclick
        btnNext.setOnClickListener(v -> proceedToStepTwo());
        btnPrev.setOnClickListener(v -> backStepOne());

    }

    private void setProgressBarColor() {
        LayerDrawable layerDrawable = (LayerDrawable) pbStatusBar.getProgressDrawable();
        layerDrawable.findDrawableByLayerId(android.R.id.background).setColorFilter(Color.parseColor("#BABDB9"),PorterDuff.Mode.SRC);
        Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
        progressDrawable.setColorFilter(Color.parseColor("#3AF806"), PorterDuff.Mode.SRC_IN);
    }


    private void proceedToStepTwo() {

        ObjectAnimator animation = ObjectAnimator.ofInt(pbStatusBar, "progress", 0, 100);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvStepperStatusTwo.setBackground(getResources().getDrawable(R.drawable.bg_circle_active));
                tvStepperStatusOne.setVisibility(View.INVISIBLE);
                ivStepperStatusOne.setVisibility(View.VISIBLE);
            }
        });
    }

    private void backStepOne() {
        tvStepperStatusTwo.setBackground(getResources().getDrawable(R.drawable.bg_circle_inactive));
        ObjectAnimator animation = ObjectAnimator.ofInt(pbStatusBar, "progress", 100, 0);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvStepperStatusOne.setVisibility(View.VISIBLE);
                ivStepperStatusOne.setVisibility(View.GONE);
            }
        });
    }
}