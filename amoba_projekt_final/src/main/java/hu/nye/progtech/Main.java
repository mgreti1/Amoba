package hu.nye.progtech;

import hu.nye.progtech.model.Coordinate;
import hu.nye.progtech.model.Symbol;
import hu.nye.progtech.persistence.HighScoreRepository;
import hu.nye.progtech.persistence.XmlManager;
import hu.nye.progtech.service.Board;
import hu.nye.progtech.service.GameLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    // A 'throws IOException' fontos a BufferedReader miatt
    public static void main(String[] args) throws IOException {
        // Scanner helyett BufferedReader-t használunk - ez sokkal megbízhatóbb
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        HighScoreRepository highScoreRepo = new HighScoreRepository();
        XmlManager xmlManager = new XmlManager();

        System.out.println("=== AMOBA JATEK (N x M) ===");
        System.out.println("1. Uj jatek");
        System.out.println("2. Jatek betoltese (XML)");
        System.out.println("3. High Score tablazat");
        System.out.println("4. Kilepes");
        System.out.print("Valassz menupontot : ");
        System.out.flush(); // Kényszerítjük a kiírást a képernyőre

        String choice = reader.readLine();
        if (choice != null) choice = choice.trim();
        else choice = "";

        if ("3".equals(choice)) {
            System.out.println("\n--- Ranglista ---");
            List<String> scores = highScoreRepo.getHighScores();
            if (scores.isEmpty()) {
                System.out.println("Meg nincs mentett eredmeny.");
            } else {
                scores.forEach(System.out::println);
            }
            return;
        } else if ("4".equals(choice)) {
            System.out.println("Viszlat!");
            return;
        }

        Board board;
        String playerName = "Jatekos";

        if ("2".equals(choice)) {
            board = xmlManager.loadFromXml("gamestate.xml");
            if (board == null) {
                System.out.println("Nem sikerult betolteni, uj jatek indul...");
                board = new Board(10, 10);
            }
        } else {
            System.out.print("Add meg a neved: ");
            System.out.flush();
            playerName = reader.readLine();
            if (playerName != null) playerName = playerName.trim();
            if (playerName == null || playerName.isEmpty()) playerName = "Nevtelen";
            board = new Board(10, 10);
        }

        GameLogic logic = new GameLogic(board);

        System.out.println("\n--- JATEK START ---");
        System.out.println("Pelda lepes: '5 c' (5. sor, C oszlop)");
        System.out.println("Parancsok: 'save', 'exit'");

        while (true) {
            System.out.println(board);

            boolean validMove = false;
            while (!validMove) {
                System.out.print(playerName + " (X) lepese: ");
                System.out.flush(); // Fontos: azonnal megjelenjen a szöveg

                String input = reader.readLine();
                if (input == null) break; // Ha vége a bemenetnek
                input = input.trim();

                if (input.isEmpty()) continue; // Ha csak entert nyomott üresen

                if ("save".equalsIgnoreCase(input)) {
                    xmlManager.saveToXml(board, playerName, "gamestate.xml");
                    System.out.println("Sikeres mentes! Folytatas...");
                    continue;
                }
                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Kilepes...");
                    return;
                }

                try {
                    // Kicseréljük a vesszőt és pontot szóközre
                    String cleanInput = input.replace(",", " ").replace(".", " ");
                    // Szétvágjuk a szóközök mentén
                    String[] parts = cleanInput.split("\\s+");

                    if (parts.length < 2) {
                        System.out.println("HIBA! Hianyos adat. Helyes formatum: Sor Oszlop (pl: 5 c)");
                        continue;
                    }

                    int r = Integer.parseInt(parts[0]) - 1;
                    char colChar = parts[1].toLowerCase().charAt(0);
                    int c = colChar - 'a';

                    Coordinate coord = new Coordinate(r, c);
                    if (logic.isValidMove(coord)) {
                        board.placeSymbol(coord, Symbol.X);
                        validMove = true;
                    } else {
                        System.out.println("ERVENYTELEN! A mezo foglalt vagy nem erintkezik masikkal.");
                    }
                } catch (Exception e) {
                    System.out.println("HIBAS FORMATUM! Nem ertem ezt: '" + input + "'");
                    System.out.println("Probald igy: 5 c");
                }
            }

            // Győzelem ellenőrzése
            if (logic.checkWin(Symbol.X)) {
                System.out.println(board);
                System.out.println("GRATULALOK " + playerName + "! NYERTEL!");
                highScoreRepo.saveWin(playerName);
                new File("gamestate.xml").delete();
                break;
            }

            if (board.isFull()) {
                System.out.println(board);
                System.out.println("Dontetlen!");
                break;
            }

            // Gép lépése
            System.out.println("A gep gondolkodik...");
            Coordinate machineMove = logic.getMachineMove();
            if (machineMove != null) {
                board.placeSymbol(machineMove, Symbol.O);
                System.out.println("Gep lepett: " + (machineMove.getRow() + 1) + " " + (char)('a' + machineMove.getCol()));
            } else {
                System.out.println("A gep nem tud lepni. Vége.");
                break;
            }

            if (logic.checkWin(Symbol.O)) {
                System.out.println(board);
                System.out.println("A gep nyert! Fel a fejjel.");
                break;
            }
        }
    }
}