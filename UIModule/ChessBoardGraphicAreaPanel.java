package Chreator.UIModule;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Chreator.ObjectModel.Point;

/**
 * Created by root on 12/26/15.
 */
public class ChessBoardGraphicAreaPanel extends JPanel {
    public enum EditingMode {SELECTING, SCALING, TRANSLATION}

    public enum ScaleAnchorOrientation {EAST, SOUTH, WEST, NORTH, SOUTH_EAST, SOUTH_WEST, NORTH_EAST, NORTH_WEST}

    private class ScaleAnchorInfo {
        ScaleAnchorOrientation orientation;
        public int width, height, posXTopLeft, posYTopLeft;

        public ScaleAnchorInfo(int posXTopLeft, int posYTopLeft, int width, int height, ScaleAnchorOrientation orientation) {
            this.posXTopLeft = posXTopLeft;
            this.posYTopLeft = posYTopLeft;
            this.width = width;
            this.height = height;
            this.orientation = orientation;
        }
    }

    private ChessBoardPanel parent;
    private BufferedImage boardImage;
    private int boardWidth, boardHeight, mouseStartX = 0, mouseStartY = 0, mouseLastX = 0, mouseLastY = 0;
    private ArrayList<Point> pointList, selectedPointList;
    private EditingMode editingMode = EditingMode.SELECTING;
    private ScaleAnchorOrientation scaleOrientation;
    private Point scaleReferencePoint;
    private boolean isDragging = false;

    public ChessBoardGraphicAreaPanel(ChessBoardPanel parent) {
        this.parent = parent;
        setBackground(Color.DARK_GRAY);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (getSize().width > 0 && getSize().height > 0) {
                    if (boardWidth == 0 && boardHeight == 0)
                        ChessBoardGraphicAreaPanel.this.parent.setTextFieldImageSize(getSize().width / 2, getSize().height);
                    updateAllPoints();
                }
            }
        });
        pointList = new ArrayList<Point>();
        selectedPointList = new ArrayList<Point>();

        MouseAdapter mouseAdapter = createMouseAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth(), panelHeight = getHeight();
        double panelTangent = 1.0 * panelHeight / panelWidth, boardTangent = 1.0 * boardHeight / boardWidth, baseX, baseY;
        baseX = (boardTangent > panelTangent) ? ((1.0 * panelWidth - panelHeight / boardTangent) / 2) : 0;
        baseY = (boardTangent < panelTangent) ? ((1.0 * panelHeight - panelWidth * boardTangent) / 2) : 0;

        g.setColor(Color.WHITE);
        if (boardImage == null) {
            if (boardTangent > panelTangent)
                g.fillRect((int) Math.round(baseX), 0, (int) Math.round(1.0 / boardTangent * panelHeight), panelHeight);
            else if (boardTangent < panelTangent)
                g.fillRect(0, (int) Math.round(baseY), panelWidth, (int) Math.round(boardTangent * panelWidth));
            else g.fillRect(0, 0, panelWidth, panelHeight);
        } else {
            if (boardTangent > panelTangent)
                g.drawImage(boardImage, (int) Math.round(baseX), 0, (int) Math.round(1.0 / boardTangent * panelHeight), panelHeight, null);
            else if (boardTangent < panelTangent)
                g.drawImage(boardImage, 0, (int) Math.round(baseY), panelWidth, (int) (boardTangent * panelWidth), null);
            else g.drawImage(boardImage, 0, 0, panelWidth, panelHeight, null);
        }

        for (Point p : pointList) {
            Point.InfoPack pixelInfo = p.getPixelInformation();
            g.setColor(new Color(0f, 0f, 1f, 0.5f));
            g.fillRect((int) Math.round(pixelInfo.posX - pixelInfo.width / 2), (int) Math.round(pixelInfo.posY - pixelInfo.height / 2), (int) Math.round(pixelInfo.width), (int) Math.round(pixelInfo.height));
            g.setColor(Color.ORANGE);
            g.drawString(p.getId() + "", (int) Math.round(pixelInfo.posX), (int) Math.round(pixelInfo.posY));
        }

        g.setColor(Color.GREEN);
        for (Point p : selectedPointList) {
            ArrayList<ScaleAnchorInfo> anchorList = getPointScaleAnchor(p);
            for (ScaleAnchorInfo anchor : anchorList)
                g.fillRect(anchor.posXTopLeft, anchor.posYTopLeft, anchor.width, anchor.height);
        }

        g.setColor(new Color(0, 127, 0));
        if (isDragging && editingMode == EditingMode.SELECTING)
            g.drawRect(
                    (mouseLastX < mouseStartX) ? mouseLastX : mouseStartX,
                    (mouseLastY < mouseStartY) ? mouseLastY : mouseStartY,
                    Math.abs(mouseLastX - mouseStartX),
                    Math.abs(mouseLastY - mouseStartY));
    }

    private ArrayList<ScaleAnchorInfo> getPointScaleAnchor(Point p) {
        ArrayList<ScaleAnchorInfo> list = new ArrayList<ScaleAnchorInfo>();
        ScaleAnchorOrientation[] orientations = {
                ScaleAnchorOrientation.NORTH_WEST, ScaleAnchorOrientation.NORTH, ScaleAnchorOrientation.NORTH_EAST,
                ScaleAnchorOrientation.WEST, null, ScaleAnchorOrientation.EAST,
                ScaleAnchorOrientation.SOUTH_WEST, ScaleAnchorOrientation.SOUTH, ScaleAnchorOrientation.SOUTH_EAST
        };

        Point.InfoPack infoPack = p.getPixelInformation();
        double dotSize = 0.1, minSize = 7.0,
                /*dotActualSize = (infoPack.height < infoPack.width) ? infoPack.height * dotSize : infoPack.width * dotSize;
        dotActualSize = (dotActualSize < minSize) ? minSize : dotActualSize;*/
                dotActualSize = minSize;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 1 || j != 1) list.add(
                        new ScaleAnchorInfo(
                                (int) Math.round(infoPack.posX - infoPack.width / 2 + (infoPack.width * i - dotActualSize) / 2),
                                (int) Math.round(infoPack.posY - infoPack.height / 2 + (infoPack.height * j - dotActualSize) / 2),
                                (int) Math.round(dotActualSize),
                                (int) Math.round(dotActualSize),
                                orientations[j * 3 + i])
                );
            }
        }
        return list;
    }

    public boolean setBoardImage(File file) {
        try {
            boardImage = ImageIO.read(file);
            boardHeight = boardImage.getHeight();
            boardWidth = boardImage.getWidth();
            updateAllPoints();
            parent.setTextFieldImageSize(boardWidth, boardHeight);
            UIHandler.refreshWindow();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            boardImage = null;
            UIHandler.refreshWindow();
            return false;
        }
    }

    public boolean setBoardImage(int width, int height) {
        if (width < 1 || height < 1)
            return false;

        boardHeight = height;
        boardWidth = width;
        boardImage = null;
        updateAllPoints();
        UIHandler.refreshWindow();
        return true;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void addSinglePoint() {
        Point p = new Point(this);
        int id = p.getId() + 5;
        id %= 10;
        p.setSizeByRelative(0.05 + 0.1 * id, 0.05 + 0.1 * id, 0.05, 0.05);
        pointList.add(p);
        UIHandler.refreshWindow();
    }

    public void addRectGrid(int row, int column) {

    }

    public void addTriGrid(int firstRow, int height, int rowDiff) {

    }

    public void deletePoints() {
        for (Point p : selectedPointList)
            pointList.remove(p);
        selectedPointList.clear();
        UIHandler.refreshWindow();
    }

    public void updateAllPoints() {
        ChessBoardPanel.PointScaleMode mode = parent.getPointScaleMode();
        if (mode == ChessBoardPanel.PointScaleMode.PANEL_ALL) {
            for (Point p : pointList) {
                Point.InfoPack pixelInfo = p.getPixelInformation();
                Point.InfoPack relativeInfo = p.getRelativeInformation();
                double
                        imageActualHeight = 1.0 * pixelInfo.height / relativeInfo.height,
                        imageActualWidth = 1.0 * pixelInfo.width / relativeInfo.width,
                        lastPanelWidth = (1.0 * pixelInfo.posX - imageActualWidth * relativeInfo.posX) * 2 + imageActualWidth,
                        lastPanelHeight = (1.0 * pixelInfo.posY - imageActualHeight * relativeInfo.posY) * 2 + imageActualHeight,
                        scaleWidth = 1.0 * getWidth() / lastPanelWidth,
                        scaleHeight = 1.0 * getHeight() / lastPanelHeight;
                p.setSizeByPixel(scaleWidth * pixelInfo.posX,
                        scaleHeight * pixelInfo.posY,
                        scaleWidth * pixelInfo.width,
                        scaleHeight * pixelInfo.height);
            }
        } else if (mode == ChessBoardPanel.PointScaleMode.IMAGE_ALL) {
            for (Point p : pointList) p.updatePixelByRelative();
        } else if (mode == ChessBoardPanel.PointScaleMode.PANEL_COORDINATES) {
            for (Point p : pointList) {
                Point.InfoPack pixelInfo = p.getPixelInformation();
                Point.InfoPack relativeInfo = p.getRelativeInformation();
                double
                        imageActualHeight = 1.0 * pixelInfo.height / relativeInfo.height,
                        imageActualWidth = 1.0 * pixelInfo.width / relativeInfo.width,
                        lastPanelWidth = (1.0 * pixelInfo.posX - imageActualWidth * relativeInfo.posX) * 2 + imageActualWidth,
                        lastPanelHeight = (1.0 * pixelInfo.posY - imageActualHeight * relativeInfo.posY) * 2 + imageActualHeight,
                        scaleWidth = 1.0 * getWidth() / lastPanelWidth,
                        scaleHeight = 1.0 * getHeight() / lastPanelHeight;
                p.setSizeByPixel(scaleWidth * pixelInfo.posX,
                        scaleHeight * pixelInfo.posY,
                        pixelInfo.width,
                        pixelInfo.height);
            }
        } else if (mode == ChessBoardPanel.PointScaleMode.IMAGE_COORDINATES) {
            for (Point p : pointList) {
                Point.InfoPack infoPack = p.getPixelInformation();
                double width = infoPack.width, height = infoPack.height;
                p.updatePixelByRelative();
                infoPack = p.getPixelInformation();
                p.setSizeByPixel(infoPack.posX, infoPack.posY, width, height);
            }
        } else {
            for (Point p : pointList) p.updateRelativeByPixel();
        }

        for (Point p : pointList) {
            Point.InfoPack infoPack = p.getPixelInformation();
            double x = infoPack.posX, y = infoPack.posY;
            x = (x < 0 ? 0 : x);
            x = (x > getWidth() ? getWidth() : x);
            y = (y < 0 ? 0 : y);
            y = (y > getHeight() ? getHeight() : y);

            p.setSizeByPixel(x, y, infoPack.width, infoPack.height);
        }
        UIHandler.refreshWindow();
    }

    private Point getCursorPointingPoint(double x, double y) {
        for (int i = pointList.size() - 1; i >= 0; i--)
            if (isCursorOnPoint(x, y, pointList.get(i))) return pointList.get(i);

        return null;
    }

    private boolean isCursorOnPoint(double x, double y, Point p) {
        Point.InfoPack infoPack = p.getPixelInformation();
        if (x >= infoPack.posX - infoPack.width / 2 &&
                x <= infoPack.posX + infoPack.width / 2 &&
                y >= infoPack.posY - infoPack.height / 2 &&
                y <= infoPack.posY + infoPack.height / 2) return true;

        if (getCursorPointingAnchorOrientation(x, y, p) != null) return true;

        return false;
    }

    private ScaleAnchorOrientation getCursorPointingAnchorOrientation(double x, double y, Point p) {
        if (!selectedPointList.contains(p)) return null;
        ArrayList<ScaleAnchorInfo> anchorList = getPointScaleAnchor(p);
        for (ScaleAnchorInfo anchor : anchorList)
            if (x >= anchor.posXTopLeft &&
                    x <= anchor.posXTopLeft + anchor.width &&
                    y >= anchor.posYTopLeft &&
                    y <= anchor.posYTopLeft + anchor.height)
                return anchor.orientation;

        return null;
    }

    public MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                editingMode = EditingMode.SELECTING;

                Point cursorPointingPoint = getCursorPointingPoint(e.getX(), e.getY());
                ScaleAnchorOrientation orientation =
                        (cursorPointingPoint != null) ?
                                getCursorPointingAnchorOrientation(e.getX(), e.getY(), cursorPointingPoint) :
                                null;

                if (cursorPointingPoint == null) {
                    selectedPointList.clear();
                } else {
                    editingMode = (orientation == null) ? EditingMode.TRANSLATION : EditingMode.SCALING;
                    scaleReferencePoint = cursorPointingPoint;
                    scaleOrientation = orientation;

                    if (!selectedPointList.contains(cursorPointingPoint)) {
                        selectedPointList.clear();
                        selectedPointList.add(cursorPointingPoint);
                    } else {
                        selectedPointList.remove(cursorPointingPoint);
                        selectedPointList.add(cursorPointingPoint);
                    }
                    pointList.remove(cursorPointingPoint);
                    pointList.add(cursorPointingPoint);
                }

                mouseStartX = e.getX();
                mouseStartY = e.getY();
                mouseLastX = e.getX();
                mouseLastY = e.getY();
                UIHandler.refreshWindow();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                isDragging = true;

                if (editingMode == EditingMode.SCALING) {
                    Point.InfoPack refInfo = scaleReferencePoint.getPixelInformation();

                    double deltaWPercent = 1, deltaHPercent = 1, deltaXPercent = 0, deltaYPercent = 0;
                    if (scaleOrientation == ScaleAnchorOrientation.SOUTH || scaleOrientation == ScaleAnchorOrientation.SOUTH_WEST || scaleOrientation == ScaleAnchorOrientation.SOUTH_EAST) {
                        deltaHPercent = refInfo.height + e.getY() - mouseLastY;
                        deltaHPercent = deltaHPercent < 1 ? 1 : deltaHPercent;
                        deltaHPercent = deltaHPercent / refInfo.height;

                        deltaYPercent = (refInfo.height * (deltaHPercent - 1)) / 2;
                        deltaYPercent = deltaYPercent / refInfo.height;
                    } else if (scaleOrientation == ScaleAnchorOrientation.NORTH || scaleOrientation == ScaleAnchorOrientation.NORTH_EAST || scaleOrientation == ScaleAnchorOrientation.NORTH_WEST) {
                        deltaHPercent = refInfo.height - e.getY() + mouseLastY;
                        deltaHPercent = deltaHPercent < 1 ? 1 : deltaHPercent;
                        deltaHPercent = deltaHPercent / refInfo.height;

                        deltaYPercent = (refInfo.height * (1 - deltaHPercent)) / 2;
                        deltaYPercent = deltaYPercent / refInfo.height;
                    }

                    if (scaleOrientation == ScaleAnchorOrientation.EAST || scaleOrientation == ScaleAnchorOrientation.NORTH_EAST || scaleOrientation == ScaleAnchorOrientation.SOUTH_EAST) {
                        deltaWPercent = refInfo.width + e.getX() - mouseLastX;
                        deltaWPercent = deltaWPercent < 1 ? 1 : deltaWPercent;
                        deltaWPercent = deltaWPercent / refInfo.width;

                        deltaXPercent = (refInfo.width * (deltaWPercent - 1)) / 2;
                        deltaXPercent = deltaXPercent / refInfo.width;
                    } else if (scaleOrientation == ScaleAnchorOrientation.WEST || scaleOrientation == ScaleAnchorOrientation.NORTH_WEST || scaleOrientation == ScaleAnchorOrientation.SOUTH_WEST) {
                        deltaWPercent = refInfo.width - e.getX() + mouseLastX;
                        deltaWPercent = deltaWPercent < 1 ? 1 : deltaWPercent;
                        deltaWPercent = deltaWPercent / refInfo.width;

                        deltaXPercent = (refInfo.width * (1 - deltaWPercent)) / 2;
                        deltaXPercent = deltaXPercent / refInfo.width;
                    }

                    for (Point p : selectedPointList) {
                        Point.InfoPack infoPack = p.getPixelInformation();
                        p.setSizeByPixel(
                                infoPack.posX + infoPack.width * deltaXPercent,
                                infoPack.posY + infoPack.height * deltaYPercent,
                                infoPack.width * deltaWPercent,
                                infoPack.height * deltaHPercent);
                    }
                } else if (editingMode == EditingMode.TRANSLATION) {
                    double deltaX = e.getX() - mouseLastX, deltaY = e.getY() - mouseLastY;
                    for (Point p : selectedPointList) {
                        Point.InfoPack infoPack = p.getPixelInformation();
                        p.setSizeByPixel(infoPack.posX + deltaX, infoPack.posY + deltaY, infoPack.width, infoPack.height);
                    }
                } else {
                    selectedPointList.clear();
                    double
                            leftX = (mouseStartX < e.getX()) ? mouseStartX : e.getX(),
                            rightX = (mouseStartX > e.getX()) ? mouseStartX : e.getX(),
                            topY = (mouseStartY < e.getY()) ? mouseStartY : e.getY(),
                            bottomY = (mouseStartY > e.getY()) ? mouseStartY : e.getY();

                    for (int i = pointList.size() - 1; i >= 0; i--) {
                        Point p = pointList.get(i);
                        Point.InfoPack infoPack = p.getPixelInformation();
                        int leftCount = 0, rightCount = 0, topCount = 0, bottomCount = 0;
                        if (infoPack.posX - infoPack.width / 2 < leftX) leftCount++;
                        if (infoPack.posX + infoPack.width / 2 < leftX) leftCount++;
                        if (infoPack.posX - infoPack.width / 2 > rightX) rightCount++;
                        if (infoPack.posX + infoPack.width / 2 > rightX) rightCount++;
                        if (infoPack.posY - infoPack.height / 2 < topY) topCount++;
                        if (infoPack.posY + infoPack.height / 2 < topY) topCount++;
                        if (infoPack.posY - infoPack.height / 2 > bottomY) bottomCount++;
                        if (infoPack.posY + infoPack.height / 2 > bottomY) bottomCount++;

                        if (leftCount < 2 && topCount < 2 && bottomCount < 2 && rightCount < 2) {
                            selectedPointList.add(p);
                            if ( i != pointList.size() - selectedPointList.size()){
                                pointList.remove(p);
                                pointList.add(p);
                            }
                        }
                    }
                }
                mouseLastX = e.getX();
                mouseLastY = e.getY();
                UIHandler.refreshWindow();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                Point cursorPointingPoint = getCursorPointingPoint(e.getX(), e.getY());
                ScaleAnchorOrientation orientation =
                        (cursorPointingPoint != null) ?
                                getCursorPointingAnchorOrientation(e.getX(), e.getY(), cursorPointingPoint)
                                : null;

                if (orientation != null) {
                    switch (orientation) {
                        case EAST:
                            setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                            break;
                        case SOUTH:
                            setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                            break;
                        case WEST:
                            setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                            break;
                        case NORTH:
                            setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                            break;
                        case NORTH_EAST:
                            setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                            break;
                        case NORTH_WEST:
                            setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                            break;
                        case SOUTH_EAST:
                            setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                            break;
                        case SOUTH_WEST:
                            setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                            break;
                    }
                    return;
                } else if (cursorPointingPoint != null) {
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    return;
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isDragging = false;
                UIHandler.refreshWindow();
            }
        };
    }
}