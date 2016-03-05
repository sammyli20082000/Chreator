package Chreator.UIModule;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextPane;

public class SimpleCodeEditor extends JTextPane{
    private static ArrayList<SimpleCodeEditor> editorList = new ArrayList<SimpleCodeEditor>();
    public SimpleCodeEditor(){
        editorList.add(this);
    }

    public static void setFontForAllEditors(String fontName, int fontSize){
        for (SimpleCodeEditor editor : editorList)
            editor.setFont(new Font(fontName, Font.PLAIN, fontSize));
    }
}