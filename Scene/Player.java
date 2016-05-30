package Scene;

import Audio.AudioPlayer;
import GameState.OptionsState;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klasa obsługująca wszystkie wydarzenia związane z postacią i jej ruchem.
 * <p>Zawiera metody ustawiające odpowiednie sprite'y podczas ruchu postaci,
 * aktualizujące jej położenie. </p>
 *
 * @see AudioPlayer
 * @see Animation
 */
public class Player {

    private BufferedImage image;

    /**
     * Składowa x położenia postaci.
     */
    protected double x;

    /**
     * Zwraca składową x położenia postaci/
     * @return Składowa x.
     */
    public double getX(){
        return x;
    }
    /**
     * Składowa y położenia postaci.
     */
    protected double y;
    /**
     * Zwraca składową y położenia postaci/
     * @return Składowa y.
     */
    public double getY(){
        return y;
    }

    /**
     * Składowa dx wektora, o który przesuwana jest postać.
     */
    protected double dx;
    /**
     * Zwraca wartość dx, jest to wartość o jaką jest przesuwana postać.
     * @return dx Wartość dx.
     */
    public double getDX(){
        return dx;
    }

    /**
     * Ustawia składową x o jaką przesuwana jest postać.
     * @param dx Składowa x.
     */
    public void setDX(double dx){
        this.dx = dx;
    }
    /**
     * Składowa dy wektora, o który przesuwana jest postać.
     */
    protected double dy;
    /**
     * Ustawia składową y o jaką przesuwana jest postać.
     */
    public void setDY(double dy){
        this.dy = dy;
    }

    private double delay = 20;

    private double gravity = 0.3;
    private double jumpForce = 12;

    private boolean JUMPING;
    /**
     * Flaga określająca czy postać jest aktualnie w powietrzu.
     */
    public boolean INAIR;

    //private boolean JUMPING;
    /**
     * Szerokość postaci.
     */
    public static double pWIDTH = 51*GamePanel.SCALE;
    /**
     * Wysokość postaci.
     */
    public static double pHEIGHT = 72*GamePanel.SCALE;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            8, 1, 1
    };

    // animation actions
    private static final int WALK = 0;
    private static final int JUMP = 1;
    private static final int FALL = 2;

    private int currentAction;

    private HashMap<Integer, AudioPlayer> sfx;

    protected Animation animation;

    /**
     * Wczytuje pocztkowy sprite postaci oraz dźwięk odtwarzany podczas skoku.
     * @see AudioPlayer
     * @see Animation
     * @param character Wybrana postać.
     */
    public Player(int character){
        // load sprites
        setCharacter(character);
        sfx = new HashMap<Integer, AudioPlayer>();
        sfx.put(JUMP,new AudioPlayer("/SFX/jump.mp3"));
        animation = new Animation();
        currentAction = WALK;
        animation.setFrames(sprites.get(WALK));
        animation.setDelay(80);
    }

    /**
     * Wczytuje odpowiedniego sprite'a dla postaci.
     * <p>Odpowiedni sprite postaci zostaje wczytany w zależności od tego, którą postać w opcjach wybrał gracz.</p>
     * @param character Wybrana postać.
     */
    public void setCharacter(int character){
        try {
            //just deafult if character is wrong
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/spritesheet_small.png"
                    )
            );
            if(character == OptionsState.DEFAULT) {
                spritesheet = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Sprites/Player/spritesheet_small.png"
                        )
                );
            }else if (character == OptionsState.PAPAJ){
                spritesheet = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Sprites/Player/spritesheet1.png"
                        )
                );
            }else if (character == OptionsState.STONOG){
                spritesheet = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Sprites/Player/spritesheetstonog.png"
                        )
                );
            }

            sprites = new ArrayList<BufferedImage[]>();

            for(int i = 0; i < numFrames.length; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];


                for(int j = 0; j < numFrames[i]; j++) {
                    bi[j] = spritesheet.getSubimage(
                            j * (int)(pWIDTH/GamePanel.SCALE),
                            i * (int)(pHEIGHT/GamePanel.SCALE),
                            (int)(pWIDTH/GamePanel.SCALE),
                            (int)(pHEIGHT/GamePanel.SCALE)
                    );
                }

                sprites.add(bi);

            }



        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ustawia pozycje postaci.
     * @param x Składowa x pozycji.
     * @param y Składowa y pozycji.
     */
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Ustawia składową y pozycji postaci.
     * @param y Składowa y.
     */
    public void setY(double y){
        this.y = y;
    }

    /**
     * Ustawia prędkośc postaci.
     * @param dx Prędkość.
     */
    public void setSpeed(double dx){
        this.dx = dx;
    }

    /**
     * Ustawia wektor o jaki przesuwa się postać.
     * @param dx Składowa dx.
     * @param dy Składowa dy.
     */
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Ustawia flagę JUMPING. Odtwarza audio skoku.
     * @param isJump Flaga mówiąca o tym, czy postać skacze.
     */
    public void setJumping(boolean isJump){
        sfx.get(JUMP).play();
        this.JUMPING = isJump;
    }

    /**
     * Ustawia flagę INAIR.
     * @param air True jeśli skacze.
     */
    public void setInAir(boolean air){
        this.INAIR = air;
    }

    /**
     * Aktualizuje położenie postaci w zalezności od tego, czy postać idzie, spada, czy skacze. <p>
     *     Animuje też ruch postaci (zmienia sprite'y).
     * </p>
     * @see Animation
     */
    public void update() {
        dy += gravity;
        x += dx;
        y += dy;
        //System.out.println(onPlatform);
        if(JUMPING){
            dy = -jumpForce;
            JUMPING = false;
        }
        if(dy > jumpForce){
            dy = jumpForce;
        }
        if(!INAIR && currentAction!=WALK){
            currentAction = WALK;
            animation.setFrames(sprites.get(WALK));
        }else if(INAIR && currentAction!=JUMP && dy<0) {
            currentAction = JUMP;
            animation.setFrames(sprites.get(JUMP));
        }
        else if(INAIR && currentAction!=FALL && dy>0){
            currentAction = FALL;
            animation.setFrames(sprites.get(FALL));
        }

        animation.setDelay((long)((delay*Math.exp(-getDX()*0.7   ))+(2*delay)));
        //animation.setDelay(20);
        animation.update();
    }

    /**
     * Wyświetla sprite'a postaci.
     * @param g Obiekt na którym rysujemy.
     */
    public void draw(Graphics2D g) {
        g.setColor(new Color(0x000000));
        g.drawImage(animation.getImage(),(int)x, (int)y,null);
        //g.fillRect((int)x, (int)y,50,50);
    }

}
