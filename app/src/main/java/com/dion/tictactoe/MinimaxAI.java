package com.dion.tictactoe;

import android.util.Pair;

public class MinimaxAI {
    private GameLogic gameLogic;

    public MinimaxAI (GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    private boolean checkMovesLeft (Player[][] board) {
        for (Player[] row : board) {
            for (Player cell : row) {
                if (cell == Player.NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    private int evaluateMove (Player[][] board) {
        for (int i = 0; i < 3; i++) {
            if (gameLogic.checkRowWin(i)) {
                if (board[i][0] == Player.AI) {
                    return +10;
                } else if (board[i][0] == Player.PLAYER_1) {
                    return -10;
                }
            }

            if (gameLogic.checkColWin(i)) {
                if (board[0][i] == Player.AI) {
                    return +10;
                } else if (board[0][i] == Player.PLAYER_1) {
                    return -10;
                }
            }
        }
        if (gameLogic.checkDiagWin()) {
            if (board[1][1] == Player.AI) {
                return +10;
            } else if (board[1][1] == Player.PLAYER_1) {
                return -10;
            }
        }
        return 0;
    }

    int minimax (Player[][] board, int depth, boolean isMax) {
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
                    if (board[i][j] == Player.NONE) {
                        board[i][j] = Player.AI;
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, false));
                        board[i][j] = Player.NONE;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == Player.NONE) {
                        board[i][j] = Player.PLAYER_1;
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, true));
                        board[i][j] = Player.NONE;
                    }
                }
            }
            return bestScore;
        }
    }

    Pair<Integer, Integer> findOptimalMove (Player[][] board) {
        int bestScore = Integer.MIN_VALUE;
        Pair<Integer, Integer> optimalMove = new Pair<>(-1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Player.NONE) {
                    board[i][j] = Player.AI;

                    int moveScore = minimax(board, 0, false);

                    board[i][j] = Player.NONE;

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