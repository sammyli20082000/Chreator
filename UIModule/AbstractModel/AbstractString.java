package Chreator.UIModule.AbstractModel;

public class AbstractString {
    private char[] chars;
    private int index, length;

    public AbstractString() {
        clear();
    }

    public AbstractString(char[] chars, int index, int length) {
        set(chars, index, length);
    }

    public void clear() {
        chars = null;
        index = -1;
        length = 0;
    }

    public void set(char[] chars, int index, int length) {
        this.chars = chars;
        this.index = index;
        this.length = length;
    }

    public String toString() {
        return chars == null ? null : new String(toCharArray());
    }

    public char[] toCharArray() {
        if (chars == null)
            return null;
        else {
            char[] c = new char[length <= chars.length - index ? length : chars.length - index];
            for (int i = 0; i < c.length; i++)
                c[i] = chars[index + i];

            return c;
        }
    }

    public char charAtLengthFromIndex(int charAt) {
        return chars[index + charAt];
    }

    public boolean isNull() {
        return chars == null;
    }

    public int index() {
        return index;
    }

    public int length() {
        return length;
    }

    public char[] getOriginalChars(){
        return chars;
    }

    public boolean equals(AbstractString s) {
        if (s == null) return false;
        if (s.length() != length) return false;
        if (s.isNull() && isNull()) return true;
        else if (!s.isNull() && !isNull()) {
            for (int i = 0; i < length; i++)
                if (s.charAtLengthFromIndex(s.index() + i) != chars[index + i]) return false;
            return true;
        } else return false;
    }
}