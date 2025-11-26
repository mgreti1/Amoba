package hu.nye.progtech.persistence;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "game_state")
public class XmlGameState {
    private int rows;
    private int cols;
    private String currentPlayerName;
    private List<String> placedSymbols;

    public XmlGameState() {}

    public int getRows() { return rows; }
    @XmlElement
    public void setRows(int rows) { this.rows = rows; }

    public int getCols() { return cols; }
    @XmlElement
    public void setCols(int cols) { this.cols = cols; }

    public String getCurrentPlayerName() { return currentPlayerName; }
    @XmlElement
    public void setCurrentPlayerName(String currentPlayerName) { this.currentPlayerName = currentPlayerName; }

    public List<String> getPlacedSymbols() { return placedSymbols; }
    @XmlElement
    public void setPlacedSymbols(List<String> placedSymbols) { this.placedSymbols = placedSymbols; }
}