package Chreator.CodeProducer.PieceModelProducer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Chreator.CodeProducer.CodeProducer;

public class PieceModelProducer {
	public static File file;
	protected static List<String> pieceCodes;
	protected static ArrayList<List> individualPieceCodes;

	public PieceModelProducer() {
		file = new File(CodeProducer.baseDir + "\\PieceModel");
		pieceCodes = new LinkedList<>();
		individualPieceCodes = new ArrayList<>();
	}

	public void producePieceModel() throws IOException {
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
