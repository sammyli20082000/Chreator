package Chreator.CodeProducer;

import java.io.File;
import java.util.ArrayList;

import javax.swing.ListModel;

import Chreator.CodeProducer.BoardCodeProducer.BoardModelCodeProducer;
import Chreator.CodeProducer.DataAndSettingCodeProducer.DataAndSettingCodeProducer;
import Chreator.ObjectModel.Point;

public class CodeProducer {
	public static String baseDir;

	private BoardModelCodeProducer boardModelCodeProducer;
	private DataAndSettingCodeProducer dataAndSettingCodeProducer;

	public CodeProducer(String pathname, String folderName, ArrayList<Point> pList, ListModel edList) {
		if (folderName.equals("") || folderName.equals(null)) {
			baseDir = pathname + "\\" + "Chess_Engine" + "\\Executable";
		} else {
			baseDir = pathname + "\\" + folderName + "\\Executable";
		}
		boardModelCodeProducer = new BoardModelCodeProducer(edList);
		dataAndSettingCodeProducer = new DataAndSettingCodeProducer(pList);
	}

	public void produceExecutable() {
		File file = new File(baseDir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}