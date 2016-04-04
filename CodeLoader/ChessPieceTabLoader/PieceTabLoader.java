package Chreator.CodeLoader.ChessPieceTabLoader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import Chreator.CodeLoader.CodeLoader;
import Chreator.ObjectModel.PieceProfile;
import Chreator.UIModule.UIHandler;

public class PieceTabLoader {

	ArrayList<File> indiPieceFiles;
	File pieceModelFolder;
	File dataAndSettingFile;

	ArrayList<String> playerSides;
	ArrayList<String> indiPieceData;
	String[] initPointId;

	public PieceTabLoader() {
		indiPieceFiles = new ArrayList<>();
		playerSides = new ArrayList<>();
		indiPieceData = new ArrayList<>();

		pieceModelFolder = new File(CodeLoader.baseDir + "\\src\\Executable\\PieceModel");
		dataAndSettingFile = new File(CodeLoader.baseDir + "\\src\\Executable\\DataAndSetting.java");
	}

	public void loadToChessPieceTab() throws FileNotFoundException, IOException {
		loadDataAndSettingPlayerSideRelated();
		loadDataAndSettingIndiPieceRelated();
		loadDataAndSettingInitPointIdRelated();

		UIHandler uiHandler = UIHandler.getInstance(null);

		for (String playerSide : playerSides)
			uiHandler.addPlayerSides(playerSide.toUpperCase());

//		for (int i = 0; i < initPointId.length; i++)
//			System.out.println(initPointId[i]);
		
		for (String line : indiPieceData) {
			String[] temp1 = line.split(" ");
			String[] temp2 = line.split(",");
			String[] temp3 = line.split("\"");

			for (String side : playerSides) {
				String pieceName = temp1[2].substring(0, temp1[2].indexOf("("));
				PieceProfile pieceProfile = new PieceProfile(side, pieceName);
				pieceProfile.imageRelativeWidth = Double.parseDouble(CodeLoader.stripNonDigits(temp2[2]));
				pieceProfile.imageRelativeHeight = Double.parseDouble(CodeLoader.stripNonDigits(temp2[3]));
				pieceProfile.sourcePicLink = CodeLoader.baseDir + "\\pic\\" + temp3[1];
				pieceProfile.pieceImage = ImageIO.read(new File(CodeLoader.baseDir + "\\pic\\" + temp3[1]));

				String tempPlayerSide = side.substring(0, 1).toUpperCase() + side.substring(1).toLowerCase();
				String tempPieceName = pieceName.substring(0, 1).toUpperCase() + pieceName.substring(1).toLowerCase();
				pieceProfile.code = loadIndiPieceCodes(new File(
						CodeLoader.baseDir + "\\Executable\\PieceModel\\" + tempPlayerSide + tempPieceName + ".java"));
			}
		}
	}

	private void loadDataAndSettingPlayerSideRelated() throws FileNotFoundException, IOException {
		if (!dataAndSettingFile.exists()) {
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(dataAndSettingFile))) {
			String line;
			String[] temp;
			while ((line = br.readLine()) != null) {
				if (line.contains("public static class PlayerSide")) {
					line = br.readLine();

					while (true) {
						if (line.trim().equals("")) {
							line = br.readLine();
							continue;
						}

						if (line.contains("}"))
							break;

						line = line.trim();
						temp = line.split("\"");
						playerSides.add(temp[1]);
						line = br.readLine();
					}

					break;
				}
			}
		}
	}
	
	private void loadDataAndSettingInitPointIdRelated() throws FileNotFoundException, IOException {
		if (!dataAndSettingFile.exists()) {
			return;
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(dataAndSettingFile))) {
			String line, temp = "";
			while((line = br.readLine()) != null) {
				if (line.contains("public static PieceDataPackage[] initialPiecePlacingData")) {
					while (true) {
						if (line.trim().equals("")) {
							line = br.readLine();
							continue;
						}

						line = br.readLine();
						line = line.trim();
						temp = temp + line;

						if (line.contains("};"))
							break;
					}
					
					System.out.println(temp);
					initPointId = temp.split("\\)");
					
					break;
				}
			}
		}
	}

	private void loadDataAndSettingIndiPieceRelated() throws FileNotFoundException, IOException {
		if (!dataAndSettingFile.exists()) {
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(dataAndSettingFile))) {
			String line, temp = "";
			while ((line = br.readLine()) != null) {
				if (line.contains("pieceType.equals")) {
					line = br.readLine();

					while (true) {
						if (line.trim().equals("")) {
							line = br.readLine();
							continue;
						}

						if (line.contains("return new")) {
							line = line.trim();
							temp += line;

							while (!line.contains(";")) {
								line = br.readLine();
								line = line.trim();
								temp += line;
							}

							indiPieceData.add(temp.trim());
						}

						temp = "";

						if (line.trim().contains("return null"))
							break;

						line = br.readLine();
					}
				}
			}
		}
	}

	private String loadIndiPieceCodes(File indiPieceFile) throws FileNotFoundException, IOException {
		String codes = "";

		try (BufferedReader br = new BufferedReader(new FileReader(indiPieceFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				codes += line;
			}
		}

		return codes;
	}
}
