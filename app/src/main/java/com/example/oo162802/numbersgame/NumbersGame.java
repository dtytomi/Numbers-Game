package com.example.oo162802.numbersgame;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
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
    private int score, answer, valueLeft, valueRight, totalNumberOfQuestion,
            level = 1,totalNumberAnswered;
    private double result, correctAnswer;
    private String  timeUpdate;
    private int timeLeft;
    private Handler handler;

    private EditText userEditText;
    private TextView numberTextView1;
    private TextView numberTextView2;
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
        handler = new Handler();


        Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(enterButtonListener);

        score = 0;
        totalNumberAnswered = 0;
        scoreTextView.setText(String.valueOf(score));

        start();

    }

    public void start(){

        switch (level){
            case 1: totalNumberOfQuestion = 10;
                             timeLeft = 30000;
                             loadQuestion();
                break;
            case 2: totalNumberOfQuestion = 15;
                                 timeLeft = 45000;
                                 loadQuestion();
                break;
            case 3: totalNumberOfQuestion = 20;
                            timeLeft = 60000;
                            loadQuestion();
                break;
            case 4: totalNumberOfQuestion = 30;
                        timeLeft = 90000;
                        loadQuestion();
                break;
        }

        new CountDownTimer(timeLeft, 1000){
            public void onTick(long millisUntilFinished) {
                timerView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerView.setText("Time Up!");
                gameEnd();
            }
        }.start();

    }

   private void loadQuestion(){

           valueLeft = 1 + randomNumber.nextInt(totalNumberOfQuestion);
           valueRight = 1 + randomNumber.nextInt(totalNumberOfQuestion);
           result = 0.0;

           int randomSymbol = randomNumber.nextInt(4);
           int questionMark = 1 + randomNumber.nextInt(5);
           int temp;

           switch (symbol[randomSymbol]) {
               case '+':
                   result = valueLeft + valueRight;
                   symbolTextView.setText("+");
                   break;
               case '/':
                   if (valueLeft < valueRight){
                       temp = valueLeft;
                       valueLeft = valueRight;
                       valueRight = temp;
                   }
                   result = valueLeft / valueRight;
                   symbolTextView.setText("/");
                   break;
               case 'x':
                   result = valueLeft * valueRight;
                   symbolTextView.setText("x");
                   break;
               case '-':
                   if (valueLeft < valueRight){
                       temp = valueLeft;
                       valueLeft = valueRight;
                       valueRight = temp;
                   }
                   result = valueLeft - valueRight;
                   symbolTextView.setText("-");
                   break;
           }

           if (questionMark % 2 == 0) {
               numberTextView1.setText(String.valueOf(valueLeft));
               correctAnswer = valueRight;
               numberTextView2.setText("?");
               resultTextView.setText(String.format("%.2f", result));
           }

           if (questionMark % 2 == 1) {
               numberTextView1.setText("?");
               correctAnswer = valueLeft;
               numberTextView2.setText(String.valueOf(valueRight));
               resultTextView.setText(String.format("%.2f", result));
           }

           if (questionMark % 2 == 1 && questionMark % 3 == 0) {
               numberTextView1.setText(String.valueOf(valueLeft));
               numberTextView2.setText(String.valueOf(valueRight));
               resultTextView.setText("?");
               correctAnswer = result;
           }


   }

     private void submitAnswer(){
        answer =  Integer.parseInt(userEditText.getText().toString());

        if(answer == correctAnswer){
            score += 1;
            scoreTextView.setText(String.valueOf(score));
            totalNumberAnswered++;

            if ( totalNumberAnswered == totalNumberOfQuestion){

                AlertDialog.Builder builder = new AlertDialog.Builder(NumbersGame.this);

                builder.setMessage("You Won");
                builder.setPositiveButton("Go To Next Stage", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level++;
                        NumbersGame.this.start();
                    }
                });

                builder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = 1;
                        NumbersGame.this.start();
                    }
                });

                AlertDialog userWon = builder.create();

                userWon.show();
            }
            else {
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                              loadQuestion();
                            }
                        }, 1000);
            }

        }
        else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NumbersGame.this);

                builder.setMessage("Game Over");
                builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = 1;
                        score = 0;
                        totalNumberAnswered = 0;
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
            submitAnswer();
            userEditText.getText().clear();
        }
    };

    private void gameEnd(){
        if ( totalNumberAnswered == totalNumberOfQuestion ){
            AlertDialog.Builder builder = new AlertDialog.Builder(NumbersGame.this);

            builder.setMessage("You Won");
            builder.setPositiveButton("Go To Next Stage", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    level++;
                    NumbersGame.this.start();
                }
            });

            builder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    level = 1;
                    NumbersGame.this.start();
                }
            });

            AlertDialog userWon = builder.create();

            userWon.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(NumbersGame.this);

            builder.setMessage("Game Over");
            builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    level = 1;
                    score = 0;
                    totalNumberAnswered = 0;
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
}
