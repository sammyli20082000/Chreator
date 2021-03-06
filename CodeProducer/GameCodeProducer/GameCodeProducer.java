package Chreator.CodeProducer.GameCodeProducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ListModel;

import Chreator.CodeProducer.CodeProducer;
import Chreator.ObjectModel.PieceProfile;
import Chreator.UIModule.ProjectSettingPanel.GameType;

public class GameCodeProducer {
	public static File file;
	protected static List<String> gameCodes;
	GameFrameProducer gameFrameProducer;
	HandleUIEventCallBackProducer handleUIEventCallBackProducer;
	HandleFileEventCallBackProducer handleFileEventCallBackProducer;
	ComputerMakeMoveCodeProducer computerMakeMoveCodeProducer;

	public GameCodeProducer(ListModel<String> playerSidesList, ArrayList<PieceProfile> piecePorfiles, GameType type) {
		file = new File(CodeProducer.baseDir + "\\Game.java");
		gameCodes = new LinkedList<>();
		gameFrameProducer = new GameFrameProducer(playerSidesList, piecePorfiles, type);
		handleUIEventCallBackProducer  = new HandleUIEventCallBackProducer(playerSidesList, type);
		computerMakeMoveCodeProducer = new ComputerMakeMoveCodeProducer();
		handleFileEventCallBackProducer = new HandleFileEventCallBackProducer();
	}

	public void produceGameJava() throws IOException {
		if (file.isDirectory()) {
			System.out.println("\"Game.java\" cannnot be opened/created properly.");
			return;
		}
		
		gameFrameProducer.printGameJavaFrame();
		handleUIEventCallBackProducer.printHandleUIEventCallBackCode();
		handleFileEventCallBackProducer.printHandleFileEventCallBackCode();
		computerMakeMoveCodeProducer.printComputerMakeMoveCodes();

		FileOutputStream fOutputStream = new FileOutputStream(file.getAbsolutePath());
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(fOutputStream, "utf-8");
		Writer writer = new BufferedWriter(oStreamWriter);

		for (String code : gameCodes) {
			writer.write(code + System.getProperty("line.separator"));
		}

		writer.close();
		oStreamWriter.close();
		fOutputStream.close();
	}
}
