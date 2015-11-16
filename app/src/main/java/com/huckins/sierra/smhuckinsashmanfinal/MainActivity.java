package com.huckins.sierra.smhuckinsashmanfinal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLeft;
    Button btnRight;
    Button btnUp;
    Button btnDown;

    MazeView maze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnRight = (Button)findViewById(R.id.btnRight);
        btnUp = (Button)findViewById(R.id.btnUp);
        btnDown = (Button)findViewById(R.id.btnDown);
        maze = (MazeView)findViewById(R.id.cviewMaze);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);

        maze.setLevel(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //setup about toast
        if (id == R.id.action_about) {
            Toast.makeText(this,"Sierra Huckins CSCD 327 Fall 2015\n Final Project - Ashman Game",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Button pressed = (Button)v;

        if (pressed.equals(findViewById(R.id.btnLeft))) {
            maze.setAshmanFacing(Character.direction.LEFT);
        }
        else if (pressed.equals(findViewById(R.id.btnRight))) {
            maze.setAshmanFacing(Character.direction.RIGHT);
        }
        else if (pressed.equals(findViewById(R.id.btnUp))) {
            maze.setAshmanFacing(Character.direction.UP);
        }
        else {
            maze.setAshmanFacing(Character.direction.DOWN);
        }

    }

    public void startStopGame(View v){
        if (!maze.active) {
            maze.startTimer();
        }
        else {
            maze.stopTimer();
            Toast.makeText(this,"Game Paused",Toast.LENGTH_SHORT).show();
        }
    }
}
