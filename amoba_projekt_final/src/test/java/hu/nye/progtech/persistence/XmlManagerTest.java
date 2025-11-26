package hu.nye.progtech.persistence;

import hu.nye.progtech.model.Coordinate;
import hu.nye.progtech.model.Symbol;
import hu.nye.progtech.service.Board;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class XmlManagerTest {

    @Test
    void testSaveAndLoad() {
        XmlManager manager = new XmlManager();
        Board originalBoard = new Board(5, 5);
        originalBoard.placeSymbol(new Coordinate(2, 2), Symbol.X);
        String testFile = "test_gamestate.xml";

        manager.saveToXml(originalBoard, "Tester", testFile);
        Board loadedBoard = manager.loadFromXml(testFile);

        assertNotNull(loadedBoard);
        assertEquals(5, loadedBoard.getRows());
        assertEquals(Symbol.X, loadedBoard.getSymbol(2, 2));

        assertTrue(new File(testFile).delete());
    }
}