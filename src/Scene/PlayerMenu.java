package Scene;

import Main.GamePanel;

import java.awt.*;

/**
 * Created by Krzysztof on 24.04.2016.
 */
public class PlayerMenu extends Player {

    private String characterNAME;
    private String[] names = {
            "DEFAULT",
            "PAPAJ",
            "STONOG"
    };
    public PlayerMenu(int character) {
        super(character);
        animation.setDelay(80);
        characterNAME = names[character];
    }

    @Override
    public void update() {
        super.x = GamePanel.WIDTH/2 - Player.pWIDTH/2;
        super.y = 200;

        animation.update();
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        //g.setColor(new Color(214, 156, 5));
        //g.drawString(characterNAME,(int)(super.getX()),(int)(super.getY()+Player.pHEIGHT*3));
    }
}
