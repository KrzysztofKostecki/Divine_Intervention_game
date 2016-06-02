package GameState;
/**
 * Klasa służąca jako interfejs dla klas obsługujących różne stany gry.
 */
public abstract class GameState {
	
	protected GameStateManager gsm;

	/**
	 * Metoda inicjalizująca dany stan gry.
	 */
	public abstract void init();

	/**
	 * Uaktualnia stan gry.
	 */
	public abstract void update();

	/**
	 * Obsługuje rysowanie na oknie gry.
	 * @param g Obiekt na którym rysujemy.
     */
	public abstract void draw(java.awt.Graphics2D g);

	/**
	 * Obsługuje sygnały wysyłane po naciśnięciu klawisza.
	 * @param k Wciśnięty klawisz.
     */
	public abstract void keyPressed(int k);

	/**
	 * Obsługuje sygnały wysyłane po puszczeniu klawisza.
	 * @param k Puszczony klawisz.
     */
	public abstract void keyReleased(int k);
	
}
