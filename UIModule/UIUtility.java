package Chreator.UIModule;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public abstract class UIUtility {
    private static String browseDir = "E:\\Download";

    public static String JavaLineCommentIndicator = "//";
    public static String JavaDocStartIndicator = "/**";
    public static String JavaBlockCommentIndicator = "/*";
    public static String JavaEmptyBlockComment = "/**/";
    public static String JavaEmptyJavaDoc = "/***/";
    public static String[] JavaCommentEndIndicators = sortArrayByInverseLength(new String[]{
            "*/", "/*/", JavaEmptyBlockComment, JavaEmptyJavaDoc
    });
    public static String[] JavaModifierSet = sortArrayByInverseLength(new String[]{
            "abstract", "private", "protected", "public", "static", "final",
            "transient", "synchronized", "native", "strictfp", "volatile"
    });
    public static String[] JavaClassIndicatorSet = sortArrayByInverseLength(new String[]{
            "class", "interface", "enum"
    });
    public static String[] JavaLiteralSet = sortArrayByInverseLength(new String[]{
            "true", "false", "null"
    });
    public static String[] JavaPrimitiveTypeSet = sortArrayByInverseLength(new String[]{
            "void", "boolean", "byte", "short", "int", "long", "char", "float", "double"
    });
    public static String[] JavaFlowControlBlockOptional = sortArrayByInverseLength(new String[]{
            "do", "while", "if", "else", "for"
    });
    public static String[] JavaFlowControlBlockMust = sortArrayByInverseLength(new String[]{
            "try", "catch", "finally", "switch", "case", "default"
    });
    public static String[] JavaKeywordSet = sortArrayByInverseLength(combineStringArrays(
            JavaModifierSet,
            JavaClassIndicatorSet,
            JavaLiteralSet,
            JavaPrimitiveTypeSet,
            JavaFlowControlBlockOptional,
            JavaFlowControlBlockMust,
            new String[]{
                    "assert", "break", "const", "continue", "extends",
                    "goto", "implements", "import", "instanceof", "new",
                    "package", "return", "super", "this", "throw", "throws"
            }));
    public static String[] JavaLineTerminator = sortArrayByInverseLength(new String[]{
            "\n", "\r", "\r\n"
    });
    public static String[] JavaWhiteSpace = sortArrayByInverseLength(new String[]{
            " ", "\t", "\014", "\n", "\r"
    });
    public static String[] JavaMinorSeparatorSet = sortArrayByInverseLength(new String[]{
            "{", "}", "[", "]", "(", ")", ";", ","
    });
    // '.' should also belong to separator, here is omitted for code analysis
    public static String[] JavaSingleCharacterEscape = sortArrayByInverseLength(new String[]{
            "b", "t", "n", "f", "r", "\"", "\'", "\\"
    });
    public static String[] JavaTodoEndIndicator = sortArrayByInverseLength(combineStringArrays(
            JavaCommentEndIndicators, JavaLineTerminator
    ));
    public static String[] JavaOperatorsSet = sortArrayByInverseLength(new String[]{
            "=", ">", "<", "!", "~", "?", ":",
            "==", "<=", ">=", "!=", "&&", "||", "++", "--",
            "+", "-", "*", "/", "&", "|", "^", "%", "<<", ">>", ">>>",
            "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=", ">>=", ">>>="
    });
    public static String[] DefaultJavaCodeFilter = sortArrayByInverseLength(UIUtility.combineStringArrays(
            JavaLineTerminator,
            JavaWhiteSpace,
            JavaMinorSeparatorSet,
            JavaOperatorsSet,
            JavaCommentEndIndicators,
            new String[]{
                    JavaLineCommentIndicator, JavaBlockCommentIndicator, JavaDocStartIndicator, // comment and java doc start indicator
                    "\"", "'", "\\", "\\\\", // string and char indicator, words for escape filter
                    "`", "#", // unused ASCII char
                    "@" //annotation and param
            }
    ));

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
            else if ((isStringArrayContains(s, JavaKeywordSet) || isStringArrayContains(s, JavaLiteralSet)) && checkKeyword)
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

    public static String[] combineStringArrays(String[]... arrays) {
        int size = 0, index = 0;
        for (String[] array : arrays)
            size += array.length;
        String[] combined = new String[size];

        for (String[] array : arrays)
            for (String s : array) {
                combined[index] = s;
                index++;
            }

        return combined;
    }

    public static String[] sortArrayByInverseLength(String[] array) {
        return sortArrayByInverseLength(array, 0, array.length);
    }

    public static String[] sortArrayByInverseLength(String[] array, int index, int length) {
        if (length <= 1) return array;
        int indexA = index, indexB = index + length / 2;
        sortArrayByInverseLength(array, indexA, length / 2);
        sortArrayByInverseLength(array, indexB, length - length / 2);
        String[] temp = new String[length];
        while (indexA < index + length / 2 || indexB < index + length) {
            if (indexA >= index + length / 2) {
                temp[indexA - index + indexB - index - length / 2] = array[indexB];
                indexB++;
            } else if (indexB >= index + length) {
                temp[indexA - index + indexB - index - length / 2] = array[indexA];
                indexA++;
            } else if (array[indexA].length() > array[indexB].length()) {
                temp[indexA - index + indexB - index - length / 2] = array[indexA];
                indexA++;
            } else {
                temp[indexA - index + indexB - index - length / 2] = array[indexB];
                indexB++;
            }
        }
        for (int i = 0; i < length; i++)
            array[index + i] = temp[i];
        return array;
    }

    public static char[][] stringArrayToChar2DArray(String[] array) {
        char[][] cArray = new char[array.length][];
        for (int i = 0; i < array.length; i++)
            cArray[i] = array[i].toCharArray();
        return cArray;
    }

    public static boolean isStringArrayContains(String target, String[] input) {
        for (String s : input)
            if (target.equals(s)) return true;
        return false;
    }

    public static boolean isCharArrayStartWith(char[] fullString, int targetIndex, char[] searchKey) {
        if (targetIndex + searchKey.length <= fullString.length) {
            for (int i = 0; i < searchKey.length; i++)
                if (fullString[targetIndex + i] != searchKey[i]) return false;
            return true;
        } else return false;
    }

    public static int validJavaEscapeLength(char[] fullString, int startIndex) {
        if (fullString == null || fullString.length == 0 || fullString[startIndex] != '\\')
            return 0;

        if (startIndex + 6 <= fullString.length && fullString[startIndex + 1] == 'u') {
            boolean isValid = true;
            for (int i = 2; i < 6 && isValid; i++) {
                char c = fullString[startIndex + i];
                if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F'))
                    isValid = false;
            }
            if (isValid) return 6;
        }

        int length;
        for (length = 1; length < 4 && startIndex + length < fullString.length; length++) {
            char c = fullString[startIndex + length];
            if (c < '0' || c > '7')
                break;
        }
        if (length > 1) return length;

        for (String escape : JavaSingleCharacterEscape) {
            if (isCharArrayStartWith(fullString, startIndex + 1, escape.toCharArray()))
                return escape.length() + 1;
        }

        return 0;
    }

    private static char[] binPrefix = {'0', 'b'}, BinPrefix = {'0', 'B'}, hexPrefix = {'0', 'x'}, HexPrefix = {'0', 'X'};

    public static int positiveSuspiciousNumberLength(char[] fullString, int index) {
        /**
         * [0~9.][0~9_.](lLdDfF)
         * [0~9.][0~9_.](eE)(+-)[0~9._](dDfF)
         * [0x 0X][0~9A~Fa~f_.](lLdDfF)
         * [0x 0X][0~9A~Fa~f_.](pP)(+-)[0~9._](dDfF)
         * [0b 0B][0~9_.](lL)
         * . must be between 0~9, except the first one
         */

        int matchedLength = 0, stringLength = fullString.length - index;
        if (stringLength == 1)
            matchedLength = fullString[index] >= '0' && fullString[index] <= '9' ? 1 : 0;
        else if (stringLength > 1) {
            if (isCharArrayStartWith(fullString, index, binPrefix) || isCharArrayStartWith(fullString, index, BinPrefix)) {
                boolean dotExisted = false;
                for (matchedLength = 2; matchedLength < stringLength; matchedLength++) {
                    char c = fullString[index + matchedLength];
                    if ((c >= '0' && c <= '9') || c == '_') ;
                    else if (c == '.') {
                        dotExisted = true;
                        if (matchedLength + 1 < stringLength) {
                            c = fullString[index + matchedLength + 1];
                            if (c < '0' || c > '9')
                                break;
                        } else break;
                    } else if (c == 'l' || c == 'L') {
                        if (!dotExisted) matchedLength++;
                        break;
                    } else {
                        break;
                    }
                }
            } else if (isCharArrayStartWith(fullString, index, hexPrefix) || isCharArrayStartWith(fullString, index, HexPrefix)) {
                boolean expoExisted = false, dotExisted = false;
                for (matchedLength = 2; matchedLength < stringLength; matchedLength++) {
                    char c = fullString[index + matchedLength];
                    if ((c >= '0' && c <= '9') || c == '_') ;
                    else if (c == '.') {
                        dotExisted = true;
                        if (matchedLength + 1 < stringLength) {
                            c = fullString[index + matchedLength + 1];
                            if (c < '0' || c > '9')
                                break;
                        } else break;
                    } else if (c == 'l' || c == 'L') {
                        if (!expoExisted && !dotExisted) matchedLength++;
                        break;
                    } else if (c == 'p' || c == 'P') {
                        if (expoExisted) break;
                        expoExisted = true;
                    } else if (c == '-' || c == '+') {
                        char cLast = fullString[index + matchedLength - 1];
                        if (cLast != 'p' && cLast != 'P') break;
                    } else if ((c >= 'a' && c <= 'f') || (c >= 'A' && c < 'F')) {
                        if (expoExisted) {
                            if (c == 'd' || c == 'D' || c == 'f' || c == 'F')
                                matchedLength++;
                            break;
                        }
                    } else
                        break;
                }
            } else {
                boolean expoExisted = false, dotExisted = false;
                for (matchedLength = 0; matchedLength < stringLength; matchedLength++) {
                    char c = fullString[index + matchedLength];
                    if (c >= '0' && c <= '9') ;
                    else if (c == '.') {
                        dotExisted = true;
                        if (matchedLength + 1 < stringLength) {
                            c = fullString[index + matchedLength + 1];
                            if (c < '0' || c > '9')
                                break;
                        } else break;
                    } else if (matchedLength > 0) {
                        if (c == '_') ;
                        else if (c == 'l' || c == 'L') {
                            if (!expoExisted && !dotExisted) matchedLength++;
                            break;
                        } else if (c == 'd' || c == 'D' || c == 'f' || c == 'F') {
                            matchedLength++;
                            break;
                        } else if (c == 'e' || c == 'E') {
                            if (expoExisted) break;
                            expoExisted = true;
                        } else if (c == '+' || c == '-') {
                            char cLast = fullString[index + matchedLength - 1];
                            if (cLast != 'e' && cLast != 'E') break;
                        } else break;
                    } else
                        break;
                }
            }
        }
        return matchedLength;
    }

    public static int lengthStringMatchedKeywords(char[] cString, int index, String[] keywords) {
        return lengthStringMatchedKeywords(cString, index, stringArrayToChar2DArray(keywords));
    }

    public static int lengthStringMatchedKeywords(char[] cString, int index, char[][] keywords) {
        for (char[] key : keywords) {
            if (isCharArrayStartWith(cString, index, key))
                return key.length;
        }
        return 0;
    }

    /***
     * eg. string = cccabccc, key = ab
     * result length = 3, 2, 3
     */
    public static int lengthStringToNextBreakPoint(char[] cString, int index, char[][] keywords) {
        int length = lengthStringMatchedKeywords(cString, index, keywords);
        if (length == 0) {
            int i;
            for (i = 1; index + i < cString.length; i++) {
                length = lengthStringMatchedKeywords(cString, index + i, keywords);
                if (length > 0) return i;
            }
            return i;
        } else
            return length;
    }

    public static ArrayList<String> splitStringAfterCondition(String string, String[] condition) {
        try {
            ArrayList<String> list = new ArrayList<String>();
            sortArrayByInverseLength(condition);
            int bufferSize = condition[0].length();
            if (bufferSize < 1) {
                list.add(string);
                return list;
            }
            int start = 0, iterator = 0;

            while (iterator < string.length()) {
                int matchedLength = lengthStringMatchedKeywords(string.toCharArray(), iterator, condition);
                if (matchedLength > 0) {
                    list.add(string.substring(start, iterator + matchedLength));
                    start = iterator + matchedLength;
                }
                iterator += matchedLength > 1 ? matchedLength : 1;
            }
            if (start < string.length())
                list.add(string.substring(start, string.length() - 1 > start ? string.length() : start));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<String> list = new ArrayList<String>();
            list.add(string);
            return list;
        }
    }
}

/**
 * rewrite split string by condition, no split mode, split position anymore, only support discrete both mode
 * write the own string compare methods, never use string.split and string.equal
 * unless they are used for chopping for a physical substring for being added
 * to the list
 * if necessary, write another chop string method specified for that purpose.
 */