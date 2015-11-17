package com.huckins.sierra.smhuckinsashmanfinal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sierra on 11/16/2015.
 */
public class MazeFactory {
    //member variables to control maze
    public enum cellState implements Parcelable {WALL, EMPTY, CAKE;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    };

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
            cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,
            cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
            cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.EMPTY,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
            cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
            cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,
            cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
            cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,
            cellState.WALL,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.CAKE,cellState.WALL,
            cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.CAKE,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL};
    private final cellState[] cheat = {cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.CAKE,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.EMPTY,cellState.WALL,
            cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL,cellState.WALL};

    public MazeFactory () {}

    public cellState[] getMaze (int level) {
        if (level == 1)
            return maze1;
        else if (level == 2)
            return maze2;
        else
            return cheat;
    }

}
