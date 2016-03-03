package Chreator.CodeProducer.GameCodeProducer;

import javax.swing.ListModel;

public class GameFrameProducer {

	ListModel playerSidesList;

	public GameFrameProducer(ListModel playerSidesList) {
		this.playerSidesList = playerSidesList;
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

		GameCodeProducer.gameCodes.add("public Game() {");
		GameCodeProducer.gameCodes.add("game = this;");
		GameCodeProducer.gameCodes.add(
				"gameLocation = (new File(Game.class.getClassLoader().getResource(\"\").getPath())).getAbsolutePath();");
		GameCodeProducer.gameCodes.add("board = new Board();");
		for (int i = 0; i < playerSidesList.getSize(); i++) {
			GameCodeProducer.gameCodes
					.add("sides.add(DataAndSetting.PieceData.PlayerSide." + playerSidesList.getElementAt(i) + ");");
		}
		GameCodeProducer.gameCodes.add("ui = new UIHandler(handleUIEventCallBack());");
		GameCodeProducer.gameCodes.add("addDataToInfoPanel();");
		GameCodeProducer.gameCodes.add("ui.setBoard(board);");
		GameCodeProducer.gameCodes.add("ui.updateStatusBarStatus(\"Select start side and start game\");");

		// TODO:
		GameCodeProducer.gameCodes.add("ai = new AI(game);");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("private void addPointsPiecesEdgesToBoard() {");
		GameCodeProducer.gameCodes.add("for (int i = 0; i < DataAndSetting.PointEdgeData.boardPointsArray.length; i++) {");
		GameCodeProducer.gameCodes.add("DataAndSetting.PointEdgeData.PointDataPackage pack = DataAndSetting.PointEdgeData.boardPointsArray[i];");
		GameCodeProducer.gameCodes.add("board.addPoint(pack.xCoordinate, pack.yCoordinate, pack.width, pack.height, pack.id);");
		GameCodeProducer.gameCodes.add("}");
		
		GameCodeProducer.gameCodes.add("for (int i = 0; i < DataAndSetting.PointEdgeData.pointEdgeRelations.length; i++) {");
		GameCodeProducer.gameCodes.add("DataAndSetting.PointEdgePackage dataPackage = DataAndSetting.PointEdgeData.pointEdgeRelations[i];");
		GameCodeProducer.gameCodes.add("board.getPointByID(dataPackage.targetID));");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("currPieces = new ArrayList<Piece>();");
		GameCodeProducer.gameCodes.add("for (int i = 0; i < DataAndSetting.PieceData.initialPiecePlacingData.length; i++) {");
		GameCodeProducer.gameCodes.add("DataAndSetting.PieceDataPackage dataPackage = DataAndSetting.PieceData.initialPiecePlacingData[i];");
		GameCodeProducer.gameCodes.add("Piece newPiece = DataAndSetting.PieceData.makeStandardPiece(dataPackage.pieceType, dataPackage.playerSide);");
		GameCodeProducer.gameCodes.add("board.getPointByID(dataPackage.pointID).setPiece(newPiece);");
		GameCodeProducer.gameCodes.add("currPieces.add(newPiece);");
		
		GameCodeProducer.gameCodes.add("ui.refreshWindow();");

		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("private FileHandler.EventCallBackHandler handleFileEventCallBack()");

		GameCodeProducer.gameCodes.add("private UIHandler.EventCallBackHandler handleUIEventCallBack()");

		GameCodeProducer.gameCodes.add("private void handleMenuBarMessage(UIHandler.MenubarMessage msg)");

		GameCodeProducer.gameCodes.add("private void programTerminate() {");
		GameCodeProducer.gameCodes.add("System.exit(0);");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("public void computerMakeMove()");

		GameCodeProducer.gameCodes.add("private void addDataToInfoPanel() {}");

		GameCodeProducer.gameCodes.add("}");
	}
}
