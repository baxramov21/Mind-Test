package com.example.datastructure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.prefs.Preferences;

public class ScoreActivity extends AppCompatActivity {
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        textViewResult = findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int max = preferences.getInt("max",0);
        int result = intent.getIntExtra("result",0);
        String score = String.format("Sizning nachijangiz: %s\nRekord: %s",result,max);
        textViewResult.setText(score);
    }

    public void onClickStartNewGame(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}