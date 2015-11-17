package com.huckins.sierra.smhuckinsashmanfinal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sierra on 11/8/2015.
 */
public class MazeView extends View {
    private Context context;

    //member variables to handle dynamic resizing of custom view
    private final int MAZESIZE = 14;
    private final float HEIGHT = (2 * MAZESIZE);
    private final float WIDTH = (2 * MAZESIZE);
    private float incomingWidth;
    private float incomingHeight;
    private float scale;

    //member variables to handle timer
    private Handler clockHandler;
    private Runnable clockTimer;
    private final int TIMER_MSEC = 70;

    //member variables to handle drawing
    private Path path;
    private Paint paint;
    private int level = 1;

    //member variables for in game variables
    private Ashman ashman;
    private int cakesRemaining = 0;
    private Ghost[] ghosts = {new Ghost(1,1, Character.direction.RIGHT),new Ghost(1,12,Character.direction.LEFT),
                                new Ghost(12,1, Character.direction.RIGHT), new Ghost(12,12, Character.direction.LEFT)};
    public boolean active = false;
    public boolean gameLost = false;
    private MazeFactory mazeFactory = new MazeFactory();
    private MazeFactory.cellState[] currentMaze;

    public MazeView(Context context) {
        super(context);

        construct(context);
    }

    public MazeView (Context context, AttributeSet attrs) {
        super(context,attrs);

        construct(context);
    }

    public MazeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        construct(context);
    }

    private void construct(Context context) {
        this.context = context;

        //setup drawing variables if they don't already exist
        if (path == null)
            path = new Path();
        if (paint == null)
            paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        //set current maze based on level
        currentMaze = mazeFactory.getMaze(level);

        //create ashman if he doesn't already exist
        if (ashman == null) {
            ashman = new Ashman(7,6, Character.direction.RIGHT);
        }

        //fix acceleration display problem in emulator
        setLayerType(this.LAYER_TYPE_SOFTWARE, null);

        //create timer
        clockHandler = new Handler() ;
        clockTimer = new Runnable() {
            @Override
            public void run() {
                //set ashman's current position in maze to empty
                int arrayPos = ashman.getCurrentX() + (ashman.getCurrentY() * MAZESIZE);
                currentMaze[arrayPos] = MazeFactory.cellState.EMPTY;

                //move ashman
                ashman.move(isWall(ashman.getCurrentX(), ashman.getCurrentY(), ashman.getFacing()));

                //move ghosts
                for (int i = 0; i < ghosts.length; i++)
                    ghosts[i].move(isWall(ghosts[i].getCurrentX(), ghosts[i].getCurrentY(), ghosts[i].getFacing()));

                invalidate();
                clockHandler.postDelayed(this, TIMER_MSEC) ;
            }
        };
    }

    //gets and sets as needed
    public MazeFactory.cellState[] getCurrentMaze() {
        return currentMaze;
    }

    //this set is needed for interaction with listeners in main activity
    public void setAshmanFacing(Character.direction facing) {
        ashman.facing = facing;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public void setCakesRemaining(int cakesRemaining) {
        this.cakesRemaining = cakesRemaining;
    }

    public int getCakesRemaining(){
        return this.cakesRemaining;
    }

    //timer control functions
    public void startTimer() {
        clockHandler.postDelayed(clockTimer, TIMER_MSEC);
        active = true;
    }

    public void stopTimer() {
        clockHandler.removeCallbacks(clockTimer);
        active = false;
    }

    //check whether a coordinate is a wall
    //used for both ashman and ghosts
    public boolean isWall(int x, int y, Character.direction facing ) {
        //convert coordinates to 1D array position
        int arrayPos = x + (y * MAZESIZE);

        //get 1D array position of the coordinate in front of given coordinate
        if (facing == Character.direction.LEFT) {
            arrayPos = arrayPos - MAZESIZE;
            //check wraparound
            if (y == 0)
                arrayPos = arrayPos + (MAZESIZE * MAZESIZE);
        }
        else if (facing == Character.direction.RIGHT) {
            arrayPos = arrayPos + MAZESIZE;
            //check wraparound
            if (y == 13)
                arrayPos = arrayPos - (MAZESIZE * MAZESIZE);
        }
        else if (facing == Character.direction.UP) {
            arrayPos = arrayPos - 1;
            //check wraparound
            if (x == 0)
                arrayPos = arrayPos + (MAZESIZE - 1);
        }
        else {
            arrayPos = arrayPos + 1;
            //check wraparound
            if (x == 13)
                arrayPos = arrayPos - (MAZESIZE - 1);
        }

        //return status of coordinate
        if (currentMaze[arrayPos] == MazeFactory.cellState.WALL)
            return true;
        else
            return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(scale, scale);
        //reset cakes remaining with every redraw
        //they will be recalculated when cells are drawn
        cakesRemaining = 0;

        //draw maze by iterating through array
        for (int x = 0; x < MAZESIZE * MAZESIZE; x++) {
            canvas.save();
            int xCoord = x % (int)MAZESIZE;
            int yCoord = x / (int)MAZESIZE;
            canvas.translate(yCoord * 2, xCoord * 2);
            drawCell(canvas,x);
            canvas.restore();
        }

        //draw ashman
        ashman.draw(canvas, path, paint);

        //draw ghosts
        for (int x = 0; x < ghosts.length; x++) {
            ghosts[x].draw(canvas, path, paint);
        }

        //check for game end condition
        for (int x = 0; x < ghosts.length; x++) {
            if (ghosts[x].currentX == ashman.currentX && ghosts[x].currentY == ashman.currentY) {
                stopTimer();
                gameLost = true;
                Toast.makeText(context, "Game Over!", Toast.LENGTH_SHORT).show();
                break;
                //TODO facilitate restart
                //TODO (optional) dialog instead of toast?
            }
        }

        //update cakes counter
        TextView cakesText = (TextView)((Activity)context).findViewById(R.id.txtDots);
        cakesText.setText("Dots To Eat: " + cakesRemaining);

        //reset game if player has won
        if (cakesRemaining == 0) {
            stopTimer();
            gameLost = false;
            Toast.makeText(context, "You win!", Toast.LENGTH_SHORT).show();
            if (level == 1)
                restartGame(1);
            else
                restartGame(2);
        }
    }

    //draws a single cell, based on cell status
    private void drawCell(Canvas canvas, int x){
        if (currentMaze[x] == MazeFactory.cellState.WALL) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0,0,2,2,paint);
        }
        else if (currentMaze[x] == MazeFactory.cellState.EMPTY) {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,2,2,paint);
        }
        else {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,2,2,paint);

            paint.setColor(Color.WHITE);
            canvas.drawCircle(1f,1f,0.5f,paint);

            //increment cakes remaining
            //serves as a counter with every redraw
            cakesRemaining++;
        }
    }

    //sets member variables back to initial values
    //to prepare for a new game
    private void restartGame(int newLevel) {
        level = newLevel;

        //member variables for in game variables
        ashman = null;
        cakesRemaining = 0;

        Ghost[] resetGhosts = {new Ghost(1,1, Character.direction.RIGHT),new Ghost(1,12,Character.direction.LEFT),
                new Ghost(12,1, Character.direction.RIGHT), new Ghost(12,12, Character.direction.LEFT)};
        ghosts = resetGhosts;

        active = false;
        gameLost = false;

    }

    //cheat for testing purposes
    //removes all walls and cakes except one cake
    //so tester can quickly cause winning condition
    public void activateCheat() {
        currentMaze = mazeFactory.getMaze(-1);
    }

    //this method handles dynamic scaling of custom view
    //based on available width and height
    //respects aspect ratio of original drawing
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);

        float widthScale = width / WIDTH;
        float heightScale = height / HEIGHT;

        if (widthScale > heightScale)
            scale = heightScale;
        else
            scale = widthScale;

        setMeasuredDimension((int) (scale * WIDTH), (int) (scale * HEIGHT));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        incomingWidth = (float)w;
        incomingHeight = (float)h;
    }

    //TODO methods to handle saving state
   /* @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelableArray(currentMaze);

        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            state = bundle.getParcelable("instanceState");
            onNum = bundle.getInt("onNum");
            onState = possibleStates[onNum];
            invalidate();
        }

        super.onRestoreInstanceState(state);
    }*/
}

