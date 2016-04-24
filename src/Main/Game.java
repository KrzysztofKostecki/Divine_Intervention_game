package Main;

import javax.swing.JFrame;
/**
 * Created by Krzysztof on 05.04.2016.
 */
public class Game {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Divine Intervention");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(960,720);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}
	
}
