package Chreator.CodeProducer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.ListModel;

import Chreator.CodeProducer.BoardModelProducer.BoardModelProducer;
import Chreator.CodeProducer.DataAndSettingCodeProducer.DataAndSettingCodeProducer;
import Chreator.CodeProducer.PieceModelProducer.PieceModelProducer;
import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

public class CodeProducer {
	public static String baseDir;

	private BoardModelProducer boardModelCodeProducer;
	private DataAndSettingCodeProducer dataAndSettingCodeProducer;
	private PieceModelProducer pieceModelProducer;

	public CodeProducer(String pathname, String folderName, ArrayList<Point> arrayList, ListModel<String> edgeDirectionList,
			ListModel<String> playerSidesList, ArrayList<PieceProfile> piecePorfiles) {
		if(pathname.equals("") || pathname.equals(null)) {
			JOptionPane.showMessageDialog(null, "Please specify a file path!");
			return;
		}
		
		if (folderName.equals("") || folderName.equals(null)) {
			baseDir = pathname + "\\" + "Chess_Engine" + "\\Executable";
		} else {
			baseDir = pathname + "\\" + folderName + "\\Executable";
		}
		boardModelCodeProducer = new BoardModelProducer(edgeDirectionList);
		dataAndSettingCodeProducer = new DataAndSettingCodeProducer(arrayList, playerSidesList, piecePorfiles);
		pieceModelProducer = new PieceModelProducer(piecePorfiles);
	}

	public void produceExecutable() throws Exception {
		File file = new File(baseDir);
		System.out.println(file.getAbsolutePath());
		if (!file.exists()) {
			file.mkdirs();
		}
		
		File picF = new File(baseDir + "\\pic");
		if (!picF.exists()) {
			picF.mkdirs();
		}

		dataAndSettingCodeProducer.produceDataAndSettingJava();
		boardModelCodeProducer.produceBoardModel();
	}
}