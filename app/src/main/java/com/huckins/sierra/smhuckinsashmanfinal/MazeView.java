package com.huckins.sierra.smhuckinsashmanfinal;

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
import android.widget.Toast;

/**
 *MazeView.java
 *Author: Sierra Huckins
 *Last Updated: 20151130
 *Description: Custom view to draw a proportionally scaled maze game board
 *Also holds majority of in game logic due to use of Runnable for game timing.
 **/
public class MazeView extends View {
    private Context context;
    private IGameInteractions updateInterface;

    //maze states
    static final int WALL = 0;
    static final int CAKE = 1;
    static final int EMPTY = 2;
    static final int POWERUP = 3;

    //member variables to handle dynamic resizing of custom view
    static final int MAZESIZE = 14;
    private final float HEIGHT = (2 * MAZESIZE);
    private final float WIDTH = (2 * MAZESIZE);
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
    private final GhostFactory ghostFactory = new GhostFactory();
    private Ghost[] ghosts;
    private boolean ghostsMove = false;
    public boolean active = false;
    public boolean gameLost = false;
    private final MazeFactory mazeFactory = new MazeFactory();
    private int[] currentMaze;

    //member variables to handle audio
    private MediaPlayer mp = null;
    private boolean newGame = true;

    //three required constructors
    //all call same construct method for simplicity
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

        if (!isInEditMode()) {
            this.updateInterface = (IGameInteractions)context;
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
            ashman = new Ashman();
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
                //checks if ashman ate a powerup
                if (currentMaze[arrayPos] == POWERUP)
                    ashman.setPowerUpActive();
                currentMaze[arrayPos] = EMPTY;

                //move ashman
                ashman.move(isWall(ashman.getCurrentX(), ashman.getCurrentY(), ashman.getFacing()));

                //move ghosts
                //condition moves ghosts slower if level is 1
                moveGhosts();

                invalidate();
                clockHandler.postDelayed(this, TIMER_MSEC) ;
            }

            private void moveGhosts() {
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
            }
        };
    }

    //plays the sound effects
    //only allows for one media player to exist at a time
    private void playSound(int sound) {
        //creates new media player for incoming sound if nothing is currently playing
        if (mp == null) {
            mp = MediaPlayer.create(context, sound);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                //nulls out media player when audio is complete
                @Override
                public void onCompletion(
                        MediaPlayer mPlayer) {
                    mp.release();
                    mp = null;
                }
            });
        }
        //if media player is not null and sound requested is anything other than eat sound
        //stop and release media player so current clip doesn't have to finish
        //create new media player for new sound
        else if (sound != R.raw.eat) {
            mp.stop();
            mp.release();
            mp = null;

            mp = MediaPlayer.create(context, sound);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(
                        MediaPlayer mPlayer) {
                    mp.release();
                    mp = null;
                }
            });
        }
        //if requested sound is eat and eat is currently playing
        //pause and seek to beginning of sound
        else if (sound == R.raw.eat) {
            mp.pause();
            mp.seekTo(0);
        }

        mp.start();

    }

    public void changeCharacterDirection(Character.direction newDirection) {
        Boolean isWall = isWall(ashman.getCurrentX(), ashman.getCurrentY(), ashman.getFacing());

        //determine if character is going opposite of previous direction
        //in order to aid with jumping effect when turning backwards
        if (newDirection == Character.direction.LEFT) {
            if (ashman.getFacing() == Character.direction.RIGHT && !isWall)
                ashman.setBackward();
        }
        else if (newDirection == Character.direction.RIGHT) {
            if (ashman.getFacing() == Character.direction.LEFT && !isWall)
                ashman.setBackward();
        }
        else if (newDirection == Character.direction.UP) {
            if (ashman.getFacing() == Character.direction.DOWN && !isWall)
                ashman.setBackward();
        }
        else {
            if (ashman.getFacing() == Character.direction.UP && !isWall)
                ashman.setBackward();
        }

        //set new direction
        ashman.setFacing(newDirection);
    }
    //timer control functions
    public void startTimer() {
        if (mp == null) {
            clockHandler.postDelayed(clockTimer, TIMER_MSEC);
            active = true;
        }
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
        //play beginning sound and setup level if this is first play after launch
        if(newGame) {
            level = 1;
            playSound(R.raw.begin);
            newGame = false;
        }

        canvas.scale(scale, scale);
        //reset cakes remaining with every redraw
        //they will be recalculated when cells are drawn
        int temp = cakesRemaining;
        cakesRemaining = 0;

        //draw maze by iterating through array
        drawMaze(canvas);

        //draw ashman
        ashman.draw(canvas, path, paint);

        //draw ghosts
        for (int x = 0; x < ghosts.length; x++) {
            ghosts[x].draw(canvas, path, paint);
        }

        //callback to containing activity to update cakes
        updateInterface.cakesChanged(cakesRemaining);

        //callback to containing activity to update level text
        updateInterface.levelChanged(level);

        //play cake sound if one was eaten
        if (cakesRemaining < temp)
            playSound(R.raw.eat);

        //check for game ended by ghost collision
        //only if ashman is not powered up
        if (!ashman.getPowerUpState()) {
            checkGameLost();
        }

        //check for game ended by all cakes eaten
        checkGameWon();
    }

    private void drawMaze(Canvas canvas) {
        for (int x = 0; x < MAZESIZE * MAZESIZE; x++) {
            canvas.save();
            int xCoord = x % (int)MAZESIZE;
            int yCoord = x / (int)MAZESIZE;
            canvas.translate(yCoord * 2, xCoord * 2);
            drawCell(canvas,x);
            canvas.restore();
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
        else if (currentMaze[x] == POWERUP) {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0, 0, 2, 2, paint);

            paint.setColor(Color.GREEN);
            canvas.drawCircle(1f, 1f, 0.5f, paint);

            cakesRemaining++;
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

    private void checkGameLost() {
        for (int x = 0; x < ghosts.length; x++) {
            if (ghosts[x].currentX == ashman.currentX && ghosts[x].currentY == ashman.currentY) {
                stopTimer();
                gameLost = true;
                Toast.makeText(context, "You got eaten! Level restarted!", Toast.LENGTH_SHORT).show();
                resetGame(level);
                playSound(R.raw.death);
                break;
            }
        }
    }

    private void checkGameWon() {
        if (cakesRemaining == 0 && active) {
            stopTimer();
            gameLost = false;
            if (level == 1) {
                Toast.makeText(context, "You beat level " + level + "!\n Let's try level 2!", Toast.LENGTH_SHORT).show();
                resetGame(2);
                playSound(R.raw.win);
            }
            else {
                Toast.makeText(context, " You beat level " + level + "!\nCongrats, you won!", Toast.LENGTH_SHORT).show();
                resetGame(1);
                playSound(R.raw.win);
            }
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

    //following two overrides needed for destruction and recreation of app during lock screen
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putInt("level", level);
        bundle.putSerializable("ashman", ashman);
        bundle.putInt("numGhosts", ghosts.length);
        for (int x = 0; x < ghosts.length; x++)
            bundle.putSerializable("ghost" + x, ghosts[x]);
        bundle.putBoolean("ghostsMove", ghostsMove);
        bundle.putBoolean("active", active);
        bundle.putBoolean("gameLost", gameLost);
        bundle.putIntArray("currentMaze", currentMaze);
        bundle.putBoolean("newGame", newGame);

        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            state = bundle.getParcelable("instanceState");
            level = bundle.getInt("level");
            ashman =(Ashman)bundle.getSerializable("ashman");
            ghosts = new Ghost[bundle.getInt("numGhosts")];
            for (int x = 0; x < ghosts.length; x++)
                ghosts[x] = (Ghost)bundle.getSerializable("ghost" + x);
            ghostsMove = bundle.getBoolean("ghostsMove");
            active = bundle.getBoolean("active");
            gameLost = bundle.getBoolean("gameLost");
            currentMaze = bundle.getIntArray("currentMaze");
            newGame = bundle.getBoolean("newGame");
            invalidate();
        }

        super.onRestoreInstanceState(state);
    }
}

