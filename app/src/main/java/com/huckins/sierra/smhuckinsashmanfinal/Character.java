package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Sierra on 11/11/2015.
 */
public abstract class Character {
    public enum direction {LEFT,RIGHT,UP,DOWN};
    private direction facing;
    private int currentX;
    private int currentY;
    private float stepCounter;

    abstract void move(boolean isWall);
    abstract void draw(Canvas canvas, Path path, Paint paint);

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public direction getFacing() {
        return facing;
    }

    public float getStepCounter() {
        return stepCounter;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public void setFacing(direction facing) {
        this.facing = facing;
    }

    public void setStepCounter(int step) {
        this.stepCounter = step;
    }

    public void incrementStepCounter() {
        stepCounter++;
        if (stepCounter > 4f) {
            stepCounter = 1f;
        }
    }
}
