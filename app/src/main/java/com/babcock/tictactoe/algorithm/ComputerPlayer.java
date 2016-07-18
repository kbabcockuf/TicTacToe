package com.babcock.tictactoe.algorithm;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Created by kevinbabcock on 7/17/16.
 */

public class ComputerPlayer {

    public ComputerPlayer() {

    }

    public Move nextMove(char[][] moves) {

        // Look at all possible moves
        PriorityQueue<Move> possibleMoves = new PriorityQueue<>(10, Collections.reverseOrder());
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (moves[row][col] == '\u0000') {
                    possibleMoves.add(findMove(moves, row, col));
                }
            }
        }

        // Return the max priority move
        return possibleMoves.peek();
    }

    private Move findMove(char[][] moves, int row, int col) {

        BoardState boardState = new BoardState(moves);

        // Take
        if (boardState.testBoardState(row, col, 'O') == BoardState.State.O_Wins) {
            return new Move(row, col, Move.Priority.Take);
        }

        // Block
        if (boardState.testBoardState(row, col, 'X') == BoardState.State.X_Wins) {
            return new Move(row, col, Move.Priority.Block);
        }

        // TakeFork

        // BlockFork

        // Center
        if (row == 1 && col == 1) {
            return new Move(row, col, Move.Priority.Center);
        }

        // Corner
        if ((row == 0 || row == 2) && (col == 0 || col == 2)) {
            if (moves[Math.abs(row - 2)][Math.abs(col - 2)] == 'X') {
                return new Move(row, col, Move.Priority.OppositeCorner);
            }
            else {
                return new Move(row, col, Move.Priority.EmptyCorner);
            }
        }
        
        // Side
        return new Move(row, col, Move.Priority.EmptySide);
    }

    public static class Move implements Comparable<Move> {

        public enum Priority {
            Take(7), Block(6), TakeFork(5), BlockFork(4), Center(3), OppositeCorner(2), EmptyCorner(1), EmptySide(0);

            private final int value;

            Priority(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        public final Priority priority;
        public final int row;
        public final int col;
        public Move(int row, int col, Priority priority) {
            this.row = row;
            this.col = col;
            this.priority = priority;
        }

        @Override
        public int compareTo(Move another) {
            if (this.priority.getValue() < another.priority.getValue())
                return -1;
            else if (this.priority.getValue() > another.priority.getValue())
                return 1;
            else
                return 0;
        }
    }
}
