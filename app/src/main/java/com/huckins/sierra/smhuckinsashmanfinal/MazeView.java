package com.huckins.sierra.smhuckinsashmanfinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sierra on 11/8/2015.
 */
public class MazeView extends View {
    public final float MAZESIZE = 14f;
    public final float HEIGHT = (2 * MAZESIZE);
    public final float WIDTH = (2 * MAZESIZE);
    public float ratio = HEIGHT/WIDTH;
    public float incomingWidth;
    public float incomingHeight;
    public float scale;
    public Path path;
    public Paint paint;
    public int level;
    public enum cellState {WALL, EMPTY, CAKE};
    public cellState[][] maze = new cellState[14][14];
    public int[][] currentMaze;
    public int[][] maze1 = {{0,0,2,0,0,0,0,0,0,0,2,0,0,0},
                            {0,2,2,2,2,2,2,2,2,2,2,2,2,0},
                            {0,2,0,2,0,0,0,0,2,0,0,0,2,0},
                            {2,2,0,2,0,2,2,2,2,2,2,0,2,2},
                            {0,0,0,2,0,2,0,0,0,0,2,0,2,0},
                            {0,2,2,2,0,2,0,2,2,2,2,2,2,0},
                            {0,2,0,2,2,2,0,1,0,0,2,0,2,0},
                            {0,2,0,2,0,0,0,2,0,0,2,0,2,0},
                            {2,2,0,2,0,2,2,2,0,0,2,0,2,2},
                            {0,2,2,2,0,2,0,0,0,0,2,0,2,0},
                            {0,2,2,0,0,2,2,2,2,2,2,0,2,0},
                            {0,2,2,0,0,0,0,2,0,0,0,0,2,0},
                            {0,2,2,2,2,2,2,2,2,2,2,2,2,0},
                            {0,0,2,0,0,0,0,0,0,0,2,0,0,0}};
    public int[][] maze2 = {{0,0,0,0,0,2,0,0,2,0,0,0,0,0},
                            {2,2,2,2,2,2,2,2,2,2,2,2,2,2},
                            {0,2,0,0,2,0,0,0,0,0,2,2,2,0},
                            {0,2,0,0,2,2,2,2,2,0,0,0,2,0},
                            {0,2,0,0,2,2,2,2,2,0,0,0,2,0},
                            {2,2,0,0,0,0,0,0,0,2,2,0,2,2},
                            {0,2,0,2,2,2,2,1,0,0,2,2,2,0},
                            {0,2,2,2,0,0,0,2,2,0,2,0,2,0},
                            {0,2,0,0,2,2,0,2,2,0,2,0,2,0},
                            {0,2,2,2,2,2,0,2,2,0,2,0,2,0},
                            {0,2,0,0,2,2,0,2,0,0,2,2,2,0},
                            {2,2,2,0,2,0,0,2,0,0,0,0,2,0},
                            {0,2,2,2,2,2,2,2,2,2,2,2,2,0},
                            {0,0,0,0,0,2,0,0,2,0,0,0,0,0}};

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

        level = 1;

        if (level == 1)
            currentMaze = maze1;
        else
            currentMaze = maze2;

        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(scale,scale);

        //draw maze by iterating through array
        for (int x = 0; x < MAZESIZE; x++) {
            for (int y = 0; y < MAZESIZE; y++) {
                canvas.save();
                canvas.translate(y * 2, x * 2);
                drawCell(canvas,x,y);
                canvas.restore();
            }
        }

        setLayerType(this.LAYER_TYPE_SOFTWARE, null);
    }

    private void drawCell(Canvas canvas, int x, int y){
        if (currentMaze[x][y] == 0) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(0,0,2,2,paint);
        }
        else if (currentMaze[x][y] == 1) {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,2,2,paint);
        }
        else {
            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,2,2,paint);

            paint.setColor(Color.WHITE);
            canvas.drawCircle(1f,1f,0.5f,paint);
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
}

