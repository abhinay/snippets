Snippets
========

Snippets provides a utility for extracting short excerpts (snippets) from given text centered around one or many query terms.  
Snippet extraction work by breaking the original text up into sentences, sentences that contain the query terms are then extracted to provide a sensible excerpt.

#Requirement

Java 8

#Installation

Maven
-----

    <dependency>
	    <groupId>org.fingertap.snippets</groupId>
	    <artifactId>snippets</artifactId>
	    <version>1.0</version>
    </dependency>

Gradle
------

    repositories {
      mavenCentral()
    }

    dependencies {
      compile group: 'org.fingertap.snippets', name: 'snippets', version: '1.0'
    }

Ivy
---

    <dependency org="org.fingertap.snippets" name="snippets" rev="1.0" />

#Usage

    Collection<String> terms = new ArrayList<>();
    terms.add("needle");
    
    String text = "massive string of haystack .."
    List<Snippet> snippets = new Snippets(text, terms).getSnippets();
    
    System.out.println(snippets.get(0).getText());