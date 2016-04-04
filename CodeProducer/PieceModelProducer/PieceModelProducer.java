package Chreator.CodeProducer.PieceModelProducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Chreator.CodeProducer.CodeProducer;
import Chreator.ObjectModel.PieceProfile;

public class PieceModelProducer {
	public static File file;
	protected static List<String> pieceCodes;
	protected static ArrayList<List<String>> individualPieceCodes;

	private ArrayList<PieceProfile> pieceProfiles;

	private PieceCodeProducer pieceCodeProducer;
	private IndividualPieceCodeProducer individualPieceCodeProducer;

	public PieceModelProducer(ArrayList<PieceProfile> pieceProfiles) {
		file = new File(CodeProducer.baseDir + "\\PieceModel");
		pieceCodes = new LinkedList<>();
		individualPieceCodes = new ArrayList<>();

		this.pieceProfiles = pieceProfiles;

		pieceCodeProducer = new PieceCodeProducer();
	}

	public void producePieceModel() throws IOException {
		if (!file.exists()) {
			file.mkdirs();
		}

		pieceCodeProducer.printPieceJavaFrameCode();

		File pieceFile = new File(file.getAbsolutePath() + "\\Piece.java");
		FileOutputStream fOutputStream1 = new FileOutputStream(pieceFile.getAbsolutePath());
		OutputStreamWriter oStreamWriter1 = new OutputStreamWriter(fOutputStream1, "utf-8");
		Writer writer1 = new BufferedWriter(oStreamWriter1);

		for (String code : pieceCodes) {
			writer1.write(code + System.getProperty("line.separator"));
		}

		writer1.close();
		oStreamWriter1.close();
		fOutputStream1.close();

		for (int i = 0; i < pieceProfiles.size(); i++) {
			individualPieceCodeProducer = new IndividualPieceCodeProducer(pieceProfiles.get(i));

			individualPieceCodeProducer.printIndividualPieceJava();

			String pieceClassName = pieceProfiles.get(i).pieceClassName.substring(0, 1).toUpperCase()
					+ pieceProfiles.get(i).pieceClassName.substring(1).toLowerCase();
			String pieceSide = pieceProfiles.get(i).playerSide.substring(0, 1).toUpperCase()
					+ pieceProfiles.get(i).playerSide.substring(1).toLowerCase();
			File individualPieceFile = new File(file.getAbsolutePath() + "\\" + pieceSide + pieceClassName + ".java");
			FileOutputStream fOutputStream2 = new FileOutputStream(individualPieceFile.getAbsolutePath());
			OutputStreamWriter oStreamWriter2 = new OutputStreamWriter(fOutputStream2, "utf-8");
			Writer writer2 = new BufferedWriter(oStreamWriter2);

			for (String code : individualPieceCodes.get(i)) {
				writer2.write(code + System.getProperty("line.separator"));
			}

			writer2.close();
			oStreamWriter2.close();
			fOutputStream2.close();
		}
	}
}
