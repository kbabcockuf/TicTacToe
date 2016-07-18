package com.babcock.tictactoe.algorithm;

/**
 * Represents the state of a TicTacToeBoard.
 *
 * May be used for both maintaining state and testing moves for state.
 *
 * Created by kevinbabcock on 7/17/16.
 */

public class BoardState {

    /**
     * TicTacToeBoard moves.
     */
    private char[][] moves;

    /**
     * Current state of the board.
     */
    private State currentState;

    public BoardState() {
        currentState = State.In_Progress;
        moves = new char[3][3];
    }

    BoardState(char[][] moves) {
        currentState = State.In_Progress;
        this.moves = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.moves[i][j] = moves[i][j];
            }
        }
    }

    /**
     * Return the current state of the board.
     * @return
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Return the moves this board state represents.
     * @return
     */
    public char[][] getMoves() {
        return moves;
    }

    /**
     * Test the resulting state of a given move.
     * @param row
     * @param col
     * @param move
     * @return the resulting state this move would have resulted in.
     */
    public State testBoardState(int row, int col, char move) {
        moves[row][col] = move;
        State state = checkBoardState(row, col, move);
        moves[row][col] = '\u0000';
        return state;
    }

    /**
     * Update the state of this board.
     * @param row
     * @param col
     * @param move
     * @return the resulting state.
     */
    public State updateBoardState(int row, int col, char move) {
        moves[row][col] = move;

        currentState = checkBoardState(row, col, move);

        return currentState;
    }

    private State checkBoardState(int row, int col, char move) {
        State newState = currentState;
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
            newState = move == 'X' ? State.X_Wins : State.O_Wins;
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
                newState = move == 'X' ? State.X_Wins : State.O_Wins;
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
                        newState = move == 'X' ? State.X_Wins : State.O_Wins;
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
                            newState = move == 'X' ? State.X_Wins : State.O_Wins;
                        }
                    }
                }
            }
        }

        // Check for a full board (Draw)
        if (newState == State.In_Progress) {
            boolean isFull = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (moves[i][j] == '\u0000') {
                        isFull = false;
                        break;
                    }
                }

                if (!isFull) {
                    break;
                }
            }

            if (isFull) {
                newState = State.Draw;
            }
        }

        return newState;
    }

    public enum State {
        In_Progress,
        X_Wins,
        O_Wins,
        Draw
    }
}
