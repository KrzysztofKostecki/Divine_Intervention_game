package GameState;

import Main.Game;
import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * Klasa obsługująca główne menu gry.
 * <p>Zawiera tablice z wszystkimi opcjami w menu. Zawiera metody do wypisywania opcji.
 * Przechwytuje sygnały wysyłane przez klawiaturę, aby uaktualnić stan gry na ten wybrany przez gracza.</p>
 */
public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	/**
	 * Tablica przechowuje opcje dostępne w menu.
	 */
	private String[] options = {
			"Start",
			"Options",
			"High Score",
			"Credits",
			"Quit"
	};

	private GraphicsEnvironment ge;
	private Font font;
	private Background foreGround;

	/**
	 * Ustawia background oraz czcionkę w menu.
	 * @param gsm Obiekt stanu gry na którym działamy.
	 */
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/level1bg.png");
			bg.setVector(-0.5,0);
			foreGround = new Background("/Backgrounds/frame_main_menu.png");

			URL fontUrl = getClass().getResource("/Fonts/Abel-Regular.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
			font = font.deriveFont(Font.PLAIN,30);

			ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);

			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void init() {

	}

	public void update() {
		bg.update();
	}

	/**
	 * Rysuje tło, tytuł gry oraz wszystkie opcje dostępne w menu.
	 * @param g Obiekt na którym rysujemy.
     */
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		foreGround.draw(g);

		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.WHITE);
			}
			else {
				g.setColor(new Color(6, 32, 29));
			}
			drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT  + i * 100,g);
		}
		
	}

	/**
	 * Ustawia {@link #currentChoice} czyli obecny stan na ten wybrany przez gracza.
	 */
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.OPTIONSSTATE);
		}
		if(currentChoice == 2) {
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

	}
	/**
	 * Przechwytuje sygnał o tym, który klawisz został wciśnięty przez użytkownika. Umożliwia nawigację po opcjach menu.
	 * @param k Wciśnięty klawisz.
	 */
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
	/**
	 * Wypisuje wypośrodkowany tekst.
	 * @param s Tekst który ma być wypisany.
	 * @param w Szerokość pola na którym tekst ma być wypisany.
	 * @param h Wysokość pola na którym tekst ma być wypisany.
	 * @param g Pole na którym tekst ma być wypisany.
	 */
	public static void drawCenteredString(String s, int w, int h, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		g.drawString(s, x, y);
	}
	
}










