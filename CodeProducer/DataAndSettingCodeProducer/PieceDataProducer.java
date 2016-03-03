package Chreator.CodeProducer.DataAndSettingCodeProducer;

import java.util.ArrayList;

import javax.swing.ListModel;

import Chreator.ObjectModel.PieceProfile;

public class PieceDataProducer {

	ListModel<String> playerSidesList;
	ArrayList<PieceProfile> pieceProfiles;

	public PieceDataProducer(ListModel<String> playerSidesList, ArrayList<PieceProfile> pieceProfiles) {
		this.playerSidesList = playerSidesList;
		this.pieceProfiles = pieceProfiles;
	}

	public void printPlayerSideCode() {
		int i = DataAndSettingCodeProducer.dataAndSettingCodes.indexOf("public static class PlayerSide");

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1, "public static class PlayerSide {");

		for (int j = 0; j < playerSidesList.getSize(); j++) {
			DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2, "public static String "
					+ playerSidesList.getElementAt(j) + " = \"" + playerSidesList.getElementAt(j) + "\";");
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + playerSidesList.getSize(), "}");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}

	public void printMakeStandardPieceCode() {
		int i = DataAndSettingCodeProducer.dataAndSettingCodes
				.indexOf("public static Piece makeStandardPiece(String pieceType, String playerSide) {}");

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1,
				"public static Piece makeStandardPiece(String pieceType, String playerSide) {");

		for (int j = 0; j < pieceProfiles.size(); j++) {
			for (int k = 0; k < playerSidesList.getSize(); k++) {
				if (j == 0) {
					DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2,
							"if (pieceType.equals(\"" + pieceProfiles.get(j).pieceClassName
									+ "\") && playerSide.equals(PlayerSide." + playerSidesList.getElementAt(k) + ")");
					
				} else {

				}
			}
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2 + pieceProfiles.size(), "}");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}
}
