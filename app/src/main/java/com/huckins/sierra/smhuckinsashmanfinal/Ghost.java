package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

/**
 *Ghost.java
 *Author: Sierra Huckins
 *Last Updated: 20151130
 *Description: Subclass of Character. Holds method specific to Ghost Character.
 **/
public class Ghost extends Character {
    private Random random = new Random();

    public Ghost (int x, int y, direction facing) {
        this.facing = facing;
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    void draw(Canvas canvas, Path path, Paint paint) {
        //save canvas for restoration at end
        canvas.save();

        //translate canvas to ghost's current position
        canvas.translate(currentY * 2, currentX * 2);

        //determine how far into next step ghost is
        //this will allow for smooth movement between cells
        float offset = getStepCounter() / substeps;

        //rotate canvas based on facing direction
        if (getFacing() == direction.LEFT)
            canvas.rotate(180f,1,1);
        else if (getFacing() == direction.UP)
            canvas.rotate(270f,1,1);
        else if (getFacing() == direction.DOWN)
            canvas.rotate(90f,1,1);

        //setup paint color and draw ghost
        paint.setColor(Color.RED);
        canvas.drawCircle(1 + offset, 1, 0.75f, paint);

        //restore canvas
        canvas.restore();
    }

    //method to move ghost if his next step in the direction he is facing
    //if not a wall
    @Override
    void move(boolean isWall) {
        if (!isWall) {
            //increment step counter so next draw of ghost
            //will be drawn partially moved forward
            incrementStepCounter();

            //if stepcounter has been reset to 0,
            //then actually update ghost's coordinates
            if (getStepCounter() == 0) {
                updateCoord();

                //randomly turn ghost after some full movements
                //allows ghost chance to go sideways
                //when there isn't a wall in front of them
                if (random.nextInt(2) % 2 == 0) {
                    turnGhost();
                }
            }
        }
        //if ghost can't move, set stepcounter back to 0
        //and change direction
        else {
            turnGhost();
        }
    }

    private void turnGhost() {
        resetStepCounter();

        //use random to pick new direction
        int newDirection = random.nextInt(4);

        if (newDirection == 0)
            facing = direction.UP;
        else if (newDirection == 1)
            facing = direction.RIGHT;
        else if (newDirection == 2)
            facing = direction.DOWN;
        else
            facing = direction.LEFT;
    }
}
