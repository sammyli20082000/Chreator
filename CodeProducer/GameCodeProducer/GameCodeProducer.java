package Chreator.CodeProducer.GameCodeProducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import Chreator.CodeProducer.CodeProducer;

public class GameCodeProducer {
	public static File file;
	protected static List<String> gameCodes;

	public GameCodeProducer() {
		file = new File(CodeProducer.baseDir + "\\Game.java");
		gameCodes = new LinkedList<>();

	}

	public void produceGameJava() throws IOException {
		if (file.isDirectory()) {
			System.out.println("\"Game.java\" cannnot be opened/created properly.");
			return;
		}

		FileOutputStream fOutputStream = new FileOutputStream(file.getAbsolutePath());
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(fOutputStream, "utf-8");
		Writer writer = new BufferedWriter(oStreamWriter);

		for (String code : gameCodes) {
			writer.write(code + "\n");
		}

		writer.close();
		oStreamWriter.close();
		fOutputStream.close();
	}
}