package Chreator.UIModule;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextPane;

public class SimpleCodeEditor extends JTextPane{
    private static ArrayList<SimpleCodeEditor> editorList = new ArrayList<SimpleCodeEditor>();
    private static Font sharedFont = new JTextPane().getFont();
    public SimpleCodeEditor(){
        editorList.add(this);
    }

    public static void setFontForAllEditors(String fontName, int fontSize){
        sharedFont = new Font(fontName, Font.PLAIN, fontSize);
        for (SimpleCodeEditor editor : editorList)
            editor.setFont(sharedFont);
    }

    public static Font getFontForAllEditors(){
        return sharedFont;
    }
}