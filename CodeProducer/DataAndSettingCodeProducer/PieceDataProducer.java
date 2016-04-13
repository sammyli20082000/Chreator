package Chreator.CodeProducer.DataAndSettingCodeProducer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.ListModel;

import Chreator.CodeProducer.CodeProducer;
import Chreator.CodeProducer.PieceModelProducer.IndividualPieceCodeProducer;
import Chreator.ObjectModel.PieceProfile;

public class PieceDataProducer {

	ListModel<String> playerSidesList;
	ArrayList<PieceProfile> pieceProfiles;

	public PieceDataProducer(ListModel<String> playerSidesList, ArrayList<PieceProfile> pieceProfiles) {
		this.playerSidesList = playerSidesList;
		this.pieceProfiles = pieceProfiles;
	}

	public void printPieceDataCode() {
		printPlayerSideCode();
		printMakeStandardPieceCode();
		printPieceDataPackageCode();
	}

	private void printPlayerSideCode() {
		int i = DataAndSettingCodeProducer.dataAndSettingCodes.indexOf("public static class PlayerSide");

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1, "public static class PlayerSide {");

		for (int j = 0; j < playerSidesList.getSize(); j++) {
			DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2, "public static String "
					+ playerSidesList.getElementAt(j) + " = \"" + playerSidesList.getElementAt(j) + "\";");
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + playerSidesList.getSize(), "}");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}

	private void printPieceDataPackageCode() {
		int addedLines = 0;
		int i = DataAndSettingCodeProducer.dataAndSettingCodes
				.indexOf("public static PieceDataPackage[] initialPiecePlacingData;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1,
				"public static PieceDataPackage[] initialPiecePlacingData = new PieceDataPackage[] {");

		for (int j = 0; j < pieceProfiles.size(); j++) {
			for (int k = 0; k < pieceProfiles.get(j).initialPointId.size(); k++) {
				addedLines++;
				DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2,
						"new PieceDataPackage(" + pieceProfiles.get(j).initialPointId.getElementAt(k) + ", \""
								+ pieceProfiles.get(j).pieceClassName + "\", PlayerSide."
								+ pieceProfiles.get(j).playerSide + "),");
			}
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + addedLines, "};");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}

	private void printMakeStandardPieceCode() {
		int addedLines = 0;
		int i = DataAndSettingCodeProducer.dataAndSettingCodes
				.indexOf("public static Piece makeStandardPiece(String pieceType, String playerSide) {}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1,
				"public static Piece makeStandardPiece(String pieceType, String playerSide) {");

		for (int j = 0; j < pieceProfiles.size(); j++) {
			File picF;
			if (!pieceProfiles.get(j).sourcePicLink.equals("")) {
				File f = new File(pieceProfiles.get(j).sourcePicLink);
				picF = new File(CodeProducer.pathname + "\\pic\\" + f.getName());
			} else {
				picF = new File(CodeProducer.pathname + "\\pic\\" + pieceProfiles.get(j).playerSide + "_"
						+ pieceProfiles.get(j).pieceClassName + ".png");

			}

			String tempPieceName = pieceProfiles.get(j).pieceClassName.substring(0, 1).toUpperCase()
					+ pieceProfiles.get(j).pieceClassName.substring(1).toLowerCase();
			String tempPlayerSide = pieceProfiles.get(j).playerSide.substring(0, 1).toUpperCase()
					+ pieceProfiles.get(j).playerSide.substring(1).toLowerCase();
			if (j == 0) {
				DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2,
						"if (pieceType.equalsIgnoreCase(\"" + pieceProfiles.get(j).pieceClassName
								+ "\") && playerSide.equals(PlayerSide." + pieceProfiles.get(j).playerSide + "))");
				DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 3,
						"return new " + tempPlayerSide + tempPieceName + "(playerSide, localDir + \"" + picF.getName()
								+ "\", " + pieceProfiles.get(j).imageRelativeWidth + ", "
								+ pieceProfiles.get(j).imageRelativeHeight + ", " + "\""
								+ pieceProfiles.get(j).pieceClassName + "\"" + ");");
			} else {
				DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + addedLines,
						"else if (pieceType.equalsIgnoreCase(\"" + pieceProfiles.get(j).pieceClassName
								+ "\") && playerSide.equals(PlayerSide." + pieceProfiles.get(j).playerSide + "))");
				DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 3 + addedLines,
						"return new " + tempPlayerSide + tempPieceName + "(playerSide, localDir + \"" + picF.getName()
								+ "\", " + pieceProfiles.get(j).imageRelativeWidth + ", "
								+ pieceProfiles.get(j).imageRelativeHeight + ", " + "\""
								+ pieceProfiles.get(j).pieceClassName + "\"" + ");");
				if (j == pieceProfiles.size() - 1) {
					DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 4 + addedLines, "else");
					addedLines++;
				}
			}
			addedLines += 2;
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + addedLines, "return null;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 3 + addedLines, "}");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}

}
