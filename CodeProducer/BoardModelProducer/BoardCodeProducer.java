package Chreator.CodeProducer.BoardModelProducer;

public class BoardCodeProducer {
	
	public BoardCodeProducer() {
		
	}
	
	public void printBoardJavaFrameCode() {
		BoardModelProducer.boardCodes.add("package Executable.BoardModel;");
		BoardModelProducer.boardCodes.add("import java.util.ArrayList;");
		BoardModelProducer.boardCodes.add("import Executable.PieceModel.Piece;");
		
		BoardModelProducer.boardCodes.add("public class Board {");
		BoardModelProducer.boardCodes.add("ArrayList<Point> points;");
		BoardModelProducer.boardCodes.add("ArrayList<Point> selectedPieceMovable;");
		
		BoardModelProducer.boardCodes.add("public Board() {");
		BoardModelProducer.boardCodes.add("points = new ArrayList<Point>();");
		BoardModelProducer.boardCodes.add("selectedPieceMovable = new ArrayList<>();");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public void addPoint(Point p) {");
		BoardModelProducer.boardCodes.add("points.add(p);");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public Point addPoint(double x, double y, double w, double h) {");
		BoardModelProducer.boardCodes.add("Point p = new Point(x, y, w, h);");
		BoardModelProducer.boardCodes.add("addPoint(p);");
		BoardModelProducer.boardCodes.add("return p;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public Point addPoint(double x, double y, double w, double h, int id) {");
		BoardModelProducer.boardCodes.add("Point p = new Point(x, y, w, h, id);");
		BoardModelProducer.boardCodes.add("addPoint(p);");
		BoardModelProducer.boardCodes.add("return p;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public void addPoint(ArrayList<Point> p) {");
		BoardModelProducer.boardCodes.add("points = p;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public Point getPointByID(int id) {");
		BoardModelProducer.boardCodes.add("for (Point p : points) {");
		BoardModelProducer.boardCodes.add("if (p.getId() == id)");
		BoardModelProducer.boardCodes.add("return p;");
		BoardModelProducer.boardCodes.add("}");
		BoardModelProducer.boardCodes.add("return null;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public ArrayList<Point> getPoints() {");
		BoardModelProducer.boardCodes.add("return points;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public void updateSelectedPieceMovable(Point p, String currentSide) {");
		BoardModelProducer.boardCodes.add("selectedPieceMovable = new ArrayList<>(p.getPieceInsideMovable());");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public ArrayList<Point> getSelectedPieceMovable() {");
		BoardModelProducer.boardCodes.add("return selectedPieceMovable;");
		BoardModelProducer.boardCodes.add("}");
		
		// TODO: select function added to Board.java according chess type selected on board setting tab
		BoardModelProducer.boardCodes.add("public void capture(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelProducer.boardCodes.add("toPoint.setPiece(piece);");
		BoardModelProducer.boardCodes.add("fromPoint.setPiece(null);");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public void movePiece(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelProducer.boardCodes.add("toPoint.setPiece(piece);");
		BoardModelProducer.boardCodes.add("fromPoint.setPiece(null);");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public static void filpboard() {");
		BoardModelProducer.boardCodes.add("// TODO: This function is used to change AI thinking perspective. Please change pieces' position by setPiece() in Point object.");
		BoardModelProducer.boardCodes.add("// if this is handled in AI and moving rules coding, the implementation of this function can be skipped.");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("}");
	}
}
