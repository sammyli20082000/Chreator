package Chreator.CodeProducer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ListModel;

import Chreator.CodeProducer.BoardModelProducer.BoardModelProducer;
import Chreator.CodeProducer.DataAndSettingCodeProducer.DataAndSettingCodeProducer;
import Chreator.CodeProducer.PieceModelProducer.PieceModelProducer;
import Chreator.ObjectModel.Point;

public class CodeProducer {
	public static String baseDir;

	private BoardModelProducer boardModelCodeProducer;
	private DataAndSettingCodeProducer dataAndSettingCodeProducer;
	private PieceModelProducer pieceModelProducer;

	public CodeProducer(String pathname, String folderName, ArrayList<Point> arrayList, ListModel<String> edgeDirectionList,
			ListModel<String> playerSidesList) {
		if (folderName.equals("") || folderName.equals(null)) {
			baseDir = pathname + "\\" + "Chess_Engine" + "\\Executable";
		} else {
			baseDir = pathname + "\\" + folderName + "\\Executable";
		}
		boardModelCodeProducer = new BoardModelProducer(edgeDirectionList);
		dataAndSettingCodeProducer = new DataAndSettingCodeProducer(arrayList, playerSidesList);
		pieceModelProducer = new PieceModelProducer();
	}

	public void produceExecutable() throws IOException {
		File file = new File(baseDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		dataAndSettingCodeProducer.produceDataAndSettingJava();
		boardModelCodeProducer.produceBoardModel();
	}
}