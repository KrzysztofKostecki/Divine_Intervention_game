package Scene;

import Main.Game;
import Main.GamePanel;
import javafx.geometry.Rectangle2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Krzysztof on 21.04.2016.
 */
public class Platform{

    private BufferedImage image;
    private BufferedImage subimage;
    private double minX;

    private double minY;

    public double getMinY(){
        return minY;
    }
    public double getMinX(){
        return minX;
    }
    public double getMaxX(){
        return minX+pWIDTH;
    }
    public double getMaxY() {
        return minY+pHEIGHT;
    }
    private double dx;
    private double dy;




    public static double pWIDTH = 165;
    public static double pHEIGHT = 60;




    //for game

    public Platform(){

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Scene/platformsprite3.png"));
            subimage = image.getSubimage(0,0,
                    (int)pWIDTH,
                    (int)pHEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Random random = new Random();
        minX = GamePanel.WIDTH;
        minY = random.nextInt(GamePanel.HEIGHT-(int)pHEIGHT);
    }

    public void reload(){
        Random random = new Random();
        minX = GamePanel.WIDTH;
        minY = random.nextInt(GamePanel.HEIGHT-(int)pHEIGHT);
    }

    public void setPosition(double x, double y){
        this.minX = x;
        this.minY = y;
    }


    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void setDX(double dx){
        this.dx = dx;
    }
    public void setDY(double dy){
        this.dy = dy;
    }

    public void update() {
        minX += dx;
        minY += dy;
        if(minY < 0){
            minY = 0;
        }
        if(minY + pHEIGHT > GamePanel.HEIGHT){
            minY = GamePanel.HEIGHT - pHEIGHT;
        }
    }

    public void draw(Graphics2D g) {

        g.setColor(new Color(0x35BB15));
        g.drawImage(image,(int) minX, (int) minY, null);
    }

    public boolean intersects(double x, double y, double w, double h){
        return y + h > minY && x+w > minX && x < minX + pWIDTH && y < minY+pHEIGHT;
    }
}
