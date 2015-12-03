package com.huckins.sierra.smhuckinsashmanfinal;

/**
 *GhostFactory.java
 *Author: Sierra Huckins
 *Last Updated: 20151130
 *Description: Factory to create new sets of ghosts as needed for each level using final sets stored here.
 **/
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
