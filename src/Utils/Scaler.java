package Utils;

import Main.GamePanel;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by Krzysztof on 26.04.2016.
 */
public class Scaler {
    AffineTransformOp scaleOp;
    BufferedImage before;
    BufferedImage after;


    public Scaler(BufferedImage bi, double imgWIDTH, double imgHEIGHT){
        before = bi;
        after = new BufferedImage((int)(imgWIDTH), (int)(imgHEIGHT), BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(GamePanel.SCALE, GamePanel.SCALE);
        scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    }

    public BufferedImage getScalledImage(){
        return scaleOp.filter(before, after);
    }
}
