package Chreator.UIModule;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

import Chreator.ObjectModel.Point;
import Chreator.UIModule.AbstractModel.ChessBoardGraphicAreaPanel;

/**
 * Created by root on 1/28/16.
 */
public class ChessPieceSetSizeGraphicAreaPanel extends ChessBoardGraphicAreaPanel {

    @Override
    protected void panelResized(ComponentEvent e) {
        updateAllPoints();
    }

    @Override
    protected MouseAdapter createMouseAdapter() {
        return null;
    }

    @Override
    protected KeyListener createKeyAdapter() {
        return null;
    }

    @Override
    protected void updateAllPoints() {
        for (Point p : pointList)
            p.updatePixelByRelative();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void updateContent() {
        ChessBoardPanel panel = UIHandler.getInstance(null).getChessBoardPanel();
        BufferedImage image = panel.getChessBoardImage();
        Dimension d = panel.getChessBoardPreferredSize();
        if (image == null)
            setBoardImage(d.width, d.height);
        else
            setBoardImage(image);

        pointList.clear();
        for (Point p : UIHandler.getInstance(null).getPointList()) {
            Point np = new Point(this, p.getId());
            Point.InfoPack infoPack = p.getRelativeInformation();
            np.setSizeByRelative(infoPack.posX, infoPack.posY, infoPack.width, infoPack.height);
            pointList.add(np);
        }
        for (Point p : UIHandler.getInstance(null).getPointList()) {
            Point np = findPointInListById(pointList, p.getId());
            for(Point.EdgePointPair edgePointPair : p.getAllEdgePointPair()){
                Point targetPoint = findPointInListById(pointList, edgePointPair.targetPoint.getId());
                if (targetPoint!=null) np.addEdgePointPairs(edgePointPair.direction, targetPoint);
            }
        }
        repaint();
    }
}
