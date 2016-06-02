package Scene;

import Main.GamePanel;

import java.awt.*;

/**
 * Klasa przehcowująca nazwy wszystkich postaci wyświetlanych w menu wyboru postaci. Przekazuje indeks postaci którą wybrał gracz do
 * klasy Player.
 *
 *  *@see {@link Player}
 */
public class PlayerMenu extends Player {

    private String characterNAME;
    private String[] names = {
            "DEFAULT",
            "PAPAJ",
            "STONOG"
    };

    /**
     * Przypisuje do wybranej przez gracza postaci jej nazwę.
     * @see Player#Player(int)
     * @param character Wybrana postać.
     */
    public PlayerMenu(int character) {
        super(character);
        animation.setDelay(80);
        characterNAME = names[character];
    }

    @Override
    /**
     * Ustawia początkowe położenie postaci.
     * @see Player#update()
     */
    public void update() {
        super.x = GamePanel.WIDTH/2 - Player.pWIDTH/2;
        super.y = 200;

        animation.update();
    }

    @Override
    /**
     * @see {@link Player#draw(Graphics2D)}
     */
    public void draw(Graphics2D g) {
        super.draw(g);
        //g.setColor(new Color(214, 156, 5));
        //g.drawString(characterNAME,(int)(super.getX()),(int)(super.getY()+Player.pHEIGHT*3));
    }
}
