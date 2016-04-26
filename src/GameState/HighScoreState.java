package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
 * Created by Kuba on 02.04.2005.
 */
public class HighScoreState extends GameState{

    private Background bg;
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private static TreeMap<Integer,String> scoresTable;

    //for read
    public static int highest;
    public static int lowest;

    {
        if (getScores() == null)
            scoresTable = new TreeMap<>(Collections.reverseOrder());
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

            font = new Font("Century Gothic", Font.PLAIN, 25);

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
    public void init() {
        if (getScores() == null)
            scoresTable = new TreeMap<>(Collections.reverseOrder());
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
        if (scoresTable != null) {
            TreeMap<Integer,String> scores = scoresTable;
            g.setFont(font);
            int i = 0;
            for (Map.Entry<Integer,String> entry : scores.entrySet()) {
                MenuState.drawCenteredString(i+ ": ", GamePanel.WIDTH -200, GamePanel.HEIGHT - 300 + i * 50, g);
                MenuState.drawCenteredString(entry.getKey()+ " ", GamePanel.WIDTH -100, GamePanel.HEIGHT - 300 + i * 50, g);
                MenuState.drawCenteredString(entry.getValue(), GamePanel.WIDTH, GamePanel.HEIGHT - 300 + i * 50, g);
                i++;
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

    //funkcja sprawdzajaca czy wynik nadaje sie do tablicy i dodajaca go do tablicy
    public static void checkAndAddHighScore(int score, String nickname) {
            scoresTable.put(score,nickname);
            if (scoresTable.size() >= 10)
            {
                scoresTable.remove(scoresTable.lastKey());
            }
            save(scoresTable);
        }




    //zapis highcore jako ObjectOutStream
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
    //odczyt highscora z pliku
    private TreeMap<Integer,String> getScores()
    {
        TreeMap<Integer,String> scores;
        try {
            FileInputStream filein = new FileInputStream("highscore.jp2");
            ObjectInputStream ois = new ObjectInputStream(filein);
            scores =(TreeMap<Integer,String>) ois.readObject();
            ois.close();
            //highest = Integer.parseInt(scores.get(0));
            //lowest = Integer.parseInt(scores.get(scores.size()-1));
            return scores;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
