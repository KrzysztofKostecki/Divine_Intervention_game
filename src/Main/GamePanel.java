package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import GameState.GameStateManager;
/**
 * Klasa obsługująca główny panel jak i pętle gry. Udostępnia metody odpowiedzialne za przechwytywanie sygnałów
 * wysyłanych z klawiatury. Ustawia podstawowe parametry dla okna gry.
 * @see JPanel
 * @see KeyListener
 */
public class GamePanel extends JPanel 
	implements Runnable, KeyListener{
	
	// resolution
	/**
	 * Określa rozdzielczość okna.
	 */
    public static final double SCALE = 1;
	/**
	 * Szerokość okna gry.
	 */
	public static final int WIDTH = (int)(1280*SCALE);
	/**
	 * Wysokość okna gry.
	 */
	public static final int HEIGHT = (int)(720*SCALE);
    private Dimension screenSize;

	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 80;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	/**
	 * Tworzy nowy GamePanel inicjalizując go wartościami {@link #HEIGHT} i {@link #WIDTH}.
	 * Ustawia flagę setFocusable na wartość true.
	 * Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
	 * @see Component#setFocusable(boolean)
	 */
	public GamePanel() {
		super();
		setPreferredSize(
			new Dimension(WIDTH, HEIGHT ));
		setFocusable(true);
		requestFocus();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	}
	
	@Override
	/**
	 * Tworzy nowy wątek i wymusza rozpoczęcie jego działania
	 * @see Component#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	
	/**
	 * Inicjalizuje {@link #image} wartościami {@link #WIDTH} oraz {@link #HEIGHT}.
	 * Ustawia flagę {@link #running} na true, co umożliwia start pętli gry.
	 * Metoda jest wywoływana przez {@link #run()}.
	 */
	private void init() {
		
		image = new BufferedImage(
				WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
		
	}
	
	/**
	 * Rozpoczyna główną pętlę gry. W każdym obiegu pętli wywoływane są metody
	 * {@link #update()}, {@link #draw()} and {@link #drawToScreen()},
	 * po czym wątek jest usypiany.
	 */
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;

			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;

			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * Aktualizuje stan gry.
	 */
	private void update() {
		gsm.update();
	}
	
	/**
	 * Obsługuje rysowanie w oknie gry.
	 * {@see GameStateManager#draw(java.awt.Graphics2D)}
	 */
	private void draw() {
		gsm.draw(g);
	}


	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,
				WIDTH, HEIGHT,
				null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}

	/**
	 * Obsługuje sygnały wysyłane po naciśnięciu klawisza.
	 * @param key Wciśnięty klawisz.
	 */
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}
	/**
	 * Obsługuje sygnały wysyłane po puszczeniu klawisza.
	 * @param key Puszczony klawisz.
	 */
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}

}
















