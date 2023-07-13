package com.tfp;

public class CityMap {
    private char[][] map;
    private int numRows;
    private int numCols;

    public CityMap(char[][] map) {
        this.map = map;
        numRows = map.length;
        numCols = map[0].length;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < numRows && y >= 0 && y < numCols && map[x][y] != '#';
    }
}
