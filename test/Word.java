
package test;

import java.util.Arrays;

public class Word {
    private Tile[] tiles;
    private int col;
    private int row;
    private boolean vertical;

    // Constructor
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = Arrays.copyOf(tiles, tiles.length);
        this.col = col;
        this.row = row;
        this.vertical = vertical;
    }

    // Getters
    public Tile[] getTiles() {
        return tiles;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(tiles);
        result = prime * result + col;
        result = prime * result + row;
        result = prime * result + (vertical ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Word other = (Word) obj;
        if (!Arrays.equals(tiles, other.tiles))
            return false;
        if (col != other.col)
            return false;
        if (row != other.row)
            return false;
        if (vertical != other.vertical)
            return false;
        return true;
    }

}
