package hu.nye.progtech.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HighScoreRepository {

    private static final String DB_URL = "jdbc:h2:file:./highscore_db";
    private static final String USER = "sa";
    private static final String PASS = "";

    public HighScoreRepository() {
        initTable();
    }

    private void initTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS highscores ("
                + "player_name VARCHAR(255) PRIMARY KEY, "
                + "wins INT DEFAULT 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException p) {
            System.err.println("Adatbazis hiba (init): " + p.getMessage());
        }
    }

    public void saveWin(String playerName) {
        String mergeSQL = "MERGE INTO highscores (player_name, wins) KEY(player_name) "
                + "VALUES (?, COALESCE((SELECT wins FROM highscores WHERE player_name = ?), 0) + 1)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(mergeSQL)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, playerName);
            pstmt.executeUpdate();
            System.out.println("Eredmeny mentve az adatbazisba!");
        } catch (SQLException p) {
            System.err.println("Adatbazis hiba (save): " + p.getMessage());
        }
    }

    public List<String> getHighScores() {
        List<String> scores = new ArrayList<>();
        String querySQL = "SELECT player_name, wins FROM highscores ORDER BY wins DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            while (rs.next()) {
                scores.add(rs.getString("player_name") + ": " + rs.getInt("wins") + " gyozelem");
            }
        } catch (SQLException p) {
            System.err.println("Adatbazis hiba (query): " +p.getMessage());
        }
        return scores;
    }
}