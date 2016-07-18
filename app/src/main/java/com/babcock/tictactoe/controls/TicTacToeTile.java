package com.babcock.tictactoe.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Represents a single tile on a TicTacToeBoard.
 *
 * Created by kevinbabcock on 7/17/16.
 */

public class TicTacToeTile extends View {

    enum State {
        Empty,
        X,
        O
    }

    private State tileState;
    private Paint paint;

    public TicTacToeTile(Context context) {
        super(context);
        init();
    }

    public TicTacToeTile(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TicTacToeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Get the current state of the tile.
     * @return
     */
    State getTileState() {
        return tileState;
    }

    /**
     * Set the state of the tile.
     * @param tileState
     */
    void setTileState(State tileState) {
        this.tileState = tileState;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        switch (tileState) {
            case Empty:
                canvas.drawColor(Color.TRANSPARENT);
                break;
            case X:
                canvas.drawLine(0, 0, width, height, paint);
                canvas.drawLine(0, height, width, 0, paint);
                break;
            case O:
                canvas.drawCircle(width/2, height/2, width < height ? width/2-10 : height/2-10, paint);
                break;
        }
    }

    private void init() {
        ButterKnife.bind(this);

        tileState = State.Empty;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }
}
