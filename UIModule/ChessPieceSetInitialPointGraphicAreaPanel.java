package Chreator.UIModule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Chreator.ObjectModel.Point;
import Chreator.UIModule.AbstractModel.ChessBoardGraphicAreaPanel;

public class ChessPieceSetInitialPointGraphicAreaPanel extends ChessBoardGraphicAreaPanel {

    public interface OnSetInitialPointCallback {
        public void onSetInitialPointCallback(int[] ids);
    }

    private OnSetInitialPointCallback callback;
    private ArrayList<Point> selectedBackupList;
    private boolean isDragging = false;
    private int mouseStartX = 0, mouseStartY = 0, mouseCurrX = 0, mouseCurrY = 0;

    public ChessPieceSetInitialPointGraphicAreaPanel(OnSetInitialPointCallback callback) {
        this.callback = callback;
        selectedBackupList = new ArrayList<Point>();
    }

    @Override
    protected void panelResized(ComponentEvent e) {
        updateAllPoints();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintComponentForChessBoard(g);
        paintComponentForBoardEdge(g);
        paintComponentForEdgeTriangles(g);
        paintComponentForAllPoints(g);
        paintComponentForEdgeTriangleInfo(g);

        g.setColor(Color.green);
        for (Point p : selectedPointList) {
            Point.InfoPack info = p.getPixelInformation();
            double pStartX = info.posX - info.width / 2,
                    pEndX = info.posX + info.width / 2,
                    pStartY = info.posY - info.height / 2,
                    pEndY = info.posY + info.height / 2;
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    g.fillRect(
                            (int) Math.round(info.posX - 2.5 - info.width / 2 + (info.width * 0.75 + 2.5) * i),
                            (int) Math.round(info.posY - 2.5 - info.height / 2 + info.height * j),
                            (int) Math.round(info.width * 0.25 + 2.5),
                            5
                    );
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    g.fillRect(
                            (int) Math.round(info.posX - 2.5 - info.width / 2 + info.width * i),
                            (int) Math.round(info.posY - 2.5 - info.height / 2 + (info.height * 0.75 + 2.5) * j),
                            5,
                            (int) Math.round(info.height * 0.25 + 2.5)
                    );
        }

        if (isDragging) {
            g.setColor(new Color(0, 128, 0));
            g.drawRect(
                    mouseStartX < mouseCurrX ? mouseStartX : mouseCurrX,
                    mouseStartY < mouseCurrY ? mouseStartY : mouseCurrY,
                    Math.abs(mouseCurrX - mouseStartX),
                    Math.abs(mouseCurrY - mouseStartY)
            );
        }
    }

    @Override
    protected MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseStartX = e.getX();
                mouseStartY = e.getY();
                mouseCurrX = e.getX();
                mouseCurrY = e.getY();

                for (Point p : selectedPointList)
                    selectedBackupList.add(p);

                Point p = getCursorPointingPoint(e.getX(), e.getY());
                if (p != null)
                    if (selectedPointList.contains(p))
                        selectedPointList.remove(p);
                    else
                        selectedPointList.add(p);

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isDragging = false;
                selectedBackupList.clear();
                int[] ids = new int[selectedPointList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = selectedPointList.get(i).getId();
                callback.onSetInitialPointCallback(ids);

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                isDragging = true;
                mouseCurrX = e.getX();
                mouseCurrY = e.getY();

                double east = mouseCurrX > mouseStartX ? mouseCurrX : mouseStartX,
                        south = mouseCurrY > mouseStartY ? mouseCurrY : mouseStartY,
                        west = mouseCurrX < mouseStartX ? mouseCurrX : mouseStartX,
                        north = mouseCurrY < mouseStartY ? mouseCurrY : mouseStartY;

                for (Point p : pointList) {
                    Point.InfoPack info = p.getPixelInformation();

                    int counter = 0;
                    if (info.posX + info.width / 2 < west) counter++;
                    if (info.posX - info.width / 2 > east) counter++;
                    if (info.posY - info.height / 2 > south) counter++;
                    if (info.posY + info.height / 2 < north) counter++;

                    if (counter == 0) {
                        if (!selectedBackupList.contains(p) && !selectedPointList.contains(p)) selectedPointList.add(p);
                        else if (selectedBackupList.contains(p) && selectedPointList.contains(p)) selectedPointList.remove(p);
                    }else{
                        if (!selectedBackupList.contains(p) && selectedPointList.contains(p)) selectedPointList.remove(p);
                        else if (selectedBackupList.contains(p) && !selectedPointList.contains(p)) selectedPointList.add(p);
                    }
                }

                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouseCurrX = e.getX();
                mouseCurrY = e.getY();

                EdgeTriangleInfo backInfo = showTriangleInfo;
                if (getCursorPointingPoint(e.getX(), e.getY()) == null)
                    showTriangleInfo = getCursorPointingEdgeTriangle(e.getX(), e.getY());
                if (backInfo != showTriangleInfo) repaint();
            }
        };
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

    public void updateContent(int[] selectedIDs) {
        ChessBoardPanel panel = UIHandler.getInstance(null).getChessBoardPanel();
        BufferedImage image = panel.getChessBoardImage();
        Dimension d = panel.getChessBoardPreferredSize();
        if (image == null)
            setBoardImage(d.width, d.height);
        else
            setBoardImage(image);

        pointList.clear();
        selectedPointList.clear();

        for (Point p : UIHandler.getInstance(null).getPointList()) {
            Point np = new Point(this, p.getId());
            Point.InfoPack infoPack = p.getRelativeInformation();
            np.setSizeByRelative(infoPack.posX, infoPack.posY, infoPack.width, infoPack.height);
            pointList.add(np);
        }
        for (Point p : UIHandler.getInstance(null).getPointList()) {
            Point np = findPointInListById(pointList, p.getId());
            for (Point.EdgePointPair edgePointPair : p.getAllEdgePointPair()) {
                Point targetPoint = findPointInListById(pointList, edgePointPair.targetPoint.getId());
                if (targetPoint != null) np.addEdgePointPairs(edgePointPair.direction, targetPoint);
            }
        }

        for (int id : selectedIDs) {
            for (Point p : pointList) {
                if (p.getId() == id) {
                    selectedPointList.add(p);
                    break;
                }
            }
        }
    }
}