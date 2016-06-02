package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Klasa reprezentująca HighScore. Podczas uruchomienia gry wczytuje z pliku wyniki lub tworzy nowy plik, jeśli takowy nie istniał, po czym
 * wypisuje highscores.
 * <p>
 *     Jest też  odpowiedzialna za funkcjonalność dodawania nowych wyników do tabeli. Zawiera pola z najwyższym i najniższym wynikiem w highscore.
 *     Zawiera metodę sprawdzającą, czy uzyskany wynik może być dodany do highscore.
 * </p>
 *
 */
public class HighScoreState extends GameState{


    private Background foreGround;
    private GraphicsEnvironment ge;
    /**
     * Grafika tła.
     */
    private Background bg;
    /**
     * Nazwa czcionki.
     */
    private Font font;
    /**
     * Mapa przechowująca wyniki.
     */
    private static TreeMap<Integer,String> scoresTable;

    //for read
    /**
     * Najlepszy wynik w highscore.
     */
    public static int highest;
    /**
     * Najsłabszy wynik w highscore.
     */
    public static int lowest;

    {
        if (getScores() == null)
            scoresTable = new TreeMap<>(Collections.reverseOrder());
        else
            scoresTable = getScores();
    }

    /**
     * Ustawia background oraz czcionkę, znajduje największy i najmniejszy rekord w highscore.
     * @param gsm Obiekt stanu gry na któym działamy.
     */
    public HighScoreState(GameStateManager gsm)
    {
        this.gsm = gsm;
        try {

            bg = new Background("/Backgrounds/level1bg.png");
            bg.setVector(-0.5,0);
            foreGround = new Background("/Backgrounds/high_score.png");

            URL fontUrl = getClass().getResource("/Fonts/Abel-Regular.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
            font = font.deriveFont(Font.PLAIN,25);

            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        if(scoresTable.size()!=0){
            highest = scoresTable.firstKey();
            lowest = scoresTable.lastKey();
        }

    }

    @Override
    /**
     *  Inicjalizuje scoresTable. Odczytuje wyniki highscore i zapisuje je do scoresTable, lub tworzy nową, pustą tabelę
     *  jeśli plik nie istnieje.
     */
    public void init() {
        if (getScores() == null)
            scoresTable = new TreeMap<>(Collections.reverseOrder());
        else
            scoresTable = getScores();
    }

    /**
     * Aktualizuje stan gry.
     */
    @Override
    public void update() {
        bg.update();
    }

    /**
     * Rysuje tło, tytuł oraz wszystkie rekordy z highscores.
     * @param g Obiekt na któym rysujemy.
     */
    @Override
    public void draw(Graphics2D g) {
        // draw bg
        bg.draw(g);
        foreGround.draw(g);
        // draw menu options
        g.setFont(font);
        if (scoresTable != null) {
            TreeMap<Integer,String> scores = scoresTable;
            g.setFont(font);
            int i = 0;
            for (Map.Entry<Integer,String> entry : scores.entrySet()) {
                MenuState.drawCenteredString((i+1)+ ": "+entry.getValue()+" "+entry.getKey(), GamePanel.WIDTH, GamePanel.HEIGHT - 100 + i * 65, g);

                i++;
            }
        }
    }


    @Override
    /**
     *  Przechwytuje sygnały z klawiatury (wciśnięty klawisz), jeśli dowolny klawisz zostanie wciśnięty,
     *  to stan gry zmienia się na menu.
     */
    public void keyPressed(int k) {
        gsm.setState(GameStateManager.MENUSTATE);
    }

    @Override
    public void keyReleased(int k) {

    }

    /**
     * Sprawdza, czy uzyskany wynik nadaje się do highscore i jesli tak, to go dodaje.
     * @param score Uzyskany wynik.
     * @param nickname Nick pod jakim wynik ma być zapisany.
     */
    public static void checkAndAddHighScore(int score, String nickname) {
            scoresTable.put(score,nickname);
            if (scoresTable.size() > 10)
            {
                scoresTable.remove(scoresTable.lastKey());
            }
            highest = scoresTable.firstKey();
            lowest = scoresTable.lastKey();
            save(scoresTable);
        }





    /**
     * Zapisuje tabelę highscore jako ObjectOutStream
     * @param scoresTable Tabela highscore.
     */
    private static void save(TreeMap<Integer,String> scoresTable)
    {
        FileOutputStream fileon = null;
        try {
            fileon = new FileOutputStream("highscore.jp2");
            ObjectOutputStream oos = new ObjectOutputStream(fileon);
            oos.writeObject(scoresTable);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2){
            e2.printStackTrace();
        }
        return;
    }

    /**
     * Odczytuje tabelę highscore z pliku.
     * @return Zwraca tabelę highscore odczytaną z pliku.
     */
    private TreeMap<Integer,String> getScores()
    {
        TreeMap<Integer,String> scores;
        try {
            FileInputStream filein = new FileInputStream("highscore.jp2");
            ObjectInputStream ois = new ObjectInputStream(filein);
            scores =(TreeMap<Integer,String>) ois.readObject();
            ois.close();
            if(scoresTable!=null){
                highest = scoresTable.firstKey();
                lowest = scoresTable.lastKey();
            }
            return scores;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
