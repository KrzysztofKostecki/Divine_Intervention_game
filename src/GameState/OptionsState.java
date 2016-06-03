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
import java.net.URL;
import java.util.HashMap;

/**
 * Klasa obsługująca menu opcji gry. Wyświetla grafikę w menu opcji, dostępne w niej postacie.
 * <p>Zawiera metody pozwalające na wybór postaci przez gracza. W tym celu przechwytuje sygnały z klawiatury.</p>
 */
public class OptionsState  extends GameState{

    private Background bg;
    private Background foreGround;
    private GraphicsEnvironment ge;

    /**
     * Obecnie wybrana przez gracza opcja.
     */
    private int currentChoice = 0;
    /**
     * Tablica przechowująca opcje dostępne w menu opcji.
     */
    private String[] options = {
            "Back"
    };

    /**
     * Kolor czcionki.
     */
    private Color titleColor;
    /**
     * Nazwa czcionki.
     */
    private Font titleFont;
    /**
     * Nazwa czcionki.
     */
    private Font font;

    //for choose character
    /**
     * Wybrana przez gracza postać.
     */
    public static int choosenCharacter;
    /**
     * Wartość przypisana do domyślnej postaci.
     */
    ////////////////////ZMIENIĆ W DOKUMENTACJI NAZWY POSTACI/////////////////////////
    public final static int DEFAULT = 0;
    /**
     * Wartość przypisana do postaci "Papaj".
     */
    public final static int RED = 1;
    /**
     * Wartość przypisana do postaci "Stonog".
     */
    public final static int BLACK = 2;
    ////////////////////ZMIENIĆ W DOKUMENTACJI NAZWY POSTACI/////////////////////////

    /**
     * Liczba dostepnych postaci.
     */
    //numbers of avaiable characters
    private Integer characters = 3;

    /**
     * Obecna postać.
     */
    private static int currentCharacter;

    /**
     * Obiekt reprezentujący postać w grze.
     */
    private PlayerMenu player;
    /**
     * Przechowuje ścieżki do mp3 odtwarzanych w menu opcji.
     */
    private HashMap<Integer, AudioPlayer> sfx;
    /**
     * Ustawia background oraz czcionkę w menu, dodaje do HashMapy ścieżki utworów.
     * @param gsm Obiekt stanu gry na którym działamy.
     */
    public OptionsState(GameStateManager gsm){

        this.gsm = gsm;

        try {

            bg = new Background("/Backgrounds/level1bg.png");
            bg.setVector(-0.5,0);
            foreGround = new Background("/Backgrounds/options.png");

            URL fontUrl = getClass().getResource("/Fonts/Abel-Regular.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
            font = font.deriveFont(Font.PLAIN,30);

            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            player = new PlayerMenu(DEFAULT);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        sfx = new HashMap<Integer, AudioPlayer>();

        sfx.put(DEFAULT, new AudioPlayer("/SFX/c.wav"));
        sfx.put(RED,new AudioPlayer("/SFX/f.wav"));
        sfx.put(BLACK,new AudioPlayer("/SFX/fsharp.wav"));

        choosenCharacter = 0;

    }

    /**
     * Ustawia początkową pozycję postaci.
     */
    public void init(){

        player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,300);
        currentCharacter = choosenCharacter;
    }

    /**
     * Aktualizuje stan gry.
     */
    public void update(){
        bg.update();
        player.update();
        player.setVector(0,0);
        player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,275);

    }

    /**
     * Rysuje tło, dostępne opcje oraz grafikę postaci.
     * @param g Obiekt na którym rysujemy.
     */
    public void draw(java.awt.Graphics2D g){

        // draw bg
        bg.draw(g);
        foreGround.draw(g);
        player.draw(g);
        g.setFont(font);
        g.setColor(Color.WHITE);
        drawCenteredString("<< >>",GamePanel.WIDTH,GamePanel.HEIGHT + 50,g);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.WHITE);
            }
            else {
                g.setColor(new Color(6, 32, 29));
            }
            drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT + 150,g);
        }

    }

    /**
     *
     */
    private void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    /**
     * Obsługuje wybór postaci.
     */
    private void selectCharacter(){
        if(currentCharacter == DEFAULT){
            sfx.get(RED).stop();
            sfx.get(BLACK).stop();
            sfx.get(DEFAULT).play();
            player = new PlayerMenu(DEFAULT);
            player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,275);
        }
        if(currentCharacter == RED){
            sfx.get(DEFAULT).stop();
            sfx.get(BLACK).stop();
            sfx.get(RED).play();
            player = new PlayerMenu(RED);
            player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,275);
        }
        if(currentCharacter == BLACK){
            sfx.get(DEFAULT).stop();
            sfx.get(RED).stop();
            sfx.get(BLACK).play();
            player = new PlayerMenu(BLACK);
            player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,275);
        }
    }
    /**
     * Przechwytuje sygnał o tym, który klawisz został wciśnięty przez użytkownika. Umożliwia nawigację po opcjach menu i wybór
     * postaci.
     *
     * <ul>
     *     <li>ENTER - ustawia choosenCharacter na aktualną postać.</li>
     *     <li>ESCAPE - ustawia stan gry na MENUSTATE (powrót do menu głównego).</li>
     * </ul>
     *
     * @param k Wciśnięty klawisz.
     */
    public void keyPressed(int k){
        if(k == KeyEvent.VK_ENTER){
            sfx.get(RED).stop();
            sfx.get(BLACK).stop();
            sfx.get(DEFAULT).stop();
            select();
            choosenCharacter = currentCharacter;
        }
        if(k == KeyEvent.VK_ESCAPE){
            sfx.get(RED).stop();
            sfx.get(BLACK).stop();
            sfx.get(DEFAULT).stop();
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    /**
     * Przechwytuje sygnał wysyłany po zwolnieniu klawisza. Umożliwia wybór postaci.
     *
     *     <ul>
     *         <li>Strzałka w prawo - kolejna postać</li>
     *         <li>Strzałka w lewo - poprzednia postać</li>
     *     </ul>
     *
     * @param k Wciśnięty klawisz.
     */
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
