package GameState;

import Audio.AudioPlayer;
import Main.GamePanel;
import Scene.Platform;
import Scene.Player;
import TileMap.Background;
import Utils.ExperienceManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Krzysztof on 05.04.2016.
 */
public class Level1State extends GameState {
    private Background bg;
    private HashMap<Integer,AudioPlayer> bgmusic;
    private Player player;

    private ArrayList<Platform> platforms;


    //let player go to next column
    private boolean success;
    private boolean settingup;
    private boolean game_over;
    private boolean paused;



    //class for experience handle
    private int experience;


    //score
    public static int score;
    //speed
    private double currentspeed;
    private final double STARTSPEED = 4*GamePanel.SCALE;

    //change speed
    private final int changeSpeed = (int)(50/GamePanel.SCALE);
    //position of landing
   private double acceleration = 0.0006*GamePanel.SCALE;
    //platform movement speed
    private double platformSpeed = 6*GamePanel.SCALE;
    //platform safezone
    private int platformSafezone = (int)(200*GamePanel.SCALE);
    //Pause
    private int currentChoice = 0;
    private String[] options = {
            "Continue",
            "Return to menu",
            "Quit"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;

    //random just for usage in many places
    private Random random;


    public Level1State(GameStateManager gsm){

        this.gsm = gsm;


        try {
            bg = new Background("/Backgrounds/level1bg.png");

        }catch (Exception e){
            e.printStackTrace();
        }
        bgmusic = new HashMap<Integer,AudioPlayer>();
        bgmusic.put(OptionsState.DEFAULT,new AudioPlayer("/Music/level1-1.mp3"));
        bgmusic.put(OptionsState.PAPAJ,new AudioPlayer("/Music/level1music.mp3"));
        bgmusic.put(OptionsState.STONOG,new AudioPlayer("/Music/stonogmusic.mp3"));

       // init();

    }
    @Override
    public void init() {
        game_over = false;
        settingup = false;
        paused = false;
        score = 0;
        currentspeed = STARTSPEED;
        bgmusic.get(OptionsState.choosenCharacter).play();
        //Rog
        experience = ExperienceManager.getExperience();
        //Rog

        player = new Player(OptionsState.choosenCharacter);
        player.setPosition(0, GamePanel.HEIGHT / 2 - Player.pHEIGHT);
        player.setVector(currentspeed, 0);
        //platforms
        if(platforms == null){
            platforms = new ArrayList<Platform>();
        }
        if(platforms.size() != 2){
            //platforma startowa, intex 0
            platforms.add(new Platform());
            //platforma którą poruszamy, index 1
            platforms.add(new Platform());


        }
        platforms.get(0).setPosition(0, GamePanel.HEIGHT / 2);
        random = new Random();
        platforms.get(1).setPosition(GamePanel.WIDTH - (Platform.pWIDTH + 330*GamePanel.SCALE), random.nextInt(GamePanel.HEIGHT-(int)Platform.pHEIGHT));
        for(Platform i: platforms){
            i.setVector(0,0);
        }
        }

    @Override
    public void update() {
        if(game_over){
            //Kolybacz HIGHSCORES
            HighScoreState.checkAndAddHighScore(score);
            ExperienceManager.saveExperience(score);
            //Rog
            //Kolybaczxjzcoidsaf END
            bgmusic.get(OptionsState.choosenCharacter).stop();
            gsm.setState(GameStateManager.GAMEOVERSTATE);
        } else if(paused){

        }else if(settingup){
            player.update();
            bg.update();
            for (Platform i : platforms) {
                i.update();
            }

            collision();
            if(platforms.get(0).getMinX() < 0 && platforms.get(1).getMaxX() < GamePanel.WIDTH-330*GamePanel.SCALE){
                settingup = false;
                platforms.get(0).setVector(0,0);
                platforms.get(1).setDX(0);
                player.setVector(currentspeed,0);

                bg.setVector(0,0);
            }
        }else {
            //bg.update();
            bgmusic.get(OptionsState.choosenCharacter).play();
            player.update();
            currentspeed += acceleration;
            player.setSpeed(currentspeed);
            if (platforms.size() == 2 )
                for (Platform i : platforms) {
                    i.update();
                }
            //check for gameover
            if (player.getY() + Player.pHEIGHT > GamePanel.HEIGHT) {
                game_over = true;
            }
            //chceck for collision
            collision();

            //check fo jumpzone
            if (Math.round(player.getX()) > Platform.pWIDTH - 60*GamePanel.SCALE && Math.round(player.getX()) < Platform.pWIDTH - 50*GamePanel.SCALE) {
                player.setJumping(true);
                player.setInAir(true);
                platforms.get(0).setDX(-currentspeed/3);


            }

            //success operations
            if(platforms.size()==2) {
                if (success && Math.round(player.getX()) > platforms.get(1).getMinX()) {
                    Platform start = platforms.remove(0);
                    int rand = (random.nextInt((GamePanel.HEIGHT-platformSafezone - platformSafezone))+platformSafezone);

                    platforms.get(0).setVector((0 - platforms.get(0).getMinX()) / changeSpeed,
                            (rand -platforms.get(0).getMinY()) / changeSpeed);
                    player.setVector((0 - platforms.get(0).getMinX()) / changeSpeed,
                            (rand -platforms.get(0).getMinY()) / changeSpeed);
                    start.reload(rand,currentspeed);
                    platforms.add(start);
                    platforms.get(1).setVector((0 - platforms.get(0).getMinX()) / changeSpeed, 0);
                    bg.setVector((0 - platforms.get(0).getMinX()) / (changeSpeed*3),0);
                    score++;
                    success = false;
                    settingup = true;
                }
            }

        }
    }


    public void collision() {
        if (platforms != null) {
            if (platforms.size() == 2) {
                for (Platform i : platforms) {
                    Line2D.Double tempMaxYLine = new Line2D.Double(i.getMinX(), i.getMinY(), i.getMaxX(), i.getMinY());
                    Line2D.Double tempMinYLine = new Line2D.Double(i.getMinX(), (i.getMinY()+40) , i.getMaxX(), (i.getMinY()+40) );
                    if (tempMaxYLine.intersects(player.getX(), player.getY(), Player.pWIDTH, Player.pHEIGHT) && !(tempMinYLine.intersects(player.getX(), player.getY(), Player.pWIDTH, Player.pHEIGHT))) {
                        player.setInAir(false);

                        player.setY(i.getMinY() - Player.pHEIGHT);
                        if (i == platforms.get(1)) {
                            success = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);



        g.setColor(new Color(0xE4EA74));
        g.drawString("Experience: "+Integer.toString(score+experience),10,35);
        g.drawString("Score: "+Integer.toString(score),GamePanel.WIDTH-200,35);
        g.drawString("Best: "+Integer.toString(HighScoreState.highest),GamePanel.WIDTH-200,70);

        if(platforms.size() != 0) {
            for (Platform i : platforms) {
                i.draw(g);
            }
        }
        player.draw(g);
        if(paused){
            g.setFont(font);
            for(int i = 0; i < options.length; i++) {
                if(i == currentChoice) {
                    g.setColor(new Color(214, 156, 5));
                }
                else {
                    g.setColor(Color.BLACK);
                }
                drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT-300  + i * 100,g);
            }
        }


    }

    @Override
    public void keyPressed(int k) {
        if(paused) {
            if(k == KeyEvent.VK_ENTER){
                select();
            }
            if(k == KeyEvent.VK_UP) {
                currentChoice--;
                if(currentChoice == -1) {
                    currentChoice = options.length - 1;
                }
            }
            if(k == KeyEvent.VK_DOWN) {
                currentChoice++;
                if(currentChoice == options.length) {
                    currentChoice = 0;
                }
            }
        }
        if(platforms!=null) {
            if (platforms.size()==2) {
                if (k == KeyEvent.VK_W) platforms.get(1).setDY(-platformSpeed);
                if (k == KeyEvent.VK_S) platforms.get(1).setDY(platformSpeed);
                if (k == KeyEvent.VK_UP) platforms.get(1).setDY(-platformSpeed);
                if (k == KeyEvent.VK_DOWN) platforms.get(1).setDY(platformSpeed);
            }
        }
    }

    @Override
    public void keyReleased(int k) {
        if(platforms!=null) {
            if (platforms.size() == 2) {
                if (k == KeyEvent.VK_W) platforms.get(1).setDY(0);
                if (k == KeyEvent.VK_S) platforms.get(1).setDY(0);
                if (k == KeyEvent.VK_UP) platforms.get(1).setDY(0);
                if (k == KeyEvent.VK_DOWN) platforms.get(1).setDY(0);
            }
        }
        if(k == KeyEvent.VK_P){
            if(!paused){
                paused=true;
            }else {
                paused=false;
            }
        }
        if(k == KeyEvent.VK_ESCAPE){
            //game_over = false;
            bgmusic.get(OptionsState.choosenCharacter).stop();
            gsm.setState(GameStateManager.MENUSTATE);


        }
    }


    private void select() {
        if(currentChoice == 0) {
            paused = false;
        }
        if(currentChoice == 1) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
        if(currentChoice == 2) {
            System.exit(0);
        }
    }
    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

}