package GameState;

import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.PaintEvent;
import java.net.URL;
import java.util.ArrayList;

/**
 * Klasa reprezentująca menu wyświetlane po przegranej rozgrywce. Obsługuje wyświetlane w tym menu opcje. Przechwytuje sygnały
 * klawiatury. Sprawdza też, czy uzyskany podczas rozgrywki wynik nadaje się do highscore.
 */
public class GameOverState extends GameState {

	private Background bg;
	private Background foreGround;
	GraphicsEnvironment ge;
	private int currentChoice = 0;
	/**
	 * Tablica przechowująca opcje dostępne po przegranej rozgrywce.
	 */
	private String[] options = {
			"Play Again",
			"Return to menu",
			"Quit"
	};

	private Font font;

	private boolean isHighscore;

	private boolean nameTyping;
	ArrayList<String> nickname;


	/**
	 * Ustawia background oraz czcionkę.
	 * @param gsm Obiekt stanu gry na którym działamy.
     */
	public GameOverState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {

			bg = new Background("/Backgrounds/level1bg.png");
            foreGround = new Background("/Backgrounds/game_over.png");
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

	/**
	 * Sprawdza, czy uzyskany wynik nadaje się do highscore. Jeśli tak, ustawia flagę isHighscore na true.
	 */
	public void init() {
		isHighscore = false;
		nickname = new ArrayList<>();
		if(Level1State.score > HighScoreState.lowest){
			isHighscore = true;
            foreGround = new Background("/Backgrounds/new_highscore.png");
		}
	}
	
	public void update() {
		bg.update();
	}

	/**
	 * Rysuje nowe menu wyświetlane po porażce.
	 *
	 *     Sprawdza, czy uzyskany wynik jest największym w highscore. Wyświetla uzyskane wynik i opcje dostępne po porażce.
	 *   	<ul>
	 *   	  <li>Play Again</li>
	 *   	  <li>Return to menu</li>
	 *   	  <li>Quit</li>
	 *   	</ul>
	 *
	 *  <p>
	 *      Jeśli uzyskany wynik wchodzi do highscore, to jest ono wyświetlane.
	 *  </p>
	 * @param g Obiekt na którym rysujemy.
     */
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		if (Level1State.score > HighScoreState.highest){
            HighScoreState.highest = Level1State.score;
        }
		foreGround.draw(g);
        g.setColor(new Color(6, 32, 29));
		drawCenteredString("Your score: " + Integer.toString(Level1State.score),
				GamePanel.WIDTH,
				GamePanel.HEIGHT-45,g);

		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.WHITE);
			}
			else {
				g.setColor(new Color(6, 32, 29));
			}
			drawCenteredString(options[i],GamePanel.WIDTH,GamePanel.HEIGHT+100  + i * 100,g);
		}

		if(isHighscore){
			g.setColor(new Color(6, 32, 29));
			g.fillRect(GamePanel.WIDTH/2-250,GamePanel.HEIGHT/2,500,200);
			g.setColor(Color.WHITE);
			for (int i1 = 0; i1 < nickname.size(); i1++) {
				drawCenteredString(nickname.get(i1), GamePanel.WIDTH - 150 + i1*150,GamePanel.HEIGHT + 160,g);
			}
		}
		
	}

	/**
	 * Obsługuje wyświetlone menu. W zależności od wyboru gracza ma miejsce jedna z akcji:
	 * <ul>
	 *     <li>Rozpoczęcie nowej gry</li>
	 *     <li>Powrót do menu</li>
	 *     <li>Wyjście z gry</li>
	 * </ul>
	 */
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {

	}

	/**
	 * Przechwytuje sygnał wysyłany po zwolnieniu klawisza.
	 * <p>
	 * Jeśli flaga isHighscore ma wartość true, pobiera nick pod jakim nowy wynik ma być zapisany, po czym
	 * wypisuje ten wynik wraz z nickiem.
	 * <p>
	 *     Jeśli flaga isHigscore ma wartość false, obsługiwany jest wybór jednej z opcji wyświetlonego menu.
	 * </p>
	 * @param k Wciśnięty klawisz.
     */
	public void keyReleased(int k) {
		if(isHighscore){
			if (k == KeyEvent.VK_BACK_SPACE) {
				if (nickname.size() != 0) {
					nickname.remove(nickname.size() - 1);
				}
			}
			if (nickname.size() < 3) {
				if (k != KeyEvent.VK_BACK_SPACE && Character.isAlphabetic(k)) nickname.add(KeyEvent.getKeyText(k));
			}

			if (k == KeyEvent.VK_ENTER && nickname.size()==3) {
				isHighscore = false;
				String nick = "";
				for(String i: nickname){
					nick += i;
				}
				System.out.println("Score: "+ Level1State.score + " Nick: " + nick );
				HighScoreState.checkAndAddHighScore(Level1State.score, nick);
			}
		}else {
			if (k == KeyEvent.VK_ENTER) {
				select();
			}
			if (k == KeyEvent.VK_UP) {
				currentChoice--;
				if (currentChoice == -1) {
					currentChoice = options.length - 1;
				}
			}
			if (k == KeyEvent.VK_DOWN) {
				currentChoice++;
				if (currentChoice == options.length) {
					currentChoice = 0;
				}
			}

		}

	}
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










