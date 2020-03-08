package analyzers;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class AnalyzerA extends Analyzer {
    public AnalyzerA() {}

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        TokenStream stream = new LowerCaseFilter(src);
        stream = new KStemFilter(stream);
        return new TokenStreamComponents(src, stream);
    }
}