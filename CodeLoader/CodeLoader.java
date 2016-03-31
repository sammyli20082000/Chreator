package Chreator.CodeLoader;

import javax.swing.JOptionPane;

public class CodeLoader {
	public static String baseDir;

	public CodeLoader(String pathname) {
		if (pathname.equals("") || pathname.equals(null)) {
			JOptionPane.showMessageDialog(null, "Please specify a file path!");
			return;
		}
	}

	public void loadCodeIntoChreator() {

	}
}
