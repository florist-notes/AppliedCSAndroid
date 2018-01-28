package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class PuzzleBoard {

    private static final int NUM_TILES = 4;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    PuzzleBoard previousBoard;
    int steps;

    // breaking up the given image into tile-sized chunks.
    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        bitmap=Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,false);
        int widthOfTile=parentWidth/NUM_TILES;
        tiles=new ArrayList<>();
        for (int i = 0; i <NUM_TILES ; i++) {
            for (int j = 0; j <NUM_TILES ; j++) {
                Bitmap tile=Bitmap.createBitmap(bitmap,j*widthOfTile,i*widthOfTile,widthOfTile,widthOfTile);
                tiles.add(new PuzzleTile(tile,NUM_TILES*i+j));
            }
        }
        tiles.remove(NUM_TILES*NUM_TILES-1);
        tiles.add(null);
        steps=0;
        previousBoard=null;
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        steps=otherBoard.steps+1;
        this.previousBoard =otherBoard;
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        for (int k=0;k<tiles.size();k++) {
            PuzzleTile tile=tiles.get(k);
            if (tile==null){
                ArrayList<PuzzleBoard> neighbours=new ArrayList<>();
                for (int[] NEIGHBOUR_COORD : NEIGHBOUR_COORDS) {
                    if (k / NUM_TILES + NEIGHBOUR_COORD[1] < NUM_TILES && k / NUM_TILES + NEIGHBOUR_COORD[1] >= 0 && k % NUM_TILES + NEIGHBOUR_COORD[0] < NUM_TILES && k % NUM_TILES + NEIGHBOUR_COORD[0] >= 0) {
                        PuzzleBoard temp = new PuzzleBoard(this);
                        temp.swapTiles(k, k + NEIGHBOUR_COORD[0] + NUM_TILES * NEIGHBOUR_COORD[1]);
                        neighbours.add(temp);
                    }
                }
                return neighbours;
            }
        }
        return null;
    }

    public int priority() {
        int manhattanDistance=0;
        for (int i = 0; i <tiles.size() ; i++) {
            if(tiles.get(i)!=null) {
                int columnChange = Math.abs(i % NUM_TILES - tiles.get(i).getNumber() % NUM_TILES);
                int rowChange = Math.abs(i / NUM_TILES - tiles.get(i).getNumber() / NUM_TILES);
                manhattanDistance+= columnChange+rowChange;
            }else {
                int columnChange = Math.abs(i % NUM_TILES - (NUM_TILES*NUM_TILES-1) % NUM_TILES);
                int rowChange = Math.abs(i / NUM_TILES - (NUM_TILES*NUM_TILES-1) / NUM_TILES);
                manhattanDistance+=columnChange+rowChange;
            }
        }
        return manhattanDistance+steps;
    }

}
