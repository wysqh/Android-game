package com.example.gtk.thebattleofplane.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.gtk.thebattleofplane.view.GameView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameView(this));
    }
}
