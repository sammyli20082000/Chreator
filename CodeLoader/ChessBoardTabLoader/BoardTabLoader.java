package Chreator.CodeLoader.ChessBoardTabLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import Chreator.CodeLoader.CodeLoader;
import Chreator.ObjectModel.Point;
import Chreator.UIModule.UIHandler;

public class BoardTabLoader {

	int preferredPixelWidth;
	int preferredPixelHeight;

	Map<Integer, Point> points;

	String boardImageName;
	String[] pointDataPackageLines;
	String[] pointEdgePackageLines;

	String[] edges;

	File dataAndSettingFile;
	File edgeFile;

	public BoardTabLoader() {
		points = new HashMap<>();
		dataAndSettingFile = new File(CodeLoader.baseDir + "\\src\\Executable\\DataAndSetting.java");
		edgeFile = new File(CodeLoader.baseDir + "\\src\\Executable\\BoardModel\\Edge.java");
	}
	
	public void loadToChreatorBoardTab() throws FileNotFoundException, IOException {
		loadBoardImage();
		loadEdgeBoardRelated();
		loadDataAndSettingBoardRelated();

		UIHandler uiHandler = UIHandler.getInstance(null);

		uiHandler.setBoardImage(CodeLoader.baseDir + "\\pic\\" + boardImageName);

		for (int i = 0; i < edges.length; i++)
			uiHandler.addEdgeDirection(edges[i]);

		String[] resultStr;
		double[] results;

		for (int i = 1; i < pointDataPackageLines.length; i++) {
			resultStr = pointDataPackageLines[i].split(",");
			results = new double[resultStr.length];

			for (int j = 0; j < results.length; j++) {
				String temp = CodeLoader.stripNonDigits(resultStr[j]);
				if (!temp.equals(""))
					results[j] = Double.parseDouble(temp);

				Point p = uiHandler.addSinglePoint(results[0], results[1], results[2], results[3], (int) results[4]);
				points.put((int) results[4], p);
			}
		}

		for (int i = 1; i < pointEdgePackageLines.length; i++) {
			resultStr = pointEdgePackageLines[i].split(",");
			
			Point p1 = points.get(Integer.parseInt(CodeLoader.stripNonDigits(resultStr[0])));
			Point p2 = points.get(Integer.parseInt(CodeLoader.stripNonDigits(resultStr[2])));
			String dir = resultStr[1].substring(16);
			
			p1.addEdgePointPairs(dir, p2);
		}
		
	}

	private void loadBoardImage() throws IOException {
		if (!dataAndSettingFile.exists() || dataAndSettingFile.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Error loading file \"DataAndSetting.java\".");
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(dataAndSettingFile))) {
			String line, temp = "";
			while ((line = br.readLine()) != null) {
				if (line.contains("imageLink")) {
					line = line.trim();
					temp = temp + line;
					boardImageName = temp.split("\"")[1];

					break;
				}
			}
		}
	}

	private void loadEdgeBoardRelated() throws FileNotFoundException, IOException {
		if (!edgeFile.exists() || edgeFile.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Error loading file \"Edge.java\". ");
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(edgeFile))) {
			String line, temp = "";
			while ((line = br.readLine()) != null) {
				if (line.contains("Direction")) {
					line = br.readLine();

					while (true) {
						if (line.trim().equals("")) {
							line = br.readLine();
							continue;
						}

						if (line.contains("}"))
							break;

						line = line.trim();
						temp += line;
						line = br.readLine();
					}

					edges = temp.trim().split(",");

					temp = "";
					break;
				}
			}
		}
	}

	private void loadDataAndSettingBoardRelated() throws FileNotFoundException, IOException {
		if (!dataAndSettingFile.exists() || dataAndSettingFile.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Error loading file \"DataAndSetting.java\". ");
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(dataAndSettingFile))) {
			String line, temp = "";
			while ((line = br.readLine()) != null) {
				if (line.contains("public static PointDataPackage[]")) {
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

					pointDataPackageLines = temp.trim().split("new PointDataPackage");

					temp = "";
				} else if (line.contains("public static PointEdgePackage[]")) {
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

					pointEdgePackageLines = temp.trim().split("new PointEdgePackage");

					temp = "";
				}
			}
		}
	}
}
