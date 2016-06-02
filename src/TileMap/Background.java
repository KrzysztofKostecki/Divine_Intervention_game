package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Klasa służąca do obsługi grafiki, jej wyświetlania i zmiany pozycji.
 */
public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;

	/**
	 * Wczytuje do {@link #image} grafikę.
	 * @param s Ścieżka do grafiki.
     */
	public Background(String s) {
		
		try {
			image = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Ustawia wektor o jaki przesuwana jest grafika.
	 * @param dx Składowa x.
	 * @param dy Składowa y.
     */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}


	/**
	 * Aktualizuje położenie grafiki.
	 */
	public void update() {
		x += dx;
		y += dy;
	}

	/**
	 * Rysuje background.
	 * @param g Obiekt na którym rysujemy.
     */
	public void draw(Graphics2D g) {

		g.drawImage(image, (int)x, (int)y, null);
		
		if(x < 0) {
			x = GamePanel.WIDTH;
			g.drawImage(
				image,
				(int)x + GamePanel.WIDTH,
				(int)y,
				null
			);
		}
		if(x > 0) {
			g.drawImage(
				image,
				(int)x - GamePanel.WIDTH,
				(int)y,
				null
			);
		}
	}


}







