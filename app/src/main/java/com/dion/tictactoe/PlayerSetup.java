package com.dion.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class PlayerSetup extends AppCompatActivity {

    private EditText player1;
    private EditText player2;

    //Initialize the game with two human players or one human and AI
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_setup);

        //Get the names of the players
        player1 = findViewById(R.id.enter_name1);
        player2 = findViewById(R.id.enter_name2);
        //Switch for AI vs Human
        Switch playVsAI = findViewById(R.id.ai_switch);
        //Set up the submit button
        Button submit = findViewById(R.id.submit_names);

        //Set up the submit button click listener
        submit.setOnClickListener(v -> {
            //Get Player names
            String namePlayer1 = player1.getText().toString().trim();
            String namePlayer2;
            if (playVsAI.isChecked()) {
                namePlayer2 = "AI";
            } else {
                namePlayer2 = player2.getText().toString().trim();
            }

            // Create a Toast for possible error messages
            Toast toast = Toast.makeText(PlayerSetup.this, "", Toast.LENGTH_SHORT);

            //For a game vs Human player
            if (!playVsAI.isChecked()) {
                if (!namePlayer1.isEmpty() && !namePlayer2.isEmpty()) {
                    Intent intent = new Intent(PlayerSetup.this, GameDisplay.class);
                    intent.putExtra("PLAYER_NAMES", new String[] {namePlayer1, namePlayer2});
                    startActivity(intent);
                }
                //Handle all cases where player names are missing
                else {
                    if (namePlayer1.isEmpty() && namePlayer2.isEmpty()) {
                        toast.setText("Both player names are missing");
                    } else if (namePlayer1.isEmpty()) {
                        toast.setText("Name of player 1 is missing");
                    } else if (namePlayer2.isEmpty()) {
                        toast.setText("Name of Player 2 is missing");
                    }
                    toast.show();
                }
            }
            // For a game vs AI player
            else {
                if (!namePlayer1.isEmpty()) {
                    Intent intent = new Intent(PlayerSetup.this, GameDisplay.class);
                    intent.putExtra("PLAYER_NAMES", new String[] {namePlayer1, "AI"});
                    startActivity(intent);
                } else {
                    toast.setText(
                            "Name of player 1 is missing");   //In case of missing name of player 1
                }
            }
        });
    }
}