package Chreator.CodeProducer.DataAndSettingCodeProducer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ListModel;

import Chreator.CodeProducer.CodeProducer;
import Chreator.CodeProducer.GameCodeProducer.GameCodeProducer;

public class DataAndSettingFrameProducer {

	File imageFile;

	public DataAndSettingFrameProducer(ListModel PlayerSidesList, String boardImageLink) {
		imageFile = new File(CodeProducer.pathname + "\\pic\\board.png");
	}

	public void printDataAndSettingJavaFrame() {
		DataAndSettingCodeProducer.dataAndSettingCodes.add("package Executable;");
		
		DataAndSettingCodeProducer.dataAndSettingCodes.add("import java.io.File;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("import Executable.BoardModel.*;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("import Executable.PieceModel.*;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public class DataAndSetting {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static String localDir = getLocalDir();");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static String getLocalDir() {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("String s;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("try {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add(
				"s = new File(DataAndSetting.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + \"\\\\pic\\\\\";");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("} catch (Exception e) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add(
				"s = new File(DataAndSetting.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + \"\\\\pic\\\\\";");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("return s;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class BoardData {");
		DataAndSettingCodeProducer.dataAndSettingCodes
				.add("public static String imageLink = localDir + \"board.png\";");
		BufferedImage bImage = null;
		try {
			bImage = ImageIO.read(imageFile);
			DataAndSettingCodeProducer.dataAndSettingCodes
			.add("public static int preferredPixelWidth = " + bImage.getWidth() + ";");
	DataAndSettingCodeProducer.dataAndSettingCodes
			.add("public static int preferredPixelHeight = " + bImage.getHeight() + ";");
	DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PointEdgePackage {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public int sourceID, targetID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public Edge.Direction edgeDirection;");
		DataAndSettingCodeProducer.dataAndSettingCodes
				.add("public PointEdgePackage(int sourceID, Edge.Direction edgeDirection, int targetID) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.sourceID = sourceID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.edgeDirection = edgeDirection;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.targetID = targetID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PieceDataPackage {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public int pointID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public String playerSide, pieceType;");
		DataAndSettingCodeProducer.dataAndSettingCodes
				.add("public PieceDataPackage(int pointID, String pieceType, String playerSide) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.pointID = pointID;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.playerSide = playerSide;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.pieceType = pieceType;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PieceData {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PlayerSide");
		DataAndSettingCodeProducer.dataAndSettingCodes
				.add("public static Piece makeStandardPiece(String pieceType, String playerSide) {}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static PieceDataPackage[] initialPiecePlacingData;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PointEdgeData {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static class PointDataPackage {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public double xCoordinate, yCoordinate, width, height;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public int id;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add(
				"public PointDataPackage(double xCoordinate, double yCoordinate, double width, double height, int id) {");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.xCoordinate = xCoordinate;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.yCoordinate = yCoordinate;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.width = width;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.height = height;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("this.id = id;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static PointDataPackage[] boardPointsArray;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add("public static PointEdgePackage[] pointEdgeRelations;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add("}"); // closing of
																	// class
																	// PointEdgeData
		DataAndSettingCodeProducer.dataAndSettingCodes.add("}"); // closing of
																	// class
																	// DataAndSetting
	}
}
