package Chreator.ObjectModel;

import java.util.HashMap;
import java.util.Map;

import Chreator.UIModule.ChessBoardGraphicAreaPanel;

/**
 * Created by him on 2015/12/30.
 */
public class Point {
    public class InfoPack {
        public double posX, posY, height, width;

        public InfoPack(double posX, double posY, double width, double height) {
            this.posX = posX;
            this.posY = posY;
            this.height = height;
            this.width = width;
        }
    }

    private static int idCounter = 0;
    private Map<String, Point> edges;
    private int id;
    private double posXPixel, posYPixel, heightPixel, widthPixel, posXRelative, posYRelative, heightRelative, widthRelative;
    private ChessBoardGraphicAreaPanel graphicAreaPanel;

    public Point(ChessBoardGraphicAreaPanel graphicAreaPanel) {
        this.graphicAreaPanel = graphicAreaPanel;
        id = idCounter;
        idCounter++;
        edges = new HashMap<String, Point>();
        setSizeByRelative(0.0, 0.0, 0.05, 0.05);
    }

    public void setSizeByRelative(double posX, double posY, double width, double height) {
        posXRelative = posX;
        posYRelative = posY;
        widthRelative = width;
        heightRelative = height;
        updatePixelByRelative();
    }

    public void setSizeByPixel(double posX, double posY, double width, double height) {
        posXPixel = posX;
        posYPixel = posY;
        widthPixel = width;
        heightPixel = height;
        updateRelativeByPixel();
    }

    public int getId() {
        return id;
    }

    public InfoPack getPixelInformation() {
        return new InfoPack(posXPixel, posYPixel, widthPixel, heightPixel);
    }

    public InfoPack getRelativeInformation() {
        return new InfoPack(posXRelative, posYRelative, widthRelative, heightRelative);
    }

    public void updateRelativeByPixel() {
        int imageHeight = graphicAreaPanel.getBoardHeight(), imageWidth = graphicAreaPanel.getBoardWidth(),
                panelHeight = graphicAreaPanel.getHeight(), panelWidth = graphicAreaPanel.getWidth();
        double imageTangent = 1.0 * imageHeight / imageWidth, panelTangent = 1.0 * panelHeight / panelWidth, imageActualWidth, imageActualHeight, baseX, baseY;

        if (imageTangent > panelTangent) {
            imageActualHeight = 1.0 * panelHeight;
            imageActualWidth = 1.0 * panelHeight / imageTangent;
            baseX = (1.0 * panelWidth - imageActualWidth) / 2;
            baseY = 0.0;
        } else if (imageTangent < panelTangent) {
            imageActualHeight = 1.0 * panelWidth * imageTangent;
            imageActualWidth = 1.0 * panelWidth;
            baseX = 0.0;
            baseY = (1.0 * panelHeight - imageActualHeight) / 2;
        } else {
            imageActualHeight = 1.0 * panelHeight;
            imageActualWidth = 1.0 * panelWidth;
            baseX = 0.0;
            baseY = 0.0;
        }
        posXRelative = (1.0 * posXPixel - baseX) / imageActualWidth;
        posYRelative = (1.0 * posYPixel - baseY) / imageActualHeight;
        widthRelative = 1.0 * widthPixel / imageActualWidth;
        heightRelative = 1.0 * heightPixel / imageActualHeight;
    }

    public void updatePixelByRelative() {
        int imageHeight = graphicAreaPanel.getBoardHeight(), imageWidth = graphicAreaPanel.getBoardWidth(),
                panelHeight = graphicAreaPanel.getHeight(), panelWidth = graphicAreaPanel.getWidth();
        double imageTangent = 1.0 * imageHeight / imageWidth, panelTangent = 1.0 * panelHeight / panelWidth, imageActualWidth, imageActualHeight, baseX, baseY;

        if (imageTangent > panelTangent) {
            imageActualWidth = 1.0 * panelHeight / imageTangent;
            imageActualHeight = panelHeight;
            baseX = (1.0 * panelWidth - imageActualWidth) / 2;
            baseY = 0.0;
        } else if (imageTangent < panelTangent) {
            imageActualWidth = panelWidth;
            imageActualHeight = 1.0 * panelWidth * imageTangent;
            baseX = 0.0;
            baseY = (1.0 * panelHeight - imageActualHeight) / 2;
        } else {
            imageActualWidth = panelWidth;
            imageActualHeight = panelHeight;
            baseX = 0.0;
            baseY = 0.0;
        }
        posXPixel = baseX + posXRelative * imageActualWidth;
        posYPixel = baseY + posYRelative * imageActualHeight;
        widthPixel = widthRelative * imageActualWidth;
        heightPixel = heightRelative * imageActualHeight;
    }
}
