package Chreator.ObjectModel;

import java.util.ArrayList;

import Chreator.UIModule.AbstractModel.ChessBoardGraphicAreaPanel;

/**
 * Created by him on 2015/12/30.
 */
public class Point {
    public static class InfoPack {
        public double posX, posY, height, width;

        public InfoPack(double posX, double posY, double width, double height) {
            this.posX = posX;
            this.posY = posY;
            this.height = height;
            this.width = width;
        }
    }

    public static class EdgePointPair {
        public String direction;
        public Point targetPoint;

        public EdgePointPair(String direction, Point targetPoint) {
            this.direction = direction;
            this.targetPoint = targetPoint;
        }
    }

    private ArrayList<EdgePointPair> edges;
    private int id;
    private double posXPixel, posYPixel, heightPixel, widthPixel, posXRelative, posYRelative, heightRelative, widthRelative;
    private ChessBoardGraphicAreaPanel graphicAreaPanel;

    public Point(ChessBoardGraphicAreaPanel graphicAreaPanel, int id) {
        this.graphicAreaPanel = graphicAreaPanel;
        this.id = id;
        edges = new ArrayList<EdgePointPair>();
        setSizeByRelative(0.0, 0.0, 0.05, 0.05);
    }
    
    public Point(ChessBoardGraphicAreaPanel graphicAreaPanel, double posX, double posY, double width, double height, int id) {
    	this.graphicAreaPanel = graphicAreaPanel;
    	this.id = id;
    	edges = new ArrayList<EdgePointPair>();
    	setSizeByRelative(posX, posY, width, height);
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

    public void addEdgePointPairs(String dir, Point point) {
        for (EdgePointPair edgePointPair : edges) {
            if (edgePointPair.direction.equals(dir)) {
                edgePointPair.targetPoint = point;
                return;
            }
        }
        edges.add(new EdgePointPair(dir, point));
    }

    public void removeEdgePointPairs(String dir) {
        for (int i = edges.size() - 1; i >= 0; i--) {
            EdgePointPair edgePointPair = edges.get(i);
            if (edgePointPair.direction.equals(dir))
                edges.remove(edgePointPair);
        }
    }

    public void removeEdgePointPairs(Point p) {
        for (int i = edges.size() - 1; i >= 0; i--) {
            EdgePointPair edgePointPair = edges.get(i);
            if (edgePointPair.targetPoint.equals(p))
                edges.remove(edgePointPair);
        }
    }

    public String getEdgeByPoint(Point p) {
        for (EdgePointPair edgePointPair : edges) {
            if (edgePointPair.targetPoint.equals(p))
                return edgePointPair.direction;
        }
        return null;
    }

    public Point getPointByEdge(String dir) {
        for (EdgePointPair edgePointPair : edges)
            if (edgePointPair.direction.equals(dir))
                return edgePointPair.targetPoint;
        return null;
    }

    public ArrayList<EdgePointPair> getAllEdgePointPair() {
        ArrayList<EdgePointPair> list = new ArrayList<EdgePointPair>();
        for (EdgePointPair edgePointPair : edges)
            list.add(new EdgePointPair(edgePointPair.direction, edgePointPair.targetPoint));
        return list;
    }
}
