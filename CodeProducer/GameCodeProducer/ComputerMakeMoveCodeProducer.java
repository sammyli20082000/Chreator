package Chreator.CodeProducer.GameCodeProducer;


public class ComputerMakeMoveCodeProducer {
	public ComputerMakeMoveCodeProducer() {
		
	}
	
	public void printComputerMakeMoveCodes() {
		int i, j;
		i = j = GameCodeProducer.gameCodes.indexOf("public void computerMakeMove(Move move)");
		
		GameCodeProducer.gameCodes.add(++j, "public void computerMakeMove(Move move) {");
		
		GameCodeProducer.gameCodes.add(++j, "if (history.size() > 1) {");
		GameCodeProducer.gameCodes.add(++j, "Move causingMove = history.get(history.size() - 1).causingMove;");
		GameCodeProducer.gameCodes.add(++j, "if (move.equals(causingMove)) return;");
		GameCodeProducer.gameCodes.add(++j, "}");
		
		GameCodeProducer.gameCodes.add(++j, "if (move.fromPoint != -1) {");
		GameCodeProducer.gameCodes.add(++j, "handleUIEventCallBack().onPieceOnPointSelected(board.getPointById(move.fromPoint));");
		GameCodeProducer.gameCodes.add(++j, "if (board.getPointById(move.toPoint).getPiece() == null) {");
		GameCodeProducer.gameCodes.add(++j, "handleUIEventCallBack().onPointSelected(board.getPointById(move.toPoint));");
		GameCodeProducer.gameCodes.add(++j, "} else {");
		GameCodeProducer.gameCodes.add(++j, "handleUIEventCallBack().onPieceOnPointSelected(board.getPointById(move.toPoint));");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "} else {");
		GameCodeProducer.gameCodes.add(++j, "Piece newPiece = DataAndSetting.PieceData.makeStandardPiece(move.pieceName, currentSide);");
		GameCodeProducer.gameCodes.add(++j, "selectedPiece = newPiece;");
		GameCodeProducer.gameCodes.add(++j, "currPieces.add(newPiece);");
		GameCodeProducer.gameCodes.add(++j, "selectedPoint = board.getPointById(move.toPoint);");
		GameCodeProducer.gameCodes.add(++j, "selectedPoint.setPiece(selectedPiece);");
		GameCodeProducer.gameCodes.add(++j, "// change side");
		GameCodeProducer.gameCodes.add(++j, "currentSide = sides.get((sides.indexOf(currentSide) + 1) % sides.size());");
		GameCodeProducer.gameCodes.add(++j, "// update move history and status bar");
		GameCodeProducer.gameCodes.add(++j, "updateUIMoveHistoryAndStatusBar(move);");
		GameCodeProducer.gameCodes.add(++j, "// reset point and piece");
		GameCodeProducer.gameCodes.add(++j, "selectedPiece = null;");
		GameCodeProducer.gameCodes.add(++j, "selectedPoint = null;");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "ui.refreshWindow();");
		GameCodeProducer.gameCodes.add(++j, "}");
		
		GameCodeProducer.gameCodes.remove(i);
	}
}
