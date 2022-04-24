package com.example.datastructure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimeLeft,textViewRightAnswers,textViewQuestion;
    private TextView textViewOption0,textViewOption1,textViewOption2,textViewOption3;

    private int answer;
    private boolean isPositive;
    private String question;
    private int answerPosition;
    private int min = 10;
    private int max = 100;
    private int earnedPoints = 0;
    private int numberOfQuestions = 0;
    private boolean gameOver = false;
    private long bonusTime;

    private ArrayList<TextView> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimeLeft = findViewById(R.id.textViewTimeLeft);
        textViewRightAnswers = findViewById(R.id.textViewRightAnswers);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewOption0 = findViewById(R.id.textView_option1);
        textViewOption1 = findViewById(R.id.textView_option2);
        textViewOption2 = findViewById(R.id.textView_option3);
        textViewOption3 = findViewById(R.id.textView_option4);
        options.add(textViewOption0);
        options.add(textViewOption1);
        options.add(textViewOption2);
        options.add(textViewOption3);

        textViewRightAnswers.setText("0/0");
        playNext();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(earnedPoints < 15) {
           preferences.edit().putLong("time",25000).apply();
            bonusTime = preferences.getLong("time",17);
        } else {
            bonusTime = 20000;
        }

        CountDownTimer timer = new CountDownTimer(bonusTime,1000) {
            @Override
            public void onTick(long l) {
                textViewTimeLeft.setText(getTime(l));
                if(l < 10000) {
                    textViewTimeLeft.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
               int max =  preferences.getInt("max",0);
                if(max <= earnedPoints) {
                    preferences.edit().putInt("max",earnedPoints).apply();
                }
                Intent intent = new Intent(MainActivity.this,ScoreActivity.class);
                intent.putExtra("result",earnedPoints);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void playNext() {
        generateQuestion();
        for (int i = 0; i < options.size(); i++) {
            if(i == answerPosition) {
                options.get(i).setText(Integer.toString(answer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
    }

    public void generateQuestion() {
        int firstNumber = (int) (Math.random() * (max - min + 1) + min);
        int secondNumber = (int) (Math.random() * (max - min + 1) + min);
        int operator = (int) (Math.random() * 2);
        isPositive = operator == 1;
        if(isPositive) {
            answer = firstNumber + secondNumber;
            question = String.format("%s + %s",firstNumber,secondNumber);
        } else {
            answer = firstNumber - secondNumber;
            question = String.format("%s - %s",firstNumber,secondNumber);
        }
        textViewQuestion.setText(question);
        answerPosition = (int) (Math.random() * 4);
    }

    private int generateWrongAnswer() {
         int result;
         do {
             result = (int) (Math.random() * max * 2 + 1) - (max - min);
         } while(result == answer);
         return result;
    }

    private String getTime(long mills) {
        int seconds = (int) (mills / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
    }

    public void onClickPlayNext(View view) {
        if(!gameOver) {
            TextView textView = (TextView) view;
            String answerString = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answerString);
            if (chosenAnswer == answer) {
                Toast.makeText(this, "To'g'ri", Toast.LENGTH_SHORT).show();
                numberOfQuestions++;
                earnedPoints++;
                String allQuestions = String.format("%s/%s", earnedPoints, numberOfQuestions);
                textViewRightAnswers.setText(allQuestions);
            } else {
                Toast.makeText(this, "Noto'g'ri", Toast.LENGTH_SHORT).show();
                numberOfQuestions++;
                String allQuestions = String.format("%s/%s", earnedPoints, numberOfQuestions);
                textViewRightAnswers.setText(allQuestions);
            }
            playNext();
        }
    }
}