package Chreator.CodeProducer.BoardCodeProducer;

public class PointCodeProducer {
	
	public PointCodeProducer() {
		
	}

	public void printPointJavaFrameCode() {
		BoardModelCodeProducer.pointCodes.add("package Executable.BoardModel;");
		BoardModelCodeProducer.pointCodes.add("import java.io.Serializable;");
		BoardModelCodeProducer.pointCodes.add("import java.util.*;");
		BoardModelCodeProducer.pointCodes.add("import Executable.PieceModel.Piece;");
		
		BoardModelCodeProducer.pointCodes.add("public class Point implements Serializable {");
		BoardModelCodeProducer.pointCodes.add("private int id;");
		BoardModelCodeProducer.pointCodes.add("private Piece piece;");
		BoardModelCodeProducer.pointCodes.add("private Map<Edge.Direction, Point> edges = new HashMap<Edge.Direction, Point>();");
		BoardModelCodeProducer.pointCodes.add("private double posX, posY, // 0 to 1, position on graphic, coordinate of center of image");
		BoardModelCodeProducer.pointCodes.add("width, height; // 0 to 1, position on graphic");
		BoardModelCodeProducer.pointCodes.add("private ArrayList<Point> pieceInsideMovable = new ArrayList<Point>();");
		
		BoardModelCodeProducer.pointCodes.add("public Point(double x, double y, double w, double h) {");
		BoardModelCodeProducer.pointCodes.add("id = idCounter;");
		BoardModelCodeProducer.pointCodes.add("idCounter++;");
		BoardModelCodeProducer.pointCodes.add("posX = x;");
		BoardModelCodeProducer.pointCodes.add("posY = y;");
		BoardModelCodeProducer.pointCodes.add("width = w;");
		BoardModelCodeProducer.pointCodes.add("height = h;");
		BoardModelCodeProducer.pointCodes.add("piece = null;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public Point(double x, double y, double w, double h, int id) {");
		BoardModelCodeProducer.pointCodes.add("this.id = id;");
		BoardModelCodeProducer.pointCodes.add("posX = x;");
		BoardModelCodeProducer.pointCodes.add("posY = y;");
		BoardModelCodeProducer.pointCodes.add("width = w;");
		BoardModelCodeProducer.pointCodes.add("height = h;");
		BoardModelCodeProducer.pointCodes.add("piece = null;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public int getId() {");
		BoardModelCodeProducer.pointCodes.add("return id;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public Piece getPiece() {");
		BoardModelCodeProducer.pointCodes.add("return piece;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public double getPosX() {");
		BoardModelCodeProducer.pointCodes.add("return posX;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public double getPosY() {");
		BoardModelCodeProducer.pointCodes.add("return posY;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public double getWidth() {");
		BoardModelCodeProducer.pointCodes.add("return width;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public double getHeight() {");
		BoardModelCodeProducer.pointCodes.add("return height;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public double setPiece(Piece p) {");
		BoardModelCodeProducer.pointCodes.add("piece = p;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public void addEdge(Edge.Direction dir, Point point) {");
		BoardModelCodeProducer.pointCodes.add("edges.put(dir, point);");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public Point getNextPointByDirection(Edge.Direction dir) {");
		BoardModelCodeProducer.pointCodes.add("return edges.get(dir);");
		BoardModelCodeProducer.pointCodes.add("}");
		
		
		
		BoardModelCodeProducer.pointCodes.add("public ArrayList<Point> getPieceInsideMovable() {");
		BoardModelCodeProducer.pointCodes.add("pieceInsideMovable = getPiece().moveInvolvingOtherPiece(this);");
		BoardModelCodeProducer.pointCodes.add("return pieceInsideMovable;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("public static void resetIdCounter() {");
		BoardModelCodeProducer.pointCodes.add("Point.idCounter = 0;");
		BoardModelCodeProducer.pointCodes.add("}");
		
		BoardModelCodeProducer.pointCodes.add("}");
	}
}
