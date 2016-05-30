package Main;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Główna klasa gry, tworzone jest tu okno gry.
 * <p>Ustawiane są wszystkie parametry okna takie jak: background color, rozmiar, położenie, widoczność, a także
 * flagi odpowiedzialne za poprawne zamykanie okna.</p>
 */
public class Game {

	/**
	 * Tworzy obiekty {@link JFrame} niezbędne do utworzenia okna gry, ustawia podstawowe parametry tego okna.
	 * @see JFrame
	 * @param args Nie są wykorzystywane.
     */
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Divine Intervention");
		window.setContentPane(new GamePanel());
		window.getContentPane().setBackground( new Color(00));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1280,720);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setUndecorated(true);
		window.pack();
		window.setVisible(true);

	}
	
}
