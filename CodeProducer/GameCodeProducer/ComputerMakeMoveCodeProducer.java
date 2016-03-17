package Chreator.CodeProducer.GameCodeProducer;

public class ComputerMakeMoveCodeProducer {
	public ComputerMakeMoveCodeProducer() {
		
	}
	
	public void printComputerMakeMoveCodes() {
		int i, j;
		i = j = GameCodeProducer.gameCodes.indexOf("public void computerMakeMove(Move move)");
		
		GameCodeProducer.gameCodes.add(++j, "public void computerMakeMove(Move move) {");
		GameCodeProducer.gameCodes.add(++j, "handleUIEventCallBack().onPieceOnPointSelected(board.getPointByID(move.fromPoint));");
		GameCodeProducer.gameCodes.add(++j, "if (board.getPointByID(move.toPoint).getPiece() == null) {");
		GameCodeProducer.gameCodes.add(++j, "handleUIEventCallBack().onPointSelected(board.getPointByID(move.toPoint));");
		GameCodeProducer.gameCodes.add(++j, "} else {");
		GameCodeProducer.gameCodes.add(++j, "handleUIEventCallBack().onPieceOnPointSelected(board.getPointByID(move.toPoint));");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "}");
		
		GameCodeProducer.gameCodes.remove(i);
	}
}
