package Scene;

import GameState.OptionsState;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Krzysztof on 21.04.2016.
 */
public class Player {

    private BufferedImage image;
    protected double x;
    public double getX(){
        return x;
    }
    protected double y;
    public double getY(){
        return y;
    }
    protected double dx;
    public double getDX(){
        return dx;
    }
    protected double dy;

    private double delay = 100;

    private double gravity = 0.3;
    private double jumpForce = 10;

    private boolean JUMPING;
    public boolean INAIR;

    //private boolean JUMPING;
    public static double pWIDTH = 70;
    public static double pHEIGHT = 99;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            8, 1
    };

    // animation actions
    private static final int WALK = 0;
    private static final int JUMP = 1;

    private int currentAction;

    protected Animation animation;


    public Player(int character){
        // load sprites
        setCharacter(character);
        animation = new Animation();
        currentAction = WALK;
        animation.setFrames(sprites.get(WALK));
        animation.setDelay(80);
    }

    public void setCharacter(int character){
        try {
            //just deafult if character is wrong
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/spritesheet.png"
                    )
            );
            if(character == OptionsState.DEFAULT) {
                spritesheet = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Sprites/Player/spritesheet.png"
                        )
                );
            }else if (character == OptionsState.PAPAJ){
                spritesheet = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Sprites/Player/spritesheet1.png"
                        )
                );
            }

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 2; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {
                    bi[j] = spritesheet.getSubimage(
                            j * (int)pWIDTH,
                            i * (int)pHEIGHT,
                            (int)pWIDTH,
                            (int)pHEIGHT
                    );

                }

                sprites.add(bi);

            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setSpeed(double dx){
        this.dx = dx;
    }


    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void setJumping(boolean isJump){
        this.JUMPING = isJump;
    }

    public void setInAir(boolean air){
        this.INAIR = air;
    }

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
        }else if(INAIR && currentAction!=JUMP) {
            currentAction = JUMP;
            animation.setFrames(sprites.get(JUMP));
        }
        animation.setDelay((long)((delay*Math.exp(-getDX()*0.7   ))+90));
        animation.update();
    }


    public void draw(Graphics2D g) {
        g.setColor(new Color(0x000000));
        g.drawImage(animation.getImage(),(int)x, (int)y,null);
        //g.fillRect((int)x, (int)y,50,50);
    }

}
