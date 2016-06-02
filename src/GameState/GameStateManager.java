package GameState;

import java.awt.*;
import java.util.ArrayList;
/**
 * Zawiera liste wszystkich stanów gry, umożliwia zmianę stanu podczas rozgrywki. Ma dostęp do wszystkich stanów gry, przy użyciu
 * obiektu tej klasy można wywołać wszystkie funkcje z klas implementujących klasę {@link GameState}.
 */
public class GameStateManager {
	/**
	 * Lista stanów gry.
	 */
	ArrayList<GameState> gameStates;
	/**
	 * Obecny stan gry.
	 */
	private int currentState;

	/**
	 * Stan gry odpowiadający menu głównemu.
	 */
	public static final int MENUSTATE = 0;
	/**
	 * Stan gry podczas rozgrywki.
	 */
	public static final int LEVEL1STATE = 1;
	/**
	 * Stan gry odpowiadający menu highscore.
	 */
	public static final int HIGHSCORESTATE = 2;
	/**
	 * Stan gry po porażce.
	 */
	public static final int GAMEOVERSTATE = 3;
	/**
	 * Stan gry odpowiadający menu opcji.
	 */
	public static final int OPTIONSSTATE = 4;
	/**
	 * Stan gry odpowiadający menu credits.
	 */
	public static final int CREDITSSTATE = 5;

	/**
	 * Tworzy listę stanów gry.
	 *
	 * <ul>
	 * <li>MENUSTATE</li>
	 * <li>LEVEL1STATE</li>
	 * <li>HIGHSCORESTATE</li>
	 * <li>GAMEOVERSTATE</li>
	 * <li>OPTIONSSTATE</li>
	 * <li>CREDITSSTATE</li>
	 * </ul>
	 *
	 * Ustawia currentState na MENUSTATE. Znaczy to tyle, że obecniy stan gry to menu.
	 * 
	 */
	public GameStateManager() {
		
		gameStates = new ArrayList<GameState>();
		
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new Level1State(this));
		gameStates.add(new HighScoreState(this));
        gameStates.add(new GameOverState(this));
		gameStates.add(new OptionsState(this));
		gameStates.add(new CreditsState(this));

	}

	/**
	 * Ustawia currentState na przekazany stan gry, wywołuje metodę init() dla przekazanego stanu gry.
	 * @param state określa stan dla którego metoda init() jest wywoływana.
     */
	public void setState(int state) {
		gameStates.get(state).init();
		currentState = state;
	}

	/**
	 * Wywołuje metodę update() dla obecnego stanu gry.
	 * @see GameState#update()
	 */
	public void update() {
		gameStates.get(currentState).update();
	}

	/**
	 * Wywołuje metodę draw(Graphics2D) dla obecnego stanu gry.
	 * @see GameState#draw(Graphics2D)
	 * @param g Obiekt na którym rysujemy
     */
	public void draw(java.awt.Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}

	/**
	 * Wywołuje metodę keyPressed(int) dla obecnego stanu gry.
	 * @see GameState#keyPressed(int)
	 * @param k Wciśnięty klawisz.
     */
	public void keyPressed(int k) {
		gameStates.get(currentState).keyPressed(k);
	}

	/**
	 * Wywołuje metodę keyReleased(int) dla obecnego stanu gry.
	 * @see GameState#keyReleased(int)
	 * @param k Wciśnięty klawisz.
     */
	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}
	
}









