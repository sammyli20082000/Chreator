package Chreator.ObjectModel;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import Chreator.UIModule.ChessPiecePanel;

/**
 * Created by root on 1/25/16.
 */
public class PieceProfile {
    public String playerSide, pieceClassName, code, sourcePicLink;
    public Color pieceColor;
    public double imageHeight, imageWidth;
    public DefaultListModel<String> initialPointId;

    public PieceProfile(String playerSide, String pieceClassName) {
        this.playerSide = playerSide;
        this.pieceClassName = pieceClassName;
        code = "";
        pieceColor = Color.WHITE;
        imageHeight = ChessPiecePanel.sharedPieceHeight;
        imageWidth = ChessPiecePanel.sharedPieceWidth;
        initialPointId = new DefaultListModel<String>();
        sourcePicLink = "";
    }
}
