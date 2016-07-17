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

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevinbabcock on 7/17/16.
 */

public class TicTacToeBoard extends android.support.v7.widget.CardView {

    private static final String SUPER_STATE = "super_state";
    private static final String X_MOVES = "x_moves";
    private static final String O_MOVES = "o_moves";

    private int[] xMoves = new int[5];
    private int[] oMoves = new int[4];

    private Paint paint;

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

    public void init() {

        inflate(getContext(), R.layout.grid_layout_board, this);
        ButterKnife.bind(this);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle b = new Bundle();
        b.putParcelable(SUPER_STATE, super.onSaveInstanceState());
        b.putIntArray(X_MOVES, xMoves);
        b.putIntArray(O_MOVES, oMoves);

        return b;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle b = (Bundle)state;
            xMoves = b.getIntArray(X_MOVES);
            oMoves = b.getIntArray(O_MOVES);
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
            if (tile.getTileState() == TicTacToeTile.State.Empty) {
                tile.setTileState(TicTacToeTile.State.O);
            }
        }
    }
}
