package Chreator.UIModule;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import Chreator.ObjectModel.Point;
import Chreator.UIModule.AbstractModel.ChessBoardGraphicAreaPanel;

/**
 * Created by root on 12/26/15.
 */
public class ChessBoardPointEditingGraphicAreaPanel extends ChessBoardGraphicAreaPanel {
    private int mouseStartX = 0, mouseStartY = 0, mouseLastX = 0, mouseLastY = 0;
    private ArrayList<Point> selectedPointBackupList, controlModeSelectionList;
    private EditingMode editingMode = EditingMode.SELECTING;
    private ScaleAnchorOrientation scaleOrientation;
    private Point scaleReferencePoint;
    private boolean isDragging = false;
    private int addPointGridGridHeight, addPointGridRowSize, addPointId, addPointGridFirstRow, addPointGridLastRow;
    private double pointStandardRelativeSize = 0.05;
    private boolean controlMode = false;

    public ChessBoardPointEditingGraphicAreaPanel() {
        controlModeSelectionList = new ArrayList<Point>();
        selectedPointBackupList = new ArrayList<Point>();
    }

    public void panelResized(ComponentEvent e) {
        if (getSize().width > 0 && getSize().height > 0) {
            if (boardWidth == 0 && boardHeight == 0)
                UIHandler.getInstance(null).getChessBoardPanel().setTextFieldImageSize(getSize().width / 2, getSize().height);
            updateAllPoints();
        }
    }

    public boolean setBoardImage(File file) {
        if (super.setBoardImage(file)) {
            ChessBoardPanel chessBoardPanel = UIHandler.getInstance(null).getChessBoardPanel();
            chessBoardPanel.setTextFieldImageSize(boardWidth, boardHeight);
            return true;
        } else
            return false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintComponentForChessBoard(g);
        paintComponentForBoardEdge(g);
        paintComponentForEdgeTriangles(g);
        paintComponentForAllPoints(g);
        paintComponentForScaleAnchorOfSelectedPoints(g);
        paintComponentForEdgeTriangleInfo(g);

        g.setColor(new Color(0, 127, 0));
        if (isDragging) {
            double leftX = (mouseStartX < mouseLastX) ? 1.0 * mouseStartX : 1.0 * mouseLastX,
                    rightX = (mouseStartX > mouseLastX) ? 1.0 * mouseStartX : 1.0 * mouseLastX,
                    topY = (mouseStartY < mouseLastY) ? 1.0 * mouseStartY : 1.0 * mouseLastY,
                    bottomY = (mouseStartY > mouseLastY) ? 1.0 * mouseStartY : 1.0 * mouseLastY;
            switch (editingMode) {
                case SELECTING:
                case RECT_GRID_ADDING:
                case TRI_GRID_ADDING:
                    g.drawRect(
                            (int) Math.round(leftX),
                            (int) Math.round(topY),
                            (int) Math.round(rightX - leftX),
                            (int) Math.round(bottomY - topY)
                    );
                    break;
            }

            g.setColor(new Color(0f, 1f, 0f, 0.5f));
            switch (editingMode) {
                case RECT_GRID_ADDING:
                    ArrayList<Point.InfoPack> rectGridInfoPack = getRectGridInfoPack();
                    for (Point.InfoPack infoPack : rectGridInfoPack)
                        g.fillRect(
                                (int) Math.round(infoPack.posX - infoPack.width / 2),
                                (int) Math.round(infoPack.posY - infoPack.height / 2),
                                (int) Math.round(infoPack.width),
                                (int) Math.round(infoPack.height)
                        );
                    break;
                case TRI_GRID_ADDING:
                    ArrayList<Point.InfoPack> triGridInfoPack = getTriGridInfoPack();
                    for (Point.InfoPack infoPack : triGridInfoPack)
                        g.fillRect(
                                (int) Math.round(infoPack.posX - infoPack.width / 2),
                                (int) Math.round(infoPack.posY - infoPack.height / 2),
                                (int) Math.round(infoPack.width),
                                (int) Math.round(infoPack.height)
                        );
                    break;
            }
        }

        g.setColor(Color.red);
        String s = "Add edge sequence: ";
        if (selectedPointList.size() > 1) s = s + selectedPointList.get(0).getId();
        for (int i = 1; i < selectedPointList.size(); i++)
            s = s + " \u2794 " + selectedPointList.get(i).getId();
        g.drawString(s, 0, (int) Math.round(getHeight() - g.getFont().getSize() / 2));
    }

    private ArrayList<Point.InfoPack> getTriGridInfoPack() {
        ArrayList<Point.InfoPack> list = new ArrayList<Point.InfoPack>();
        double leftX = (mouseStartX < mouseLastX) ? 1.0 * mouseStartX : 1.0 * mouseLastX,
                rightX = (mouseStartX > mouseLastX) ? 1.0 * mouseStartX : 1.0 * mouseLastX,
                topY = (mouseStartY < mouseLastY) ? 1.0 * mouseStartY : 1.0 * mouseLastY,
                bottomY = (mouseStartY > mouseLastY) ? 1.0 * mouseStartY : 1.0 * mouseLastY;
        Point tempPoint = new Point(this, -1);
        tempPoint.setSizeByRelative(0, 0,
                (boardWidth <= boardHeight) ? pointStandardRelativeSize : pointStandardRelativeSize * boardHeight / boardWidth,
                (boardHeight <= boardWidth) ? pointStandardRelativeSize : pointStandardRelativeSize * boardWidth / boardHeight);
        double dotSize = tempPoint.getPixelInformation().height;

        if (addPointGridLastRow > addPointGridFirstRow) {
            for (int i = addPointGridFirstRow; i <= addPointGridLastRow; i++) {
                for (int j = 0; j < i; j++) {
                    double posX, posY, diff = (rightX - leftX) / (addPointGridLastRow - 1);

                    posY = topY + (bottomY - topY) / (addPointGridLastRow - addPointGridFirstRow) * (i - addPointGridFirstRow);
                    posX = leftX + diff * (addPointGridLastRow - i) / 2 + j * diff;

                    list.add(new Point.InfoPack(posX, posY, dotSize, dotSize));
                }
            }
        } else if (addPointGridFirstRow > addPointGridLastRow) {
            for (int i = addPointGridFirstRow; i >= addPointGridLastRow; i--) {
                for (int j = 0; j < i; j++) {
                    double posX, posY, diff = (rightX - leftX) / (addPointGridFirstRow - 1);

                    posY = topY + (bottomY - topY) / (addPointGridFirstRow - addPointGridLastRow) * (addPointGridFirstRow - i);
                    posX = leftX + diff * (addPointGridFirstRow - i) / 2 + j * diff;

                    list.add(new Point.InfoPack(posX, posY, dotSize, dotSize));
                }
            }
        } else {
            for (int i = 0; i < addPointGridFirstRow; i++) {
                double posX = (addPointGridFirstRow == 1) ? (leftX + rightX) / 2 : leftX + (rightX - leftX) / (addPointGridFirstRow - 1) * i,
                        posY = (topY + bottomY) / 2;
                list.add(new Point.InfoPack(posX, posY, dotSize, dotSize));
            }
        }
        return list;
    }

    private ArrayList<Point.InfoPack> getRectGridInfoPack() {
        ArrayList<Point.InfoPack> list = new ArrayList<Point.InfoPack>();
        double leftX = (mouseStartX < mouseLastX) ? 1.0 * mouseStartX : 1.0 * mouseLastX,
                rightX = (mouseStartX > mouseLastX) ? 1.0 * mouseStartX : 1.0 * mouseLastX,
                topY = (mouseStartY < mouseLastY) ? 1.0 * mouseStartY : 1.0 * mouseLastY,
                bottomY = (mouseStartY > mouseLastY) ? 1.0 * mouseStartY : 1.0 * mouseLastY;

        Point tempPoint = new Point(this, -1);
        tempPoint.setSizeByRelative(0, 0,
                (boardWidth <= boardHeight) ? pointStandardRelativeSize : pointStandardRelativeSize * boardHeight / boardWidth,
                (boardHeight <= boardWidth) ? pointStandardRelativeSize : pointStandardRelativeSize * boardWidth / boardHeight);
        double dotSize = tempPoint.getPixelInformation().height;

        for (int i = 0; i < addPointGridRowSize; i++) {
            for (int j = 0; j < addPointGridGridHeight; j++) {
                double posX, posY;

                if (addPointGridGridHeight == 1 && addPointGridRowSize == 1) {
                    posX = (leftX + rightX) / 2;
                    posY = (topY + bottomY) / 2;
                } else if (addPointGridRowSize == 1) {
                    posX = (leftX + rightX) / 2;
                    posY = topY + (bottomY - topY) / (addPointGridGridHeight - 1) * j;
                } else if (addPointGridGridHeight == 1) {
                    posX = leftX + (rightX - leftX) / (addPointGridRowSize - 1) * i;
                    posY = (topY + bottomY) / 2;
                } else {
                    posX = leftX + (rightX - leftX) / (addPointGridRowSize - 1) * i;
                    posY = topY + (bottomY - topY) / (addPointGridGridHeight - 1) * j;
                }
                list.add(new Point.InfoPack(posX, posY, dotSize, dotSize));
            }
        }

        return list;
    }

    public boolean addSinglePoint(int id) {
        if (!verifyPointID(id)) return false;
        addPointId = id;
        Point p = new Point(ChessBoardPointEditingGraphicAreaPanel.this, addPointId);
        double wPercent = pointStandardRelativeSize, hPercent = pointStandardRelativeSize;
        if (1.0 * boardHeight / boardWidth > 1)
            hPercent = wPercent * boardWidth / boardHeight;
        else if (1.0 * boardHeight / boardWidth < 1)
            wPercent = hPercent * boardHeight / boardWidth;
        p.setSizeByRelative(1.0 * (addPointId % 10) / 10 + pointStandardRelativeSize / 2,
                1.0 * ((addPointId / 10) % 10) / 10 + pointStandardRelativeSize / 2,
                wPercent, hPercent);
        pointList.add(p);
        selectedPointList.clear();
        selectedPointList.add(p);

        editingMode = EditingMode.SELECTING;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        UIHandler.getInstance(null).getChessBoardPanel().setNextPointIdField(getNextUsableId());
        repaint();
        return true;
    }
    
    public Point addSinglePoint(double posX, double posY, double width, double height, int id) {
    	Point p = new Point(ChessBoardPointEditingGraphicAreaPanel.this, posX,posY, width, height, id);
        pointList.add(p);
        selectedPointList.clear();
        selectedPointList.add(p);

        editingMode = EditingMode.SELECTING;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        UIHandler.getInstance(null).getChessBoardPanel().setNextPointIdField(getNextUsableId());
        UIHandler.getInstance(null).getChessBoardPanel().setNextPointIdField(getNextUsableId());
        repaint();
        return p;
    }

    public boolean addRectGrid(int id, int row, int column) {
        if (!verifyPointID(id)) return false;
        editingMode = EditingMode.RECT_GRID_ADDING;
        addPointGridGridHeight = row;
        addPointGridRowSize = column;
        addPointId = id;
        return true;
    }

    public boolean addTriGrid(int id, int firstRow, int lastRow) {
        if (!verifyPointID(id)) return false;
        editingMode = EditingMode.TRI_GRID_ADDING;
        addPointGridLastRow = lastRow;
        addPointGridFirstRow = firstRow;
        addPointId = id;
        return true;
    }

    public void deletePoints() {
        for (Point p : pointList) {
            if (!selectedPointList.contains(p)) {
                for (Point toBeRemove : selectedPointList)
                    p.removeEdgePointPairs(toBeRemove);
            }
        }
        for (Point p : selectedPointList)
            pointList.remove(p);
        selectedPointList.clear();
        UIHandler.getInstance(null).getChessBoardPanel().setNextPointIdField(getOptimizedNextUsableId());
        repaint();
    }

    protected void updateAllPoints() {
        ChessBoardPanel.PointScaleMode mode = UIHandler.getInstance(null).getChessBoardPanel().getPointScaleMode();
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
        repaint();
    }

    protected MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                requestFocus();

                if (editingMode == EditingMode.RECT_GRID_ADDING || editingMode == EditingMode.TRI_GRID_ADDING) {
                    selectedPointList.clear();
                } else {
                    editingMode = EditingMode.SELECTING;

                    Point cursorPointingPoint = getCursorPointingPoint(e.getX(), e.getY());
                    ScaleAnchorOrientation orientation =
                            (cursorPointingPoint != null) && selectedPointList.contains(cursorPointingPoint)?
                                    getCursorPointingAnchorOrientation(e.getX(), e.getY(), cursorPointingPoint) :
                                    null;

                    for (Point p : selectedPointList)
                        selectedPointBackupList.add(p);

                    if (controlMode) {
                        if (cursorPointingPoint != null) {
                            if (!controlModeSelectionList.contains(cursorPointingPoint))
                                controlModeSelectionList.add(cursorPointingPoint);

                            if (selectedPointList.contains(cursorPointingPoint))
                                selectedPointList.remove(cursorPointingPoint);
                            else
                                selectedPointList.add(cursorPointingPoint);

                            pointList.remove(cursorPointingPoint);
                            pointList.add(cursorPointingPoint);
                        }
                    } else {
                        if (cursorPointingPoint == null) {
                            selectedPointList.clear();
                        } else {
                            editingMode = (orientation == null) ? EditingMode.TRANSLATION : EditingMode.SCALING;
                            scaleReferencePoint = cursorPointingPoint;
                            scaleOrientation = orientation;

                            if (!selectedPointList.contains(cursorPointingPoint)) {
                                selectedPointList.clear();
                                selectedPointList.add(cursorPointingPoint);
                                pointList.remove(cursorPointingPoint);
                                pointList.add(cursorPointingPoint);
                            }
                        }
                    }
                }

                mouseStartX = e.getX();
                mouseStartY = e.getY();
                mouseLastX = e.getX();
                mouseLastY = e.getY();
                repaint();
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
                } else if (editingMode == EditingMode.SELECTING) {
                    double
                            leftX = (mouseStartX < e.getX()) ? mouseStartX : e.getX(),
                            rightX = (mouseStartX > e.getX()) ? mouseStartX : e.getX(),
                            topY = (mouseStartY < e.getY()) ? mouseStartY : e.getY(),
                            bottomY = (mouseStartY > e.getY()) ? mouseStartY : e.getY();

                    ArrayList<Point> toBeAddedList = new ArrayList<Point>();
                    if (controlMode) selectedPointList.clear();
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


                        if (controlMode) {
                            if (leftCount < 2 && topCount < 2 && bottomCount < 2 && rightCount < 2) {
                                if (!controlModeSelectionList.contains(p))
                                    toBeAddedList.add(p);
                            } else if (controlModeSelectionList.contains(p))
                                controlModeSelectionList.remove(p);

                        } else {
                            if (leftCount < 2 && topCount < 2 && bottomCount < 2 && rightCount < 2) {
                                if (!selectedPointList.contains(p))
                                    toBeAddedList.add(p);
                            } else if (selectedPointList.contains(p)) selectedPointList.remove(p);
                        }
                    }
                    if (controlMode) {
                        selectedPointList.clear();
                        for (int i = toBeAddedList.size() - 1; i >= 0; i--) {
                            Point p = toBeAddedList.get(i);
                            if (!controlModeSelectionList.contains(p))
                                controlModeSelectionList.add(p);
                        }
                        for (Point p : selectedPointBackupList) {
                            if (!controlModeSelectionList.contains(p)) selectedPointList.add(p);
                        }
                        for (Point p : controlModeSelectionList) {
                            if (!selectedPointBackupList.contains(p))
                                selectedPointList.add(p);
                        }
                        for (Point p : selectedPointList) {
                            pointList.remove(p);
                            pointList.add(p);
                        }

                    } else {
                        for (int i = toBeAddedList.size() - 1; i >= 0; i--) {
                            Point p = toBeAddedList.get(i);
                            selectedPointList.add(p);
                            pointList.remove(p);
                            pointList.add(p);
                        }
                    }
                    toBeAddedList.clear();
                }

                mouseLastX = e.getX();
                mouseLastY = e.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                EdgeTriangleInfo triangleInfo = showTriangleInfo;
                showTriangleInfo = null;
                if (controlMode) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                if (editingMode == EditingMode.RECT_GRID_ADDING || editingMode == EditingMode.TRI_GRID_ADDING) {
                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    if (triangleInfo != showTriangleInfo) repaint();
                    return;
                }
                showTriangleInfo = getCursorPointingEdgeTriangle(e.getX(), e.getY());
                if (triangleInfo != showTriangleInfo) repaint();
                Point cursorPointingPoint = getCursorPointingPoint(e.getX(), e.getY());
                ScaleAnchorOrientation orientation =
                        (cursorPointingPoint != null) && selectedPointList.contains(cursorPointingPoint)?
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
                ChessBoardPanel chessBoardPanel = UIHandler.getInstance(null).getChessBoardPanel();

                if (editingMode == EditingMode.RECT_GRID_ADDING) {
                    ArrayList<Point.InfoPack> infoPacks = getRectGridInfoPack();

                    for (Point.InfoPack infoPack : infoPacks) {
                        Point p = new Point(ChessBoardPointEditingGraphicAreaPanel.this, getNextUsableId());
                        p.setSizeByPixel(infoPack.posX, infoPack.posY, infoPack.width, infoPack.height);
                        pointList.add(p);
                        selectedPointList.add(p);
                    }

                    if (infoPacks.size() > 1) {
                        if (infoPacks.get(0).posX == infoPacks.get(1).posX) { //vertical mode
                            for (int i = 0; i < addPointGridRowSize; i++) {
                                for (int j = 0; j < addPointGridGridHeight; j++) {
                                    if (i > 0)
                                        selectedPointList.get(i * addPointGridGridHeight + j).
                                                addEdgePointPairs("WEST", selectedPointList.get((i - 1) * addPointGridGridHeight + j));
                                    if (j > 0)
                                        selectedPointList.get(i * addPointGridGridHeight + j).
                                                addEdgePointPairs("NORTH", selectedPointList.get(i * addPointGridGridHeight + j - 1));
                                    if (i < addPointGridRowSize - 1)
                                        selectedPointList.get(i * addPointGridGridHeight + j).
                                                addEdgePointPairs("EAST", selectedPointList.get((i + 1) * addPointGridGridHeight + j));
                                    if (j < addPointGridGridHeight - 1)
                                        selectedPointList.get(i * addPointGridGridHeight + j).
                                                addEdgePointPairs("SOUTH", selectedPointList.get(i * addPointGridGridHeight + j + 1));
                                }
                            }
                        } else { //horizontal mode
                            for (int i = 0; i < addPointGridGridHeight; i++) {
                                for (int j = 0; j < addPointGridRowSize; j++) {
                                    if (i > 0)
                                        selectedPointList.get(i * addPointGridRowSize + j).
                                                addEdgePointPairs("NORTH", selectedPointList.get((i - 1) * addPointGridRowSize + j));
                                    if (j > 0)
                                        selectedPointList.get(i * addPointGridRowSize + j).
                                                addEdgePointPairs("WEST", selectedPointList.get(i * addPointGridRowSize + j - 1));
                                    if (i < addPointGridGridHeight - 1)
                                        selectedPointList.get(i * addPointGridRowSize + j).
                                                addEdgePointPairs("SOUTH", selectedPointList.get((i + 1) * addPointGridRowSize + j));
                                    if (j < addPointGridRowSize - 1)
                                        selectedPointList.get(i * addPointGridRowSize + j).
                                                addEdgePointPairs("EAST", selectedPointList.get(i * addPointGridRowSize + j + 1));
                                }
                            }
                        }
                    }

                    chessBoardPanel.setNextPointIdField(getNextUsableId());
                    editingMode = EditingMode.SELECTING;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                } else if (editingMode == EditingMode.TRI_GRID_ADDING) {
                    ArrayList<Point.InfoPack> infoPacks = getTriGridInfoPack();
                    for (Point.InfoPack infoPack : infoPacks) {
                        Point p = new Point(ChessBoardPointEditingGraphicAreaPanel.this, getNextUsableId());
                        p.setSizeByPixel(infoPack.posX, infoPack.posY, infoPack.width, infoPack.height);
                        pointList.add(p);
                        selectedPointList.add(p);
                    }

                    int pointIndex = 0;
                    if (addPointGridLastRow > addPointGridFirstRow) {
                        for (int i = addPointGridFirstRow; i <= addPointGridLastRow; i++) {
                            for (int j = 0; j < i; j++) {
                                if (j > 0) {
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_180", selectedPointList.get(pointIndex - 1));
                                    if (i > addPointGridFirstRow)
                                        selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_120", selectedPointList.get(pointIndex - i));

                                }
                                if (j < i - 1) {
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_0", selectedPointList.get(pointIndex + 1));
                                    if (i > addPointGridFirstRow)
                                        selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_60", selectedPointList.get(pointIndex - i + 1));
                                }
                                if (i < addPointGridLastRow) {
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_240", selectedPointList.get(pointIndex + i));
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_300", selectedPointList.get(pointIndex + +i + 1));
                                }
                                pointIndex++;
                            }
                        }
                    } else if (addPointGridFirstRow > addPointGridLastRow) {
                        for (int i = addPointGridFirstRow; i >= addPointGridLastRow; i--) {
                            for (int j = 0; j < i; j++) {
                                if (j > 0) {
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_180", selectedPointList.get(pointIndex - 1));
                                    if (i > addPointGridLastRow)
                                        selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_240", selectedPointList.get(pointIndex + i - 1));
                                }
                                if (j < i - 1) {
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_0", selectedPointList.get(pointIndex + 1));
                                    if (i > addPointGridLastRow)
                                        selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_300", selectedPointList.get(pointIndex + i));
                                }
                                if (i < addPointGridFirstRow) {
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_120", selectedPointList.get(pointIndex - i - 1));
                                    selectedPointList.get(pointIndex).addEdgePointPairs("ANGLE_60", selectedPointList.get(pointIndex - i));
                                }
                                pointIndex++;
                            }
                        }
                    } else {
                        for (int i = 0; i < selectedPointList.size(); i++) {
                            if (i > 0)
                                selectedPointList.get(i).addEdgePointPairs("ANGLE_180", selectedPointList.get(i - 1));
                            if (i < selectedPointList.size() - 1)
                                selectedPointList.get(i).addEdgePointPairs("ANGLE_0", selectedPointList.get(i + 1));
                        }
                    }

                    chessBoardPanel.setNextPointIdField(getNextUsableId());
                    editingMode = EditingMode.SELECTING;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                } else if (editingMode == EditingMode.TRANSLATION || editingMode == EditingMode.SCALING) {
                    for (Point p : pointList) {
                        Point.InfoPack infoPack = p.getPixelInformation();
                        double x = infoPack.posX, y = infoPack.posY;
                        x = (x < 0 ? 0 : x);
                        x = (x > ChessBoardPointEditingGraphicAreaPanel.this.getWidth() ? ChessBoardPointEditingGraphicAreaPanel.this.getWidth() : x);
                        y = (y < 0 ? 0 : y);
                        y = (y > ChessBoardPointEditingGraphicAreaPanel.this.getHeight() ? ChessBoardPointEditingGraphicAreaPanel.this.getHeight() : y);
                        p.setSizeByPixel(x, y, infoPack.width, infoPack.height);
                    }
                } else if (editingMode == EditingMode.SELECTING) {
                    controlModeSelectionList.clear();
                    selectedPointBackupList.clear();
                }
                repaint();
            }
        };
    }

    protected KeyAdapter createKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                ChessBoardPanel chessBoardPanel = UIHandler.getInstance(null).getChessBoardPanel();
                switch (e.getKeyCode()) {
                    case 17:
                        if (!controlMode)
                            selectedPointBackupList.clear();

                        controlMode = true;
                        break;
                    case 65:
                        chessBoardPanel.addPointAction();
                        break;
                    case 67:
                        chessBoardPanel.copyAndPastePointButtonAction();
                        break;
                    case 68:
                        chessBoardPanel.deletePointButtonAction();
                        break;
                    case 69:
                        chessBoardPanel.createEdgeForPointsButtonAction();
                        break;
                    case 82:
                        chessBoardPanel.deleteEdgeForPointsButtonAction();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                switch (e.getKeyCode()) {
                    case 17:
                        controlMode = false;
                        controlModeSelectionList.clear();
                        break;

                }
            }
        };
    }

    private int getNextUsableId() {
        if (pointList.size() > 0) {
            int[] idList = new int[pointList.size()];

            for (int i = 0; i < pointList.size(); i++)
                idList[i] = pointList.get(i).getId();
            Arrays.sort(idList);

            int index;
            do {
                addPointId++;
                for (index = 0; index < idList.length; index++) {
                    if (addPointId == idList[index]) break;
                }
            } while (index < pointList.size());
        }
        return addPointId;
    }

    private int getOptimizedNextUsableId() {
        addPointId = -1;
        if (pointList.size() > 0) {
            getNextUsableId();
        } else {
            addPointId++;
        }
        return addPointId;
    }

    private boolean verifyPointID(int id) {
        for (Point p : pointList)
            if (p.getId() == id) return false;
        return true;
    }

    public void copyAndPasteSelectedPoint() {
        ArrayList<Point> copyList = new ArrayList<Point>();
        double minX = 1, minY = 1, maxX = 0, maxY = 0;
        for (Point p : selectedPointList) {
            p.getPixelInformation();
            Point np = new Point(this, addPointId);
            Point.InfoPack infoPack = p.getRelativeInformation();
            np.setSizeByRelative(infoPack.posX + 0.05, infoPack.posY + 0.05, infoPack.width, infoPack.height);

            if (infoPack.posX + 0.05 < minX) minX = infoPack.posX + 0.05;
            if (infoPack.posX + 0.05 > maxX) maxX = infoPack.posX + 0.05;
            if (infoPack.posY + 0.05 < minY) minY = infoPack.posY + 0.05;
            if (infoPack.posY + 0.05 > maxY) maxY = infoPack.posY + 0.05;

            copyList.add(np);
            addPointId = getNextUsableId();
        }

        if (maxX > 1 || maxY > 1) {
            for (Point p : copyList) {
                Point.InfoPack infoPack = p.getRelativeInformation();
                p.setSizeByRelative(infoPack.posX - minX + 0.05, infoPack.posY - minY + 0.05, infoPack.width, infoPack.height);
            }
        }

        for (int i = 0; i < selectedPointList.size(); i++) {
            Point p = selectedPointList.get(i);
            for (Point.EdgePointPair edgePointPair : p.getAllEdgePointPair()) {
                int tar;
                for (tar = 0; tar < selectedPointList.size(); tar++) {
                    if (edgePointPair.targetPoint == selectedPointList.get(tar)) break;
                }
                if (tar < copyList.size())
                    copyList.get(i).addEdgePointPairs(edgePointPair.direction, copyList.get(tar));
            }
        }

        for (Point p : copyList)
            pointList.add(p);

        selectedPointList = copyList;
        UIHandler.getInstance(null).getChessBoardPanel().setNextPointIdField(addPointId);
        repaint();
    }

    public void createEdgesForSelectedPoints(String direction) {
        String msg = "<html>";
        for (int i = 0; i < selectedPointList.size(); i++) {
            msg = msg + selectedPointList.get(i).getId();
            if (i < selectedPointList.size() - 1) msg = msg + " \u2794 ";
            else msg = msg + "</html>";
            if (i % 10 == 9) msg = msg + "<br>";
        }

        int result = JOptionPane.showConfirmDialog(UIHandler.getMainWindow(),
                "<html><center>Are you sure to add edge with direction " + direction + " for points in the following order?<br>"
                        + msg + "</html>", "Add edge for points - Chreator", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            for (int i = 0; i < selectedPointList.size() - 1; i++)
                selectedPointList.get(i).addEdgePointPairs(direction, selectedPointList.get(i + 1));
            repaint();
        }
    }

    public void deleteEdgesForSelectedPoints() {
        for (Point p1 : selectedPointList)
            for (Point p2 : selectedPointList)
                if (p1 != p2) p1.removeEdgePointPairs(p2);
        repaint();
    }

}