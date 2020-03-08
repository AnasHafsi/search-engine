import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.similarities.Similarity;

public class Engine {
	
	private String label;
	private Analyzer analyzer;
	private Similarity similarity;
    private String indexPath;
	private String outputFilePath;
	
	
	public Engine() {}
	
	public Engine(String label,Analyzer analyzer, Similarity similarity, String indexPath, String outputFilePath) {
		this.label = label;
		this.analyzer = analyzer;
		this.similarity = similarity;
		this.indexPath= indexPath;
		this.outputFilePath = outputFilePath;
	}
	public String getLabel()
	{
		return label;
	}
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	public Similarity getSimilarity() {
		return similarity;
	}
	public void setSimilarity(Similarity similarity) {
		this.similarity = similarity;
	}
	
	public String getIndexPath() {
		return this.indexPath;
	}
	
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	public String getOutputFilePath() {
		return outputFilePath;
	}
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
	
	

}
