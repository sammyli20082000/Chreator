package Chreator.CodeLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import Chreator.CodeLoader.ChessBoardTabLoader.BoardTabLoader;
import Chreator.CodeLoader.ChessPieceTabLoader.PieceTabLoader;

public class CodeLoader {
	public static String baseDir = "";

	BoardTabLoader boardTabLoader;
	PieceTabLoader pieceTabLoader;

	public CodeLoader(String pathname) {
		if (pathname.equals("") || pathname.equals(null)) {
			JOptionPane.showMessageDialog(null, "Please specify a file path!");
			return;
		} else {
			File file = new File(pathname + "\\src");

			if (!file.isDirectory()) {
				JOptionPane.showMessageDialog(null, "Please browse to the directory containing \"src\" folder.");
				return;
			}

			baseDir = pathname;
		}

		boardTabLoader = new BoardTabLoader();
		pieceTabLoader = new PieceTabLoader();
	}

	public static String stripNonDigits(final CharSequence input) {
		final StringBuilder sBuilder = new StringBuilder(input.length());

		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if ((c > 47 && c < 58) || c == '.') {
				sBuilder.append(c);
			}
		}

		return sBuilder.toString();
	}

	public void loadCodeIntoChreator() throws FileNotFoundException, IOException {
		if (!baseDir.equals("")) {
			boardTabLoader.loadToChreatorBoardTab();
			pieceTabLoader.loadToChessPieceTab();
		}
	}
}
