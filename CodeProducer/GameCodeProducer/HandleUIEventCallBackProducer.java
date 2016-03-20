package Chreator.CodeProducer.GameCodeProducer;

import java.util.ArrayList;

import javax.swing.ListModel;

public class HandleUIEventCallBackProducer {

	ListModel<String> playerSidesList;

	public HandleUIEventCallBackProducer(ListModel<String> playerSidesList) {
		this.playerSidesList = playerSidesList;
	}

	public void printHandleUIEventCallBackCode() {
		int i, j;
		i = j = GameCodeProducer.gameCodes.indexOf("private UIHandler.EventCallBackHandler handleUIEventCallBack()");

		GameCodeProducer.gameCodes.add(++j, "private UIHandler.EventCallBackHandler handleUIEventCallBack() {");
		GameCodeProducer.gameCodes.add(++j, "return new UIHandler.EventCallBackHandler() {");

		GameCodeProducer.gameCodes.add(++j, "public void onMenuBarItemClicked(UIHandler.MenubarMessage msg) {");
		GameCodeProducer.gameCodes.add(++j, "handleMenuBarMessage(msg);");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void onCancelMovement() {");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void onConfirmMovement() {");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void onStartGame(String playerSide) {");
		GameCodeProducer.gameCodes.add(++j, "// define starter side");
		GameCodeProducer.gameCodes.add(++j, "ui.updateStatusBarStatus(\"Start game: \" + playerSide);");
		GameCodeProducer.gameCodes.add(++j, "switch (playerSide) {");
		for (int k = 0; k < playerSidesList.getSize(); k++) {
			GameCodeProducer.gameCodes.add(++j, "case \"" + playerSidesList.getElementAt(k) + "\":");
			GameCodeProducer.gameCodes.add(++j,
					"currentSide = DataAndSetting.PieceData.PlayerSide." + playerSidesList.getElementAt(k) + ";");
			GameCodeProducer.gameCodes.add(++j, "break;");
		}
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "addPointsPiecesEdgesToBoard();");
		GameCodeProducer.gameCodes.add(++j, "ui.setStatusBarButtonsEnabled(true);");
		GameCodeProducer.gameCodes.add(++j, "history = new ArrayList<>();");
		GameCodeProducer.gameCodes.add(++j, "Map<Integer, Integer> state = new HashMap<>();");
		GameCodeProducer.gameCodes.add(++j, "for (Point everyPoint : board.getPoints()) {");
		GameCodeProducer.gameCodes.add(++j, "if (everyPoint.getPiece() != null) {");
		GameCodeProducer.gameCodes.add(++j, "state.put(everyPoint.getId(), everyPoint.getPiece().getId());");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "history.add(new Node(state, currentSide));");
		GameCodeProducer.gameCodes.add(++j, "ui.addMovementHistoryRecord(\"Initial\");");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public Point getSelectedPointOrPiece() {");
		GameCodeProducer.gameCodes.add(++j, "return selectedPoint;");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public ArrayList<Point> getPieceNextMovePointCandidateList() {");
		GameCodeProducer.gameCodes.add(++j, "if (selectedPoint != null && selectedPiece != null)");
		GameCodeProducer.gameCodes.add(++j, "return board.getSelectedPieceMovable();");
		GameCodeProducer.gameCodes.add(++j, "else");
		GameCodeProducer.gameCodes.add(++j, "return null;");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void onUndo(int undoStep) {");
		GameCodeProducer.gameCodes.add(++j, "undo(undoStep);");
		GameCodeProducer.gameCodes.add(++j, "ui.refreshWindow();");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "public void onPointSelected(Point point) {");
		for (String code : printOnPointSelectedCode()) {
			GameCodeProducer.gameCodes.add(++j, code);
		}
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "public void onPieceOnPointSelected(Point point) {");
		for (String code : printOnPieceOnPointSelectedCode()) {
			GameCodeProducer.gameCodes.add(++j, code);
		}
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "};");
		GameCodeProducer.gameCodes.add(++j, "}");
		
		GameCodeProducer.gameCodes.remove(i);
	}
	
	private ArrayList<String> printOnPointSelectedCode() {
		ArrayList<String> codes = new ArrayList<>();
		
		codes.add("if (selectedPiece != null && point != null) {");
		codes.add("if (board.getSelectedPieceMovable().contains(point)) {");
		codes.add("// move");
		codes.add("board.movePiece(selectedPiece, selectedPoint, point);");
		
		codes.add("// change side");
		codes.add("currentSide = sides.get((sides.indexOf(currentSide) + 1) % sides.size());");
		
		codes.add("// update move history and status bar");
		codes.add("updateUIMoveHistoryAndStatusBar(point);");
		
		codes.add("// reset point and piece");
		codes.add("selectedPiece = null;");
		codes.add("selectedPoint = null;");
		codes.add("} else {");
		codes.add("selectedPoint = point;");
		codes.add("}");
		codes.add("} else {");
		codes.add("selectedPoint = point;");
		codes.add("selectedPiece = null;");
		codes.add("}");
		
		return codes;
	}
	
	private ArrayList<String> printOnPieceOnPointSelectedCode() {
		ArrayList<String> codes = new ArrayList<>();
		
		codes.add("if (selectedPiece != null && board.getSelectedPieceMovable().contains(point)) {");
		codes.add("// capture");
		codes.add("board.capture(selectedPiece, selectedPoint, point);");
		
		codes.add("// change side");
		codes.add("currentSide = sides.get((sides.indexOf(currentSide) + 1) % sides.size());");
		
		codes.add("// update move history and status bar");
		codes.add("updateUIMoveHistoryAndStatusBar(point);");

		codes.add("// reset point and piece");
		codes.add("selectedPiece = null;");
		codes.add("selectedPoint = null;");
		codes.add("} else {");
		codes.add("if (point.getPiece().getSide() == currentSide) {");
		codes.add("board.updateSelectedPieceMovable(point, currentSide);");
		codes.add("selectedPiece = point.getPiece();");
		codes.add("selectedPoint = point;");
		codes.add("}");
		codes.add("}");
		
		return codes;
	}
}