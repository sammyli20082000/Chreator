package Chreator.CodeProducer.GameCodeProducer;

public class HandleFileEventCallBackProducer {
	public HandleFileEventCallBackProducer() {
		
	}
	
	public void printHandleFileEventCallBackCode() {
		int i, j;
		i = j = GameCodeProducer.gameCodes.indexOf("private FileHandler.EventCallBackHandler handleFileEventCallBack()");
		
		GameCodeProducer.gameCodes.add(++j, "private FileHandler.EventCallBackHandler handleFileEventCallBack() {");
		GameCodeProducer.gameCodes.add(++j, "return new FileHandler.EventCallBackHandler() {");
		
		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void newGame() {");
		GameCodeProducer.gameCodes.add(++j, "board = new Board();");
		GameCodeProducer.gameCodes.add(++j, "currentSide = \"\";");
		GameCodeProducer.gameCodes.add(++j, "currPieces = null;");
		GameCodeProducer.gameCodes.add(++j, "ai = new AI(game);");
		GameCodeProducer.gameCodes.add(++j, "ui.setBoard(board);");
		GameCodeProducer.gameCodes.add(++j, "ui.enableStartGameButton();");
		GameCodeProducer.gameCodes.add(++j, "history = null;");
		GameCodeProducer.gameCodes.add(++j, "ui.restoreMovementHistoryList();");
		GameCodeProducer.gameCodes.add(++j, "selectedPiece = null;");
		GameCodeProducer.gameCodes.add(++j, "selectedPoint = null;");
		GameCodeProducer.gameCodes.add(++j, "ui.refreshWindow();");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void saveGame() {");
		GameCodeProducer.gameCodes.add(++j, "FileOutputStream fout = null;");
		GameCodeProducer.gameCodes.add(++j, "ObjectOutputStream oos = null;");
		GameCodeProducer.gameCodes.add(++j, "try {");
		GameCodeProducer.gameCodes.add(++j, "fout = new FileOutputStream(\"save.chesshis\");");
		GameCodeProducer.gameCodes.add(++j, "oos = new ObjectOutputStream(fout);");
		GameCodeProducer.gameCodes.add(++j, "oos.writeObject(history);");
		GameCodeProducer.gameCodes.add(++j, "} catch (Exception e) {");
		GameCodeProducer.gameCodes.add(++j, "// e.printStackTrace();");
		GameCodeProducer.gameCodes.add(++j, "JOptionPane.showMessageDialog(null, \"Game cannot be saved.\", \"Saving Error\", JOptionPane.YES_OPTION);");
		GameCodeProducer.gameCodes.add(++j, "} finally {");
		GameCodeProducer.gameCodes.add(++j, "try {");
		GameCodeProducer.gameCodes.add(++j, "fout.close();");
		GameCodeProducer.gameCodes.add(++j, "oos.close();");
		GameCodeProducer.gameCodes.add(++j, "} catch (IOException e) {}");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "@Override");
		GameCodeProducer.gameCodes.add(++j, "public void loadGame() {");
		GameCodeProducer.gameCodes.add(++j, "FileInputStream fin = null;");
		GameCodeProducer.gameCodes.add(++j, "ObjectInputStream ois = null;");
		GameCodeProducer.gameCodes.add(++j, "try {");
		GameCodeProducer.gameCodes.add(++j, "fin = new FileInputStream(\"save.chesshis\");");
		GameCodeProducer.gameCodes.add(++j, "ois = new ObjectInputStream(fin);");
		GameCodeProducer.gameCodes.add(++j, "ArrayList<Node> oldHistory = (ArrayList<Node>) ois.readObject();");
		GameCodeProducer.gameCodes.add(++j, "newGame();");
		GameCodeProducer.gameCodes.add(++j, "addPointsPiecesEdgesToBoard();");
		GameCodeProducer.gameCodes.add(++j, "ui.setStatusBarButtonsEnabled(true);");
		GameCodeProducer.gameCodes.add(++j, "currentSide = oldHistory.get(0).getState().getSide();");
		GameCodeProducer.gameCodes.add(++j, "history = new ArrayList<>();");
		GameCodeProducer.gameCodes.add(++j, "Map<Integer, Integer> state = new HashMap<>();");
		GameCodeProducer.gameCodes.add(++j, "for (Point everyPoint : board.getPoints()) {");
		GameCodeProducer.gameCodes.add(++j, "if (everyPoint.getPiece() != null) {");
		GameCodeProducer.gameCodes.add(++j, "state.put(everyPoint.getId(), everyPoint.getPiece().getId());");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "history.add(new Node(state, currentSide));");
		GameCodeProducer.gameCodes.add(++j, "ui.addMovementHistoryRecord(\"Initial\");");
		GameCodeProducer.gameCodes.add(++j, "for (int i = 1; i < oldHistory.size(); i++) {");
		GameCodeProducer.gameCodes.add(++j, "computerMakeMove(Move.transformingMove(oldHistory.get(i).movedFromPointId, oldHistory.get(i).movedToPointId));");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "ui.refreshWindow();");
		GameCodeProducer.gameCodes.add(++j, "} catch (Exception e) {");
		GameCodeProducer.gameCodes.add(++j, "// e.printStackTrace();");
		GameCodeProducer.gameCodes.add(++j, "JOptionPane.showMessageDialog(null, \"Game cannot be loaded.\", \"Loading Error\", JOptionPane.YES_OPTION);");
		GameCodeProducer.gameCodes.add(++j, "} finally {");
		GameCodeProducer.gameCodes.add(++j, "try {");
		GameCodeProducer.gameCodes.add(++j, "fin.close();");
		GameCodeProducer.gameCodes.add(++j, "ois.close();");
		GameCodeProducer.gameCodes.add(++j, "} catch (Exception e) {}");
		GameCodeProducer.gameCodes.add(++j, "}");
		GameCodeProducer.gameCodes.add(++j, "}");

		GameCodeProducer.gameCodes.add(++j, "};");
		GameCodeProducer.gameCodes.add(++j, "}");
		
		GameCodeProducer.gameCodes.remove(i);
	}
}
