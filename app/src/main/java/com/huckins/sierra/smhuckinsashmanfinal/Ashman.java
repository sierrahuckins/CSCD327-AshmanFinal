package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 *Ashman.java
 *Author: Sierra Huckins
 *Last Updated: 20151130
 *Description: Subclass of Character. Holds method specific to Ashman Character.
 **/
public class Ashman extends Character {
    private boolean powerUpActive = false;
    private int powerUpTimer = 0;

    public Ashman() {
        this.facing = direction.RIGHT;
        this.currentX = 7;
        this.currentY = 6;
    }

    public void setPowerUpActive() {
        powerUpActive = true;
        powerUpTimer = 10;
    }

    public boolean getPowerUpState() {
        return powerUpActive;
    }

    private void decrementPowerUpTimer() {
        if (powerUpTimer > 0)
            powerUpTimer--;
        else if (powerUpTimer == 0)
            powerUpActive = false;
    }

    @Override
    public void draw(Canvas canvas, Path path, Paint paint){
        //save canvas for restoration at end
        canvas.save();

        //translate canvas to ashman's current position
        canvas.translate(currentY * 2, currentX * 2);

        //determine how far into next step ashman is
        //this will allow for smooth movement between cells
        float offset = getStepCounter() / substeps;
        if (backward)
            offset = offset * (-1);

        //rotate canvas based on facing direction
        if (facing == direction.LEFT)
            canvas.rotate(180f,1,1);
        else if (facing == direction.UP)
            canvas.rotate(270f,1,1);
        else if (facing == direction.DOWN)
            canvas.rotate(90f,1,1);

        //setup paint color and draw ashman
        if (powerUpActive)
            paint.setColor(Color.GREEN);
        else
            paint.setColor(Color.YELLOW);
        canvas.drawCircle(1 + offset, 1, 0.75f, paint);

        //setup paint color and draw triangle for open mouthed ashman
        //only happens every other step and if ashman is not stopped
        if (getStepCounter() % 2 == 1 && getStepCounter() != 1) {
            paint.setColor(Color.BLUE);
            path = makeTrianglePath(path, offset);
            canvas.drawPath(path, paint);
        }

        //restore canvas
        canvas.restore();
    }

    private Path makeTrianglePath(Path path, float offset) {
        path.moveTo(offset + 1,1);

        path.lineTo(offset + 2,0.2f);
        path.lineTo(offset + 2,1.8f);

        path.close();

        return path;
    }

    //method to move ashman one step in the direction he is facing
    //if not facing a wall
    @Override
    void move(boolean isWall) {
        if (!isWall) {
            if (!backward)
                //increment step counter so next draw of ashman
                //will be drawn partially moved forward
                incrementStepCounter();
            else {
                //decrement step counter so next draw is returning
                //back to center of coordinate
                //helps fix jumpiness when going opposite direction
                decrementStepCounter();
            }

            //if stepcounter has been reset to 0 and he is not backward,
            //then actually update ashman's coordinates
            if (getStepCounter() == 0 && !backward) {
                updateCoord();

                //decrement powerup counter if needed
                decrementPowerUpTimer();
            }
            else if (getStepCounter() == 0 && backward)
                backward = false;
        }
        //if we can't move, set stepcounter back to 0
        //so ashman isn't drawn part way into wall
        else {
            resetStepCounter();
        }
    }
}
