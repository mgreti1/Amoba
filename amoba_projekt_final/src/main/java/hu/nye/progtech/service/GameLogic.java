package hu.nye.progtech.service;

import hu.nye.progtech.model.Coordinate;
import hu.nye.progtech.model.Symbol;
import java.util.Random;

public class GameLogic {
    private final Board board;

    public GameLogic(Board board) {
        this.board = board;
    }

    public boolean isValidMove(Coordinate coord) {
        int r = coord.getRow();
        int c = coord.getCol();
        
        if (r < 0 || r >= board.getRows() || c < 0 || c >= board.getCols()) {
            return false;
        }
        if (board.getSymbol(r, c) != Symbol.EMPTY) {
            return false;
        }
        if (isBoardEmpty()) {
            return true; 
        }
        return hasNeighbor(r, c);
    }
    
    private boolean isBoardEmpty() {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getSymbol(i, j) != Symbol.EMPTY) return false;
            }
        }
        return true;
    }

    private boolean hasNeighbor(int r, int c) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nr = r + i;
                int nc = c + j;
                if (nr >= 0 && nr < board.getRows() && nc >= 0 && nc < board.getCols()) {
                    if (board.getSymbol(nr, nc) != Symbol.EMPTY) return true;
                }
            }
        }
        return false;
    }

    public boolean checkWin(Symbol symbol) {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (checkDirection(r, c, 1, 0, symbol) ||
                    checkDirection(r, c, 0, 1, symbol) ||
                    checkDirection(r, c, 1, 1, symbol) ||
                    checkDirection(r, c, 1, -1, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int r, int c, int dr, int dc, Symbol symbol) {
        int winLength = 4;
        for (int i = 0; i < winLength; i++) {
            int nr = r + i * dr;
            int nc = c + i * dc;
            if (nr < 0 || nr >= board.getRows() || nc < 0 || nc >= board.getCols()) return false;
            if (board.getSymbol(nr, nc) != symbol) return false;
        }
        return true;
    }
    
    public Coordinate getMachineMove() {
        Random rand = new Random();
        int attempt = 0;
        while (attempt < 1000) {
            int r = rand.nextInt(board.getRows());
            int c = rand.nextInt(board.getCols());
            Coordinate coord = new Coordinate(r, c);
            if (isValidMove(coord)) {
                return coord;
            }
            attempt++;
        }
        return null;
    }
}