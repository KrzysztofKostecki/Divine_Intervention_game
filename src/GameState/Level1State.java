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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Klasa obsługująca wszystkie wydarzenia które mają miejsce podczas rozgrywki. Uaktualnia stan gry, tzn. położenie postaci,
 * platform, zwiększa prędkość postaci. Przechwytuje sygnały z klawiatury
 */
public class Level1State extends GameState {
    private Background bg;
    private Background foreGround;
    private GraphicsEnvironment ge;
    private HashMap<Integer,AudioPlayer> bgmusic;
    private Player player;

    private ArrayList<Platform> platforms;


    //let player go to next column
    private boolean success;
    private boolean settingup;
    private boolean game_over;
    private boolean paused;

    private boolean isJumping;



    //class for experience handle
    private int experience;


    //score
    static int score;
    //speed
    private double currentspeed;
    private final double STARTSPEED = 6*GamePanel.SCALE;


    /**
     * Zmiana prędkości
     */
    private final int changeSpeed = (int)(50/GamePanel.SCALE);

    /**
     * Przyspieszenie.
     */
    private double acceleration = 0.0006*GamePanel.SCALE;
    /**
     * Prędkość platform.
     */
    private double platformSpeed = 6*GamePanel.SCALE;
    /**
     * Szerokość obszaru platformy na którym może stanąć postać.
     */
    private int platformSafezone = (int)(200*GamePanel.SCALE);
    //Pause
    private int currentChoice = 0;
    /**
     * Tablica przechowująca opcje dostępne w menu pauzy.
     */
    private String[] options = {
            "Continue",
            "Return to menu",
            "Quit"
    };

    private Font font;

    //random just for usage in many places
    private Random random;

    private int MOVEABLEPLATFORM = 1;
    /**
     * Ustawia background oraz ścieżkę dźwiękową.
     * @param gsm Obiekt stanu gry na któym działamy.
     */
    public Level1State(GameStateManager gsm){

        this.gsm = gsm;


        try {
            bg = new Background("/Backgrounds/level1bg.png");
            foreGround = new Background("/Backgrounds/pause.png");
            URL fontUrl = getClass().getResource("/Fonts/Abel-Regular.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
            font = font.deriveFont(Font.PLAIN,30);

            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        }catch (Exception e){
            e.printStackTrace();
        }
        bgmusic = new HashMap<Integer,AudioPlayer>();
        bgmusic.put(OptionsState.DEFAULT,new AudioPlayer("/Music/level1-1.mp3"));
        bgmusic.put(OptionsState.RED,new AudioPlayer("/Music/level1-1.mp3"));
        bgmusic.put(OptionsState.BLACK,new AudioPlayer("/Music/level1-1.mp3"));


    }
    @Override
    /**
     *  Tworzy i określa położenie obiektów: postaci oraz początkowych platform.
     *  <p>
     *      Tworzy wybraną wcześniej przez gracza postać i umieszcza ją na mapie.
     *  </p>
     *  <p>
     *      Tworzy trzy początkowe platformy. Położenie pierwszej platformy jest zawsze takie samo (0,HEIGhT/2), natomiast położenie
     *      drugiej i trzeciej platformy jest losowe.
     *  </p>
     */
    public void init() {
        isJumping = true;
        game_over = false;
        settingup = false;
        paused = false;
        score = 0;
        currentspeed = STARTSPEED;
        bgmusic.get(OptionsState.choosenCharacter).play();
        experience = ExperienceManager.getExperience();

        player = new Player(OptionsState.choosenCharacter);
        player.setPosition(0, (GamePanel.HEIGHT / 2 - Player.pHEIGHT) + 10);
        player.setVector(currentspeed, 0);
        //platforms
        if(platforms == null){
            platforms = new ArrayList<Platform>();
        }
        if(platforms.size() != 3){
            //platforma startowa, index 0
            platforms.add(new Platform());
            //platforma którą poruszamy, index 1
            platforms.add(new Platform());
            //platforma po prawej od obszaru gry, index 2
            platforms.add(new Platform());
        }
        platforms.get(0).setPosition(0, GamePanel.HEIGHT / 2);
        random = new Random();
        platforms.get(1).setPosition(GamePanel.WIDTH - (Platform.pWIDTH + 425*GamePanel.SCALE),GamePanel.HEIGHT);
        platforms.get(2).reload((int)platforms.get(0).getMinY(),currentspeed);
        for(Platform i: platforms){i.setVector(0,0);}
        }

    @Override
    /**
     * Aktualizuje stan gry.
     * <p> Jeśli rozgrywka została zakończona to zapisuje uzyskany wynik i wyświetla menu dostępne po przegranej grze.</p>
     * <p> Jeśli rozgrywka została zapauzowana to wyświetla dostępne opcje menu.</p>
     * <p> Jeśli rozgrywka przebiega normalnie, to zwiększana jest prędkość postaci, aktualizowane jest położenie platform,
     * sprawdzane są warunki kolizji. Sprawdzane jest położenie postaci (w powietrzu czy na platformie). Jeśli nie doszło do kolizji,
     * to usuwane są te platformy, które zostały już przeskoczone, a generowane są nowe.</p>
     */
    public void update() {
        if(game_over){
            ExperienceManager.saveExperience(score);
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

            if(platforms.get(0).getMinX() < 100 ){
                platforms.get(0).setVector(0,0);
            }
            if(platforms.get(1).getMaxX() < GamePanel.WIDTH-425*GamePanel.SCALE){
                settingup = false;
                platforms.get(2).setPosition(GamePanel.WIDTH,360);

                platforms.get(0).setVector(0,0);
                platforms.get(1).setDX(0);
                platforms.get(2).setVector(0,0);

                player.setVector(currentspeed,0);
                bg.setVector(0,0);
            }
        }else {
            //bg.update();
            bgmusic.get(OptionsState.choosenCharacter).play();
            player.update();
            currentspeed += acceleration;
            player.setSpeed(currentspeed);
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
            if (Math.round(player.getX()) > platforms.get(0).getMaxX()-20 && isJumping) {
                player.setInAir(true);
                player.setJumping(true);
                player.update();
                isJumping = false;

            }

            //success operations
            if(platforms.size()==3) {
                if (success && Math.round(player.getX()) > platforms.get(1).getMinX()+10) {
                    Platform start = platforms.remove(0);
                    platforms.add(start);
                    int rand = (random.nextInt((GamePanel.HEIGHT-platformSafezone - platformSafezone))+platformSafezone);

                    platforms.get(0).setVector((100 - platforms.get(0).getMinX()) / changeSpeed,
                            (rand -platforms.get(0).getMinY()) / changeSpeed);
                    platforms.get(1).reload(rand,currentspeed);

                    platforms.get(1).setVector((100 - platforms.get(0).getMinX()) / changeSpeed,  0);
                    platforms.get(2).setVector((100 - platforms.get(0).getMinX()) / changeSpeed, (rand -platforms.get(0).getMinY()) / changeSpeed);

                    player.setVector((100 - platforms.get(0).getMinX()) / changeSpeed, (rand -platforms.get(0).getMinY()) / changeSpeed);
                    bg.setVector(-currentspeed/2,0);
                    score++;
                    success = false;
                    settingup = true;
                    isJumping = true;
                }
            }

        }
    }

    /**
     * Sprawdza czy doszło do kolizji między postacią a platformą.
     */
    public void collision() {
        if (platforms != null) {
            if (platforms.size() == 3) {
                for (Platform i : platforms) {
                    Line2D.Double tempMaxYLine = new Line2D.Double(i.getMinX(), i.getMinY()+15, i.getMaxX(), i.getMinY()+15);
                    Line2D.Double tempMinYLine = new Line2D.Double(i.getMinX(), (i.getMinY()+60) , i.getMaxX(), (i.getMinY()+60) );
                    if (tempMaxYLine.intersects(player.getX(), player.getY(), Player.pWIDTH, Player.pHEIGHT) && !(tempMinYLine.intersects(player.getX(), player.getY(), Player.pWIDTH, Player.pHEIGHT))) {
                        player.setInAir(false);

                        player.setY((i.getMinY()+15) - Player.pHEIGHT);
                        if (i == platforms.get(1)) {
                                success = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    /**
     * Rysuje postać, platformy, wyświetla Experience, Score oraz Best (najlepszy wynik z highscore).
     * <p>Gdy gra jest zapauzowana rysuje tło oraz opcje dostępne w menu pauzy.</p>
     * @param g Pole na którym rysujemy.
     */
    public void draw(Graphics2D g) {
        bg.draw(g);

        g.setColor(new Color(0x0C1D20));
        g.drawString("Experience: "+Integer.toString(score+experience),10,35);
        g.drawString("Score: "+Integer.toString(score),GamePanel.WIDTH-200,35);
        g.drawString("Best: "+Integer.toString(HighScoreState.highest),GamePanel.WIDTH-200,70);

        if(platforms.size() != 0) {
            int count = 0;
            for (Platform i : platforms) {
                if(count == 1){
                   i.drawCurrent(g);
                }else{
                    i.draw(g);
                }
                //g.drawString(Integer.toString(count),(int)i.getMinX(),(int)i.getMinY());
                count++;
            }
        }
        player.draw(g);
        if(paused){
            foreGround.draw(g);
            g.setFont(font);
            for(int i = 0; i < options.length; i++) {
                if(i == currentChoice) {
                    g.setColor(Color.WHITE);
                }
                else {
                    g.setColor(new Color(6, 32, 29));
                }
                drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT-100  + i * 100,g);
            }
        }


    }

    @Override
    /**
     * Przechwytuje sygnał o tym, który klawisz został wciśnięty przez użytkownika.
     * <p>Obsługuje ruch platform.
     * <ul>
     *     <li>W lub UP- ruch platformy w górę</li>
     *     <li>S lub DOWN- ruch platformy w dół</li>
     * </ul></p>
     * <p>Jeśli gra jest zapauzowana to przechwytuje też sygnały z menu pauzy.</p>
     * @param k Wciśnięty klawisz.
     */
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
            if (platforms.size()==3) {
                if (k == KeyEvent.VK_W) platforms.get(MOVEABLEPLATFORM).setDY(-platformSpeed);
                if (k == KeyEvent.VK_S) platforms.get(MOVEABLEPLATFORM).setDY(platformSpeed);
                if (k == KeyEvent.VK_UP) platforms.get(MOVEABLEPLATFORM).setDY(-platformSpeed);
                if (k == KeyEvent.VK_DOWN) platforms.get(MOVEABLEPLATFORM).setDY(platformSpeed);
            }
        }
    }

    @Override
    /**
     * Przechwytuje sygnał wysyłany po zwolnieniu klawisza. Platforma przestaje się poruszać gdy
     * klawisz zostaje puszczony.
     * <p>
     *     <ul>
     *         <li>P powoduje ustawienie flagi pause na true.</li>
     *         <li>ESCAPE powoduje ustawienie stanu gry na MENUSTATE i powrót do menu głównego gry.</li>
     *     </ul>
     * </p>
     * @param k Wciśnięty klawisz.
     */
    public void keyReleased(int k) {
        if(platforms!=null) {
            if (platforms.size() == 3) {
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

    /**
     * W zależności od wyboru ustawia flagę paused na wartość false, ustawia stan gry na
     * MENUSTATE bądź kończy działanie gry.
     */
    private void select() {
        if(currentChoice == 0) {
            paused = false;
        }
        if(currentChoice == 1) {
            bgmusic.get(OptionsState.choosenCharacter).stop();
            gsm.setState(GameStateManager.MENUSTATE);
        }
        if(currentChoice == 2) {
            bgmusic.get(OptionsState.choosenCharacter).stop();
            System.exit(0);
        }
    }

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