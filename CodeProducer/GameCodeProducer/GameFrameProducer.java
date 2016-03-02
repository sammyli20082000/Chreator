package Chreator.CodeProducer.GameCodeProducer;

public class GameFrameProducer {
	
	public GameFrameProducer() {
		
	}
	
	public void printGameJavaFrame() {
		GameCodeProducer.gameCodes.add("package Executable;");
		
		GameCodeProducer.gameCodes.add("import java.io.*;");
		GameCodeProducer.gameCodes.add("import java.util.*;");
		
		GameCodeProducer.gameCodes.add("import Executable.BoardModel.Board;");
		GameCodeProducer.gameCodes.add("import Executable.BoardModel.Point;");
		GameCodeProducer.gameCodes.add("import Executable.FileHandlerModel.FileHandler;");
		GameCodeProducer.gameCodes.add("import Executable.PieceModel.Piece;");
		GameCodeProducer.gameCodes.add("import Executable.UIHandlerModel.UIHandler;");
		
		GameCodeProducer.gameCodes.add("public class Game implements Serializable {");
		
		GameCodeProducer.gameCodes.add("Game game;");
		GameCodeProducer.gameCodes.add("Board board;");
		GameCodeProducer.gameCodes.add("AI ai;");
		GameCodeProducer.gameCodes.add("boolean canCapture = true;");
		GameCodeProducer.gameCodes.add("ArrayList<String> sides = new ArrayList<>();");
		GameCodeProducer.gameCodes.add("String currentSide;");
		GameCodeProducer.gameCodes.add("ArrayList<Piece> currPieces;");
		GameCodeProducer.gameCodes.add("ArrayList<Node> history;");
		GameCodeProducer.gameCodes.add("Piece selectedPiece;");
		GameCodeProducer.gameCodes.add("Point selectedPoint;");
		GameCodeProducer.gameCodes.add("UIHandler ui;");
		GameCodeProducer.gameCodes.add("String gameLocation;");
		
		GameCodeProducer.gameCodes.add("public static void main(String[] args) {");
		GameCodeProducer.gameCodes.add("new Game();");
		GameCodeProducer.gameCodes.add("}");
		
		GameCodeProducer.gameCodes.add("public Game()");
		
		GameCodeProducer.gameCodes.add("private void addPointsPiecesEdgesToBoard()");
		
		GameCodeProducer.gameCodes.add("private FileHandler.EventCallBackHandler handleFileEventCallBack()");
		
		GameCodeProducer.gameCodes.add("private UIHandler.EventCallBackHandler handleUIEventCallBack()");
		
		GameCodeProducer.gameCodes.add("private void handleMenuBarMessage(UIHandler.MenubarMessage msg)");
		
		GameCodeProducer.gameCodes.add("private void programTerminate()");
		
		GameCodeProducer.gameCodes.add("public void computerMakeMove()");
		
		GameCodeProducer.gameCodes.add("private void addDataToInfoPanel()");
		
		GameCodeProducer.gameCodes.add("}");
	}
}
