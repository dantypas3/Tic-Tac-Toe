package com.example.myapplication;

import android.util.Pair;

public class MinimaxAI {
    private GameLogic gameLogic;
    private final int AI = 2;
    private final int PLAYER = 1;
    private final int EMPTY = 0;

    public MinimaxAI (GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    private boolean checkMovesLeft (int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    private int evaluateMove (int[][] board) {
        for (int i = 0; i < 3; i++) {
            if (gameLogic.checkRowWin(i, 0)) {
                if (board[i][0] == 2) {
                    return +10;
                } else if (board[i][0] == 1) {
                    return -10;
                }
            }

            if (gameLogic.checkColWin(0, i)) {
                if (board[0][i] == 2) {
                    return +10;
                } else if (board[0][i] == 1) {
                    return -10;
                }
            }
        }
        if (gameLogic.checkDiagWin()) {
            if (board[1][1] == 2) {
                return +10;
            } else if (board[1][1] == 1) {
                return -10;
            }
        }
        return 0;
    }

    int minimax (int[][] board, int depth, boolean isMax) {
        int score = evaluateMove(board);

        if (score == 10 || score == -10) {
            return score;
        }
        if (!checkMovesLeft(board)) {
            return 0;
        }

        if (isMax) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI;
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, false));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER;
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, true));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return bestScore;
        }
    }

    Pair<Integer, Integer> findOptimalMove (int[][] board) {
        int bestScore = Integer.MIN_VALUE;
        Pair<Integer, Integer> optimalMove = new Pair<>(-1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = AI;

                    int moveScore = minimax(board, 0, false);

                    board[i][j] = EMPTY;

                    if (moveScore > bestScore) {
                        optimalMove = new Pair<>(i, j);
                        bestScore = moveScore;
                    }
                }
            }
        }
        return optimalMove;
    }
}