package hu.nye.progtech.service;

import hu.nye.progtech.model.Coordinate;
import hu.nye.progtech.model.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    private Board board;
    private GameLogic underTest;

    @BeforeEach
    void setUp() {
        board = new Board(10, 10);
        underTest = new GameLogic(board);
    }

    @Test
    void testIsValidMoveShouldReturnTrueForEmptyBoardCenter() {
        Coordinate center = new Coordinate(5, 5);
        assertTrue(underTest.isValidMove(center));
    }

    @Test
    void testIsValidMoveShouldReturnFalseForOutOfBounds() {
        Coordinate out = new Coordinate(11, 11);
        assertFalse(underTest.isValidMove(out));
    }

    @Test
    void testCheckWinShouldReturnTrueForHorizontalWin() {
        board.placeSymbol(new Coordinate(0,0), Symbol.X);
        board.placeSymbol(new Coordinate(0,1), Symbol.X);
        board.placeSymbol(new Coordinate(0,2), Symbol.X);
        board.placeSymbol(new Coordinate(0,3), Symbol.X);
        assertTrue(underTest.checkWin(Symbol.X));
    }
}