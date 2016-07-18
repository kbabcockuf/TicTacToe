package com.babcock.tictactoe.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.babcock.tictactoe.R;
import com.babcock.tictactoe.algorithm.BoardState;
import com.babcock.tictactoe.algorithm.ComputerPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.babcock.tictactoe.algorithm.BoardState.State.In_Progress;
import static com.babcock.tictactoe.controls.TicTacToeTile.State.O;

/**
 * Represents a square TicTacToeBoard for playing TicTacToe.
 *
 * Handles both portrait and landscape orientations, saving and restoring state during orientation
 * change.
 *
 * Created by kevinbabcock on 7/17/16.
 */

public class TicTacToeBoard extends android.support.v7.widget.CardView {

    private static final String SUPER_STATE = "super_state";
    private static final String MOVES_1 = "moves_1";
    private static final String MOVES_2 = "moves_2";
    private static final String MOVES_3 = "moves_3";

    /**
     * Provides callback when a game is completed.
     */
    public interface Listener {
        void onComplete(BoardState.State completionState);
    }

    /**
     * Paint used to draw the hash
     */
    private Paint paint;

    /**
     * ComputerPlayer the human will play against.
     */
    private ComputerPlayer computerPlayer;

    /**
     * Maintains the state (InProgress, Win, Lose, Draw) of our TicTacToeBoard.
     */
    private BoardState boardState;

    /**
     * List of listeners.
     */
    private List<Listener> listeners;

    /**
     * List of all 9 board tiles.
     */
    @BindViews({ R.id.vTile1, R.id.vTile2, R.id.vTile3, R.id.vTile4,
            R.id.vTile5, R.id.vTile6, R.id.vTile7, R.id.vTile8, R.id.vTile9 })
    List<TicTacToeTile> tiles;

    public TicTacToeBoard(Context context) {
        super(context);
        init();
    }

    public TicTacToeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TicTacToeBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle b = new Bundle();
        b.putParcelable(SUPER_STATE, super.onSaveInstanceState());

        // Save our current board state into a bundle for later retrieval.
        char[][] moves = boardState.getMoves();
        b.putCharArray(MOVES_1, moves[0]);
        b.putCharArray(MOVES_2, moves[1]);
        b.putCharArray(MOVES_3, moves[2]);

        return b;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle b = (Bundle)state;

            // Retrieve our previously saved state.
            char[][] moves = new char[3][3];
            moves[0] = b.getCharArray(MOVES_1);
            moves[1] = b.getCharArray(MOVES_2);
            moves[2] = b.getCharArray(MOVES_3);

            // Replay moves onto board
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    char move = moves[i][j];

                    // Don't play "empty" moves.
                    if (move != '\u0000') {
                        boardState.updateBoardState(i, j, moves[i][j]);
                    }
                }
            }

            // Set all the tiles with the proper state.
            for (int i = 0; i < tiles.size(); i++) {
                if (moves[i/3][i%3] == 'X') {
                    tiles.get(i).setTileState(TicTacToeTile.State.X);
                }
                else if (moves[i/3][i%3] == 'O') {
                    tiles.get(i).setTileState(O);
                }
            }

            // If the board state represents a completed game, inform listeners.
            if (boardState.getCurrentState() != In_Progress) {
                notifyListeners();
            }

            state = b.getParcelable(SUPER_STATE);
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        float width = getMeasuredWidth();
        float verticalHashSpacing = (width-10) / 3;

        float height = getMeasuredHeight();
        float horizontalHashSpacing = (height-10) / 3;

        // Draw the hash (#) on the board.
        canvas.drawLine(verticalHashSpacing, 0, verticalHashSpacing, height, paint);
        canvas.drawLine(verticalHashSpacing*2 + 10, 0, verticalHashSpacing*2 + 10, height, paint);
        canvas.drawLine(0, horizontalHashSpacing, width, horizontalHashSpacing, paint);
        canvas.drawLine(0, horizontalHashSpacing*2 + 10, width, horizontalHashSpacing*2 + 10, paint);
    }

    @OnClick({ R.id.vTile1, R.id.vTile2, R.id.vTile3, R.id.vTile4,
            R.id.vTile5, R.id.vTile6, R.id.vTile7, R.id.vTile8, R.id.vTile9 })
    public void tileClick(View v) {
        if (v instanceof TicTacToeTile) {
            TicTacToeTile tile = (TicTacToeTile) v;

            // Update the board and tile state and engage the computer player for their next move.
            if (tile.getTileState() == TicTacToeTile.State.Empty) {
                int index = tiles.indexOf(tile);
                if (index >= 0) {

                    // Update the board and tile with this move.
                    boardState.updateBoardState(index/3, index%3, 'X');
                    tile.setTileState(TicTacToeTile.State.X);

                    // If the board is still open, prompt the computer player for a move.
                    if (boardState.getCurrentState() == In_Progress) {

                        // ToDo: Some dependencies here need to be cleaned up...
                        ComputerPlayer.Move nextMove = computerPlayer.nextMove(boardState.getMoves());
                        boardState.updateBoardState(nextMove.row, nextMove.col, 'O');
                        tiles.get(nextMove.row*3 + nextMove.col).setTileState(O);
                    }

                    // If the board is no longer open, notify listeners of completion.
                    if (boardState.getCurrentState() != In_Progress) {
                        notifyListeners();
                    }
                }
            }
        }
    }

    /**
     * Reset the board for a new game.
     */
    public void reset() {
        computerPlayer = new ComputerPlayer();
        boardState = new BoardState();

        for (TicTacToeTile tile : tiles) {
            tile.setTileState(TicTacToeTile.State.Empty);
        }
    }

    /**
     * Add a listener for game completion.
     * @param listener
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Remove this listener from game completion.
     * @param listener
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void init() {

        inflate(getContext(), R.layout.grid_layout_board, this);
        ButterKnife.bind(this);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        computerPlayer = new ComputerPlayer();
        boardState = new BoardState();

        listeners = new ArrayList<>();
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.onComplete(boardState.getCurrentState());
        }
    }
}
