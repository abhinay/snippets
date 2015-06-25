package org.fingertap.snippets;

public class MatchedTerm {
    private final String text;
    private final int startIndex;
    private final int length;

    public MatchedTerm(String text, int startIndex) {
        this.text = text.trim();
        this.startIndex = startIndex;
        this.length = text.length();
    }

    public String getText() {
        return text;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchedTerm that = (MatchedTerm) o;

        return startIndex == that.startIndex && length == that.length && !(!text.equals(that.text));

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + startIndex;
        result = 31 * result + length;
        return result;
    }

    @Override
    public String toString() {
        return "\nMatchedTerm{" +
                "text='" + text + '\'' +
                ", startIndex=" + startIndex +
                ", length=" + length +
                '}';
    }
}
