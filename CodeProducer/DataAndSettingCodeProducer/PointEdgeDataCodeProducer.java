package Chreator.CodeProducer.DataAndSettingCodeProducer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ListModel;

import Chreator.CodeProducer.CodeProducer;
import Chreator.ObjectModel.Point;
import Chreator.ObjectModel.Point.EdgePointPair;
import Chreator.ObjectModel.Point.InfoPack;
public class PointEdgeDataCodeProducer {

	private ArrayList<Point> pointList;

	public PointEdgeDataCodeProducer(ArrayList<Point> arrayList) {
		this.pointList = arrayList;
	}

	public void printPointEdgeDataCode() {
		printBoardPointsArrayCode();
		printPointEdgeRelationsCode();
	}

	private void printBoardPointsArrayCode() {
		int i = DataAndSettingCodeProducer.dataAndSettingCodes
				.indexOf("public static PointDataPackage[] boardPointsArray;");

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1,
				"public static PointDataPackage[] boardPointsArray = new PointDataPackage[]{");

		for (Point point : pointList) {
			InfoPack infoPack = point.getRelativeInformation();
			
			DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2, "new PointDataPackage(" + infoPack.posX + ", "
					+ infoPack.posY + ", " + infoPack.width + ", " + infoPack.height + ", " + point.getId() + "),");
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + pointList.size() + 2, "};");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}

	private void printPointEdgeRelationsCode() {
		
		int i = DataAndSettingCodeProducer.dataAndSettingCodes
				.indexOf("public static PointEdgePackage[] pointEdgeRelations;");
		int k = 0;

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 1,
				"public static PointEdgePackage[] pointEdgeRelations = new PointEdgePackage[]{");

		for (Point point : pointList) {
			ArrayList<EdgePointPair> edgePointPairs = point.getAllEdgePointPair();
			for (EdgePointPair edgePointPair : edgePointPairs) {
				DataAndSettingCodeProducer.dataAndSettingCodes.add(i + 2,
						"new PointEdgePackage(" + point.getId() + ", Edge.Direction." + edgePointPair.direction + ", "
								+ edgePointPair.targetPoint.getId() + "),");
				k++;
			}
		}

		DataAndSettingCodeProducer.dataAndSettingCodes.add(i + k + 2, "};");
		DataAndSettingCodeProducer.dataAndSettingCodes.remove(i);
	}
}
