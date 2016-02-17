package Chreator.CodeProducer.BoardModelProducer;

import javax.swing.ListModel;

public class EdgeCodeProducer {
	
	ListModel edgeDirectionList;
	
	public EdgeCodeProducer(ListModel edgeDirectionlList) {
		this.edgeDirectionList = edgeDirectionlList;
	}
	
	public void printEdgeJavaFrameCode() {
		BoardModelProducer.edgeCodes.add("package Executable.BoardModel;");
		
		BoardModelProducer.edgeCodes.add("public class Edge {");
		
		BoardModelProducer.edgeCodes.add("public enum Direction {");
		
		for (int i = 0; i < edgeDirectionList.getSize(); i++) {
			BoardModelProducer.edgeCodes.add(edgeDirectionList.getElementAt(i) + ",");
		}
		
		BoardModelProducer.edgeCodes.add("}");
		
		BoardModelProducer.edgeCodes.add("}");
	}
}
