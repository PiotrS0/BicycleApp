package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class InfoActivity extends AppCompatActivity {

    private String TEXT;
    private TextView textView;
    private Button button;
    //private Toolbar toolbar;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        textView = findViewById(R.id.textInfo);
//        setInfoText();
//        textView.setText(TEXT);
//        button = findViewById(R.id.btn_back5);
//        button.setOnClickListener((view -> {finish();}));
    }

    private void functionTest(){


    }

    private void setInfoText(){
        TEXT = "Aplikacja została wykonana przez Piotra Sawczuka.";
    }
}