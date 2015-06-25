package org.fingertap.snippets;

import java.util.List;

public class Snippet {
    private final String text;
    private final List<MatchedTerm> matchedTerms;

    public Snippet(String text, List<MatchedTerm> matchedTerms) {
        this.text = text.trim();
        this.matchedTerms = matchedTerms;
    }

    public String getText() {
        return text;
    }

    public List<MatchedTerm> getMatchedTerms() {
        return matchedTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snippet snippet = (Snippet) o;

        return !(!text.equals(snippet.text)) && !(matchedTerms != null ? !matchedTerms.equals(snippet.matchedTerms) : snippet.matchedTerms != null);

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + (matchedTerms != null ? matchedTerms.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nSnippet{" +
                "text='" + text + '\'' +
                ", matchedTerms=" + matchedTerms +
                '}';
    }
}
