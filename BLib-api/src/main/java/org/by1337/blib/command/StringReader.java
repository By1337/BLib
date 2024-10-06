package org.by1337.blib.command;

public class StringReader {
    private final String string;
    private int cursor = 0;

    public StringReader(final String string) {
        this.string = string;
    }

    public boolean hasNext() {
        return cursor < string.length();
    }

    public char next() {
        char ch = string.charAt(cursor++);
        boundCheck();
        return ch;
    }
    private void boundCheck(){
        if (cursor < 0) cursor = 0;
        if (cursor > string.length()) cursor = string.length();
    }
    public void pop() {
        skip();
    }

    public char peek() {
        return string.charAt(cursor);
    }

    public void skip() {
        skip(1);
    }

    public void skip(int n) {
        cursor += n;
        boundCheck();
    }

    public void reset() {
        cursor = 0;
    }

    public char back() {
        if (cursor == 0) return string.charAt(0);
        return string.charAt(cursor -= 1);
    }

    public char peekPrevious() {
        return string.charAt(cursor - 1);
    }

    public String getString() {
        return string;
    }

    public int getCursor() {
        return cursor;
    }

    public String readToSpace() {
        return readTo(' ');
    }

    public String getExtra() {
        return string.substring(cursor);
    }

    public String readTo(char c) {
        StringBuilder sb = new StringBuilder();
        while (hasNext() && peek() != c) {
            sb.append(next());
        }
        return sb.toString();
    }

    public String readAll() {
        if (!hasNext()) return "";
        String s = string.substring(cursor);
        cursor = string.length();
        return s;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        if (cursor == 0) return ">>" + string;
        return string.substring(0, cursor - 1) + ">>" + string.substring(cursor - 1);
    }
}
