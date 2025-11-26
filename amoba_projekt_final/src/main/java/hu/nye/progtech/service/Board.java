package hu.nye.progtech.service;

import hu.nye.progtech.model.Coordinate;
import hu.nye.progtech.model.Symbol;

public class Board {
    private final int rows;
    private final int cols;
    private final Symbol[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Symbol[rows][cols];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Symbol.EMPTY;
            }
        }
    }

    public void placeSymbol(Coordinate coord, Symbol symbol) {
        grid[coord.getRow()][coord.getCol()] = symbol;
    }

    public Symbol getSymbol(int row, int col) {
        return grid[row][col];
    }

    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == Symbol.EMPTY) return false;
            }
        }
        return true;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 0; j < cols; j++) {
            sb.append((char) ('A' + j)).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < rows; i++) {
            sb.append(String.format("%2d", i + 1)).append(" ");
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}