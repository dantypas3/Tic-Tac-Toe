package com.example.myapplication;

public class GameLogic {
    private static GameLogic instance;
    //Array for game Board
    private int[][] gameBoard;
    //Default player is 1
    private int activePlayer = 1;
    private boolean isOver = false;
    public static final int winRow = 1;
    public static final int winCollumn = 2;
    public static final int winDiag = 3;
    public static final int draw = 4;
    private int winningColumn;
    private int winningRow;
    private int winningDiagonal;
    private int winningCondition;
    private boolean isVsAI;

    //Initialize empty game board
    GameLogic () {
        gameBoard = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoard[i][j] = 0;
            }
        }
    }

    public static synchronized GameLogic getInstance () {
        if (instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    public boolean updateGameBoard (int row, int column) {
        if (row >= 0 && row < 3 && column >= 0 && column < 3) {
            if (gameBoard[row][column] == 0) {
                gameBoard[row][column] = activePlayer;
                togglePlayer();
                return true;
            }
        }
        return false;
    }

    public void resetBoard () {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoard[i][j] = 0;
            }
        }
        activePlayer = 1;
    }

    private void togglePlayer () {
        activePlayer = (activePlayer == 1) ? 2 : 1;
    }

    public int[][] getGameBoard () {
        return gameBoard;
    }

    public int getActivePlayer () {
        return activePlayer;
    }

    public boolean checkColWin (int row, int column) {
        int firstVal = gameBoard[0][column];
        if (firstVal == 0) {return false;}

        for (int i = 1; i < 3; i++) {
            if (gameBoard[i][column] != firstVal) {
                return false;
            }
        }
        winningColumn = column;
        winningCondition = winCollumn;
        return true;
    }

    public boolean checkRowWin (int row, int column) {
        int firstVal = gameBoard[row][0];
        if (firstVal == 0) {return false;}

        for (int i = 1; i < 3; i++) {
            if (gameBoard[row][i] != firstVal) {
                return false;
            }
        }
        winningRow = row;
        winningCondition = winRow;
        return true;
    }

    public boolean checkDiagWin () {
        // Check main diagonal
        if (gameBoard[0][0] != 0 && gameBoard[0][0] == gameBoard[1][1] && gameBoard[0][0] == gameBoard[2][2]) {
            winningDiagonal = 0;
            winningCondition = winDiag;
            return true;
        }
        // Check anti-diagonal
        if (gameBoard[0][2] != 0 && gameBoard[0][2] == gameBoard[1][1] && gameBoard[0][2] == gameBoard[2][0]) {
            winningDiagonal = 1;
            winningCondition = winDiag;
            return true;
        }
        return false;
    }

    public boolean checkDraw () {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameBoard[i][j] == 0) {
                    return false;
                }
            }
        }
        winningCondition = draw;
        return !isOver;
    }

    public boolean checkIsOver () {
        return isOver;
    }

    public void setIsOver (boolean gameOver) {
        isOver = gameOver;
    }

    public void setIsVsAI (boolean isVsAI) {this.isVsAI = isVsAI;}

    public boolean isVsAI () {return isVsAI;}

    public int getWinningCondition () {return winningCondition;}

    public int getWinningRow () {return winningRow;}

    public int getWinningColumn () {return winningColumn;}

    public int getWinningDiagonal () {return winningDiagonal;}
}