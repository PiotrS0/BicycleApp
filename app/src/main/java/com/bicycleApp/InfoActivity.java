package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private String TEXT;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        textView = findViewById(R.id.textInfo);
        setInfoText();
        textView.setText(TEXT);
        button = findViewById(R.id.btn_back5);
        button.setOnClickListener((view -> {finish();}));
    }

    private void setInfoText(){
        TEXT = "Aplikacja została wykonana przez Piotra Sawczuka.";
    }
}