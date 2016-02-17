package Chreator.UIModule.AbstractModel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Chreator.ObjectModel.Point;
import Chreator.UIModule.ChessBoardPanel;
import Chreator.UIModule.UIHandler;

/**
 * Created by him on 2016/2/2.
 */
public abstract class ChessBoardGraphicAreaPanel extends JPanel {
    public enum EditingMode {SELECTING, SCALING, TRANSLATION, RECT_GRID_ADDING, TRI_GRID_ADDING}

    public enum ScaleAnchorOrientation {EAST, SOUTH, WEST, NORTH, SOUTH_EAST, SOUTH_WEST, NORTH_EAST, NORTH_WEST}

    public static class ScaleAnchorInfo {
        public int width, height, posXTopLeft, posYTopLeft;
        public ScaleAnchorOrientation orientation;

        public ScaleAnchorInfo(int posXTopLeft, int posYTopLeft, int width, int height, ScaleAnchorOrientation orientation) {
            this.posXTopLeft = posXTopLeft;
            this.posYTopLeft = posYTopLeft;
            this.width = width;
            this.height = height;
            this.orientation = orientation;
        }
    }

    public static class EdgeTriangleInfo {
        public double pos1X, pos1Y, pos2X, pos2Y, pos3X, pos3Y;
        public int sourcePointId, targetPointId;
        public String direction;
        public int mouseX, mouseY;

        public void setPoints(double pos1X, double pos1Y, double pos2X, double pos2Y, double pos3X, double pos3Y) {
            this.pos1X = pos1X;
            this.pos1Y = pos1Y;
            this.pos2X = pos2X;
            this.pos2Y = pos2Y;
            this.pos3X = pos3X;
            this.pos3Y = pos3Y;
        }

        public void setSourceTargetRelation(int sourcePointId, int targetPointId, String orientation) {
            this.sourcePointId = sourcePointId;
            this.targetPointId = targetPointId;
            this.direction = orientation;
        }

        public void setMouseLocation(int x, int y) {
            mouseX = x;
            mouseY = y;
        }
    }

    protected BufferedImage boardImage;
    protected int boardWidth, boardHeight;
    protected ArrayList<Point> pointList, selectedPointList;
    protected EdgeTriangleInfo showTriangleInfo;

    public ChessBoardGraphicAreaPanel() {
        setBackground(Color.DARK_GRAY);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panelResized(e);
            }
        });

        pointList = new ArrayList<Point>();
        selectedPointList = new ArrayList<Point>();

        MouseAdapter mouseAdapter = createMouseAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addKeyListener(createKeyAdapter());
        setFocusable(true);
    }

    protected EdgeTriangleInfo getCursorPointingEdgeTriangle(int mouseX, int mouseY) {
        ArrayList<EdgeTriangleInfo> triangleInfos = getAllEdgeTriangleInfo();
        for (EdgeTriangleInfo info : triangleInfos) {
            if (getTriangleArea(1.0 * mouseX, 1.0 * mouseY, info.pos2X, info.pos2Y, info.pos3X, info.pos3Y) +
                    getTriangleArea(info.pos1X, info.pos1Y, 1.0 * mouseX, 1.0 * mouseY, info.pos3X, info.pos3Y) +
                    getTriangleArea(info.pos1X, info.pos1Y, info.pos2X, info.pos2Y, 1.0 * mouseX, 1.0 * mouseY)
                    <=
                    getTriangleArea(info.pos1X, info.pos1Y, info.pos2X, info.pos2Y, info.pos3X, info.pos3Y) + 1) {
                info.setMouseLocation(mouseX, mouseY);
                return info;
            }
        }
        return null;
    }

    protected double getTriangleArea(double xa, double ya, double xb, double yb, double xc, double yc) {
        return 0.5 * Math.abs(xa * (yb - yc) + xb * (yc - ya) + xc * (ya - yb));
    }

    protected ArrayList<EdgeTriangleInfo> getAllEdgeTriangleInfo() {
        ArrayList<EdgeTriangleInfo> list = new ArrayList<EdgeTriangleInfo>();
        for (Point source : pointList) {
            ArrayList<Point.EdgePointPair> edgePointPairs = source.getAllEdgePointPair();
            Point.InfoPack sourceInfo = source.getPixelInformation();
            for (Point.EdgePointPair edgePointPair : edgePointPairs) {
                Point targetPoint = edgePointPair.targetPoint;
                Point.InfoPack targetInfo = targetPoint.getPixelInformation();

                if (!(targetInfo.posX - sourceInfo.posX == 0 && targetInfo.posY - sourceInfo.posY == 0)) {
                    EdgeTriangleInfo triangleInfo = new EdgeTriangleInfo();

                    double triangleSize = 15.0,
                            edgeLength = Math.sqrt((targetInfo.posX - sourceInfo.posX) * (targetInfo.posX - sourceInfo.posX) +
                                    (targetInfo.posY - sourceInfo.posY) * (targetInfo.posY - sourceInfo.posY)),
                            refX = sourceInfo.posX + (targetInfo.posX - sourceInfo.posX) * (0.5 - triangleSize / edgeLength),
                            refY = sourceInfo.posY + (targetInfo.posY - sourceInfo.posY) * (0.5 - triangleSize / edgeLength),
                            p1x = refX + triangleSize / edgeLength * (targetInfo.posX - sourceInfo.posX),
                            p1y = refY + triangleSize / edgeLength * (targetInfo.posY - sourceInfo.posY),
                            p2x = refX + triangleSize / edgeLength / 2 * (targetInfo.posY - sourceInfo.posY),
                            p2y = refY - triangleSize / edgeLength / 2 * (targetInfo.posX - sourceInfo.posX),
                            p3x = refX - triangleSize / edgeLength / 2 * (targetInfo.posY - sourceInfo.posY),
                            p3y = refY + triangleSize / edgeLength / 2 * (targetInfo.posX - sourceInfo.posX);

                    triangleInfo.setSourceTargetRelation(source.getId(), targetPoint.getId(), edgePointPair.direction);
                    triangleInfo.setPoints(p1x, p1y, p2x, p2y, p3x, p3y);
                    list.add(triangleInfo);
                }
            }
        }
        return list;
    }

    protected ArrayList<ScaleAnchorInfo> getPointScaleAnchor(Point p) {
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

    public boolean setBoardImage(BufferedImage image) {
        if (image == null)
            return false;
        boardImage = image;
        boardWidth = boardImage.getWidth();
        boardHeight = boardImage.getHeight();
        return true;
    }

    public boolean setBoardImage(File file) {
        try {
            boardImage = ImageIO.read(file);
            boardHeight = boardImage.getHeight();
            boardWidth = boardImage.getWidth();
            updateAllPoints();
            repaint();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            boardImage = null;
            repaint();
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
        repaint();
        return true;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public BufferedImage getBoardImage() {
        return boardImage;
    }

    public Dimension getChessBoardPreferredSize() {
        return new Dimension(boardWidth, boardHeight);
    }

    public ArrayList<Point> getPointList() {
        return pointList;
    }

    protected void paintComponentForChessBoard(Graphics g){
        double panelTangent = 1.0 * getHeight() / getWidth(), boardTangent = 1.0 * boardHeight / boardWidth,
        baseX = (boardTangent > panelTangent) ? ((1.0 * getWidth() - getHeight() / boardTangent) / 2) : 0,
        baseY = (boardTangent < panelTangent) ? ((1.0 * getHeight() - getWidth() * boardTangent) / 2) : 0;

        g.setColor(Color.WHITE);
        if (boardImage == null) {
            if (boardTangent > panelTangent)
                g.fillRect((int) Math.round(baseX), 0, (int) Math.round(1.0 / boardTangent * getHeight()), getHeight());
            else if (boardTangent < panelTangent)
                g.fillRect(0, (int) Math.round(baseY), getWidth(), (int) Math.round(boardTangent * getWidth()));
            else g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            if (boardTangent > panelTangent)
                g.drawImage(boardImage, (int) Math.round(baseX), 0, (int) Math.round(1.0 / boardTangent * getHeight()), getHeight(), null);
            else if (boardTangent < panelTangent)
                g.drawImage(boardImage, 0, (int) Math.round(baseY), getWidth(), (int) (boardTangent * getWidth()), null);
            else g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

    protected void paintComponentForBoardEdge(Graphics g){
        g.setColor(Color.RED);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3.0f));
        for (Point p : pointList) {
            Point.InfoPack sourceInfoPack = p.getPixelInformation();
            ArrayList<Point.EdgePointPair> outgoingEdgePointList = p.getAllEdgePointPair();
            for (Point.EdgePointPair edgePointPair : outgoingEdgePointList) {
                Point.InfoPack targetInfo = edgePointPair.targetPoint.getPixelInformation();
                g2.draw(new Line2D.Float(
                        (float) sourceInfoPack.posX,
                        (float) sourceInfoPack.posY,
                        (float) targetInfo.posX,
                        (float) targetInfo.posY
                ));
            }
        }
        g2.setStroke(new BasicStroke(1.0f));
    }

    protected void paintComponentForEdgeTriangles(Graphics g){
        for (EdgeTriangleInfo triangleInfo : getAllEdgeTriangleInfo()) {
            ChessBoardPanel chessBoardPanel = getChessBoardPanel();
            g.setColor(chessBoardPanel.getEdgeDirectionColor(triangleInfo.direction));
            g.fillPolygon(new int[]{
                    (int) Math.round(triangleInfo.pos1X),
                    (int) Math.round(triangleInfo.pos2X),
                    (int) Math.round(triangleInfo.pos3X)
            }, new int[]{
                    (int) Math.round(triangleInfo.pos1Y),
                    (int) Math.round(triangleInfo.pos2Y),
                    (int) Math.round(triangleInfo.pos3Y)
            }, 3);
        }
    }

    protected void paintComponentForAllPoints(Graphics g){
        for (Point p : pointList) {
            Point.InfoPack pixelInfo = p.getPixelInformation();
            g.setColor(new Color(0f, 0f, 1f, 0.5f));
            g.fillRect((int) Math.round(pixelInfo.posX - pixelInfo.width / 2), (int) Math.round(pixelInfo.posY - pixelInfo.height / 2), (int) Math.round(pixelInfo.width), (int) Math.round(pixelInfo.height));
            g.setColor(Color.ORANGE);
            g.drawString(p.getId() + "", (int) Math.round(pixelInfo.posX), (int) Math.round(pixelInfo.posY));
        }
    }

    protected void paintComponentForScaleAnchorOfSelectedPoints(Graphics g){
        g.setColor(Color.GREEN);
        for (Point p : selectedPointList) {
            ArrayList<ScaleAnchorInfo> anchorList = getPointScaleAnchor(p);
            for (ScaleAnchorInfo anchor : anchorList)
                g.fillRect(anchor.posXTopLeft, anchor.posYTopLeft, anchor.width, anchor.height);
        }
    }

    protected void paintComponentForEdgeTriangleInfo(Graphics g){
        if (showTriangleInfo != null) {
            String s = showTriangleInfo.sourcePointId + " to " + showTriangleInfo.targetPointId + " (" + showTriangleInfo.direction + ")";
            Color textColor = Color.WHITE, background = new Color(0.0f, 0.0f, 0.0f, 0.75f);
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(s, g);
            g.setColor(background);
            g.fillRect(showTriangleInfo.mouseX, showTriangleInfo.mouseY - fm.getAscent(), (int) Math.round(rect.getWidth()), (int) Math.round(rect.getHeight()));
            g.setColor(textColor);
            g.drawString(s, showTriangleInfo.mouseX, showTriangleInfo.mouseY);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    protected Point getCursorPointingPoint(double x, double y) {
        for (int i = pointList.size() - 1; i >= 0; i--)
            if (isCursorOnPoint(x, y, pointList.get(i))) return pointList.get(i);

        return null;
    }

    protected boolean isCursorOnPoint(double x, double y, Point p) {
        Point.InfoPack infoPack = p.getPixelInformation();
        if (x >= infoPack.posX - infoPack.width / 2 &&
                x <= infoPack.posX + infoPack.width / 2 &&
                y >= infoPack.posY - infoPack.height / 2 &&
                y <= infoPack.posY + infoPack.height / 2) return true;

        if (getCursorPointingAnchorOrientation(x, y, p) != null) return true;

        return false;
    }

    protected ScaleAnchorOrientation getCursorPointingAnchorOrientation(double x, double y, Point p) {
        ArrayList<ScaleAnchorInfo> anchorList = getPointScaleAnchor(p);
        for (ScaleAnchorInfo anchor : anchorList)
            if (x >= anchor.posXTopLeft &&
                    x <= anchor.posXTopLeft + anchor.width &&
                    y >= anchor.posYTopLeft &&
                    y <= anchor.posYTopLeft + anchor.height)
                return anchor.orientation;

        return null;
    }

    abstract protected void panelResized(ComponentEvent e);

    abstract protected MouseAdapter createMouseAdapter();

    abstract protected KeyListener createKeyAdapter();

    abstract protected void updateAllPoints();

    abstract protected ChessBoardPanel getChessBoardPanel();

    protected Point findPointInListById(ArrayList<Point> pointList, int id) {
        for (Point p : pointList) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }
}
