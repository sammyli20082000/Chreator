package Chreator.UIModule;

import java.awt.Color;
import java.awt.Cursor;
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

/**
 * Created by root on 1/28/16.
 */
public class ChessPieceSetSizeGraphicAreaPanel extends ChessBoardGraphicAreaPanel {

    public interface OnSetSizeCallBack {
        public void onSetSize(double relativeWidth, double relativeHeight);
    }

    public static class EditablePiece extends Point {
        private BufferedImage pieceImage;
        private Color color;
        private String className;

        public EditablePiece(ChessBoardGraphicAreaPanel graphicAreaPanel, int id) {
            super(graphicAreaPanel, id);
        }

        public void setPieceImage(BufferedImage image) {
            pieceImage = image;
        }

        public BufferedImage getPieceImage() {
            return pieceImage;
        }

        public void setColor(Color c) {
            color = c;
        }

        public Color getColor() {
            return color;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    private EditablePiece editablePiece;
    private EditingMode editingMode = EditingMode.SELECTING;
    private int mouseLastX, mouseLastY;
    private ScaleAnchorOrientation anchorOrientation;
    private OnSetSizeCallBack callBack;

    public ChessPieceSetSizeGraphicAreaPanel(OnSetSizeCallBack onSetSizeCallBack) {
        editablePiece = new EditablePiece(this, -1);
        callBack = onSetSizeCallBack;
    }

    @Override
    protected void panelResized(ComponentEvent e) {
        updateAllPoints();
    }

    @Override
    protected MouseAdapter createMouseAdapter() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                anchorOrientation =
                        getCursorPointingAnchorOrientation(event.getX(), event.getY(), editablePiece);
                if (anchorOrientation != null) {
                    editingMode = EditingMode.SCALING;
                } else if (isCursorOnPoint(event.getX(), event.getY(), editablePiece)) {
                    editingMode = EditingMode.TRANSLATION;
                } else {
                    editingMode = EditingMode.SELECTING;
                }
                mouseLastX = event.getX();
                mouseLastY = event.getY();
            }

            public void mouseReleased(MouseEvent event) {
                if (editingMode == EditingMode.SCALING) {
                    Point.InfoPack infoPack = editablePiece.getRelativeInformation();
                    callBack.onSetSize(infoPack.width, infoPack.height);
                }
                editingMode = EditingMode.SELECTING;
            }

            public void mouseMoved(MouseEvent event) {
                EdgeTriangleInfo backupInfo = showTriangleInfo;
                showTriangleInfo = null;

                if (editingMode == EditingMode.SELECTING && !isCursorOnPoint(event.getX(), event.getY(), editablePiece))
                    showTriangleInfo = getCursorPointingEdgeTriangle(event.getX(), event.getY());

                ScaleAnchorOrientation anchorOrientation = getCursorPointingAnchorOrientation(event.getX(), event.getY(), editablePiece);
                if (anchorOrientation != null) {
                    switch (anchorOrientation) {
                        case NORTH:
                            setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                            break;
                        case EAST:
                            setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                            break;
                        case SOUTH:
                            setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                            break;
                        case WEST:
                            setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
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
                } else if (isCursorOnPoint(event.getX(), event.getY(), editablePiece))
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                else
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                if (backupInfo != showTriangleInfo) repaint();

            }

            public void mouseDragged(MouseEvent event) {
                Point.InfoPack info = editablePiece.getPixelInformation();

                if (editingMode == EditingMode.TRANSLATION) {
                    double newPosX = info.posX + event.getX() - mouseLastX,
                            newPosY = info.posY + event.getY() - mouseLastY;

                    if (newPosX < 0)
                        newPosX = 0;
                    else if (newPosX > getWidth())
                        newPosX = getWidth();

                    if (newPosY < 0)
                        newPosY = 0;
                    else if (newPosY > getHeight())
                        newPosY = getHeight();

                    editablePiece.setSizeByPixel(newPosX, newPosY, info.width, info.height);
                } else if (editingMode == editingMode.SCALING) {
                    double newPosX = info.posX, newPosY = info.posY, newHeight = info.height, newWidth = info.width;

                    switch (anchorOrientation) {
                        case NORTH_EAST:
                        case EAST:
                        case SOUTH_EAST:
                            newWidth = newWidth + event.getX() - mouseLastX;
                            if (newWidth < 1) newWidth = 1;
                            newPosX = info.posX - info.width / 2 + newWidth / 2;
                            break;
                        case SOUTH_WEST:
                        case WEST:
                        case NORTH_WEST:
                            newWidth = newWidth + mouseLastX - event.getX();
                            if (newWidth < 1) newWidth = 1;
                            newPosX = info.posX + info.width / 2 - newWidth / 2;
                            break;
                    }

                    switch (anchorOrientation) {
                        case NORTH_WEST:
                        case NORTH:
                        case NORTH_EAST:
                            newHeight = newHeight + mouseLastY - event.getY();
                            if (newHeight < 1) newHeight = 1;
                            newPosY = info.posY + info.height / 2 - newHeight / 2;
                            break;
                        case SOUTH_EAST:
                        case SOUTH:
                        case SOUTH_WEST:
                            newHeight = newHeight + event.getY() - mouseLastY;
                            if (newHeight < 1) newHeight = 1;
                            newPosY = info.posY + newHeight / 2 - info.height / 2;
                            break;
                    }

                    if (newPosX < 0) newPosX = 0;
                    if (newPosY < 0) newPosY = 0;
                    if (newPosX > getWidth()) newPosX = getWidth();
                    if (newPosY > getHeight()) newPosY = getHeight();

                    editablePiece.setSizeByPixel(newPosX, newPosY, newWidth, newHeight);
                }

                mouseLastX = event.getX();
                mouseLastY = event.getY();
                repaint();
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
        editablePiece.updatePixelByRelative();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintComponentForChessBoard(g);
        paintComponentForBoardEdge(g);
        paintComponentForEdgeTriangles(g);
        paintComponentForAllPoints(g);

        Point.InfoPack infoPack = editablePiece.getPixelInformation();
        if (editablePiece.getPieceImage() != null)
            g.drawImage(editablePiece.getPieceImage(),
                    (int) Math.round(infoPack.posX - 0.5 * infoPack.width),
                    (int) Math.round(infoPack.posY - 0.5 * infoPack.height),
                    (int) Math.round(infoPack.width),
                    (int) Math.round(infoPack.height),
                    null);
        else {
            g.setColor(editablePiece.getColor() == null ? new Color(0, 0, 0) : editablePiece.getColor());
            g.fillOval(
                    (int) Math.round(infoPack.posX - 0.5 * infoPack.width),
                    (int) Math.round(infoPack.posY - 0.5 * infoPack.height),
                    (int) Math.round(infoPack.width),
                    (int) Math.round(infoPack.height)
            );
            g.setColor(new Color(255 - g.getColor().getRed(), 255 - g.getColor().getGreen(), 255 - g.getColor().getBlue()));

            int stringWidth = g.getFontMetrics().stringWidth(editablePiece.getClassName()), stringHeight = g.getFont().getSize();
            g.setColor(new Color(
                    (editablePiece.color.getRed() + 128) % 256,
                    (editablePiece.color.getGreen() + 128) % 256,
                    (editablePiece.color.getBlue() + 128) % 256

            ));
            g.drawString(editablePiece.getClassName(),
                    (int) Math.round(infoPack.posX) - stringWidth / 2,
                    (int) Math.round(infoPack.posY) + stringHeight / 2);
        }

        ArrayList<ScaleAnchorInfo> scaleAnchorInfos = getPointScaleAnchor(editablePiece);
        g.setColor(Color.green);
        for (ScaleAnchorInfo info : scaleAnchorInfos)
            g.fillRect(info.posXTopLeft, info.posYTopLeft, info.width, info.height);

        paintComponentForEdgeTriangleInfo(g);
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
            for (Point.EdgePointPair edgePointPair : p.getAllEdgePointPair()) {
                Point targetPoint = findPointInListById(pointList, edgePointPair.targetPoint.getId());
                if (targetPoint != null) np.addEdgePointPairs(edgePointPair.direction, targetPoint);
            }
        }
    }

    public void setPiece(double width, double height, BufferedImage image, Color color, String className) {
        editablePiece.setPieceImage(image);
        editablePiece.setColor(color);
        editablePiece.setClassName(className);
        editablePiece.setSizeByRelative(0.5, 0.5, width, height);
    }
}
