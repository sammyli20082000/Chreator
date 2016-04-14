package Chreator.UIModule;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import Chreator.UIModule.AbstractModel.SourceCodeCharacteristicScanner;

/**
 * sites mentioned in the slide
 * java oracle official tutorial and coding standard
 * <p/>
 * work to do
 * keyword recognition
 * code recommendation
 * code formatting
 * error detection
 * jtextpane performance fix
 */

public class SimpleCodeEditor extends JTextPane {

    private static class StyleColorSet {
        public String name;
        public Color color;

        StyleColorSet(String name, Color color) {
            this.color = color;
            this.name = name;
        }
    }

    private static ArrayList<SimpleCodeEditor> editorList = new ArrayList<SimpleCodeEditor>();
    private static Font sharedFont = new JTextPane().getFont();
    private static StyleContext sc = new StyleContext();

    private static Color
            COLOR_BACKGROUND = new Color(43, 43, 43),
            COLOR_CARET = new Color(187, 187, 187),
            COLOR_CARET_ROW = new Color(50, 50, 50),
            COLOR_DEFAULT_TEXT = new Color(169, 183, 198),
            COLOR_KEYWORD = new Color(204, 120, 50),
            COLOR_TODO = new Color(168, 192, 35),
            COLOR_ERROR = new Color(188, 63, 60),
            COLOR_STRING = new Color(106, 135, 89),
            COLOR_ESCAPE = new Color(204, 120, 50),
            COLOR_COMMA = new Color(204, 120, 50),
            COLOR_SEMICOLON = new Color(204, 120, 50),
            COLOR_NUMBER = new Color(104, 151, 187),
            COLOR_COMMENT = new Color(128, 128, 128),
            COLOR_JAVADOC = new Color(98, 151, 85),
            COLOR_JAVADOC_TAG = new Color(98, 151, 85),
            COLOR_INSTANCE_FUNCTION_DECLARATION = new Color(255, 198, 109),
            COLOR_INSTANCE_VARIABLE = new Color(152, 118, 170),
            COLOR_STATIC_FUNCTION_DECLARATION = new Color(255, 198, 109),
            COLOR_STATIC_VARIABLE = new Color(152, 118, 170),
            COLOR_ANNOTATION = new Color(187, 181, 41),
            COLOR_TYPE_PARAMETER = new Color(80, 120, 116),
            COLOR_ANONYMOUS_CLASS_PARAMETER = new Color(179, 137, 197);

    private static String
            STYLE_DEFAULT_TEXT = "STYLE_DEFAULT_TEXT",
            STYLE_KEYWORD = "STYLE_KEYWORD",
            STYLE_TODO = "STYLE_TODO",
            STYLE_STRING = "STYLE_STRING",
            STYLE_ESCAPE = "STYLE_ESCAPE",
            STYLE_NUMBER = "STYLE_NUMBER",
            STYLE_COMMA = "STYLE_COMMA",
            STYLE_SEMICOLON = "STYLE_SEMICOLON",
            STYLE_COMMENT = "STYLE_COMMENT",
            STYLE_JAVADOC = "STYLE_JAVADOC",
            STYLE_JAVADOC_TAG = "STYLE_JAVADOC_TAG",
            STYLE_INSTANCE_FUNCTION_DECLARATION = "STYLE_INSTANCE_FUNCTION_DECLARATION",
            STYLE_INSTANCE_VARIABLE = "STYLE_INSTANCE_VARIABLE",
            STYLE_STATIC_FUNCTION_DECLARATION = "STYLE_STATIC_FUNCTION_DECLARATION",
            STYLE_STATIC_VARIABLE = "STYLE_STATIC_VARIABLE",
            STYLE_ANNOTATION = "STYLE_ANNOTATION",
            STYLE_TYPE_PARAMETER = "STYLE_TYPE_PARAMETER",
            STYLE_ANONYMOUS_CLASS_PARAMETER = "STYLE_ANONYMOUS_CLASS_PARAMETER",
            STYLE_STATIC_FUNCTION_CALL = "STYLE_STATIC_FUNCTION_CALL";

    public static void setFontForAllEditors(String fontName, int fontSize) {
        sharedFont = new Font(fontName, Font.PLAIN, fontSize);
        for (SimpleCodeEditor editor : editorList)
            editor.setFont(sharedFont);
    }

    public static Font getFontForAllEditors() {
        return sharedFont;
    }

    /**
     * [ClassModifier...] class Identifier [TypeParameters...] [Super] [Interfaces...] ClassBody
     * [modifiers...] returnType methodName (arguments...) [throws...] methodBody(){} methodSignature();
     * [modifiers] Type identifiers, ... [] =
     * [modifiers] enum name {static identifiers... methods...}
     * [modifiers] interface Identifier {static final variables... methodsSignatures();... }
     *
     * @interface {} // being the same as the normal interface
     * for
     * if else
     * do while
     * switch case default
     * try catch finally
     */
    private CodeStyleApplier codeStyleApplier;

    public SimpleCodeEditor() {
        codeStyleApplier = new CodeStyleApplier(this);
        editorList.add(this);

        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                super.componentShown(e);
                codeStyleApplier.reanalyze();
                removeComponentListener(this);
            }
        });

        setBackground(COLOR_BACKGROUND);
        setCaretColor(COLOR_CARET);
        setStyledDocument(new DefaultStyledDocument(sc));
        ((AbstractDocument) getStyledDocument()).setDocumentFilter(createDocumentFilter());

        addCaretListener(createCaretListener());

        try {
            getHighlighter().addHighlight(0, 0, createCaretRowHighLighter());
        } catch (Exception e) {
        }

        setupCodeStyles();

        /*Class c = SourceCodeCharacteristicScanner.class;
        String s = "";
        do {
            printClass(c, s);
            s += "\t";
            c = c.getSuperclass();
        } while (c != null);*/
    }

    private Highlighter.HighlightPainter createCaretRowHighLighter() {
        return new Highlighter.HighlightPainter() {
            @Override
            public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
                paintCaretRow(g, c);
            }
        };
    }

    private void printClass(Class c, String tab) {
        System.out.println(tab + "Class: " + c);
        for (Field f : c.getDeclaredFields())
            System.out.println(tab + "\tField: " + f);
        for (Method m : c.getDeclaredMethods())
            printMethod(m, tab + "\t");
        for (Class c2 : c.getDeclaredClasses())
            printClass(c2, tab + "\t");
    }

    private void printMethod(Method m, String tab) {
        System.out.println(tab + "Method: " + m);
        for (Annotation a : m.getDeclaredAnnotations())
            System.out.println(tab + "\tAnnotation: " + a);
    }

    private CaretListener createCaretListener() {
        return new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                repaint();
            }
        };
    }

    private DocumentFilter createDocumentFilter() {
        return new DocumentFilter() {
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
                codeChangedPostTask();
            }

            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                super.insertString(fb, offset, string, attr);
                codeChangedPostTask();
            }

            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                super.replace(fb, offset, length, text, attrs);
                codeChangedPostTask();
            }
        };
    }

    private void codeChangedPostTask() {
        codeStyleApplier.reanalyze();
        compileProjectForMoreInformation();
    }

    private void compileProjectForMoreInformation() {

    }

    private void setupCodeStyles() {
        StyleColorSet[] styleColorSets = {
                new StyleColorSet(STYLE_DEFAULT_TEXT, COLOR_DEFAULT_TEXT),
                new StyleColorSet(STYLE_KEYWORD, COLOR_KEYWORD),
                new StyleColorSet(STYLE_TODO, COLOR_TODO),
                new StyleColorSet(STYLE_STRING, COLOR_STRING),
                new StyleColorSet(STYLE_ESCAPE, COLOR_ESCAPE),
                new StyleColorSet(STYLE_NUMBER, COLOR_NUMBER),
                new StyleColorSet(STYLE_COMMA, COLOR_COMMA),
                new StyleColorSet(STYLE_SEMICOLON, COLOR_SEMICOLON),
                new StyleColorSet(STYLE_COMMENT, COLOR_COMMENT),
                new StyleColorSet(STYLE_JAVADOC, COLOR_JAVADOC),
                new StyleColorSet(STYLE_JAVADOC_TAG, COLOR_JAVADOC_TAG),
                new StyleColorSet(STYLE_INSTANCE_FUNCTION_DECLARATION, COLOR_INSTANCE_FUNCTION_DECLARATION),
                new StyleColorSet(STYLE_INSTANCE_VARIABLE, COLOR_INSTANCE_VARIABLE),
                new StyleColorSet(STYLE_STATIC_FUNCTION_DECLARATION, COLOR_STATIC_FUNCTION_DECLARATION),
                new StyleColorSet(STYLE_STATIC_VARIABLE, COLOR_STATIC_VARIABLE),
                new StyleColorSet(STYLE_ANNOTATION, COLOR_ANNOTATION),
                new StyleColorSet(STYLE_TYPE_PARAMETER, COLOR_TYPE_PARAMETER),
                new StyleColorSet(STYLE_ANONYMOUS_CLASS_PARAMETER, COLOR_ANONYMOUS_CLASS_PARAMETER),
                new StyleColorSet(STYLE_STATIC_FUNCTION_CALL, COLOR_DEFAULT_TEXT)
        };

        Style style;
        for (StyleColorSet styleColorSet : styleColorSets) {
            style = sc.addStyle(styleColorSet.name, null);
            StyleConstants.setForeground(style, styleColorSet.color);
        }

        style = sc.getStyle(STYLE_TODO);
        StyleConstants.setItalic(style, true);

        style = sc.getStyle(STYLE_JAVADOC_TAG);
        StyleConstants.setItalic(style, true);
        StyleConstants.setBold(style, true);
        StyleConstants.setUnderline(style, true);

        style = sc.getStyle(STYLE_STATIC_FUNCTION_DECLARATION);
        StyleConstants.setItalic(style, true);

        style = sc.getStyle(STYLE_STATIC_VARIABLE);
        StyleConstants.setItalic(style, true);

        style = sc.getStyle(STYLE_STATIC_FUNCTION_CALL);
        StyleConstants.setItalic(style, true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //paintErrorWavedLine(g);
    }

    private void paintErrorWavedLine(Graphics g) {
        int start = 0, end = getCaretPosition();
        try {
            g.setColor(COLOR_ERROR);
            Rectangle rS = modelToView(start), rL, rC = rS;
            for (int counter = start; counter <= end; counter++) {
                rL = rC;
                rC = modelToView(counter);
                if (rC.y != rS.y) {
                    for (int i = 0; i < rL.x - rS.x; i++)
                        g.fillRect(rS.x + i, rS.y + rS.height - i % 4 + (i % 4) / 3 * 2, 1, 1);
                    rS = rC;
                } else if (counter == end) {
                    for (int i = 0; i < rC.x - rS.x; i++)
                        g.fillRect(rS.x + i, rS.y + rS.height - i % 4 + (i % 4) / 3 * 2, 1, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paintCaretRow(Graphics g, JTextComponent c) {
        try {
            int caretPosition = c.getCaretPosition();
            Rectangle r = c.modelToView(caretPosition);
            g.setColor(COLOR_CARET_ROW);
            g.fillRect(0, r.y, c.getWidth(), r.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class CodeStyleApplier {
        private static class CodeStyleIndexPack {
            public int index;
            public String style;

            CodeStyleIndexPack(int index, String style) {
                this.index = index;
                this.style = style;
            }
        }

        private enum CodeMode {STRING, BLOCK_COMMENT, LINE_COMMENT, JAVADOC, CHAR, NORMAL}

        private CodeMode codeMode;
        private SourceCodeCharacteristicScanner sourceCodeCharacteristicScanner;
        private JTextPane jTextPane;
        private int escapeEndPosition;
        private char[][] javaIdentifierFilterSet,
                lineTerminator = UIUtility.stringArrayToChar2DArray(UIUtility.JavaLineTerminator);
        private char[] fullCode;
        private long lastCall = 0;
        private int delay = 250;
        private Thread applyThread;
        private ArrayList<CodeStyleIndexPack> codeStyleIndexPacks;

        public CodeStyleApplier(JTextPane jTextPane) {
            this.jTextPane = jTextPane;
            sourceCodeCharacteristicScanner = new SourceCodeCharacteristicScanner();
            javaIdentifierFilterSet = UIUtility.stringArrayToChar2DArray(UIUtility.sortArrayByInverseLength(UIUtility.DefaultJavaCodeFilter));
        }

        public void reanalyze() {
            lastCall = System.nanoTime();
            if (applyThread == null || !applyThread.isAlive()) {
                applyThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            do {
                                do {
                                    Thread.sleep(delay);
                                } while (System.nanoTime() - lastCall < delay * 1000 * 1000);
                                runReanalyze();
                            } while (System.nanoTime() - lastCall < delay * 1000 * 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                applyThread.start();
            }
        }

        private void runReanalyze() throws Exception {
            fullCode = jTextPane.getStyledDocument().getText(0, jTextPane.getStyledDocument().getLength()).toCharArray();
            sourceCodeCharacteristicScanner.scanFullCodeForCharacteristic(fullCode);

            codeMode = CodeMode.NORMAL;
            escapeEndPosition = 0;
            codeStyleIndexPacks = new ArrayList<CodeStyleIndexPack>();
            int index = 0, matchedLength;
            while (index < fullCode.length) {
                if (System.nanoTime() - lastCall < delay * 1000 * 1000) return;
                matchedLength = UIUtility.lengthStringToNextBreakPoint(fullCode, index, javaIdentifierFilterSet);

                switch (codeMode) {
                    case STRING:
                        stringCodeModeHandler(fullCode, index, matchedLength);
                        break;
                    case CHAR:
                        charCodeModeHandler(fullCode, index, matchedLength);
                        break;
                    case LINE_COMMENT:
                        lineCommentCodeModeHandler(fullCode, index, matchedLength);
                        break;
                    case BLOCK_COMMENT:
                        blockCommentCodeModeHandler(fullCode, index, matchedLength);
                        break;
                    case JAVADOC:
                        javaDocCodeModeHandler(fullCode, index, matchedLength);
                        break;
                    case NORMAL:
                        normalCodeModeHandler(fullCode, index, matchedLength);
                }
                index += matchedLength > 0 ? matchedLength : 1;
            }
            applyCodeStyle();
        }

        char[][] commentEndToken = UIUtility.stringArrayToChar2DArray(UIUtility.JavaCommentEndIndicators),
                todoEndModeToken = UIUtility.stringArrayToChar2DArray(UIUtility.JavaTodoEndIndicator);

        int todoModeEndIndex;
        boolean isClearDocNewLine = true, exceptionHappened = false;

        private void javaDocCodeModeHandler(char[] code, int index, int length) {
            int todoModeStartIndex = todoModeStartIndex(code, index, length);
            if (todoModeStartIndex > 0) {
                todoModeEndIndex = UIUtility.lengthStringToNextBreakPoint(code, index, todoEndModeToken) + todoModeStartIndex;
                registerStyleBreakPoint(todoModeStartIndex, STYLE_TODO);
                registerStyleBreakPoint(todoModeEndIndex, STYLE_JAVADOC);
            }
            if (code[index] == '@' && index >= todoModeEndIndex && isClearDocNewLine &&
                    UIUtility.lengthStringMatchedKeywords(code, index + 1, javaIdentifierFilterSet) == 0) {
                int paramLengthToEnd = UIUtility.lengthStringToNextBreakPoint(code, index + 1, javaIdentifierFilterSet);
                registerStyleBreakPoint(index, STYLE_JAVADOC_TAG);
                registerStyleBreakPoint(index + paramLengthToEnd + 1, STYLE_JAVADOC);
            } else if (UIUtility.lengthStringMatchedKeywords(code, index, commentEndToken) > 0) {
                codeMode = CodeMode.NORMAL;
                registerStyleBreakPoint(index + length, STYLE_DEFAULT_TEXT);
            }

            if (UIUtility.lengthStringMatchedKeywords(code, index, lineTerminator) > 0) {
                isClearDocNewLine = true;
                exceptionHappened = false;
            } else if (UIUtility.lengthStringMatchedKeywords(code, index, UIUtility.JavaWhiteSpace) > 0)
                ;
            else if (code[index] == '*' && !exceptionHappened)
                exceptionHappened = true;
            else
                isClearDocNewLine = false;
        }

        private void blockCommentCodeModeHandler(char[] code, int index, int length) {
            int todoModeStartIndex = todoModeStartIndex(code, index, length);
            if (todoModeStartIndex > 0) {
                int lengthToEnd = UIUtility.lengthStringToNextBreakPoint(code, index, todoEndModeToken);
                registerStyleBreakPoint(index, STYLE_TODO);
                registerStyleBreakPoint(index + lengthToEnd, STYLE_COMMENT);
            }
            if (UIUtility.lengthStringMatchedKeywords(code, index, commentEndToken) > 0) {
                codeMode = CodeMode.NORMAL;
                registerStyleBreakPoint(index + length, STYLE_DEFAULT_TEXT);
            }
        }

        private void lineCommentCodeModeHandler(char[] code, int index, int length) {
            int todoModeStartIndex = todoModeStartIndex(code, index, length);
            if (todoModeStartIndex >= 0) {
                registerStyleBreakPoint(todoModeStartIndex, STYLE_TODO);
            }
            if (UIUtility.lengthStringMatchedKeywords(code, index, lineTerminator) > 0) {
                codeMode = CodeMode.NORMAL;
                registerStyleBreakPoint(index, STYLE_DEFAULT_TEXT);
            }
        }

        private void charCodeModeHandler(char[] code, int index, int length) {
            if (code[index] == '\\') {
                escapeEndPosition = index + UIUtility.validJavaEscapeLength(code, index);
                registerStyleBreakPoint(index, STYLE_ESCAPE);
                registerStyleBreakPoint(escapeEndPosition, STYLE_STRING);
            } else if ((code[index] == '\'' && index >= escapeEndPosition) || (UIUtility.lengthStringMatchedKeywords(code, index, lineTerminator) > 0)) {
                codeMode = CodeMode.NORMAL;
                registerStyleBreakPoint(index + length, STYLE_DEFAULT_TEXT);
            }
        }

        private void stringCodeModeHandler(char[] code, int index, int length) {
            if (code[index] == '\\') {
                escapeEndPosition = index + UIUtility.validJavaEscapeLength(code, index);
                registerStyleBreakPoint(index, STYLE_ESCAPE);
                registerStyleBreakPoint(escapeEndPosition, STYLE_STRING);
            } else if ((code[index] == '"' && index >= escapeEndPosition) || (UIUtility.lengthStringMatchedKeywords(code, index, lineTerminator) > 0)) {
                codeMode = CodeMode.NORMAL;
                registerStyleBreakPoint(index + length, STYLE_DEFAULT_TEXT);
            }
        }

        private char[] lineCommentIndicator = UIUtility.JavaLineCommentIndicator.toCharArray(),
                javaDocStartingIndicator = UIUtility.JavaDocStartIndicator.toCharArray(),
                blockCommentStartingIndicator = UIUtility.JavaBlockCommentIndicator.toCharArray(),
                emptyBlockCommentIndicator = UIUtility.JavaEmptyBlockComment.toCharArray(),
                emptyJavaDocIndicator = UIUtility.JavaEmptyJavaDoc.toCharArray();
        private char[][] dot = {{'.'}};

        private void normalCodeModeHandler(char[] code, int index, int length) {
            if (UIUtility.isCharArrayStartWith(code, index, emptyJavaDocIndicator)) {
                registerStyleBreakPoint(index, STYLE_JAVADOC);
                registerStyleBreakPoint(index + emptyJavaDocIndicator.length, STYLE_DEFAULT_TEXT);
            } else if (UIUtility.isCharArrayStartWith(code, index, emptyBlockCommentIndicator)) {
                registerStyleBreakPoint(index, STYLE_COMMENT);
                registerStyleBreakPoint(index + emptyBlockCommentIndicator.length, STYLE_DEFAULT_TEXT);
            } else if (UIUtility.isCharArrayStartWith(code, index, lineCommentIndicator)) {
                codeMode = CodeMode.LINE_COMMENT;
                registerStyleBreakPoint(index, STYLE_COMMENT);
            } else if (UIUtility.isCharArrayStartWith(code, index, javaDocStartingIndicator)) {
                codeMode = CodeMode.JAVADOC;
                todoModeEndIndex = 0;
                exceptionHappened = true;
                isClearDocNewLine = true;
                registerStyleBreakPoint(index, STYLE_JAVADOC);
            } else if (UIUtility.isCharArrayStartWith(code, index, blockCommentStartingIndicator)) {
                codeMode = CodeMode.BLOCK_COMMENT;
                registerStyleBreakPoint(index, STYLE_COMMENT);
            } else if (code[index] == '"') {
                codeMode = CodeMode.STRING;
                registerStyleBreakPoint(index, STYLE_STRING);
            } else if (code[index] == '\'') {
                codeMode = CodeMode.CHAR;
                registerStyleBreakPoint(index, STYLE_STRING);
            } else {
                scanNormalRecursively(code, index, index + length);
            }
        }

        char[][] javaReservedWords = UIUtility.stringArrayToChar2DArray(UIUtility.JavaKeywordSet);

        private void scanNormalRecursively(char[] code, int currentPosition, int fixedEndPosition) {
            int matchedLength = UIUtility.positiveSuspiciousNumberLength(code, currentPosition);
            if (matchedLength > 0) {
                registerStyleBreakPoint(currentPosition, STYLE_NUMBER);
                registerStyleBreakPoint(currentPosition + matchedLength, STYLE_DEFAULT_TEXT);
            } else {
                matchedLength = UIUtility.lengthStringToNextBreakPoint(code, currentPosition, dot);
                matchedLength = currentPosition + matchedLength > fixedEndPosition ? fixedEndPosition - currentPosition : matchedLength;
                boolean needChangeToDefault = true;

                if (UIUtility.lengthStringMatchedKeywords(code, currentPosition, javaReservedWords) == matchedLength)
                    registerStyleBreakPoint(currentPosition, STYLE_KEYWORD);
                else if (code[currentPosition] == ';')
                    registerStyleBreakPoint(currentPosition, STYLE_SEMICOLON);
                else if (code[currentPosition] == ',')
                    registerStyleBreakPoint(currentPosition, STYLE_COMMA);
                else if (currentPosition > 0 && code[currentPosition - 1] == '@' && currentPosition + matchedLength >= fixedEndPosition)
                    registerStyleBreakPoint(currentPosition - 1, STYLE_ANNOTATION);
                else
                    needChangeToDefault = false;

                if (needChangeToDefault)
                    registerStyleBreakPoint(currentPosition + matchedLength, STYLE_DEFAULT_TEXT);
            }

            if (currentPosition + matchedLength < fixedEndPosition)
                scanNormalRecursively(code, currentPosition + matchedLength, fixedEndPosition);
        }

        private void registerStyleBreakPoint(int index, String style) {
            codeStyleIndexPacks.add(new CodeStyleIndexPack(index, style));
        }

        public void applyCodeStyle() {
            for (int i = 0; i < codeStyleIndexPacks.size(); i++) {
                CodeStyleIndexPack curr = codeStyleIndexPacks.get(i), next;
                if (System.nanoTime() - lastCall < delay * 1000 * 1000) return;

                if (i == codeStyleIndexPacks.size() - 1) {
                    if (curr.index < fullCode.length)
                        jTextPane.getStyledDocument().setCharacterAttributes(curr.index, fullCode.length - curr.index, sc.getStyle(curr.style), true);
                } else {
                    next = codeStyleIndexPacks.get(i + 1);
                    jTextPane.getStyledDocument().setCharacterAttributes(curr.index, next.index - curr.index, sc.getStyle(curr.style), true);
                }
            }
        }

        private int todoModeStartIndex(char[] code, int index, int lineLength) {
            if (index > 0) {
                for (int shift = 0; shift + 4 <= lineLength; shift++) {
                    char c = code[shift + index - 1];
                    if (c == '_' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                        ;
                    else if ((code[index + shift] == 't' || code[index + shift] == 'T') &&
                            (code[index + shift + 1] == 'o' || code[index + shift + 1] == 'O') &&
                            (code[index + shift + 2] == 'd' || code[index + shift + 2] == 'D') &&
                            (code[index + shift + 3] == 'o' || code[index + shift + 3] == 'O')) {
                        if (shift + 4 == lineLength) return index + shift;
                        else {
                            c = code[shift + index + 4];
                            if (c == '_' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                                ;
                            else return index + shift;
                        }
                    }
                }
            }
            return -1;
        }
    }
}