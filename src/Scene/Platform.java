package Scene;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

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
    public double getDX() {
        return dx;
    }
    private double dy;
    public double getDY() {
        return dy;
    }




    public static double pWIDTH = (int)(165* GamePanel.SCALE);
    public static double pHEIGHT = (int)(60*GamePanel.SCALE);




    //for game

    public Platform(){

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Scene/platformsprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Random random = new Random();
        minX = GamePanel.WIDTH;
        minY = random.nextInt(GamePanel.HEIGHT-(int)pHEIGHT);
    }

    public void reload(int startHeight, double speed){
        Random random = new Random();
        minX = GamePanel.WIDTH;
        int delta = (int)(2000/speed);
        if(delta>360)delta=360;
        //obliczanie położenia Y nowej platformy tak aby była max delta w górę lub max delta w dół od aktualnego położenia gracza.
        minY = random.nextInt((startHeight+delta) - (startHeight-delta))+(startHeight-delta);
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
        g.drawImage(image,(int) minX, (int) minY, null);
    }

    public boolean intersects(double x, double y, double w, double h){
        return y + h > minY && x+w > minX && x < minX + pWIDTH && y < minY+pHEIGHT;
    }
}
