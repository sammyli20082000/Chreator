package Chreator.CodeProducer.BoardCodeProducer;

public class BoardCodeProducer {
	
	public BoardCodeProducer() {
		
	}
	
	public void printBoardJavaFrameCode() {
		BoardModelCodeProducer.boardCodes.add("package Executable.BoardModel;");
		BoardModelCodeProducer.boardCodes.add("import java.util.ArrayList;");
		BoardModelCodeProducer.boardCodes.add("import Executable.PieceModel.Piece;");
		
		BoardModelCodeProducer.boardCodes.add("public class Board {");
		BoardModelCodeProducer.boardCodes.add("ArrayList<Point> points;");
		BoardModelCodeProducer.boardCodes.add("ArrayList<Point> selectedPieceMovable;");
		
		BoardModelCodeProducer.boardCodes.add("public Board() {");
		BoardModelCodeProducer.boardCodes.add("points = new ArrayList<Point>();");
		BoardModelCodeProducer.boardCodes.add("selectedPieceMovable = new ArrayList<>();");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public void addPoint(Point p) {");
		BoardModelCodeProducer.boardCodes.add("points.add(p);");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public Point addPoint(double x, double y, double w, double h) {");
		BoardModelCodeProducer.boardCodes.add("Point p = new Point(x, y, w, h);");
		BoardModelCodeProducer.boardCodes.add("addPoint(p);");
		BoardModelCodeProducer.boardCodes.add("return p;");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public Point addPoint(double x, double y, double w, double h, int id) {");
		BoardModelCodeProducer.boardCodes.add("Point p = new Point(x, y, w, h, id);");
		BoardModelCodeProducer.boardCodes.add("addPoint(p);");
		BoardModelCodeProducer.boardCodes.add("return p;");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public void addPoint(ArrayList<Point> p) {");
		BoardModelCodeProducer.boardCodes.add("points = p;");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public Point getPointByID(int id) {");
		BoardModelCodeProducer.boardCodes.add("for (Point p : points) {");
		BoardModelCodeProducer.boardCodes.add("if (p.getId() == id)");
		BoardModelCodeProducer.boardCodes.add("return p;");
		BoardModelCodeProducer.boardCodes.add("}");
		BoardModelCodeProducer.boardCodes.add("return null;");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public ArrayList<Point> getPoints() {");
		BoardModelCodeProducer.boardCodes.add("return points;");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public void updateSelectedPieceMovable(Point p, String currentSide) {");
		BoardModelCodeProducer.boardCodes.add("selectedPieceMovable = new ArrayList<>(p.getPieceInsideMovable());");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public ArrayList<Point> getSelectedPieceMovable() {");
		BoardModelCodeProducer.boardCodes.add("return selectedPieceMovable;");
		BoardModelCodeProducer.boardCodes.add("}");
		
		// TODO: select function added to Board.java according chess type selected on board setting tab
		BoardModelCodeProducer.boardCodes.add("public void capture(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelCodeProducer.boardCodes.add("toPoint.setPiece(piece);");
		BoardModelCodeProducer.boardCodes.add("fromPoint.setPiece(null);");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public void movePiece(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelCodeProducer.boardCodes.add("toPoint.setPiece(piece);");
		BoardModelCodeProducer.boardCodes.add("fromPoint.setPiece(null);");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("public static void filpboard() {");
		BoardModelCodeProducer.boardCodes.add("// TODO: This function is used to change AI thinking perspective. Please change pieces' position by setPiece() in Point object.");
		BoardModelCodeProducer.boardCodes.add("// if this is handled in AI and moving rules coding, the implementation of this function can be skipped.");
		BoardModelCodeProducer.boardCodes.add("}");
		
		BoardModelCodeProducer.boardCodes.add("}");
	}
}
