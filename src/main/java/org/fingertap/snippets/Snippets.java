package org.fingertap.snippets;

import java.text.BreakIterator;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Snippets {
    private static final int MIN_SNIPPET_LENGTH = 100;
    private static final int MAX_SNIPPET_LENGTH = 200;
    private static final int LOOKAHEAD = 7;

    private final Integer min_snippet_length;
    private final Integer max_snippet_length;
    private final Integer lookahead_count;

    private List<Snippet> snippets;

    public Snippets(String text, Collection<String> terms, Integer min_snippet_length, Integer max_snippet_length, Integer lookahead_count) {
        this.min_snippet_length = min_snippet_length != null ? min_snippet_length : MIN_SNIPPET_LENGTH;
        this.max_snippet_length = max_snippet_length != null ? max_snippet_length : MAX_SNIPPET_LENGTH;
        this.lookahead_count    = lookahead_count != null ? lookahead_count : LOOKAHEAD;

        this.snippets = breakTextIntoSentences(text).stream()
                .map(sentence -> new Snippet(sentence, findMatchedTerms(sentence, terms)))
                .filter(snippet -> snippet.getMatchedTerms().size() > 0)
                .map(snippet -> checkSnippetTextSize(snippet, terms))
                .collect(toList());
    }

    public Snippets(String text, Collection<String> terms) {
        this(text, terms, null, null, null);
    }

    public List<Snippet> getSnippets() {
        return snippets;
    }

    private Snippet checkSnippetTextSize(Snippet snippet, Collection<String> terms) {
        String text = snippet.getText();

        if (text.length() <= max_snippet_length)
            return snippet;

        String keyword = snippet.getMatchedTerms().get(0).getText();
        boolean keywordFirstWordInSnippet = text.indexOf(keyword) == 0;

        String[] snippetTokens = text.split(Pattern.quote(keyword));
        String newSnippet = "";

        // if keyword was NOT the first word in snippet then we need some prefix words for context
        if (!keywordFirstWordInSnippet) {
            newSnippet += prefixWords(snippetTokens[0]);
            newSnippet += keyword;
        }

        // now add some words after the keyword appears
        if (keywordFirstWordInSnippet || snippetTokens.length > 1) {
            int startIndexForPostfixWords = 1;

            // if keyword was the first word in snippet then there were no prefix words
            if (keywordFirstWordInSnippet) {
                startIndexForPostfixWords = 0;
            }

            newSnippet += postfixWords(snippetTokens, startIndexForPostfixWords, keyword, newSnippet.length());
        }

        if (newSnippet.length() < max_snippet_length)
            newSnippet = prefixPaddingWords(snippetTokens[0], newSnippet.length()) + newSnippet;


        if (!keywordFirstWordInSnippet && text.indexOf(newSnippet.replaceAll(" \\.\\.\\.$", "")) != 0)
            newSnippet = "... " + newSnippet;

        newSnippet = newSnippet.trim();

        return new Snippet(newSnippet, findMatchedTerms(newSnippet, terms));
    }

    private String prefixPaddingWords(String value, int currentSnippetLength) {
        String[] tokens = value.split(" ");
        String prefixPaddingWords = "";

        if (tokens.length > lookahead_count) {
            for (int i = tokens.length - lookahead_count - 1; i >= 0; i--) {
                prefixPaddingWords = tokens[i] + " " + prefixPaddingWords;

                if (prefixPaddingWords.length() + currentSnippetLength >= max_snippet_length)
                    break;
            }
        }

        return prefixPaddingWords;
    }

    private String prefixWords(String value) {
        String[] tokens = value.split(" ");
        StringBuilder prefixWords = new StringBuilder();

        if (tokens.length <= lookahead_count) {
            for (String token : tokens) {
                prefixWords.append(token).append(" ");
            }
        } else {
            for (int i = tokens.length - lookahead_count; i < tokens.length; i++) {
                prefixWords.append(tokens[i]).append(" ");
            }
        }

        return prefixWords.toString();
    }

    private String postfixWords(String[] values, int startIndex, String keyword, int currentSnippetSize) {
        String postfixString = "";

        mainloop:
        for (int i = startIndex; i < values.length; i++) {
            String valueToken = values[i];

            String[] tokens = valueToken.split(" ");
            for (String token : tokens) {
                if ((postfixString.length() + currentSnippetSize) > max_snippet_length) {
                    postfixString += "...";
                    break mainloop;
                }

                postfixString += token + " ";
            }
            if (i < values.length - 1)
                postfixString += keyword;
        }

        return postfixString;
    }

    private List<String> breakTextIntoSentences(String text) {
        List<String> sentences = new ArrayList<>();

        if (text == null)
            return sentences;

        BreakIterator boundary = BreakIterator.getSentenceInstance();
        boundary.setText(text);

        int start = boundary.first();
        String previousSentence = "";
        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {

            String sentence = text.substring(start, end);

            if (sentence.trim().equals(""))
                continue;

            // pad sentence if too small
            if (sentence.length() < this.min_snippet_length) {
                start = end;
                end = boundary.next();

                // append to the next line, unless we've reached the end, in which case, append the previous line to this.
                if (end != BreakIterator.DONE) {
                    String nextSentence = text.substring(start, end);
                    sentence += nextSentence;
                } else {
                    sentence = previousSentence + sentence;
                    sentences.remove(previousSentence);
                }
            }

            sentences.add(sentence);
            previousSentence = sentence;
        }

        return sentences;
    }

    private List<MatchedTerm> findMatchedTerms(String sentence, Collection<String> terms) {
        List<MatchedTerm> termsFound = new ArrayList<>();

        if (terms == null)
            return termsFound;

        for (String term : terms) {
            if (term == null || term.trim().equals(""))
                continue;

            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = sentence.toLowerCase().indexOf(term.toLowerCase(), lastIndex);

                if (lastIndex != -1) {
                    String termToUse = sentence.substring(lastIndex, lastIndex + term.length());
                    termsFound.add(new MatchedTerm(termToUse, lastIndex));
                    lastIndex += term.length();
                }
            }
        }

        return termsFound;
    }
}
