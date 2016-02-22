package Chreator.CodeProducer.GameCodeProducer;

public class GameFrameProducer {
	
	public GameFrameProducer() {
		
	}
	
	public void printGameJavaFrame() {
		String codes[] = {
				"package Executable;",
				
				"import java.io.*;",
				"import java.util.*;",
				
				"import Executable.BoardModel.Board;",
				"import Executable.BoardModel.Point;",
				"import Executable.FileHandlerModel.FileHandler;",
				"import Executable.PieceModel.Piece;",
				"import Executable.UIHandlerModel.UIHandler;",
				
				"public class Game implements Serializable {",
				
				"Game game;",
				"Board board;",
				"AI ai;",
				"boolean canCapture;",
				"ArrayList<String> sides = new ArrayList<>();",
				"String currentSide;",
				"ArrayList<Piece> currPieces;",
				"ArrayList<Node> history;",
				"Piece selectedPiece;",
				"Point selectedPoint;",
				"UIHandler ui;",
				"String gameLocation;",
				
				"public static void main(String[] args) {",
				"new Game();",
				"}",
				
				
				
				"}"
		};
	}
}
