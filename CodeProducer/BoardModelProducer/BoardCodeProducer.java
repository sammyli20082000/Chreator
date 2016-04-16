package Chreator.CodeProducer.BoardModelProducer;

import java.util.ArrayList;

public class BoardCodeProducer {
	
	public BoardCodeProducer() {
		
	}
	
	public void printBoardJavaFrameCode() {
		BoardModelProducer.boardCodes.add("package Executable.BoardModel;");
		BoardModelProducer.boardCodes.add("import java.util.ArrayList;");
		BoardModelProducer.boardCodes.add("import Executable.PieceModel.Piece;");
		BoardModelProducer.boardCodes.add("import Executable.ObjectModel.Move;");
		
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
		
		BoardModelProducer.boardCodes.add("public Point getPointById(int id) {");
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
		
		BoardModelProducer.boardCodes.add("public void capture(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelProducer.boardCodes.add("toPoint.setPiece(piece);");
		BoardModelProducer.boardCodes.add("fromPoint.setPiece(null);");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public void movePiece(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelProducer.boardCodes.add("toPoint.setPiece(piece);");
		BoardModelProducer.boardCodes.add("fromPoint.setPiece(null);");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public String getMoveString(Piece piece, Point fromPoint, Point toPoint) {");
		BoardModelProducer.boardCodes.add("if (toPoint == null)");
		BoardModelProducer.boardCodes.add("return piece.getName() + \" from \" + fromPoint.getId() + \" to \" + toPoint.getId();");
		BoardModelProducer.boardCodes.add("else");
		BoardModelProducer.boardCodes.add("return piece.getName() + \" added to \" + fromPoint.getId();");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public String getMoveString(Move move) {");
		BoardModelProducer.boardCodes.add("if (move.fromPoint != -1)");
		BoardModelProducer.boardCodes.add("return move.pieceName + \" from \" + move.fromPoint + \" to \" + move.toPoint;");
		BoardModelProducer.boardCodes.add("else");
		BoardModelProducer.boardCodes.add("return move.pieceName + \" added to \" + move.toPoint;");
		BoardModelProducer.boardCodes.add("}");
		
		
		BoardModelProducer.boardCodes.add("public ArrayList<Move> generateAllValidMoves(String side) {");
		BoardModelProducer.boardCodes.add("ArrayList<Move> validMoves = new ArrayList<>();");
		BoardModelProducer.boardCodes.add("for (Point point : points) {");
		BoardModelProducer.boardCodes.add("if (point.getPiece() != null && point.getPiece().getSide().equals(side)) {");
		BoardModelProducer.boardCodes.add("ArrayList<Point> tempMovables = point.getPieceInsideMovable();");
		BoardModelProducer.boardCodes.add("for (Point tempMovable : tempMovables) {");
		BoardModelProducer.boardCodes.add("validMoves.add(Move.transformingMove(point.getPiece().getName(), point.getId(), tempMovable.getId()));");
		BoardModelProducer.boardCodes.add("}");
		BoardModelProducer.boardCodes.add("}");
		BoardModelProducer.boardCodes.add("}");
		BoardModelProducer.boardCodes.add("return validMoves;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public boolean canContinue() {");
		BoardModelProducer.boardCodes.add("return true;");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("public static void filpboard() {");
		BoardModelProducer.boardCodes.add("// TODO: This function is used to change AI thinking perspective. Please change pieces' position by setPiece() in Point object.");
		BoardModelProducer.boardCodes.add("// if this is handled in AI and moving rules coding, the implementation of this function can be skipped.");
		BoardModelProducer.boardCodes.add("}");
		
		BoardModelProducer.boardCodes.add("}");
	}
}
