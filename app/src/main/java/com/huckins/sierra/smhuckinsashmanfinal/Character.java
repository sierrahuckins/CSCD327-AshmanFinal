package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.io.Serializable;

/**
 *Character.java
 *Author: Sierra Huckins
 *Last Updated: 20151130
 *Description: Abstract class. Extended by Ashman and Ghost.
 **/
public abstract class Character implements Serializable {
    public enum direction {LEFT,RIGHT,UP,DOWN}
    protected direction facing;
    protected boolean backward = false;
    protected int currentX;
    protected int currentY;
    //needs to be private so it can only be incremented, not set arbitrarily
    private float stepCounter;
    protected final float substeps = 3f;

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

    protected void setFacing(direction incoming) {
        facing = incoming;
    }

    protected void setBackward() { backward = true; }

    protected float getStepCounter() {
        return stepCounter;
    }

    protected void incrementStepCounter() {
        stepCounter++;
        if (stepCounter > substeps) {
            resetStepCounter();
        }
    }

    protected void decrementStepCounter() {
        if (backward && stepCounter > 0) {
            stepCounter--;
        }
    }

    protected void resetStepCounter() {
        stepCounter = 0;
    }

    //moves character one forward in the direction they are facing
    protected void updateCoord() {
        if (facing == direction.LEFT) {
            currentX =(currentX + 0);
            //allow for wrap around left side of map
            if (currentY == 0)
                currentY = (currentY + 14 - 1);
            else
                currentY = (currentY - 1);
        } else if (facing == direction.UP) {
            //allow for wrap around top of map
            if (currentX == 0)
                currentX = (currentX + 14 - 1);
            else
                currentX = (currentX - 1);
            currentY = (currentY + 0);
        } else if (facing == direction.DOWN) {
            //allow for wrap around bottom of map
            if (currentX == 13)
                currentX = (currentX - 14 + 1);
            else
                currentX = (currentX + 1);
            currentY = (currentY + 0);
        } else {
            currentX = (currentX + 0);
            //allow for wrap around right side of map
            if (currentY == 13)
                currentY = (currentY - 14 + 1);
            else
                currentY = (currentY + 1);
        }
    }


}
