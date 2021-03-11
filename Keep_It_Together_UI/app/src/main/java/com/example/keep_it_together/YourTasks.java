package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class YourTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_tasks);
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        Drawable buttonDrawableLayout = getDrawable(R.drawable.menu_button);

        ArrayList<Button> buttons = new ArrayList<Button>();

        for(int i = 0; i < 10; i++){
            Button button = new Button(this);
            buttons.add(button);
            button.setBackground(buttonDrawableLayout);
            button.setCompoundDrawablePadding();
            buttonLayout.addView(button);
        }

    }
}