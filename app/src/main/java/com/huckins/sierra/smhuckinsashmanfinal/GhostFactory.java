package com.huckins.sierra.smhuckinsashmanfinal;

/**
 * Created by Sierra on 11/16/2015.
 */
public class GhostFactory {
    private final Ghost[] ghostsLevel1 = {new Ghost(1, 1, Character.direction.RIGHT), new Ghost(1, 12, Character.direction.LEFT),
            new Ghost(12, 7, Character.direction.RIGHT)};
    private final Ghost[] ghostsLevel2 = {new Ghost(1, 1, Character.direction.RIGHT), new Ghost(1, 12, Character.direction.LEFT),
            new Ghost(12, 1, Character.direction.RIGHT), new Ghost(12, 7, Character.direction.RIGHT), new Ghost(12, 12, Character.direction.RIGHT)};

    public GhostFactory() {
    }

    public Ghost[] getGhosts(int level) {

        if (level == 1) {
            return initGhosts(ghostsLevel1);
        } else {
            return initGhosts(ghostsLevel2);
        }
    }

    private Ghost[] initGhosts(Ghost[] original) {
        Ghost[] toReturn = new Ghost[original.length];

        for (int i = 0; i < original.length; i++) {
            Ghost newGhost = new Ghost(original[i].currentX, original[i].currentY, Character.direction.RIGHT);
            toReturn[i] = newGhost;
        }

        return toReturn;
    }
}
