package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Artur on 23.04.2016.
 */
public class CreditsState extends GameState {

    private Background bg;

    private Color titleColor;
    private Font titleFont;

    private Font font;

    private String[] options = {
            "Tezeusz Woronko",
            "Jakub Kolybacz",
            "Krzysztof Kostecki",
            "Marcelina Kochman",
            "Kinga Piasecka",
            "Artur Róg",
            "Jakub Skalak"
    };

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

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }
}
