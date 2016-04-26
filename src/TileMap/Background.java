package TileMap;

import Main.GamePanel;
import Utils.Scaler;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Created by Krzysztof on 05.04.2016.
 */
public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;

	
	public Background(String s) {
		
		try {
			image = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
			Scaler sw = new Scaler(image,GamePanel.WIDTH,GamePanel.HEIGHT);
			image = sw.getScalledImage();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}


	
	public void update() {
		x += dx;
		y += dy;
	}
	
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







