package Chreator.CodeProducer.BoardCodeProducer;

import javax.swing.ListModel;

public class EdgeCodeProducer {
	
	ListModel edgeDirectionList;
	
	public EdgeCodeProducer(ListModel edgeDirectionlList) {
		this.edgeDirectionList = edgeDirectionlList;
	}
	
	public void printEdgeJavaFrameCode() {
		BoardModelCodeProducer.edgeCodes.add("package Executable.BoardModel;");
		
		BoardModelCodeProducer.edgeCodes.add("public class Edge {");
		
		BoardModelCodeProducer.edgeCodes.add("public enum Direction {");
		
		for (int i = 0; i < edgeDirectionList.getSize(); i++) {
			BoardModelCodeProducer.edgeCodes.add(edgeDirectionList.getElementAt(i) + ",");
		}
		
		BoardModelCodeProducer.edgeCodes.add("}");
		
		BoardModelCodeProducer.edgeCodes.add("}");
	}
}
