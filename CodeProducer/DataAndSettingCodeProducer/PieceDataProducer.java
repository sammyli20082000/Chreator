package Chreator.CodeProducer.DataAndSettingCodeProducer;

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
import Chreator.CodeProducer.PieceModelProducer.individualPieceCodeProducer;
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

//		File f = new File(pieceProfiles.get(j).sourcePicLink);
//		File picF = new File(CodeProducer.baseDir + "\\pictures\\" + f.getName());
//		try {
//			Files.copy(Paths.get(f.getAbsolutePath()), Paths.get(picF.getAbsolutePath()),
//					StandardCopyOption.REPLACE_EXISTING);
//		} catch (Exception e) {
//
//		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + addedLines, "else");
		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 3 + addedLines, "return null;");
		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 4 + addedLines, "}");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}

}
