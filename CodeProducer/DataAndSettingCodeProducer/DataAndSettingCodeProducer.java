package Chreator.CodeProducer.DataAndSettingCodeProducer;

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
import Chreator.ObjectModel.Point;

public class DataAndSettingCodeProducer {
	public static File file;
	protected static List<String> dataAndSettingCodes;
	private DataAndSettingFrameProducer dataAndSettingFrameProducer;
	private PointEdgeDataCodeProducer pointEdgeDataCodeProducer;

	public DataAndSettingCodeProducer(ArrayList<Point> pList) {
		file = new File(CodeProducer.baseDir + "\\DataAndSetting.java");
		dataAndSettingCodes = new LinkedList<>();

		dataAndSettingFrameProducer = new DataAndSettingFrameProducer();
		pointEdgeDataCodeProducer = new PointEdgeDataCodeProducer(pList);
	}

	public void produceDataAndSettingJava() throws IOException {
		if (file.isDirectory()) {
			System.out.println("\"DataAndSetting.java\" cannnot be opened/created properly.");
			return;
		}

		FileOutputStream fOutputStream = new FileOutputStream(file.getAbsolutePath());
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(fOutputStream, "utf-8");
		Writer writer = new BufferedWriter(oStreamWriter);

		dataAndSettingFrameProducer.printDataAndSettingJavaFrame();
		pointEdgeDataCodeProducer.printPointEdgeDataCode();

		for (String code : dataAndSettingCodes) {
			writer.write(code + "\n");
		}

		writer.close();
		oStreamWriter.close();
		fOutputStream.close();
	}
}
