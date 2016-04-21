package com.example.oo162802.numbersgame;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import java.util.Random;

public class NumbersGame extends AppCompatActivity {

    private char[] symbol = {'-','+','x','/'};
    private int sum, score, answer, valueLeft, valueRight;
    private boolean left = false, right = false, neutral = false;
    private double result;

    private EditText userEditText;
    private TextView numberTextView1;
    private TextView numberTextView2;
    private TextView scoreTextView1;
    private TextView symbolTextView;
    private TextView scoreTextView;
    private TextView resultTextView;

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

        Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(enterButtonListner);

        score = 0;
        scoreTextView.setText(String.valueOf(score));

        problem();
    }

   private void problem(){

       valueLeft = 1 + randomNumber.nextInt(10);
       valueRight = 1 + randomNumber.nextInt(10);
       result = 0.0;

       int randomSymbol =  randomNumber.nextInt(4);
       int questionMark = 1 + randomNumber.nextInt(5);

       switch (symbol[randomSymbol]){
           case '+' : result = valueLeft + valueRight;
                        symbolTextView.setText("+");
                    break;
           case '/' : result = valueLeft / valueRight;
                        symbolTextView.setText("/");
                    break;
           case 'x' : result = valueLeft * valueRight;
                        symbolTextView.setText("x");
                    break;
           case '-' : result = valueLeft - valueRight;
                        symbolTextView.setText("-");
                    break;
       }

        if(questionMark % 2 == 0) {
            right = true;
            numberTextView1.setText(String.valueOf(valueLeft));
            numberTextView2.setText("?");
            resultTextView.setText(String.format("%.2f", result));
        }

       if(questionMark % 2 == 1) {
           left = true;
           numberTextView1.setText("?");
           numberTextView2.setText(String.valueOf(valueRight));
           resultTextView.setText(String.format("%.2f", result));
       }

       if(questionMark % 2 == 1 && questionMark % 3 == 0) {
           neutral = true;
           numberTextView1.setText(String.valueOf(valueLeft));
           numberTextView2.setText(String.valueOf(valueRight));
           resultTextView.setText("?");
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
        else if (rightValue != usersInput ){
                AlertDialog.Builder builder = new AlertDialog.Builder(NumbersGame.this);

                builder.setMessage("Game Over");
                builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NumbersGame.this.problem();
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

    public OnClickListener enterButtonListner = new OnClickListener() {
        @Override
        public void onClick(View v) {
          answer =  Integer.parseInt(userEditText.getText().toString());

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
