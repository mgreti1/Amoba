package hu.nye.progtech.persistence;

import hu.nye.progtech.model.Coordinate;
import hu.nye.progtech.model.Symbol;
import hu.nye.progtech.service.Board;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlManager {

    public void saveToXml(Board board, String playerName, String filename) {
        try {
            XmlGameState state = new XmlGameState();
            state.setRows(board.getRows());
            state.setCols(board.getCols());
            state.setCurrentPlayerName(playerName);

            List<String> symbols = new ArrayList<>();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    Symbol s = board.getSymbol(i, j);
                    if (s != Symbol.EMPTY) {
                        symbols.add(i + "," + j + "," + s.name());
                    }
                }
            }
            state.setPlacedSymbols(symbols);

            JAXBContext context = JAXBContext.newInstance(XmlGameState.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(state, new File(filename));
            System.out.println("Jatekallas sikeresen mentve XML-be: " + filename);

        } catch (Exception e) {
            System.err.println("XML mentesi hiba: " + e.getMessage());
        }
    }

    public Board loadFromXml(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return null;

            JAXBContext context = JAXBContext.newInstance(XmlGameState.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XmlGameState state = (XmlGameState) unmarshaller.unmarshal(file);

            Board board = new Board(state.getRows(), state.getCols());
            for (String entry : state.getPlacedSymbols()) {
                String[] parts = entry.split(",");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);
                Symbol s = Symbol.valueOf(parts[2]);
                board.placeSymbol(new Coordinate(r, c), s);
            }
            System.out.println("Jatekallas betoltve XML-bol. Jatekos: " + state.getCurrentPlayerName());
            return board;
        } catch (Exception e) {
            System.err.println("XML betoltesi hiba: " + e.getMessage());
            return null;
        }
    }
}