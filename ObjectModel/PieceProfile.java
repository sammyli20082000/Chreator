package Chreator.ObjectModel;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListModel;

/**
 * Created by root on 1/25/16.
 */
public class PieceProfile {
    public String playerSide, pieceClassName, code, sourcePicLink;
    public Color pieceColor;
    public double imageRelativeHeight, imageRelativeWidth;
    public DefaultListModel<String> initialPointId;
    public BufferedImage pieceImage;

    public PieceProfile(String playerSide, String pieceClassName) {
        this.playerSide = playerSide;
        this.pieceClassName = pieceClassName;
        code = "";
        pieceColor = Color.WHITE;
        imageRelativeHeight = 0.1;
        imageRelativeWidth = 0.1;
        initialPointId = new DefaultListModel<String>();
        sourcePicLink = "";
        pieceImage = null;
    }
}
