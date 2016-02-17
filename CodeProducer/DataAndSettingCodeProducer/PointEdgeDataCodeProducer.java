package Chreator.CodeProducer.DataAndSettingCodeProducer;

import java.util.ArrayList;

import Chreator.ObjectModel.Point;
import Chreator.ObjectModel.Point.EdgePointPair;
import Chreator.ObjectModel.Point.InfoPack;
public class PointEdgeDataCodeProducer {

	private ArrayList<Point> pointList;

	public PointEdgeDataCodeProducer(ArrayList<Point> pList) {
		pointList = pList;
	}

	public void printPointEdgeDataCode() {
		printBoardPointsArrayCode();
		printPointEdgeRelationsCode();
	}

	private void printBoardPointsArrayCode() {

		int i = DataAndSettingCodeProducer.DataAndSettingCodes
				.indexOf("public static PointDataPackage[] boardPointsArray;");

		DataAndSettingCodeProducer.DataAndSettingCodes.add(i + 1,
				"public static PointDataPackage[] boardPointsArray = new PointDataPackage[]{");

		for (Point point : pointList) {
			InfoPack infoPack = point.getPixelInformation();
			
			DataAndSettingCodeProducer.DataAndSettingCodes.add(i + 2, "new PointDataPackage(" + infoPack.posX + ", "
					+ infoPack.posY + ", " + infoPack.width + ", " + infoPack.height + ", " + point.getId() + "),");
		}

		DataAndSettingCodeProducer.DataAndSettingCodes.add(i + pointList.size() + 2, "};");
		DataAndSettingCodeProducer.DataAndSettingCodes.remove(i);
	}

	private void printPointEdgeRelationsCode() {
		
		int i = DataAndSettingCodeProducer.DataAndSettingCodes
				.indexOf("public static PointEdgePackage[] pointEdgeRelations;");
		int k = 0;

		DataAndSettingCodeProducer.DataAndSettingCodes.add(i + 1,
				"public static PointEdgePackage[] pointEdgeRelations = new PointEdgePackage[]{");

		for (Point point : pointList) {
			ArrayList<EdgePointPair> edgePointPairs = point.getAllEdgePointPair();
			for (EdgePointPair edgePointPair : edgePointPairs) {
				DataAndSettingCodeProducer.DataAndSettingCodes.add(i + 2,
						"new PointEdgePackage(" + point.getId() + ", Edge.Direction." + edgePointPair.direction + ", "
								+ edgePointPair.targetPoint.getId() + "),");
				k++;
			}
		}

		DataAndSettingCodeProducer.DataAndSettingCodes.add(i + k + 2, "};");
		DataAndSettingCodeProducer.DataAndSettingCodes.remove(i);
	}
}
