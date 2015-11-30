package com.huckins.sierra.smhuckinsashmanfinal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
    static final int WALL = 0;
    static final int CAKE = 1;
    static final int EMPTY = 2;

    //member variables to handle dynamic resizing of custom view
    static final int MAZESIZE = 14;
    private final float HEIGHT = (2 * MAZESIZE);
    private final float WIDTH = (2 * MAZESIZE);
    private float incomingWidth;
    private float incomingHeight;
    private float scale;

    //member variables to handle timer
    private Handler clockHandler;
    private Runnable clockTimer;
    private Runnable delayRestart;
    private final int TIMER_MSEC = 70;

    //member variables to handle drawing
    private Path path;
    private Paint paint;
    private int level = 1;

    //member variables for in game variables
    private Ashman ashman;
    private int cakesRemaining = 0;
    private GhostFactory ghostFactory = new GhostFactory();
    private Ghost[] ghosts;
    private boolean ghostsMove = false;
    public boolean active = false;
    public boolean gameLost = false;
    private MazeFactory mazeFactory = new MazeFactory();
    private int[] currentMaze;

    //member variables to handle audio
    private MediaPlayer mp = null;
    private int currentPlaying;
    private boolean newGame = true;

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

        //play new board sound
        //if this is first play after launch
        if(newGame == true) {
            playSound(R.raw.begin);
            newGame = false;
        }

        //setup drawing variables if they don't already exist
        if (path == null)
            path = new Path();
        if (paint == null)
            paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        //set current maze based on level
        currentMaze = mazeFactory.getMaze(level);

        //create ghosts based on level
        ghosts = ghostFactory.getGhosts(level);

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
                currentMaze[arrayPos] = EMPTY;

                //move ashman
                ashman.move(isWall(ashman.getCurrentX(), ashman.getCurrentY(), ashman.getFacing()));

                //move ghosts
                //condition moves ghosts slower if level is 1
                if (level == 1 && ghostsMove) {
                    for (int i = 0; i < ghosts.length; i++)
                        ghosts[i].move(isWall(ghosts[i].getCurrentX(), ghosts[i].getCurrentY(), ghosts[i].getFacing()));
                    ghostsMove = false;
                }
                else if (level == 2) {
                    for (int i = 0; i < ghosts.length; i++)
                        ghosts[i].move(isWall(ghosts[i].getCurrentX(), ghosts[i].getCurrentY(), ghosts[i].getFacing()));
                }
                else {
                    ghostsMove = true;
                }

                invalidate();
                clockHandler.postDelayed(this, TIMER_MSEC) ;
            }
        };

        delayRestart = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    public void playSound(int sound) {
        if (mp == null || currentPlaying != sound) {
            mp = null;
            mp = MediaPlayer.create(context, sound);
            currentPlaying = sound;

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(
                        MediaPlayer mPlayer) {
                    mp.release();
                    mp = null;
                }
            });
        }
        else if (currentPlaying == sound) {
            mp.pause();
            mp.seekTo(0);
        }

        mp.start();

    }

    //this set is needed for interaction with listeners in main activity
    public void setAshmanFacing(Character.direction facing) {
        ashman.facing = facing;
    }

    public void setLevel(int level) {
        this.level = level;
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
        if (currentMaze[arrayPos] == WALL)
            return true;
        else
            return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(scale, scale);
        //reset cakes remaining with every redraw
        //they will be recalculated when cells are drawn
        int temp = cakesRemaining;
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

        //fill in level data
        TextView levelText = (TextView)((Activity)context).findViewById(R.id.txtLevel);
        levelText.setText("Level: " + level);

        //update cakes counter
        TextView cakesText = (TextView)((Activity)context).findViewById(R.id.txtDots);
        cakesText.setText("Dots To Eat: " + cakesRemaining);

        //play cake sound if one was eaten
        if (cakesRemaining < temp)
            playSound(R.raw.eat);

        //check for game ended by ghost collision
        for (int x = 0; x < ghosts.length; x++) {
            if (ghosts[x].currentX == ashman.currentX && ghosts[x].currentY == ashman.currentY) {
                stopTimer();
                gameLost = true;
                Toast.makeText(context, "You got eaten! Level restarted!", Toast.LENGTH_SHORT).show();
                resetGame(level);
                playSound(R.raw.death);
                break;
                //TODO (optional) dialog instead of toast?
            }
        }

        //check for game ended by all cakes eaten
        if (cakesRemaining == 0 && active == true) {
            stopTimer();
            gameLost = false;
            if (level == 1) {
                Toast.makeText(context, "You beat level " + level + "!\n Let's try level 2!", Toast.LENGTH_SHORT).show();
                resetGame(2);
                playSound(R.raw.win);
            }
            else {
                Toast.makeText(context, "You beat level " + level + "!\nCongrats, you won!", Toast.LENGTH_SHORT).show();
                resetGame(1);
                playSound(R.raw.win);
            }
        }
    }

    //draws a single cell, based on cell status
    private void drawCell(Canvas canvas, int x){
        if (currentMaze[x] == WALL) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0,0,2,2,paint);
        }
        else if (currentMaze[x] == EMPTY) {
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
    public void resetGame(int newLevel) {
        level = newLevel;

        //member variables for in game variables
        ashman = null;
        cakesRemaining = 0;
        active = false;
        gameLost = false;

        construct(context);
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


}

