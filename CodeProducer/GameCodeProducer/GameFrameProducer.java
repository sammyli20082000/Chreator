package Chreator.CodeProducer.GameCodeProducer;

import java.util.ArrayList;

import javax.swing.JRadioButton;
import javax.swing.ListModel;

import Chreator.ObjectModel.PieceProfile;
import Chreator.UIModule.ProjectSettingPanel.GameType;

public class GameFrameProducer {

	ListModel<String> playerSidesList;
	ArrayList<PieceProfile> profiles;
	GameType type;

	public GameFrameProducer(ListModel<String> playerSidesList, ArrayList<PieceProfile> piecePorfiles, GameType type) {
		this.playerSidesList = playerSidesList;
		this.profiles = piecePorfiles;
		this.type = type;
	}

	public void printGameJavaFrame() {
		GameCodeProducer.gameCodes.add("package Executable;");

		GameCodeProducer.gameCodes.add("import java.io.*;");
		GameCodeProducer.gameCodes.add("import java.util.*;");

		GameCodeProducer.gameCodes.add("import javax.swing.*;");

		GameCodeProducer.gameCodes.add("import Executable.BoardModel.Board;");
		GameCodeProducer.gameCodes.add("import Executable.BoardModel.Point;");
		GameCodeProducer.gameCodes.add("import Executable.FileHandlerModel.FileHandler;");
		GameCodeProducer.gameCodes.add("import Executable.PieceModel.Piece;");
		GameCodeProducer.gameCodes.add("import Executable.UIHandlerModel.UIHandler;");
		GameCodeProducer.gameCodes.add("import Executable.ObjectModel.*;");

		if (type.equals(GameType.ADD_TYPE))
			GameCodeProducer.gameCodes.add("// GameType: ADD_TYPE");
		else if (type.equals(GameType.MOVE_TYPE))
			GameCodeProducer.gameCodes.add("// GameType: MOVE_TYPE");

		GameCodeProducer.gameCodes.add("public class Game implements Serializable {");

		GameCodeProducer.gameCodes.add("Game game;");
		GameCodeProducer.gameCodes.add("public Board board;");
		GameCodeProducer.gameCodes.add("AI ai;");
		GameCodeProducer.gameCodes.add("boolean canCapture = true;");
		GameCodeProducer.gameCodes.add("public ArrayList<String> sides = new ArrayList<>();");
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

		GameCodeProducer.gameCodes.add("ai = new AI(game);");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("private void addPointsPiecesEdgesToBoard() {");
		GameCodeProducer.gameCodes
				.add("for (int i = 0; i < DataAndSetting.PointEdgeData.boardPointsArray.length; i++) {");
		GameCodeProducer.gameCodes.add(
				"DataAndSetting.PointEdgeData.PointDataPackage pack = DataAndSetting.PointEdgeData.boardPointsArray[i];");
		GameCodeProducer.gameCodes
				.add("board.addPoint(pack.xCoordinate, pack.yCoordinate, pack.width, pack.height, pack.id);");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes
				.add("for (int i = 0; i < DataAndSetting.PointEdgeData.pointEdgeRelations.length; i++) {");
		GameCodeProducer.gameCodes.add(
				"DataAndSetting.PointEdgePackage dataPackage = DataAndSetting.PointEdgeData.pointEdgeRelations[i];");
		GameCodeProducer.gameCodes.add("board.getPointById(dataPackage.sourceID).addEdge(dataPackage.edgeDirection,");
		GameCodeProducer.gameCodes.add("board.getPointById(dataPackage.targetID));");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("currPieces = new ArrayList<Piece>();");
		GameCodeProducer.gameCodes
				.add("for (int i = 0; i < DataAndSetting.PieceData.initialPiecePlacingData.length; i++) {");
		GameCodeProducer.gameCodes.add(
				"DataAndSetting.PieceDataPackage dataPackage = DataAndSetting.PieceData.initialPiecePlacingData[i];");
		GameCodeProducer.gameCodes.add(
				"Piece newPiece = DataAndSetting.PieceData.makeStandardPiece(dataPackage.pieceType, dataPackage.playerSide);");
		GameCodeProducer.gameCodes.add("board.getPointById(dataPackage.pointID).setPiece(newPiece);");
		GameCodeProducer.gameCodes.add("currPieces.add(newPiece);");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("ui.refreshWindow();");

		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("private FileHandler.EventCallBackHandler handleFileEventCallBack()");

		GameCodeProducer.gameCodes.add("private UIHandler.EventCallBackHandler handleUIEventCallBack()");

		GameCodeProducer.gameCodes.add("private void updateUIMoveHistoryAndStatusBar(Point point) {");
		GameCodeProducer.gameCodes.add("if (!AI.virtualMoving) {");
		GameCodeProducer.gameCodes
				.add("ui.updateStatusBarStatus(board.getMoveString(selectedPiece, selectedPoint, point));");
		GameCodeProducer.gameCodes
				.add("ui.addMovementHistoryRecord(board.getMoveString(selectedPiece, selectedPoint, point));");
		GameCodeProducer.gameCodes.add("}");
		GameCodeProducer.gameCodes.add("Map<Integer, Integer> state = new HashMap<>();");
		GameCodeProducer.gameCodes.add("for (Point everyPoint : board.getPoints()) {");
		GameCodeProducer.gameCodes.add("if (everyPoint.getPiece() != null)");
		GameCodeProducer.gameCodes.add("state.put(everyPoint.getId(), everyPoint.getPiece().getId());");
		GameCodeProducer.gameCodes.add("}");
		GameCodeProducer.gameCodes
				.add("history.add(new Node(state, currentSide, selectedPoint.getId(), point.getId(),");
		GameCodeProducer.gameCodes
				.add("board.getMoveString(selectedPiece, selectedPoint, point), history.get(history.size() - 1)));");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("public void undo(int undoStep) {");
		GameCodeProducer.gameCodes.add("Node nodeGoingBackTo = history.get(history.size() - undoStep - 1);");
		GameCodeProducer.gameCodes.add("currentSide = nodeGoingBackTo.getState().getSide();");
		GameCodeProducer.gameCodes.add("for (Point point : board.getPoints()) {");
		GameCodeProducer.gameCodes.add("if (nodeGoingBackTo.getState().getPointsState().containsKey(point.getId())) {");
		GameCodeProducer.gameCodes
				.add("point.setPiece(currPieces.get(nodeGoingBackTo.getState().getPointsState().get(point.getId())));");
		GameCodeProducer.gameCodes.add("} else {");
		GameCodeProducer.gameCodes.add("point.setPiece(null);");
		GameCodeProducer.gameCodes.add("}");
		GameCodeProducer.gameCodes.add("}");
		GameCodeProducer.gameCodes.add("for (int i = 0; i < undoStep; i++) {");
		GameCodeProducer.gameCodes.add("history.remove(history.size() - 1);");
		GameCodeProducer.gameCodes.add("}");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("private void handleMenuBarMessage(UIHandler.MenubarMessage msg) {");
		GameCodeProducer.gameCodes.add("switch (msg) {");
		GameCodeProducer.gameCodes.add("case MENUITEM_WINDOW_CLOSING:");
		GameCodeProducer.gameCodes.add("programTerminate();");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("case MENUITEM_NEW_GAME:");
		GameCodeProducer.gameCodes.add("ui.updateStatusBarStatus(\"New game\");");
		GameCodeProducer.gameCodes.add("handleFileEventCallBack().newGame();");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("case MENUITEM_LOAD_GAME:");
		GameCodeProducer.gameCodes.add("ui.updateStatusBarStatus(\"Load game\");");
		GameCodeProducer.gameCodes.add("handleFileEventCallBack().loadGame();");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("case MENUITEM_STEP_REDO:");
		GameCodeProducer.gameCodes.add("ui.updateStatusBarStatus(\"Step redo\");");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("//case MENUITEM_GAME_DISTRIBUTE_COMPUTING:");
		GameCodeProducer.gameCodes.add("//ui.updateStatusBarStatus(\"Distributed computing\");");
		GameCodeProducer.gameCodes.add("//break;");
		GameCodeProducer.gameCodes.add("case MENUITEM_VIEW_SHOW_DEBUG:");
		GameCodeProducer.gameCodes.add("ui.updateStatusBarStatus(\"Show Debug: \" + ui.getIsShowDebug());");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("case MENUITEM_VIEW_SHOW_PIECE_PLACING_POINT:");
		GameCodeProducer.gameCodes
				.add("ui.updateStatusBarStatus(\"Show Piece Placing Point: \" + ui.getIsShowPiecePlacingPoint());");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("//case MENUITEM_VIEW_DETAIL_SYSTEM_INFO:");
		GameCodeProducer.gameCodes.add("//ui.updateStatusBarStatus(\"Detailed system information\");");
		GameCodeProducer.gameCodes.add("//break;");
		GameCodeProducer.gameCodes.add("//case MENUITEM_VIEW_AI_THINKING_STEP:");
		GameCodeProducer.gameCodes.add("//ui.updateStatusBarStatus(\"AI thinking step\");");
		GameCodeProducer.gameCodes.add("//break;");
		GameCodeProducer.gameCodes.add("//case MENUITEM_VIEW_GAME_TREE:");
		GameCodeProducer.gameCodes.add("//ui.updateStatusBarStatus(\"Game tree\");");
		GameCodeProducer.gameCodes.add("//break;");
		GameCodeProducer.gameCodes.add("//case MENUITEM_HELP_ABOUT:");
		GameCodeProducer.gameCodes.add("//ui.updateStatusBarStatus(\"About\");");
		GameCodeProducer.gameCodes.add("//break;");
		GameCodeProducer.gameCodes.add("//case MENUITEM_HELP_TUTORIAL:");
		GameCodeProducer.gameCodes.add("//ui.updateStatusBarStatus(\"Tutorial\");");
		GameCodeProducer.gameCodes.add("//break;");
		GameCodeProducer.gameCodes.add("case MENUITEM_VIEW_FIT_WINDOW:");
		GameCodeProducer.gameCodes.add("ui.fitWindow();");
		GameCodeProducer.gameCodes.add("break;");
		GameCodeProducer.gameCodes.add("}");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("private void programTerminate() {");
		GameCodeProducer.gameCodes.add("System.exit(0);");
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("public void computerMakeMove(Move move)");

		GameCodeProducer.gameCodes.add("private void addDataToInfoPanel() {");
		for (int i = 0; i < playerSidesList.getSize(); i++) {
			GameCodeProducer.gameCodes.add(
					"String " + playerSidesList.getElementAt(i) + " = \"" + playerSidesList.getElementAt(i) + "\";");
			GameCodeProducer.gameCodes.add(
					"ui.infoPanelUpdatePlayerSideData(\"Player " + i + "\", " + playerSidesList.getElementAt(i) + ");");
		}
		for (int i = 0; i < profiles.size(); i++) {
			if (!profiles.get(i).playerSide.equalsIgnoreCase(playerSidesList.getElementAt(0))) continue;
			String buttonName = profiles.get(i).pieceClassName.toLowerCase() + "Button";
			GameCodeProducer.gameCodes.add("JRadioButton " + buttonName + " = new JRadioButton(\""
					+ profiles.get(i).pieceClassName.toLowerCase() + "\");");
			GameCodeProducer.gameCodes.add(buttonName + ".setActionCommand(\""
					+ profiles.get(i).pieceClassName.toLowerCase() + "\");");
			GameCodeProducer.gameCodes.add("ui.infoPanelUpdateSystemInfoData(" + buttonName + ");");
		}
		GameCodeProducer.gameCodes.add("}");

		GameCodeProducer.gameCodes.add("}");
	}
}
