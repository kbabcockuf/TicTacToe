package com.babcock.tictactoe.algorithm;

/**
 * Created by kevinbabcock on 7/17/16.
 */

public class BoardState {

    private char[][] moves = new char[3][3];
    private State currentState;
    private int totalMoves = 0;

    public BoardState() {
        currentState = State.In_Progress;
    }

    public State getCurrentState() {
        return currentState;
    }

    public char[][] getMoves() {
        return moves;
    }

    public State updateBoardState(int row, int col, char move) {
        moves[row][col] = move;
        totalMoves++;

        char nullChar = '\u0000';
        boolean isWinner;

        // Check for across
        isWinner = true;
        for (int i = 0; i < 3; i++) {
            if (moves[row][i] != move) {
                isWinner = false;
                break;
            }
        }

        if (isWinner) {
            currentState = move == 'X' ? State.X_Wins : State.O_Wins;
        }

        // Check for down
        if (!isWinner) {
            isWinner = true;
            for (int i = 0; i < 3; i++) {
                if (moves[i][col] != move) {
                    isWinner = false;
                    break;
                }
            }

            if (isWinner) {
                currentState = move == 'X' ? State.X_Wins : State.O_Wins;
            }
        }

        // Check for diagonal
        if (!isWinner) {

            if ((row + col) % 2 == 0) {

                // Diagonal Up
                if (row + col == 2) {
                    isWinner = true;
                    for (int i = 0; i < 3; i++) {
                        if (moves[2-i][i] != move) {
                            isWinner = false;
                            break;
                        }
                    }

                    if (isWinner) {
                        currentState = move == 'X' ? State.X_Wins : State.O_Wins;
                    }
                }

                // Diagonal Down
                if (!isWinner) {
                    if (row == col) {
                        isWinner = true;
                        for (int i = 0; i < 3; i++) {
                            if (moves[i][i] != move) {
                                isWinner = false;
                                break;
                            }
                        }

                        if (isWinner) {
                            currentState = move == 'X' ? State.X_Wins : State.O_Wins;
                        }
                    }
                }
            }
        }

        // Check for a full board and no winner
        if (totalMoves == 9 && currentState == State.In_Progress) {
            currentState = State.Cats;
        }

        return currentState;
    }

    public enum State {
        In_Progress,
        X_Wins,
        O_Wins,
        Cats
    }
}
