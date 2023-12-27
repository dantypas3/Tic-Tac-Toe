package com.dion.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TicTacToeBoard extends View {
    private final int boardColor;
    private final int XColor;
    private final int OColor;
    private final int winningLineColor;
    private final Paint paint = new Paint();
    private int cellSize = getWidth() / 3;

    /*private final int AI = 2;
    private final int PLAYER = 1;*/
    private GameLogic gameLogic;
    private MinimaxAI minimaxAI;

    public TicTacToeBoard (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gameLogic = GameLogic.getInstance();
        minimaxAI = new MinimaxAI(gameLogic);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicTacToeBoard,
                                                                 0, 0);

        try {
            boardColor = a.getInteger(R.styleable.TicTacToeBoard_boardColor, 0);
            XColor = a.getInteger(R.styleable.TicTacToeBoard_XColor, 0);
            OColor = a.getColor(R.styleable.TicTacToeBoard_OColor, 0);
            winningLineColor = a.getColor(R.styleable.TicTacToeBoard_winningLineColor, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure (int width, int height) {
        super.onMeasure(width, height);
        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension / 3;
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        drawGameBoard(canvas);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameLogic.getGameBoard()[i][j] == Player.PLAYER_1) {
                    drawX(canvas, i, j);
                } else if (gameLogic.getGameBoard()[i][j] == Player.PLAYER_2 ||
                           gameLogic.getGameBoard()[i][j] == Player.AI) {
                    drawO(canvas, i, j);
                }
            }
        }
        if (gameLogic.checkIsOver() && !gameLogic.checkDraw()) {
            drawWinningLine(canvas);
        }
    }

    private void drawGameBoard (Canvas canvas) {
        paint.setColor(boardColor);
        paint.setStrokeWidth(20);

        for (int i = 1; i < 3; i++) {
            canvas.drawLine(cellSize * i, 0, cellSize * i, canvas.getWidth(), paint);
        }
        for (int j = 1; j < 3; j++) {
            canvas.drawLine(0, cellSize * j, canvas.getWidth(), cellSize * j, paint);
        }
    }

    private void drawX (Canvas canvas, int row, int collumn) {
        paint.setColor(XColor);

        canvas.drawLine((float) ((collumn + 1) * cellSize - cellSize * 0.2),
                        (float) (row * cellSize + cellSize * 0.2),
                        (float) (collumn * cellSize + cellSize * 0.2),
                        (float) ((row + 1) * cellSize - cellSize * 0.2), paint);
        canvas.drawLine((float) (collumn * cellSize + cellSize * 0.2),
                        (float) (row * cellSize + cellSize * 0.2),
                        (float) ((collumn + 1) * cellSize - cellSize * 0.2),
                        (float) ((row + 1) * cellSize - cellSize * 0.2), paint);
    }

    private void drawO (Canvas canvas, int row, int collumn) {
        paint.setColor(OColor);

        canvas.drawOval((float) (collumn * cellSize + cellSize * 0.2),
                        (float) (row * cellSize + cellSize * 0.2),
                        (float) ((collumn * cellSize + cellSize) - cellSize * 0.2),
                        (float) ((row * cellSize + cellSize) - cellSize * 0.2), paint);
    }

    private void drawWinningLine (Canvas canvas) {
        paint.setColor(winningLineColor);
        paint.setStrokeWidth(20);

        switch (gameLogic.getWinningCondition()) {
            case WIN_ROW:
                float startY = gameLogic.getWinningRow() * cellSize + cellSize / 2f;
                canvas.drawLine(0, startY, canvas.getWidth(), startY, paint);
                break;
            case WIN_COLUMN:
                float startX = gameLogic.getWinningColumn() * cellSize + cellSize / 2f;
                canvas.drawLine(startX, 0, startX, canvas.getHeight(), paint);
                break;
            case WIN_DIAGONAL:
                if (gameLogic.getWinningDiagonal() == 0) { // Main diagonal
                    canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
                } else if (gameLogic.getWinningDiagonal() == 1) { // Anti-diagonal
                    canvas.drawLine(canvas.getWidth(), 0, 0, canvas.getHeight(), paint);
                }
                break;
        }
    }

    private void checkWin (int row, int column) {
        if (gameLogic.checkRowWin(row)) {
            gameLogic.setIsOver(true);
        } else if (gameLogic.checkColWin(column)) {
            gameLogic.setIsOver(true);
        } else if (gameLogic.checkDiagWin()) {
            gameLogic.setIsOver(true);
        }
    }

    private void AIMove () {
        if (gameLogic.isVsAI() && gameLogic.getActivePlayer() == Player.AI && !gameLogic.checkIsOver()) {
            Pair<Integer, Integer> bestMove = minimaxAI.findOptimalMove(gameLogic.getGameBoard());
            gameLogic.updateGameBoard(bestMove.first, bestMove.second);
            checkWin(bestMove.first, bestMove.second);
            if (gameLogic.checkIsOver() && gameLogic.checkDraw()) {
                gameLogic.setIsOver(true);
            }
            invalidate(); // Redraw the board after AI's move
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameLogic.checkIsOver()) {
            int row = (int) (event.getY() / cellSize);
            int column = (int) (event.getX() / cellSize);

            if (gameLogic.updateGameBoard(row, column)) {
                invalidate();
                checkWin(row, column);

                if (!gameLogic.checkIsOver() && gameLogic.isVsAI()) {
                    AIMove();
                }
                if (!gameLogic.checkIsOver() && gameLogic.checkDraw()) {
                    gameLogic.setIsOver(true);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}