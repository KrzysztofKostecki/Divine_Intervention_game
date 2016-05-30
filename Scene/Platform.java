package Scene;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.Random;

/**
 * Klasa odpowiadająca za wszystkie operacje wykonywane na platformach. Wyświetla odpowiednie sprite'y platform w zależności od tego,
 * czy platforma jest w ruchu czy nie. Obsługuje zmianę położenia platformy gdy ta jest przesuwana przez gracza.
 * <p>Posiada settery i gettery do ustawiania i odczytywania położenia platform.</p>
 */
public class Platform{

    private BufferedImage image;
    private BufferedImage image2;
    private BufferedImage subimage;
    private double minX;

    private double minY;

    /**
     * Zwraca minimalną składową położenia Y.
     * @return Składową Y.
     */
    public double getMinY(){
        return minY;
    }
    /**
     * Zwraca minimalną składową położenia X.
     * @return Składową X.
     */
    public double getMinX(){
        return minX;
    }
    /**
     * Zwraca maksymalną składową położenia X.
     * @return Składową X.
     */
    public double getMaxX(){
        return minX+pWIDTH;
    }
    /**
     * Zwraca maksymalną składową położenia Y.
     * @return Składową Y.
     */
    public double getMaxY() {
        return minY+pHEIGHT;
    }


    private double dx;

    /**
     * Zwraca wartość dx, jest to wartość o jaką jest przesuwana platforma.
     * @return dx
     */
    public double getDX() {
        return dx;
    }
    private double dy;
    /**
     * Zwraca wartość dy, jest to wartość o jaką jest przesuwana platforma.
     * @return dy
     */
    public double getDY() {
        return dy;
    }


    /**
     * Szerokość platformy.
     */
    public static double pWIDTH = (int)(165* GamePanel.SCALE);
    /**
     * Wysokość platformy.
     */
    public static double pHEIGHT = (int)(60*GamePanel.SCALE);




    //for game

    /**
     * Wczytuje sprite'y dla platformy. Jeden sprite wyświetlany jest gdy platforma jest poruszana, drugi gdy jest w spoczynku
     */
    public Platform(){

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Scene/platformsprite3.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/Scene/platformsprite4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Random random = new Random();
        minX = GamePanel.WIDTH;
        minY = random.nextInt(GamePanel.HEIGHT-(int)pHEIGHT);
    }

    /**
     * Oblicza położenie nowej platformy. Nowe położenie musi być takie, że realne jest przesunięcie platformy na odpowiednią
     * pozycję zanim zakończy się skok.
     * @param startHeight Położenie obecne, przed zmianą.
     * @param speed Obecna prędkość postaci.
     */
    public void reload(int startHeight, double speed){
        Random random = new Random();
        minX = GamePanel.WIDTH;
        int delta = (int)(2000/speed);
        if(delta>360)delta=200;
        //obliczanie położenia Y nowej platformy tak aby była max delta w górę lub max delta w dół od aktualnego położenia gracza.
        minY = random.nextInt((startHeight+delta) - (startHeight-delta))+(startHeight-delta);
    }

    //Settery

    /**
     * Ustawia pozycje platformy.
     * @param x Składowa x.
     * @param y Składowa y.
     */
    public void setPosition(double x, double y){
        this.minX = x;
        this.minY = y;
    }

    /**
     * Ustawia wektor o jaki przesuwana jest platforma.
     * @param dx Składowa x.
     * @param dy Składowa y.
     */
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Ustawia pojedynczą składową o którą przesuwana  platforma.
     * @param dx Przesunięcie dx.
     */
    public void setDX(double dx){
        this.dx = dx;
    }
    /**
     * Ustawia pojedynczą składową o którą przesuwana  platforma.
     * @param dy Przesunięcie dy.
     */
    public void setDY(double dy){
        this.dy = dy;
    }

    /**
     * Zmienia położenia platformy gdy ta jest poruszana przez gracza.
     */
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

    /**
     * Rysuje sprite'a platformy gdy ta jest w spoczynku.
     * @param g Obiekt na którym rysujemy.
     *          @see Graphics2D#drawImage(Image, int, int, ImageObserver)
     */
    public void draw(Graphics2D g) {
        g.drawImage(image,(int) minX, (int) minY, null);
    }

    /**
     * Rysuje sprite'a platformy gdy ta jest przesuwana przez gracza.
     * @param g Obiekt na którym rysujemy.
     *          @see Graphics2D#drawImage(Image, int, int, ImageObserver)
     */
    public void drawCurrent(Graphics2D g) {
        g.drawImage(image2,(int) minX, (int) minY, null);
    }

    public boolean intersects(double x, double y, double w, double h){
        return y + h > minY && x+w > minX && x < minX + pWIDTH && y < minY+pHEIGHT;
    }
}
