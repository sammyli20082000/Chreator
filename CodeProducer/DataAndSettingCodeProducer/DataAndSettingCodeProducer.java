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
	protected static List<String> DataAndSettingCodes;
	private DataAndSettingFrameProducer dataAndSettingFrameProducer;
	private PointEdgeDataCodeProducer pointEdgeDataCodeProducer;
	
	
	public DataAndSettingCodeProducer(ArrayList<Point> pList) {
		file = new File(CodeProducer.baseDir);
		DataAndSettingCodes = new LinkedList<>();
		
		dataAndSettingFrameProducer = new DataAndSettingFrameProducer();
		pointEdgeDataCodeProducer = new PointEdgeDataCodeProducer(pList);
	}
	
	public void produceDataAndSetting() throws IOException {
		
		if(!file.exists() || file.isDirectory()) {
			System.out.println("\"DataAndSetting.java\" cannnot be opened/created properly.");
			return;
		}
		
		dataAndSettingFrameProducer.printDataAndSettingJavaFrame();
		
		FileOutputStream fOutputStream = new FileOutputStream(file.getAbsolutePath());
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(fOutputStream, "utf-8");
		Writer writer = new BufferedWriter(oStreamWriter);
		
		dataAndSettingFrameProducer.printDataAndSettingJavaFrame();
		pointEdgeDataCodeProducer.printPointEdgeDataCode();
		
		for (String code : DataAndSettingCodes) {
			writer.write(code + "\n");
		}
		
		writer.close();
		oStreamWriter.close();
		fOutputStream.close();
	}
}
