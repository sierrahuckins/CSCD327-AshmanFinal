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
        this.setFacing(facing);
        this.setCurrentX(x);
        this.setCurrentY(y);
    }

    @Override
    public void draw(Canvas canvas, Path path, Paint paint){
        int ashmanX = getCurrentX();
        int ashmanY = getCurrentY();
        canvas.save();
        canvas.translate(ashmanY * 2, ashmanX * 2);

        float offset = getStepCounter() / 3;

        //rotate canvas based on facing direction
        if (getFacing() == direction.LEFT)
            canvas.rotate(180f,1,1);
        else if (getFacing() == direction.UP)
            canvas.rotate(270f,1,1);
        else if (getFacing() == direction.DOWN)
            canvas.rotate(90f,1,1);

        paint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 2, 2, paint);

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(1 + offset, 1, 0.75f, paint);


        if (getStepCounter() % 2 == 1 && getStepCounter() != 1) {
            paint.setColor(Color.BLUE);
            path = makePath(path, offset);
            canvas.drawPath(path, paint);
        }

    }

    private Path makePath(Path path, float offset) {
        path.moveTo(offset + 1,1);

        path.lineTo(offset + 2,0.2f);
        path.lineTo(offset + 2,1.8f);

        path.close();

        return path;
    }

    @Override
    void move(boolean isWall) {
        if (!isWall) {
            incrementStepCounter();

            int x = getCurrentX();
            int y = getCurrentY();

            if (getStepCounter() == 1) {
                if (getFacing() == direction.LEFT) {
                    setCurrentX(x + 0);
                    if (y == 0)
                        setCurrentY(y + 14 - 1);
                    else
                        setCurrentY(y - 1);
                } else if (getFacing() == direction.UP) {
                    if (x == 0)
                        setCurrentX(x + 14 - 1);
                    else
                        setCurrentX(x - 1);
                    setCurrentY(y + 0);
                } else if (getFacing() == direction.DOWN) {
                    if (x == 13)
                        setCurrentX(x - 14 + 1);
                    else
                        setCurrentX(x + 1);
                    setCurrentY(getCurrentY() + 0);
                } else {
                    setCurrentX(getCurrentX() + 0);
                    if (y == 13)
                        setCurrentY(y - 14 + 1);
                    else
                        setCurrentY(y + 1);
                }
            }
        }
        else {
            setStepCounter(0);
        }
    }
}
