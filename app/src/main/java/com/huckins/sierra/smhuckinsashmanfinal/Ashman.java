package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Sierra on 11/11/2015.
 */
public class Ashman extends Character {

    public Ashman(int x, int y, direction facing) {
        this.facing = facing;
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    public void draw(Canvas canvas, Path path, Paint paint){
        //save canvas for restoration at end
        canvas.save();

        //translate canvas to ashman's current position
        canvas.translate(currentY * 2, currentX * 2);

        //determine how far into next step ashman is
        //this will allow for smooth movement between cells
        float offset = getStepCounter() / 3;

        //rotate canvas based on facing direction
        if (facing == direction.LEFT)
            canvas.rotate(180f,1,1);
        else if (facing == direction.UP)
            canvas.rotate(270f,1,1);
        else if (facing == direction.DOWN)
            canvas.rotate(90f,1,1);

        //setup paint color and draw ashman
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

    //method to move ashman if his next step in the direction he is facing
    //if not a wall
    @Override
    void move(boolean isWall) {
        if (!isWall) {
            //increment step counter so next draw of ashman
            //will be drawn partially moved forward
            incrementStepCounter();

            //if stepcounter has been reset to 1,
            //then actually update ashman's coordinates
            if (getStepCounter() == 1) {
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
        //if we can't move, set stepcounter back to 0
        //so ashman isn't drawn part way into wall
        else {
            resetStepCounter();
        }
    }
}
