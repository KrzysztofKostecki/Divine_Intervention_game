package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kuba on 02.04.2005.
 */
public class HighScoreState extends GameState{

    private Background bg;
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private static ArrayList<String> scoresTable;

    //for read
    public static int highest;
    public static int lowest;

    {
        if (getScores() == null)
            scoresTable = new ArrayList<>();
        else
            scoresTable = getScores();
    }
    public HighScoreState(GameStateManager gsm)
    {
        this.gsm = gsm;
        try {

            bg = new Background("/Backgrounds/level1bg.png");


            titleColor = new Color(128, 0, 0);
            titleFont = new Font(
                    "Century Gothic",
                    Font.BOLD,
                    70);

            font = new Font("Century Gothic", Font.PLAIN, 20);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init() {
        if (getScores() == null)
            scoresTable = new ArrayList<>();
        else
            scoresTable = getScores();

    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Divine Intervention", 150, GamePanel.HEIGHT/2-200);
        // draw menu options
        g.setFont(font);
        if (getScores() != null) {
            ArrayList<String> scores = getScores();
            g.setFont(font);
            for (int i = 0; i < scores.size(); i++) {
                MenuState.drawCenteredString(Integer.toString(i+1)+ ": ", GamePanel.WIDTH -100, GamePanel.HEIGHT - 300 + i * 50, g);
                MenuState.drawCenteredString(scores.get(i), GamePanel.WIDTH, GamePanel.HEIGHT - 300 + i * 50, g);
            }
        }
    }


    @Override
    public void keyPressed(int k) {
        gsm.setState(GameStateManager.MENUSTATE);
    }

    @Override
    public void keyReleased(int k) {

    }

    //zwraca true jesli wynik, nadaje sie do wrzucenia do tablicy najlepszych wynikow
    private static boolean checkCondition(int score)
    {

        //wynikow zerowych nie dodajemy
        if (score == 0)
            return false;
        if (scoresTable.isEmpty())
            return true;
        for (int i=0; i<scoresTable.size(); i++)
        {
            // blokowanie dublowania wynikow
            if (score == Integer.parseInt(scoresTable.get(i)))
                return false;
            // sprawdzanie czy rekord zostal pobity
            if (score > Integer.parseInt(scoresTable.get(i)))
               return true;
        }
        // pierwsze 10 elementow zostaje dodanych niezaleznie od tego czy sa lepsze od poprzednich
        if (scoresTable.size() < 10)
            return true;
        return false;
    }
    //funkcja sprawdzajaca czy wynik nadaje sie do tablicy i dodajaca go do tablicy
    public static void checkAndAddHighScore(int score) {
        if (checkCondition(score)) {
            if (scoresTable.size() >= 10)
            {
                scoresTable.remove(9);
            }
            scoresTable.add(Integer.toString(score));
            Collections.sort(scoresTable, new StrCmp());
            save(scoresTable);
        }
    }



    //zapis highcore jako ObjectOutStream
    private static void save(ArrayList<String> scoresTable)
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
    //odczyt highscora z pliku
    private ArrayList<String> getScores()
    {
        ArrayList<String> scores;
        try {
            FileInputStream filein = new FileInputStream("highscore.jp2");
            ObjectInputStream ois = new ObjectInputStream(filein);
            scores =(ArrayList<String>) ois.readObject();
            ois.close();
            highest = Integer.parseInt(scores.get(0));
            lowest = Integer.parseInt(scores.get(scores.size()-1));
            return scores;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}


class StrCmp implements Comparator<String>{
    public int compare(String s1, String s2)
    {
        int i1 = Integer.parseInt(s1);
        int i2 = Integer.parseInt(s2);
        return i2 - i1;
    }
}