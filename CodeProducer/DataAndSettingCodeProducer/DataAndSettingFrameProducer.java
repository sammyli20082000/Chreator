package Chreator.CodeProducer.DataAndSettingCodeProducer;

public class DataAndSettingFrameProducer {

	public DataAndSettingFrameProducer() {
		
	}

	public void printDataAndSettingJavaFrame() {

		DataAndSettingCodeProducer.dataAndSettingCodes.add("import Executable.BoardModel.*;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("import Executable.PieceModel.*;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public class DataAndSetting {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static String localDir;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class BoardData {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PointEdgePackage {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public int sourceID, targetID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public Edge.Direction edgeDirection;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public PointEdgePackage(int sourceID, Edge.Direction edgeDirection, int targetID) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.sourceID = sourceID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.edgeDirection = edgeDirection;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.targetID = targetID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PieceDataPackage {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public int pointID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public String playerSide, pieceType;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public PieceDataPackage(int pointID, String pieceType, String playerSide) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.pointID = pointID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.playerSide = playerSide;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.pieceType = pieceType;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PieceData {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PlayerSide {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static Piece makeStandardPiece(String pieceType, String playerSide) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static PieceDataPackage[] initialPiecePlacingData;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PointEdgeData {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PointDataPackage {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public double xCoordinate, yCoordinate, width, height;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public int id;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public PointDataPackage(double xCoordinate, double yCoordinate, double width, double height, int id) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.xCoordinate = xCoordinate;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.yCoordinate = yCoordinate;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.width = width;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.height = height;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.id = id;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static PointDataPackage[] boardPointsArray;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static PointEdgePackage[] pointEdgeRelations;");
		
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");	// closing of class PointEdgeData
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");	// closing of class DataAndSetting
	}
}
