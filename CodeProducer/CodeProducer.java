package Chreator.CodeProducer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.ListModel;

import Chreator.CodeProducer.BoardModelProducer.BoardModelProducer;
import Chreator.CodeProducer.DataAndSettingCodeProducer.DataAndSettingCodeProducer;
import Chreator.CodeProducer.GameCodeProducer.GameCodeProducer;
import Chreator.CodeProducer.PieceModelProducer.PieceModelProducer;
import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

public class CodeProducer {
	public static String baseDir;
	public static String pathname;

	private BoardModelProducer boardModelCodeProducer;
	private DataAndSettingCodeProducer dataAndSettingCodeProducer;
	private PieceModelProducer pieceModelProducer;
	private GameCodeProducer gameCodeProducer;

	public CodeProducer(String pathname, String folderName, ArrayList<Point> arrayList,
			ListModel<String> edgeDirectionList, ListModel<String> playerSidesList,
			ArrayList<PieceProfile> piecePorfiles, String boardImageLink) {
		if (pathname.equals("") || pathname.equals(null)) {
			JOptionPane.showMessageDialog(null, "Please specify a file path!");
			return;
		}

		if (folderName.equals("") || folderName.equals(null)) {
			this.pathname = pathname + "\\" + "Chess_Engine";
			baseDir = pathname + "\\" + "Chess_Engine" + "\\src\\Executable";
		} else {
			this.pathname = pathname + "\\" + folderName;
			baseDir = pathname + "\\" + folderName + "\\src\\Executable";
		}
		boardModelCodeProducer = new BoardModelProducer(edgeDirectionList);
		dataAndSettingCodeProducer = new DataAndSettingCodeProducer(arrayList, playerSidesList, piecePorfiles, boardImageLink);
		pieceModelProducer = new PieceModelProducer(piecePorfiles);
		gameCodeProducer = new GameCodeProducer(playerSidesList);
	}

	public void produceExecutable() throws Exception {
		File file = new File(baseDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		File picF = new File(pathname + "\\pic");
		if (!picF.exists()) {
			picF.mkdirs();
		}

		dataAndSettingCodeProducer.produceDataAndSettingJava();
		boardModelCodeProducer.produceBoardModel();
		pieceModelProducer.producePieceModel();
		gameCodeProducer.produceGameJava();

		File uiHandlerModelFile = new File("UIHandlerModel");
		File uiFile = new File(baseDir + "\\UIHandlerModel");
		FolderCopyer.copyFolder(uiHandlerModelFile, uiFile);

		File fileHandlerModelFile = new File("FileHandlerModel");
		File fFile = new File(baseDir + "\\FileHandlerModel");
		FolderCopyer.copyFolder(fileHandlerModelFile, fFile);

		File objectModelFile = new File("ObjectModel");
		File omFile = new File(baseDir + "\\ObjectModel");
		FolderCopyer.copyFolder(objectModelFile, omFile);
	}
}