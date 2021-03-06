package Chreator.CodeProducer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;
import javax.swing.ListModel;

import Chreator.ChreatorLauncher;
import Chreator.CodeProducer.BoardModelProducer.BoardModelProducer;
import Chreator.CodeProducer.DataAndSettingCodeProducer.DataAndSettingCodeProducer;
import Chreator.CodeProducer.GameCodeProducer.GameCodeProducer;
import Chreator.CodeProducer.PieceModelProducer.PieceModelProducer;
import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;
import Chreator.UIModule.ProjectSettingPanel.GameType;

public class CodeProducer {
	public static String baseDir;
	public static String pathname;

	private BoardModelProducer boardModelCodeProducer;
	private DataAndSettingCodeProducer dataAndSettingCodeProducer;
	private PieceModelProducer pieceModelProducer;
	private GameCodeProducer gameCodeProducer;

	public CodeProducer(String pathname, String folderName, ArrayList<Point> arrayList,
			ListModel<String> edgeDirectionList, ListModel<String> playerSidesList,
			ArrayList<PieceProfile> piecePorfiles, String boardImageLink, GameType type) {
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
		dataAndSettingCodeProducer = new DataAndSettingCodeProducer(arrayList, playerSidesList, piecePorfiles,
				boardImageLink);
		pieceModelProducer = new PieceModelProducer(piecePorfiles);
		gameCodeProducer = new GameCodeProducer(playerSidesList, piecePorfiles, type);
	}

	public void produceExecutable() throws IOException {
		File file = new File(baseDir);
		if (!file.exists()) {
			file.mkdirs();
		} else {
			file.setWritable(true);
		}

		File picF = new File(pathname + "\\pic");
		if (!picF.exists()) {
			picF.mkdirs();
		}

		dataAndSettingCodeProducer.produceDataAndSettingJava();
		boardModelCodeProducer.produceBoardModel();
		pieceModelProducer.producePieceModel();
		gameCodeProducer.produceGameJava();

		JarFile jar = new JarFile(ChreatorLauncher.class.getProtectionDomain().getCodeSource().getLocation().getPath());

		Enumeration<JarEntry> enumEntries = jar.entries();

		while (enumEntries.hasMoreElements()) {

			JarEntry entry = (JarEntry) enumEntries.nextElement();
			// JOptionPane.showMessageDialog(null, entry.getName());
			File uiFile = new File(baseDir + "\\" + entry.getName());

			if (entry.getName().startsWith("UIHandlerModel") || entry.getName().startsWith("FileHandlerModel")
					|| entry.getName().startsWith("ObjectModel") || entry.getName().startsWith("SocketConnection")) {
				if (entry.isDirectory()) {
					uiFile.mkdir();
					continue;
				}

				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(uiFile);
				while (is.available() > 0) {
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}
		}

//		File uiHandlerModelFile = new File("UIHandlerModel");
//		File uiFile = new File(baseDir + "\\UIHandlerModel");
//		FolderCopyer.copyFolder(uiHandlerModelFile, uiFile);
//
//		File fileHandlerModelFile = new File("FileHandlerModel");
//		File fFile = new File(baseDir + "\\FileHandlerModel");
//		FolderCopyer.copyFolder(fileHandlerModelFile, fFile);
//
//		File objectModelFile = new File("ObjectModel");
//		File omFile = new File(baseDir + "\\ObjectModel");
//		FolderCopyer.copyFolder(objectModelFile, omFile);
//		
//		File socketConnectionFile = new File("SocketConnection");
//		File scFile = new File(baseDir + "\\SocketConnection");
//		FolderCopyer.copyFolder(socketConnectionFile, scFile);
	}
}