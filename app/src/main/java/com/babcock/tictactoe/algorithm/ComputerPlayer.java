package com.babcock.tictactoe.algorithm;

/**
 * Created by kevinbabcock on 7/17/16.
 */

public class ComputerPlayer {

    public ComputerPlayer() {

    }

    public int nextMove(char[][] moves) {
        int i = 0;
        while(moves[i/3][i%3] != '\u0000' && i < 9) {
            i++;
        }
        return i;
    }
}
