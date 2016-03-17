package Chreator.CodeProducer.BoardModelProducer;

import org.omg.CORBA.PUBLIC_MEMBER;

public class PointCodeProducer {
	
	public PointCodeProducer() {
		
	}

	public void printPointJavaFrameCode() {
		BoardModelProducer.pointCodes.add("package Executable.BoardModel;");
		BoardModelProducer.pointCodes.add("import java.io.Serializable;");
		BoardModelProducer.pointCodes.add("import java.util.*;");
		BoardModelProducer.pointCodes.add("import Executable.PieceModel.Piece;");
		
		BoardModelProducer.pointCodes.add("public class Point implements Serializable {");
		BoardModelProducer.pointCodes.add("public static int idCounter = 0;");
		BoardModelProducer.pointCodes.add("private int id;");
		BoardModelProducer.pointCodes.add("private Piece piece;");
		BoardModelProducer.pointCodes.add("private Map<Edge.Direction, Point> edges = new HashMap<Edge.Direction, Point>();");
		BoardModelProducer.pointCodes.add("private double posX, posY, // 0 to 1, position on graphic, coordinate of center of image");
		BoardModelProducer.pointCodes.add("width, height; // 0 to 1, position on graphic");
		BoardModelProducer.pointCodes.add("private ArrayList<Point> pieceInsideMovable = new ArrayList<Point>();");
		
		BoardModelProducer.pointCodes.add("public Point(double x, double y, double w, double h) {");
		BoardModelProducer.pointCodes.add("id = idCounter;");
		BoardModelProducer.pointCodes.add("idCounter++;");
		BoardModelProducer.pointCodes.add("posX = x;");
		BoardModelProducer.pointCodes.add("posY = y;");
		BoardModelProducer.pointCodes.add("width = w;");
		BoardModelProducer.pointCodes.add("height = h;");
		BoardModelProducer.pointCodes.add("piece = null;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public Point(double x, double y, double w, double h, int id) {");
		BoardModelProducer.pointCodes.add("this.id = id;");
		BoardModelProducer.pointCodes.add("posX = x;");
		BoardModelProducer.pointCodes.add("posY = y;");
		BoardModelProducer.pointCodes.add("width = w;");
		BoardModelProducer.pointCodes.add("height = h;");
		BoardModelProducer.pointCodes.add("piece = null;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public int getId() {");
		BoardModelProducer.pointCodes.add("return id;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public Piece getPiece() {");
		BoardModelProducer.pointCodes.add("return piece;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public double getPosX() {");
		BoardModelProducer.pointCodes.add("return posX;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public double getPosY() {");
		BoardModelProducer.pointCodes.add("return posY;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public double getWidth() {");
		BoardModelProducer.pointCodes.add("return width;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public double getHeight() {");
		BoardModelProducer.pointCodes.add("return height;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public void setPiece(Piece p) {");
		BoardModelProducer.pointCodes.add("piece = p;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public void addEdge(Edge.Direction dir, Point point) {");
		BoardModelProducer.pointCodes.add("edges.put(dir, point);");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public Point getImmediateNextPointAt(Edge.Direction dir) {");
		BoardModelProducer.pointCodes.add("try {");
		BoardModelProducer.pointCodes.add("return edges.get(dir);");
		BoardModelProducer.pointCodes.add("} catch (Exception e) {");
		BoardModelProducer.pointCodes.add("return null;");
		BoardModelProducer.pointCodes.add("}");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public Point getImmediateNextPointAt(Edge.Direction[] dirs) {");
		BoardModelProducer.pointCodes.add("Point point = this;");
		BoardModelProducer.pointCodes.add("for (int i = 0; i < dirs.length; i++) {");
		BoardModelProducer.pointCodes.add("try {");
		BoardModelProducer.pointCodes.add("point = point.getImmediateNextPointAt(dirs[i]);");
		BoardModelProducer.pointCodes.add("} catch (Exception e) {");
		BoardModelProducer.pointCodes.add("return null;");
		BoardModelProducer.pointCodes.add("}");
		BoardModelProducer.pointCodes.add("}");
		BoardModelProducer.pointCodes.add("return point;");
		BoardModelProducer.pointCodes.add("}");
		
		// TODO: add more functions facilitating points selections
		
		BoardModelProducer.pointCodes.add("public ArrayList<Point> getPieceInsideMovable() {");
		BoardModelProducer.pointCodes.add("pieceInsideMovable = getPiece().moveInvolvingOtherPiece(this);");
		BoardModelProducer.pointCodes.add("return pieceInsideMovable;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("public static void resetIdCounter() {");
		BoardModelProducer.pointCodes.add("Point.idCounter = 0;");
		BoardModelProducer.pointCodes.add("}");
		
		BoardModelProducer.pointCodes.add("}");
	}
}
