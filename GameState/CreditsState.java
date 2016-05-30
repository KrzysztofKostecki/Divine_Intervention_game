package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Klasa reprezentująca menu "Credits". Obsługuje wyświetlane w menu napisy, ustawia jego tło. Przechwytuje sygnały
 * wysyłane przez klawiaturę.
 */
public class CreditsState extends GameState {

    private Background bg;

    private Color titleColor;
    private Font titleFont;

    private Font font;

    /**
     * Tablica przechowująca imiona i nazwiska twórców.
     */
    private String[] options = {
            "Tezeusz Woronko",
            "Jakub Kolybacz",
            "Krzysztof Kostecki",
            "Marcelina Kochman",
            "Kinga Piasecka",
            "Artur Róg",
            "Jakub Skalak"
    };

    /**
     * Ustawia background oraz czcionkę.
     * @param gsm Obiekt stanu gry na którym działamy.
     */
    public CreditsState(GameStateManager gsm){
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
    }

    @Override
    public void init(){}
    public void update(){bg.update();}

    /**
     * Rysuje tło oraz wypisuje imiona i nazwiska autorów.
     * @param g Pole na którym działamy.
     */
    public void draw(java.awt.Graphics2D g){
        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Credits", 350, GamePanel.HEIGHT/2-200);

        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT-300  + i * 100,g);
        }

    }
    public void keyPressed(int k){
        gsm.setState(GameStateManager.MENUSTATE);
    }
    public void keyReleased(int k){}

    /**
     * Wypisuje wypośrodkowany tekst.
     * @param s Tekst który ma być wypisany.
     * @param w Szerokość pola na którym tekst ma być wypisany.
     * @param h Wysokość pola na którym tekst ma być wypisany.
     * @param g Pole na którym tekst ma być wypisany.
     */
    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }
}
