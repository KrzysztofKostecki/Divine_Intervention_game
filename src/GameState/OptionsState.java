package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by xxx on 2016-04-23.
 */
public class OptionsState  extends GameState{

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "Back"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public OptionsState(GameStateManager gsm){

        this.gsm = gsm;

        try {

            bg = new Background("/Backgrounds/level1bg.png");


            titleColor = new Color(128, 0, 0);
            titleFont = new Font(
                    "Century Gothic",
                    Font.BOLD,
                    70);

            font = new Font("Century Gothic", Font.PLAIN, 30);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void init(){}
    public void update(){bg.update();}
    public void draw(java.awt.Graphics2D g){

        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Divine Intervention", 150, GamePanel.HEIGHT/2-200);

        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.BLACK);
            }
            else {
                g.setColor(new Color(214, 156, 5));
            }
            drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT-300  + i * 100,g);
        }

    }

    private void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    public void keyPressed(int k){
        if(k == KeyEvent.VK_ENTER){
            select();
        }
    }
    public void keyReleased(int k){}

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }
}
