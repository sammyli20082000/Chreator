package Chreator.CodeProducer.DataAndSettingCodeProducer;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ListModel;

import Chreator.CodeProducer.CodeProducer;
import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

public class DataAndSettingCodeProducer {
	public static File file;
	protected static List<String> dataAndSettingCodes;
	private DataAndSettingFrameProducer dataAndSettingFrameProducer;
	private PointEdgeDataCodeProducer pointEdgeDataCodeProducer;
	private PieceDataProducer pieceDataProducer;
	
	private String boardImageLink;

	public DataAndSettingCodeProducer(ArrayList<Point> arrayList, ListModel<String> playerSidesList, ArrayList<PieceProfile> pieceProfiles, String boardImageLink) {
		file = new File(CodeProducer.baseDir + "\\DataAndSetting.java");
		dataAndSettingCodes = new LinkedList<>();

		dataAndSettingFrameProducer = new DataAndSettingFrameProducer(playerSidesList, boardImageLink);
		pointEdgeDataCodeProducer = new PointEdgeDataCodeProducer(arrayList);
		pieceDataProducer = new PieceDataProducer(playerSidesList, pieceProfiles);
		
		this.boardImageLink = boardImageLink;
	}

	public void produceDataAndSettingJava() throws IOException {
		if (file.isDirectory()) {
			System.out.println("\"DataAndSetting.java\" cannnot be opened/created properly.");
			return;
		}

		FileOutputStream fOutputStream = new FileOutputStream(file.getAbsolutePath());
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(fOutputStream, "utf-8");
		Writer writer = new BufferedWriter(oStreamWriter);
		
		File imageFile = new File(CodeProducer.pathname + "\\pic\\board.png");
		File boardImage = new File(boardImageLink);
		try {
			Files.copy(Paths.get(boardImage.getAbsolutePath()), Paths.get(imageFile.getAbsolutePath()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {

		}
		if (boardImageLink.equals("board.png")) {
			boardImage.delete();
		}

		dataAndSettingFrameProducer.printDataAndSettingJavaFrame();
		pointEdgeDataCodeProducer.printPointEdgeDataCode();
		pieceDataProducer.printPieceDataCode();

		for (String code : dataAndSettingCodes) {
			writer.write(code + System.getProperty("line.separator"));
		}

		writer.close();
		oStreamWriter.close();
		fOutputStream.close();
	}
}
