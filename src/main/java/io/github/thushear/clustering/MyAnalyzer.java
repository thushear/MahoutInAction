/*
 * Source code for Listing 9.5
 * 
 */

package io.github.thushear.clustering;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAnalyzer extends Analyzer {
  
  private final Pattern alphabets = Pattern.compile("[a-z]+");
  
  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    TokenStream result = new StandardTokenizer(Version.LUCENE_CURRENT, reader);
    result = new StandardFilter(result);
    result = new LowerCaseFilter(result);
    result = new StopFilter(true, result, StandardAnalyzer.STOP_WORDS_SET);
    
    TermAttribute termAtt = (TermAttribute) result.addAttribute(TermAttribute.class);
    StringBuilder buf = new StringBuilder();
    try {
      while (result.incrementToken()) {
        if (termAtt.termLength() < 3) continue;
        String word = new String(termAtt.termBuffer(), 0, termAtt.termLength());
        Matcher m = alphabets.matcher(word);
        
        if (m.matches()) {
          buf.append(word).append(" ");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return new WhitespaceTokenizer(new StringReader(buf.toString()));
  }
}