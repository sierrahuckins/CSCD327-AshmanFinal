package com.huckins.sierra.smhuckinsashmanfinal;

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

/**
 * Created by Sierra on 11/8/2015.
 */
public class MazeView extends View {
    private final int MAZESIZE = 14;
    private final float HEIGHT = (2 * MAZESIZE);
    private final float WIDTH = (2 * MAZESIZE);
    private float ratio = HEIGHT/WIDTH;
    private float incomingWidth;
    private float incomingHeight;
    private float scale;
    private Handler clockHandler;
    private Runnable clockTimer;
    private final int TIMER_MSEC = 70;
    private Path path;
    private Paint paint;
    private int level = 1;
    private Ashman ashman;
    private int cakesRemaining = 0;
    private Ghost[] ghosts = new Ghost[4];
    public boolean active = false;
    private enum cellState implements Parcelable {WALL, EMPTY, CAKE;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    };

    private cellState[] currentMaze;
    private final cellState[] maze1 = {cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,
                                    cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.EMPTY,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL};
    private final cellState[] maze2 = {cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,
                                    cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.EMPTY,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
                                    cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL};

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
        if (path == null)
            path = new Path();
        if (paint == null)
            paint = new Paint();

        if (level == 1)
            currentMaze = maze1;
        else
            currentMaze = maze2;

        paint.setStyle(Paint.Style.FILL);

        if (ashman == null) {
            ashman = new Ashman(7,6, Character.direction.RIGHT);
        }

        setLayerType(this.LAYER_TYPE_SOFTWARE, null);

        clockHandler = new Handler() ;
        clockTimer = new Runnable() {
            @Override
            public void run() {
                ashman.move( isWall(ashman.getCurrentX(), ashman.getCurrentY(), ashman.getFacing()));
                invalidate();
                clockHandler.postDelayed(this, TIMER_MSEC) ;
            }
        };
    }

    public void setAshmanFacing(Character.direction facing) {
        ashman.setFacing(facing);
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

    public void startTimer() {
        clockHandler.postDelayed(clockTimer,TIMER_MSEC);
        active = true;
    }

    public void stopTimer() {
        clockHandler.removeCallbacks(clockTimer);
        active = false;
    }

    public cellState[] getCurrentMaze() {
        return currentMaze;
    }

    public boolean isWall(int x, int y, Character.direction facing ) {
        int arrayPos = x + (y * MAZESIZE);

        if (facing == Character.direction.LEFT) {
            arrayPos = arrayPos - MAZESIZE;
            if (y == 0)
                arrayPos = arrayPos + (MAZESIZE * MAZESIZE);
        }
        else if (facing == Character.direction.RIGHT) {
            arrayPos = arrayPos + MAZESIZE;
            if (y == 13)
                arrayPos = arrayPos - (MAZESIZE * MAZESIZE);
        }
        else if (facing == Character.direction.UP) {
            arrayPos = arrayPos - 1;
            if (x == 0)
                arrayPos = arrayPos + (MAZESIZE - 1);
        }
        else {
            arrayPos = arrayPos + 1;
            if (x == 13)
                arrayPos = arrayPos + (MAZESIZE - 1);
        }


        if (currentMaze[arrayPos] == cellState.WALL)
            return true;
        else
            return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(scale, scale);
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
    }

    private void drawCell(Canvas canvas, int x){
        if (currentMaze[x] == cellState.WALL) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0,0,2,2,paint);
        }
        else if (currentMaze[x] == cellState.EMPTY) {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,2,2,paint);
        }
        else {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,2,2,paint);

            paint.setColor(Color.WHITE);
            canvas.drawCircle(1f,1f,0.5f,paint);

            cakesRemaining++;
        }
    }

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

