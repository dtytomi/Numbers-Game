package com.example.oo162802.numbersgame;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Chronometer;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import java.util.Random;

public class NumbersGame extends AppCompatActivity {

    private char[] symbol = {'-','+','x','/'};
    private int score, answer, valueLeft, valueRight;
    private boolean left = false, right = false, neutral = false;
    private double result;
    private String level = "Beginner";

    private EditText userEditText;
    private TextView numberTextView1;
    private TextView numberTextView2;
    private TextView scoreTextView1;
    private TextView symbolTextView;
    private TextView scoreTextView;
    private TextView resultTextView;
    private Chronometer timerView;

    Random randomNumber = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberTextView1 = (TextView) findViewById(R.id.numberTextView1);
        numberTextView2 = (TextView) findViewById(R.id.numberTextView2);
        symbolTextView = (TextView) findViewById(R.id.symbolTextView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView1);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        symbolTextView = (TextView) findViewById(R.id.symbolTextView);
        userEditText = (EditText) findViewById(R.id.userInputAnswer);
        timerView = (Chronometer) findViewById(R.id.timerView);


        Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(enterButtonListener);

        score = 0;
        scoreTextView.setText(String.valueOf(score));

       start();

    }

    public void start(){
        switch (level){
            case "Beginner": problem(9, 30000);
                break;
            case "Intermediate": problem(15, 45000);
                break;
            case "Advance": problem(19, 50000);
                break;
            case "Pro": problem(29, 60000);
                break;
        }
    }

   private void problem( int numOfQuestions, final int timeLeft){

       new CountDownTimer(timeLeft, 1000){
           public void onTick(long millisUntilFinished) {
               timerView.setText("seconds remaining: " + millisUntilFinished / 1000);
           }

           public void onFinish() {
               timerView.setText("DONE!");
           }
       }.start();

       int questionBandWidth = numOfQuestions;

       while(numOfQuestions != 0 && timeLeft != 0 ) {

           valueLeft = 1 + randomNumber.nextInt(questionBandWidth);
           valueRight = 1 + randomNumber.nextInt(questionBandWidth);
           result = 0.0;

           int randomSymbol = randomNumber.nextInt(4);
           int questionMark = 1 + randomNumber.nextInt(5);

           switch (symbol[randomSymbol]) {
               case '+':
                   result = valueLeft + valueRight;
                   symbolTextView.setText("+");
                   break;
               case '/':
                   result = valueLeft / valueRight;
                   symbolTextView.setText("/");
                   break;
               case 'x':
                   result = valueLeft * valueRight;
                   symbolTextView.setText("x");
                   break;
               case '-':
                   result = valueLeft - valueRight;
                   symbolTextView.setText("-");
                   break;
           }

           if (questionMark % 2 == 0) {
               right = true;
               numberTextView1.setText(String.valueOf(valueLeft));
               numberTextView2.setText("?");
               resultTextView.setText(String.format("%.2f", result));
           }

           if (questionMark % 2 == 1) {
               left = true;
               numberTextView1.setText("?");
               numberTextView2.setText(String.valueOf(valueRight));
               resultTextView.setText(String.format("%.2f", result));
           }

           if (questionMark % 2 == 1 && questionMark % 3 == 0) {
               neutral = true;
               numberTextView1.setText(String.valueOf(valueLeft));
               numberTextView2.setText(String.valueOf(valueRight));
               resultTextView.setText("?");
           }

           --numOfQuestions;
       }

   }

    private void updateScore() {
        score += 1;
        scoreTextView.setText(String.valueOf(score));
    }

    private void checkAnswer(double rightValue, int usersInput){
        if(rightValue == usersInput){
            updateScore();
        }
        else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NumbersGame.this);

                builder.setMessage("Game Over");
                builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NumbersGame.this.start();
                    }
                });

            builder.setNegativeButton("End Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    NumbersGame.this.finish();
                }
            });

            AlertDialog userLose = builder.create();

            userLose.show();
        }
    }

    public OnClickListener enterButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
          answer =  Integer.parseInt(userEditText.getText().toString());
           userEditText.clearFocus();

          if(left){
              checkAnswer(valueLeft, answer);
          }

          if(right){
             checkAnswer(valueRight, answer);
          }

          if(neutral){
             checkAnswer(result, answer);
          }

        }
    };
}
