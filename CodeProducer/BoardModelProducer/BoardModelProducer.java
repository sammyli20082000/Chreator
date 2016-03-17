package Chreator.CodeProducer.BoardModelProducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.plaf.metal.MetalBorders.InternalFrameBorder;

import Chreator.CodeProducer.CodeProducer;

public class BoardModelProducer {
	public static File file;
	protected static List<String> boardCodes;
	protected static List<String> edgeCodes;
	protected static List<String> pointCodes;

	private BoardCodeProducer boardCodeProducer;
	private EdgeCodeProducer edgeCodeProducer;
	private PointCodeProducer pointCodeProducer;

	public BoardModelProducer(ListModel edList) {
		file = new File(CodeProducer.baseDir + "\\BoardModel");
		boardCodes = new LinkedList<>();
		edgeCodes = new LinkedList<>();
		pointCodes = new LinkedList<>();

		boardCodeProducer = new BoardCodeProducer();
		edgeCodeProducer = new EdgeCodeProducer(edList);
		pointCodeProducer = new PointCodeProducer();
	}

	public void produceBoardModel() throws IOException {
		if (!file.exists()) {
			file.mkdirs();
		}

		boardCodeProducer.printBoardJavaFrameCode();
		edgeCodeProducer.printEdgeJavaFrameCode();
		pointCodeProducer.printPointJavaFrameCode();

		File boardFile = new File(file.getAbsolutePath() + "\\Board.java");
		File edgeFile = new File(file.getAbsolutePath() + "\\Edge.java");
		File pointFile = new File(file.getAbsolutePath() + "\\Point.java");

		FileOutputStream fOutputStream1 = new FileOutputStream(boardFile.getAbsolutePath());
		OutputStreamWriter oStreamWriter1 = new OutputStreamWriter(fOutputStream1, "utf-8");
		Writer writer1 = new BufferedWriter(oStreamWriter1);

		FileOutputStream fOutputStream2 = new FileOutputStream(edgeFile.getAbsolutePath());
		OutputStreamWriter oStreamWriter2 = new OutputStreamWriter(fOutputStream2, "utf-8");
		Writer writer2 = new BufferedWriter(oStreamWriter2);

		FileOutputStream fOutputStream3 = new FileOutputStream(pointFile.getAbsolutePath());
		OutputStreamWriter oStreamWriter3 = new OutputStreamWriter(fOutputStream3, "utf-8");
		Writer writer3 = new BufferedWriter(oStreamWriter3);

		for (String code : boardCodes) {
			writer1.write(code + System.getProperty("line.separator"));
		}

		for (String code : edgeCodes) {
			writer2.write(code + System.getProperty("line.separator"));
		}

		for (String code : pointCodes) {
			writer3.write(code + System.getProperty("line.separator"));
		}

		writer1.close();
		oStreamWriter1.close();
		fOutputStream1.close();

		writer2.close();
		oStreamWriter2.close();
		fOutputStream2.close();

		writer3.close();
		oStreamWriter3.close();
		fOutputStream3.close();
	}
}
