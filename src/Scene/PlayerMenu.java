package Scene;

import Main.GamePanel;

/**
 * Created by Krzysztof on 24.04.2016.
 */
public class PlayerMenu extends Player {
    public PlayerMenu(int character) {
        super(character);
        animation.setDelay(80);
    }

    @Override
    public void update() {
        super.x = GamePanel.WIDTH/2 - Player.pWIDTH/2;
        super.y = 200;

        animation.update();
    }
}
