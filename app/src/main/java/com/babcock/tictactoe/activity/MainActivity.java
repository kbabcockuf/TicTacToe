package com.babcock.tictactoe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babcock.tictactoe.R;
import com.babcock.tictactoe.algorithm.BoardState;
import com.babcock.tictactoe.controls.TicTacToeBoard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Host a TicTacToeBoard
 *
 * Created by kevinbabcock on 7/17/16.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ticTacToeBoard)
    TicTacToeBoard ticTacToeBoard;

    @BindView(R.id.llStatus)
    LinearLayout statusLayout;

    @BindView(R.id.tvStatus)
    TextView status;

    @BindView(R.id.btnReset)
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ticTacToeBoard.addListener(new TicTacToeBoard.Listener() {
            @Override
            public void onComplete(BoardState.State completionState) {
                switch (completionState) {
                    case X_Wins:
                        status.setText("You Win");
                        break;
                    case O_Wins:
                        status.setText("You Lose");
                        break;
                    case Draw:
                        status.setText("CATS!!!");
                        break;
                }

                statusLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.btnReset)
    public void reset(View v) {
        statusLayout.setVisibility(View.INVISIBLE);

        ticTacToeBoard.reset();
    }
}
