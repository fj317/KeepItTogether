package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class YourTasks extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_tasks);
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable buttonDrawableLayout = getDrawable(R.drawable.menu_button);

        ArrayList<Button> buttons = new ArrayList<Button>();

        for(int i = 0; i <= 10; i++){
            Button button = new Button(this);
            //buttons.add(button);
            button.setBackground(buttonDrawableLayout);
            //button.setTranslationY(i * 50);
            //button.setTextColor(0xffffff);
            button.setTextSize(25);
            button.setText("Task " + i);
            params.setMargins(0,0,0,25);
            button.setLayoutParams(params);
            buttonLayout.addView(button);
        }
    }
}