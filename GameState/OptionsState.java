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
 * Klasa obsługująca menu opcji gry. Wyświetla grafikę w menu opcji, dostępne w niej postacie.
 * <p>Zawiera metody pozwalające na wybór postaci przez gracza. W tym celu przechwytuje sygnały z klawiatury.</p>
 */
public class OptionsState  extends GameState{

    private Background bg;

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
    public final static int PAPAJ = 1;
    /**
     * Wartość przypisana do postaci "Stonog".
     */
    public final static int STONOG = 2;
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

    /**
     * Ustawia początkową pozycję postaci.
     */
    public void init(){

        player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);
        currentCharacter = choosenCharacter;
        //if(ExperienceManager.getExperience() > 2137){
        //    characters = 2;
        // xDDD 2137 hehehe
        //}
    }

    /**
     * Aktualizuje stan gry.
     */
    public void update(){
        bg.update();
        player.update();
        player.setVector(0,0);
        player.setPosition(GamePanel.WIDTH/2 - Player.pWIDTH/2,200);

    }

    /**
     * Rysuje tło, dostępne opcje oraz grafikę postaci.
     * @param g Obiekt na którym rysujemy.
     */
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
