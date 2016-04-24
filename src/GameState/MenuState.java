package GameState;

import Main.Game;
import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
/**
 * Created by Krzysztof on 05.04.2016.
 */
public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
			"Start",
			"Options",
			// Kolybacz HighScore
			"High Score",
			"Credits",
			// Kolybacz
			"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/level1bg.png");

			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.BOLD,
					70);
			font = new Font("Century Gothic", Font.PLAIN, 30);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Divine Intervention", 150, GamePanel.HEIGHT/2-200);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(new Color(214, 156, 5));
			}
			else {
				g.setColor(Color.BLACK);
			}
			drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT-300  + i * 100,g);
		}
		
	}

	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.OPTIONSSTATE);
		}
		// Kolybacz HighScore
		if(currentChoice == 2) {
			//high score
			gsm.setState(GameStateManager.HIGHSCORESTATE);
		}
		if (currentChoice == 3)
		{
			gsm.setState(GameStateManager.CREDITSSTATE);

		}
		if (currentChoice == 4)
		{
			System.exit(0);

		}
		// Kolybacz
	}
	
	public void keyPressed(int k) {
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
	public void keyReleased(int k) {}

	public static void drawCenteredString(String s, int w, int h, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		g.drawString(s, x, y);
	}
	
}










