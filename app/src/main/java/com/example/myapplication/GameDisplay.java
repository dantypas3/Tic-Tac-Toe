package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GameDisplay extends AppCompatActivity {

    private String player1;
    private String player2;
    private TicTacToeBoard ticTacToeBoard;
    private GameLogic gameLogic;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display); // Ensure you have the correct layout

        // Initialize GameLogic
        gameLogic = GameLogic.getInstance();

        // Get the names of the players from Intent
        Intent intent = getIntent();
        String[] playerNames = intent.getStringArrayExtra("PLAYER_NAMES");

        //Player names for game against human or AI player
        if (playerNames != null && playerNames.length == 2 && !playerNames[1].equals("AI"))  {
            player1 = playerNames[0];
            player2 = playerNames[1];
        } else if (playerNames != null && playerNames[1].equals("AI")) {
            gameLogic.setIsVsAI(true);
            player1 = playerNames[0];
            player2 = playerNames[1];
        }

        // Find and set up the TicTacToeBoard view
        ticTacToeBoard = findViewById(R.id.ticTacToeBoard);

        // Set up header TextView
        header = findViewById(R.id.game_header);
        header.setText(player1 + " vs " + player2);

        // Set up buttons
        Button homeButton = findViewById(R.id.button_home);
        Button resetButton = findViewById(R.id.button_PlayAgain);

        // Home button click listener
        homeButton.setOnClickListener(v -> navigateHome());
        // Reset button click listener
        resetButton.setOnClickListener(v -> resetGame());
    }

    private void navigateHome() {
        Intent homeIntent = new Intent(this, MainActivity.class);
        resetGame();
        startActivity(homeIntent);
    }

    private void resetGame() {
        gameLogic.resetBoard();
        ticTacToeBoard.invalidate(); // Redraw the TicTacToeBoard
        updateHeader(); // Update the header for the next game
        gameLogic.setIsOver(false);
    }

    private void updateHeader() {
        // Update the header with the current player or reset text
        if(!gameLogic.isVsAI()) {
            header.setText(player1 + " vs " + player2);
        } else {
            header.setText(player1 + " vs AI");
        }
    }
}