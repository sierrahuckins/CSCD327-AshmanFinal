package com.huckins.sierra.smhuckinsashmanfinal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Sierra on 11/11/2015.
 */
public class Ghost extends Character {

    public Ghost (int x, int y, direction facing) {
        this.setFacing(facing);
        this.setCurrentX(x);
        this.setCurrentY(y);
    }

    @Override
    void draw(Canvas canvas, Path path, Paint paint) {

    }

    @Override
    void move(boolean isWall) {

    }
}
