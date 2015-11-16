package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Sierra on 11/11/2015.
 */
public abstract class Character {
    public enum direction {LEFT,RIGHT,UP,DOWN};
    protected direction facing;
    protected int currentX;
    protected int currentY;
    //needs to be private so it can only be incremented, not set
    private float stepCounter;

    abstract void move(boolean isWall);
    abstract void draw(Canvas canvas, Path path, Paint paint);

    protected int getCurrentX() {
        return currentX;
    }

    protected int getCurrentY() {
        return currentY;
    }

    protected direction getFacing() {
        return facing;
    }

    protected float getStepCounter() {
        return stepCounter;
    }
    protected void incrementStepCounter() {
        stepCounter++;
        if (stepCounter > 4f) {
            stepCounter = 1f;
        }
    }

    protected void resetStepCounter() {
        stepCounter = 0;
    }

}
