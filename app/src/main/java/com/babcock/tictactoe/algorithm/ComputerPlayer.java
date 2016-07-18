package com.babcock.tictactoe.algorithm;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Computer Player for a TicTacToeBoard that picks optimal choices (always Wins or Draws).
 *
 * Created by kevinbabcock on 7/17/16.
 */

public class ComputerPlayer {

    private static final int WIN_VALUE = 20;
    private static final int DRAW_VALUE = 0;
    private static final int LOSE_VALUE = -1000;

    public ComputerPlayer() { }

    /**
     * Prompt the computer player for its next move.
     * @param moves
     * @return the computer player's next move.
     */
    public Move nextMove(char[][] moves) {

        // Look at all possible moves
        PriorityQueue<Move> possibleMoves = new PriorityQueue<>(9, Collections.reverseOrder());
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (moves[row][col] == '\u0000') {
                    possibleMoves.add(findMove(moves, row, col, true));
                }
            }
        }

        // Get the top priority possible move as our potential next move
        Move nextMove = possibleMoves.peek();

        // Return the move if it's a Win, Draw or Block (end game / required move)
        if (nextMove.priority == Move.Priority.Win || nextMove.priority == Move.Priority.Draw || nextMove.priority == Move.Priority.Block) {
            return nextMove;
        }
        // Else all moves are indeterminate, so select the one with the highest value
        else {

            Move maxMove = possibleMoves.peek();
            for (Move move : possibleMoves) {
                move.value += nextMove(moves, move, true).value;
                if (move.value > maxMove.value) {
                    maxMove = move;
                }
            }

            return maxMove;
        }
    }

    private Move nextMove(char[][] moves, Move lastMove, boolean isCpu) {
        BoardState boardState = new BoardState(moves);
        boardState.updateBoardState(lastMove.row, lastMove.col, isCpu ? 'O' : 'X');

        // Look at all possible moves
        PriorityQueue<Move> possibleMoves = new PriorityQueue<>(9, Collections.reverseOrder());
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (boardState.getMoves()[row][col] == '\u0000') {
                    possibleMoves.add(findMove(boardState.getMoves(), row, col, !isCpu));
                }
            }
        }

        // Get the top priority possible move as our potential next move
        Move nextMove = possibleMoves.peek();

        // Return the move if it's a Win or Draw (end game)
        if (nextMove.priority == Move.Priority.Win || nextMove.priority == Move.Priority.Draw) {
            lastMove.value += nextMove.value;
        }
        // If move is a block, choose the move and recurse.
        else if (nextMove.priority == Move.Priority.Block) {
            lastMove.value += nextMove(boardState.getMoves(), nextMove, !isCpu).value;
        }
        // Else move is indeterminate, so iterate through all possible moves and add up their values.
        else {
            for (Move move : possibleMoves) {
                lastMove.value += nextMove(boardState.getMoves(), move, !isCpu).value;
            }
        }

        // Return the max priority move
        return lastMove;
    }

    private Move findMove(char[][] moves, int row, int col, boolean isCpu) {

        BoardState boardState = new BoardState(moves);

        if (isCpu) {
            // Win
            if (boardState.testBoardState(row, col, 'O') == BoardState.State.O_Wins) {
                return new Move(row, col, Move.Priority.Win, WIN_VALUE);
            }

            // Draw
            if (boardState.testBoardState(row, col, '0') == BoardState.State.Draw) {
                return new Move(row, col, Move.Priority.Draw, DRAW_VALUE);
            }

            // Block
            if (boardState.testBoardState(row, col, 'X') == BoardState.State.X_Wins) {
                return new Move(row, col, Move.Priority.Block);
            }
        }
        else {
            // Win (i.e. cpu loses)
            if (boardState.testBoardState(row, col, 'X') == BoardState.State.X_Wins) {
                return new Move(row, col, Move.Priority.Win, LOSE_VALUE);
            }

            // Draw
            if (boardState.testBoardState(row, col, 'X') == BoardState.State.Draw) {
                return new Move(row, col, Move.Priority.Draw, DRAW_VALUE);
            }

            // Block
            if (boardState.testBoardState(row, col, 'O') == BoardState.State.O_Wins) {
                return new Move(row, col, Move.Priority.Block);
            }
        }

        return new Move(row, col, Move.Priority.Indeterminate);
    }

    /**
     * Represents a Move that can be made on a TicTacToeBoard.
     */
    public static class Move implements Comparable<Move> {

        /**
         * The priority of a given move.  High priority moves must be executed.
         */
        enum Priority {
            Win(3), Draw(2), Block(1), Indeterminate(0);

            private final int value;

            Priority(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        Priority priority;
        int value;
        public final int row;
        public final int col;

        Move(int row, int col, Priority priority) {
            this.row = row;
            this.col = col;
            this.priority = priority;
            this.value = 0;
        }

        Move(int row, int col, Priority priority, int value) {
            this.row = row;
            this.col = col;
            this.priority = priority;
            this.value = value;
        }

        @Override
        public int compareTo(@NonNull Move another) {
            if (this.priority.getValue() < another.priority.getValue())
                return -1;
            else if (this.priority.getValue() > another.priority.getValue())
                return 1;
            else
                return 0;
        }
    }
}
