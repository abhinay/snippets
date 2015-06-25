package org.fingertap.snippets;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SnippetsTest extends TestCase {
    public SnippetsTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(SnippetsTest.class);
    }

    private final String text = "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of testable explanations and predictions about the universe.[nb 2] In an older and closely related meaning, \"science\" also refers to this body of knowledge itself, of the type that can be rationally explained and reliably applied. Ever since classical antiquity, science as a type of knowledge has been closely linked to philosophy. In the West during the early modern period the words \"science\" and \"philosophy of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy (which is today called \"natural science\").";

    public void testTermGeneratesSnippets() {
        String term = "philosophy";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(text, terms).getSnippets();

        assertEquals(2, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "Ever since classical antiquity, science as a type of knowledge has been closely linked to philosophy.",
                firstSnippet.getText());
        assertEquals(1, firstSnippet.getMatchedTerms().size());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(term, firstMatchedTerm.getText());
        assertEquals(term.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(term), firstMatchedTerm.getStartIndex());

        Snippet secondSnippet = snippets.get(1);
        assertEquals(
                "... early modern period the words \"science\" and \" philosophy of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy (which is today called \"natural science\").",
                secondSnippet.getText());
        assertEquals(2, secondSnippet.getMatchedTerms().size());

        List<MatchedTerm> secondMatchedTerms = secondSnippet.getMatchedTerms();
        assertEquals(term, secondMatchedTerms.get(0).getText());
        assertEquals(term.length(), secondMatchedTerms.get(0).getLength());
        int indexOfFirstMatch = secondSnippet.getText().indexOf(term);
        assertEquals(indexOfFirstMatch, secondMatchedTerms.get(0).getStartIndex());

        assertEquals(term, secondMatchedTerms.get(1).getText());
        assertEquals(term.length(), secondMatchedTerms.get(1).getLength());
        assertEquals(secondSnippet.getText().indexOf(term, indexOfFirstMatch+10), secondMatchedTerms.get(1).getStartIndex());
    }

    public void testTermCaseIsIgnoredWhenGeneratingSnippets() {
        String term = "philosophy";

        Collection<String> terms = new ArrayList<>();
        terms.add("PHILosophy");

        List<Snippet> snippets = new Snippets(text, terms).getSnippets();

        assertEquals(2, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "Ever since classical antiquity, science as a type of knowledge has been closely linked to philosophy.",
                firstSnippet.getText());
        assertEquals(1, firstSnippet.getMatchedTerms().size());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(term, firstMatchedTerm.getText());
        assertEquals(term.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(term), firstMatchedTerm.getStartIndex());

        Snippet secondSnippet = snippets.get(1);
        assertEquals(
                "... early modern period the words \"science\" and \" philosophy of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy (which is today called \"natural science\").",
                secondSnippet.getText());
        assertEquals(2, secondSnippet.getMatchedTerms().size());

        MatchedTerm secondMatchedTerm = secondSnippet.getMatchedTerms().get(0);
        assertEquals(term, secondMatchedTerm.getText());
        assertEquals(term.length(), secondMatchedTerm.getLength());
        assertEquals(secondSnippet.getText().indexOf(term), secondMatchedTerm.getStartIndex());
    }

    public void testMultipleTermsGeneratesSnippets() {
        String firstTerm = "Science";
        String secondTerm = "systematic enterprise";

        Collection<String> terms = new ArrayList<>();
        terms.add(firstTerm);
        terms.add(secondTerm);

        List<Snippet> snippets = new Snippets(text, terms).getSnippets();

        assertEquals(4, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of testable explanations and predictions about the universe.",
                firstSnippet.getText());
        assertEquals(2, firstSnippet.getMatchedTerms().size());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(firstTerm, firstMatchedTerm.getText());
        assertEquals(firstTerm.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(firstTerm), firstMatchedTerm.getStartIndex());

        MatchedTerm secondMatchTerm = firstSnippet.getMatchedTerms().get(1);
        assertEquals(secondTerm, secondMatchTerm.getText());
        assertEquals(secondTerm.length(), secondMatchTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(secondTerm), secondMatchTerm.getStartIndex());

        Snippet secondSnippet = snippets.get(1);
        assertEquals(
                "[nb 2] In an older and closely related meaning, \"science\" also refers to this body of knowledge itself, of the type that can be rationally explained and reliably applied.",
                secondSnippet.getText());
        assertEquals(1, secondSnippet.getMatchedTerms().size());

        MatchedTerm secondMatchedTerm = secondSnippet.getMatchedTerms().get(0);
        assertEquals("science", secondMatchedTerm.getText());
        assertEquals("science".length(), secondMatchedTerm.getLength());
        assertEquals(secondSnippet.getText().indexOf("science"), secondMatchedTerm.getStartIndex());
    }

    public void testGeneratesSingleSnippet() {
        String term = "systematic enterprise";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(text, terms).getSnippets();

        assertEquals(1, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of testable explanations and predictions about the universe.",
                firstSnippet.getText());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(term, firstMatchedTerm.getText());
        assertEquals(term.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(term), firstMatchedTerm.getStartIndex());
    }

    public void testNullTerms() {
        List<Snippet> snippets = new Snippets(text, null).getSnippets();
        assertTrue(snippets.isEmpty());
    }

    public void testNonMatchigTerms() {
        String term = "This term should not match";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(text, terms).getSnippets();
        assertTrue(snippets.isEmpty());
    }

    public void testNullText() {
        String term = "philosophy";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(null, terms).getSnippets();
        assertTrue(snippets.isEmpty());
    }

    public void testNullTextAndTerms() {
        List<Snippet> snippets = new Snippets(null, null).getSnippets();
        assertTrue(snippets.isEmpty());
    }

    public void testLargeSnippetsGetTrimmed() {
        String textWithoutFullStops = "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of " +
                "testable explanations and predictions about the universe [nb 2] In an older and closely related meaning, " +
                "\"science\" also refers to this body of knowledge itself, of the type that can be rationally explained " +
                "and reliably applied. Ever since classical antiquity, science as a type of knowledge has been closely " +
                "linked to philosophy. In the West during the early modern period the words \"science\" and \"philosophy " +
                "of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy " +
                "(which is today called \"natural science\")";

        String term = "systematic enterprise";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(textWithoutFullStops, terms).getSnippets();

        assertEquals(1, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of testable explanations and predictions about the universe [nb 2] In an older and closely related meaning, \"science\" ...",
                firstSnippet.getText());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(term, firstMatchedTerm.getText());
        assertEquals(term.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(term), firstMatchedTerm.getStartIndex());
    }

    public void testLargeSentenceWhereTermAtStart() {
        String textWithoutFullStops = "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of " +
                "testable explanations and predictions about the universe [nb 2] In an older and closely related meaning, " +
                "\"science\" also refers to this body of knowledge itself, of the type that can be rationally explained " +
                "and reliably applied. Ever since classical antiquity, science as a type of knowledge has been closely " +
                "linked to philosophy. In the West during the early modern period the words \"science\" and \"philosophy " +
                "of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy " +
                "(which is today called \"natural science\")";

        String term = "Science[nb 1]";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(textWithoutFullStops, terms).getSnippets();

        assertEquals(1, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of testable explanations and predictions about the universe [nb 2] In an older and closely related meaning, \"science\" ...",
                firstSnippet.getText());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(term, firstMatchedTerm.getText());
        assertEquals(term.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(term), firstMatchedTerm.getStartIndex());
    }

    public void testLargeSentenceWhereTermAtEnd() {
        String textWithoutFullStops = "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of " +
                "testable explanations and predictions about the universe [nb 2] In an older and closely related meaning, " +
                "\"science\" also refers to this body of knowledge itself, of the type that can be rationally explained " +
                "and reliably applied. Ever since classical antiquity, science as a type of knowledge has been closely " +
                "linked to philosophy. In the West during the early modern period the words \"science\" and \"philosophy " +
                "of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy " +
                "(which is today called \"natural science\")";

        String term = "(which is today called \"natural science\")";

        Collection<String> terms = new ArrayList<>();
        terms.add(term);

        List<Snippet> snippets = new Snippets(textWithoutFullStops, terms).getSnippets();

        assertEquals(1, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "... the early modern period the words \"science\" and \"philosophy of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy (which is today called \"natural science\")",
                firstSnippet.getText());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals(term, firstMatchedTerm.getText());
        assertEquals(term.length(), firstMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf(term), firstMatchedTerm.getStartIndex());
    }

    public void testLargeSentenceWithMultiMatchTerms() {
        String textWithoutFullStops = "Science[nb 1] is a systematic enterprise that builds and organizes knowledge in the form of " +
                "testable explanations and predictions about the universe [nb 2] In an older and closely related meaning, " +
                "\"science\" also refers to this body of knowledge itself, of the type that can be rationally explained " +
                "and reliably applied. Ever since classical antiquity, science as a type of knowledge has been closely " +
                "linked to philosophy. In the West during the early modern period the words \"science\" and \"philosophy " +
                "of nature\" were sometimes used interchangeably,[2]:p.3 and until the 19th century natural philosophy " +
                "(which is today called \"natural science\")";

        Collection<String> terms = new ArrayList<>();
        terms.add("knowledge itself");
        terms.add("rationally explained");

        List<Snippet> snippets = new Snippets(textWithoutFullStops, terms).getSnippets();

        assertEquals(1, snippets.size());

        Snippet firstSnippet = snippets.get(0);
        assertEquals(
                "... predictions about the universe [nb 2] In an older and closely related meaning, \"science\" also refers to this body of knowledge itself, of the type that can be rationally explained and reliably applied.",
                firstSnippet.getText());
        assertEquals(2, firstSnippet.getMatchedTerms().size());

        MatchedTerm firstMatchedTerm = firstSnippet.getMatchedTerms().get(0);
        assertEquals("knowledge itself", firstMatchedTerm.getText());
        assertEquals("knowledge itself".length(), firstMatchedTerm.getLength());
                assertEquals(firstSnippet.getText().indexOf("knowledge itself"), firstMatchedTerm.getStartIndex());

        MatchedTerm secondMatchedTerm = firstSnippet.getMatchedTerms().get(1);
        assertEquals("rationally explained", secondMatchedTerm.getText());
        assertEquals("rationally explained".length(), secondMatchedTerm.getLength());
        assertEquals(firstSnippet.getText().indexOf("rationally explained"), secondMatchedTerm.getStartIndex());
    }
}
