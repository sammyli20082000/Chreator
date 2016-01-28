package Chreator.CodeProducer.DataAndSettingCodeProducer;

public class DataAndSettingFrameProducer {

	public DataAndSettingFrameProducer() {
		
	}

	public void printDataAndSettingJavaFrame() {

		DataAndSettingCodeProducer.DataAndSettingCodes.add("import Executable.BoardModel.*;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("import Executable.PieceModel.*;");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public class DataAndSetting {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static String localDir;");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class BoardData {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class PointEdgePackage {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public int sourceID, targetID;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public Edge.Direction edgeDirection;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public PointEdgePackage(int sourceID, Edge.Direction edgeDirection, int targetID) {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.sourceID = sourceID;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.edgeDirection = edgeDirection;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.targetID = targetID;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class PieceDataPackage {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public int pointID;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public String playerSide, pieceType;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public PieceDataPackage(int pointID, String pieceType, String playerSide) {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.pointID = pointID;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.playerSide = playerSide;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.pieceType = pieceType;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class PieceData {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class PlayerSide {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static Piece makeStandardPiece(String pieceType, String playerSide) {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static PieceDataPackage[] initialPiecePlacingData;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class PointEdgeData {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static class PointDataPackage {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public double xCoordinate, yCoordinate, width, height;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public int id;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public PointDataPackage(double xCoordinate, double yCoordinate, double width, double height, int id) {");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.xCoordinate = xCoordinate;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.yCoordinate = yCoordinate;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.width = width;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.height = height;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("this.id = id;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static PointDataPackage[] boardPointsArray;");
		DataAndSettingCodeProducer.DataAndSettingCodes.add("public static PointEdgePackage[] pointEdgeRelations;");
		
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");	// closing of class PointEdgeData
		DataAndSettingCodeProducer.DataAndSettingCodes.add("}");	// closing of class DataAndSetting
	}
}
