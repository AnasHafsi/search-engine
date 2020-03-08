package analyzers;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class AnalyzerM extends StopwordAnalyzerBase {
	public static final CharArraySet ENGLISH_STOP_WORDS_SET;

	  static {
	    final List<String> stopWords = Arrays.asList(
	        "a", "an", "and", "are", "as", "at", "be", "but", "by",
	        "for", "if", "in", "into", "is", "it",
	        "no", "not", "of", "on", "or", "such",
	        "that", "the", "their", "then", "there", "these",
	        "they", "this", "to", "was", "will", "with"
	    );
	    final CharArraySet stopSet = new CharArraySet(stopWords, false);
	    ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	  }

	
	
	public static CharArraySet getDefaultStopSet(){
		    return ENGLISH_STOP_WORDS_SET;
		  }
	
	public AnalyzerM() {
	    this(ENGLISH_STOP_WORDS_SET);
	}
	public AnalyzerM(CharArraySet stopwords) {
	    super(stopwords);
	}
	  @Override
	  protected TokenStreamComponents createComponents(String fieldName) {
	    final Tokenizer source = new StandardTokenizer();
	    TokenStream result  = new LowerCaseFilter(source);
	    result = new StopFilter(result, stopwords);
	    result = new PorterStemFilter(result);
	    return new TokenStreamComponents(source, result);
	  }
	

}