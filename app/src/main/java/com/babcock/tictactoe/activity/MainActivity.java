package com.babcock.tictactoe.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.percent.PercentLayoutHelper;
import android.support.v7.app.AppCompatActivity;

import com.babcock.tictactoe.R;
import com.babcock.tictactoe.controls.TicTacToeBoard;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ticTacToeBoard)
    TicTacToeBoard ticTacToeBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        PercentLayoutHelper.PercentLayoutParams layoutParams = (PercentLayoutHelper.PercentLayoutParams) ticTacToeBoard.getLayoutParams();
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                layoutParams.getPercentLayoutInfo().heightPercent = 1.0f;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                layoutParams.getPercentLayoutInfo().widthPercent = 1.0f;
                break;
        }
    }
}
