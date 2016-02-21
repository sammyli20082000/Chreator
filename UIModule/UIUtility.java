package Chreator.UIModule;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

abstract public class UIUtility {

    private static String browseDir = "E:\\Download\\XiangQi_material";

    public static File showFileDirectorySelectionDialog(int JFileChooserSelectionMode) {
        File selectedFile = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooserSelectionMode);
        if (browseDir != null) fileChooser.setCurrentDirectory(new File(browseDir));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            browseDir = selectedFile.getParent();
        }
        return selectedFile;
    }

    public static String showVariableInputDialog(String title, String majorMessage, String minorMessage, boolean checkKeyword) {
        String s;
        do {
            s = JOptionPane.showInputDialog(UIHandler.getMainWindow(), "<html><center>" + majorMessage + "<br>" +
                    "Name must only contain English alphabet, arabic numerals, dollar sign ($) or underscore (_).<br>" +
                    "Name cannot start with arabic numerals.<br>" + minorMessage + "</html>", title, JOptionPane.QUESTION_MESSAGE);
            if (s == null) return null;
            if (!s.matches("^[A-Za-z0-9_$]+$") || (s.charAt(0) + "").matches("[0-9]"))
                JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "<html><center>Input error.<br>" +
                        "Name must only contain English alphabet, arabic numerals, dollar sign ($) or underscore (_).<br>" +
                        "Name cannot start with arabic numerals.</html>", "ERROR - " + title, JOptionPane.ERROR_MESSAGE);
            else if (isJavaKeyword(s) && checkKeyword)
                JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "<html><center>Input error.<br>" +
                        "Input name is Java keyword,", "ERROR - " + title, JOptionPane.ERROR_MESSAGE);
            else
                break;
        } while (true);
        return s;
    }

    public static ArrayList<Integer> getPositiveIntegerListFromString(String s) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        try {
            boolean isReadingInt = false;
            int i = 0;
            char c;
            for (int j = 0; j < s.length(); j++) {
                c = s.charAt(j);
                if (c >= '0' && c <= '9') {
                    if (isReadingInt)
                        i = i * 10 - '0' + c;
                    else {
                        isReadingInt = true;
                        i = 0 - '0' + c;
                    }
                } else {
                    if (isReadingInt) {
                        isReadingInt = false;
                        list.add(new Integer(i));
                    }
                }
            }
            if (isReadingInt) list.add(new Integer(i));
        } catch (Exception e) {
            e.printStackTrace();
            list.clear();
        }
        return list;
    }

    public static boolean isJavaKeyword(String word) {
        String[] list = {
                "abstract", "assert", "boolean", "break", "byte",
                "case", "catch", "char", "class", "const",
                "continue", "default", "do", "double", "else",
                "enum", "extends", "final", "finally", "float",
                "for", "goto", "if", "implements", "import",
                "instanceof", "int", "interface", "long", "native",
                "new", "package", "private", "protected", "public",
                "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws",
                "transient", "try", "void", "volatile", "while"
        };
        for (String s : list)
            if (s.equals(word)) return true;
        return false;
    }
}