package GameState;

import Audio.AudioPlayer;
import Main.GamePanel;
import Scene.Platform;
import Scene.Player;
import Scene.PlayerMenu;
import TileMap.Background;
import Utils.ExperienceManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

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

    //for choose character
    public static int choosenCharacter;
    public final static int DEFAULT = 0;
    public final static int PAPAJ = 1;
    public final static int STONOG = 2;
    //numbers of avaiable characters
    private Integer characters = 3;
    private static int currentCharacter;

    private PlayerMenu player;

    private HashMap<Integer, AudioPlayer> sfx;

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
            player = new PlayerMenu(DEFAULT);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        sfx = new HashMap<Integer, AudioPlayer>();
        sfx.put(PAPAJ,new AudioPlayer("/SFX/okrutnik.mp3"));
        sfx.put(STONOG,new AudioPlayer("/SFX/stonog.mp3"));

        choosenCharacter = 0;

    }
    public void init(){

        player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);
        currentCharacter = choosenCharacter;
        //if(ExperienceManager.getExperience() > 2137){
        //    characters = 2;
        //}
    }
    public void update(){
        bg.update();
        player.update();
        player.setVector(0,0);
        player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);

    }
    public void draw(java.awt.Graphics2D g){

        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Choose Character", 180, GamePanel.HEIGHT/2-200);
        player.draw(g);
        g.setFont(font);
        drawCenteredString("<< >>",GamePanel.WIDTH,GamePanel.HEIGHT-50,g);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(new Color(214, 156, 5));
            }
            else {
                g.setColor(new Color(214, 156, 5));
            }
            drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT + 100  + i * 100,g);
        }

    }

    private void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    private void selectCharacter(){
        if(currentCharacter == DEFAULT){
            sfx.get(STONOG).stop();
            sfx.get(PAPAJ).stop();
            player = new PlayerMenu(DEFAULT);
            player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);
        }
        if(currentCharacter == PAPAJ){
            sfx.get(STONOG).stop();
            sfx.get(PAPAJ).play();
            player = new PlayerMenu(PAPAJ);
            player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);
        }
        if(currentCharacter == STONOG){
            sfx.get(PAPAJ).stop();
            sfx.get(STONOG).play();
            player = new PlayerMenu(STONOG);
            player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);
        }
    }

    public void keyPressed(int k){
        if(k == KeyEvent.VK_ENTER){
            sfx.get(STONOG).stop();
            sfx.get(PAPAJ).stop();
            select();
            choosenCharacter = currentCharacter;
        }
        if(k == KeyEvent.VK_ESCAPE){
            sfx.get(STONOG).stop();
            sfx.get(PAPAJ).stop();
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
    public void keyReleased(int k){
        if(k == KeyEvent.VK_RIGHT){
            currentCharacter++;
            if(currentCharacter == characters){
                currentCharacter = 0;
            }
            selectCharacter();
        }
        if(k == KeyEvent.VK_LEFT){
            currentCharacter--;
            if(currentCharacter == -1){
                currentCharacter = characters-1;
            }
            selectCharacter();
        }
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

}
