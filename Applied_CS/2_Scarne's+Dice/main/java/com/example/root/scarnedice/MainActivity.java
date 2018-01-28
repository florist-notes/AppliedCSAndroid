package com.example.root.scarnedice;

//import the libraries
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.os.Bundle;

import android.view.View;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

// Java Class Starts here
public class MainActivity extends AppCompatActivity {

    public static final int MAX_SCORE = 100; // max score to win
    public static final int MAX_DICE_VALUE = 6; // max possible dice value
    // 4 global variables
    public int userOverallScore = 0;
    public int userTurnScore = 0;
    public int compOverallScore = 0;
    public int compTurnScore = 0;

    public int currentDiceValue = 0;
    public boolean userTurn = true; //1 is user & 0 is computer // user starts the game always
    public boolean holdButtonPressed = false; //0 is no & 1 is yes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rollButton = (Button)findViewById(R.id.rollButton);
        final Button holdButton = (Button)findViewById(R.id.holdButton);
        final Button resetButton = (Button)findViewById(R.id.resetButton);
        TextView winTextView = (TextView) findViewById(R.id.winText);

        winTextView.setVisibility(View.INVISIBLE);

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll();
            }
        }); //calls the roll functionality upon touch
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });  //calls the reset functionality upon touch
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold();
            }
        });  //calls the hold functionality upon touch
    }
    //roll functionality
    public void roll()
    {
        Random random = new Random();
        currentDiceValue = random.nextInt(MAX_DICE_VALUE-1)+1;
        Log.d(" Dice Value : ",""+currentDiceValue);
        ImageView diceImage = (ImageView)findViewById(R.id.imageView);
        if(currentDiceValue == 1)
        {
            diceImage.setImageResource(R.drawable.dice1);
        }
        if(currentDiceValue == 2)
        {
            diceImage.setImageResource(R.drawable.dice2);
        }
        if(currentDiceValue == 3)
        {
            diceImage.setImageResource(R.drawable.dice3);
        }
        if(currentDiceValue == 4)
        {
            diceImage.setImageResource(R.drawable.dice4);
        }
        if(currentDiceValue == 5)
        {
            diceImage.setImageResource(R.drawable.dice5);
        }
        if(currentDiceValue == 6)
        {
            diceImage.setImageResource(R.drawable.dice6);
        }

        if(userTurn)
        {
            userTurnScore+=currentDiceValue;
        }
        else
        {
            compTurnScore+=currentDiceValue;
        }
        if(currentDiceValue == 1)
        {
            updateAction("rolled one");
            userTurn=!userTurn;
            userTurnScore=0;
            compTurnScore=0;
            updateTextView();
            updateUserTurnText();
            Log.d("Rolled 1","calling"+userTurn); //debug
            if(!userTurn)
                computerTurn();
            Log.d("1 is facing up","Turn values Reset"); //debug

        }
        updateTextView();
        updateUserTurnText();
    }
    // hold functionality
    public void hold()
    {
        TextView winTextView = (TextView) findViewById(R.id.winText);
        updateTextView();
        userOverallScore += userTurnScore; // update
        compOverallScore += compTurnScore; //update
        updateTextView();
        userTurnScore = 0;
        compTurnScore = 0;
        updateAction("holds");
        userTurn = !userTurn;
        if(!userTurn)
        {
            computerTurn();
        }

        updateTextView();
        if(userOverallScore >= 100 || compOverallScore >= 100)
        {
            winTextView.setVisibility(View.VISIBLE);
            if(userOverallScore >= 100)
                winTextView.setText(" USER WINS ! ");
            else
                winTextView.setText(" COMPUTER WINS ! ");

            userOverallScore = 0;
            userTurnScore = 0;
            compOverallScore = 0;
            compTurnScore = 0;
            currentDiceValue = 0;
            updateAction("Scarne\'s Dice");
            updateUserTurnText();
        }
        updateUserTurnText();
        updateUserTurnText();
    }
    // reset functionality
    public void reset()
    {
        Button rollButton = (Button)findViewById(R.id.rollButton);
        final Button holdButton = (Button)findViewById(R.id.holdButton);
        TextView winTextView = (TextView) findViewById(R.id.winText);
        // would want to make like initial state
        userOverallScore = 0;
        userTurnScore = 0;
        compOverallScore = 0;
        compTurnScore = 0;
        currentDiceValue = 0;
        userTurn = true;

        updateUserTurnText();
        updateTextView();

        holdButtonPressed = false;

        winTextView.setVisibility(View.INVISIBLE);
        holdButton.setEnabled(false);
        rollButton.setEnabled(false);
    }
    //To update the scores in UI
    public void updateTextView()
    {
        TextView textView = (TextView) findViewById(R.id.scoreView);
        TextView textView1 = (TextView) findViewById(R.id.tempScore);
        String textViewString="Your Overall Score:"+userOverallScore+" | Computer Overall Score:"+compOverallScore+".";
        String textViewString1="Your Score:"+userTurnScore+" | Computer Score:"+compTurnScore+".";
        textView.setText(textViewString);
        textView1.setText(textViewString1);
    }

    public void updateAction(String action)
    {
        TextView textActionView = (TextView) findViewById(R.id.action);
        String actionText = action;
        if (userTurn) {
            actionText = " User " + action;
        } else {
            actionText = " Computer " + action;
        }
        textActionView.setText(actionText);

    }

    public void updateUserTurnText()
    {
        TextView holdText = (TextView)findViewById(R.id.holdText);
        if(userTurn)
            holdText.setText(" Next: User's Turn");
        else
            holdText.setText(" Next: Computer's Turn");

    }

    public void computerTurn()
    {

        Button rollButton = (Button)findViewById(R.id.rollButton);
        final Button holdButton = (Button)findViewById(R.id.holdButton);
        holdButton.setEnabled(false);
        rollButton.setEnabled(false);
        Random random = new Random();
        int numberOfTurns=random.nextInt(6);//at max computer will go for 6 turns before hold
        for(int i=0;i<numberOfTurns;i++) {
            roll();
        }
            hold();
        userTurn=true;
        holdButton.setEnabled(true);
        rollButton.setEnabled(true);
    }




}