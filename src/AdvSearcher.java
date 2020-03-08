import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import analyzers.AnalyzerA;
import analyzers.AnalyzerM;
/** Simple command-line based search demo. */
public class AdvSearcher {
	private static final String INPUT_PATH = "/export/home/users/ingenieurs/info3/11810036/search-engine/in_index/";
	private static final String OUTPUT_PATH = "/export/home/users/ingenieurs/info3/11810036/search-engine/output_search/";
    private AdvSearcher() {}

    public static List<Engine> enginesFeed()
    {
    	List<Engine> engines = new ArrayList<>();
    	Engine analyzerM = new Engine("Stemmer with stopwords",new AnalyzerM(),new BM25Similarity(),INPUT_PATH +
    			"analyzerM/",OUTPUT_PATH + "AnalyzerM.txt");
    	Engine analyzerA = new Engine("(K) Stemmer without stopwords",new AnalyzerA(),new BM25Similarity(),INPUT_PATH +
    			"analyzerA/",OUTPUT_PATH + "AnalyzerA.txt");
    	Engine simpleStopWordsWithBM25 = new Engine("Simple Words with BM25Similarity",new EnglishAnalyzer(),new BM25Similarity(),INPUT_PATH +
    			"simplestopwords/",OUTPUT_PATH + "simple_stop_words.txt");
    	Engine standard = new Engine("Standard with BM25Similarity", new StandardAnalyzer(),new BM25Similarity(),INPUT_PATH + 
    			"standard/",OUTPUT_PATH + "standard.txt");
    	engines.add(simpleStopWordsWithBM25);
    	engines.add(standard);
    	engines.add(analyzerA);
    	engines.add(analyzerM);
    	return engines;
    }
    
    /** Simple command-line based search demo. */
    public static void main(String[] args) throws Exception {

        String field = "contents";
        String queries = "/export/home/users/ingenieurs/info3/11810036/search-engine/queries/q.txt";
        String simstring = "default";
        List<Engine> engines = enginesFeed();
        System.out.println(new Date().toLocaleString() + ": Launching Advanced Searcher for " + engines.size() + " engines");
        System.out.println();
        for (Engine e : engines) {
        	System.out.println("Querying for Engine " + e.getLabel());
        	System.out.println("Index directory " + e.getIndexPath());
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(e.getIndexPath()).toPath()));
            IndexSearcher searcher = new IndexSearcher(reader);        
            Analyzer analyzer = e.getAnalyzer();
        	Similarity simfn = e.getSimilarity();
            searcher.setSimilarity(simfn);

            BufferedReader in = null;
            if (queries != null) {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(new FileInputStream("queries"), "UTF-8"));
            }
            QueryParser parser = new QueryParser(field, analyzer);
            
            // Writer stream for every engine
       	 	File file = new File(e.getOutputFilePath());
      	    FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            String content = "";
            while (true) {
                String line = in.readLine();
                if (line == null || line.length() == -1) {
                    break;
                }
                line = line.trim();
                if (line.length() == 0) {
                    break;
                }
                String id = line;

                if (line == null || line.length() == -1) {
                    break;
                }

                line = line.trim();
                if (line.length() == 0) {
                    break;
                }
                String arg=line;
                Query query = parser.parse(arg);
                content+=doBatchSearch(in, searcher, id, query, simstring);
            }
            out.write(content);
            out.close();
            System.out.println("Query results written in " + e.getOutputFilePath());
            System.out.println();
            reader.close();
        }
        System.out.println("Finished.");
    }

    /**
     * This function performs a top-1000 search for the query as a basic TREC run.
     */
    public static String doBatchSearch(BufferedReader in, IndexSearcher searcher, String qid, Query query, String runtag)
            throws IOException {
    	String result = "";
        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, 1000);
        ScoreDoc[] hits = results.scoreDocs;
        HashMap<String, String> seen = new HashMap<String, String>(1000);
        TotalHits numTotalHits = results.totalHits;

        int start = 0;
        int end = (int) Math.min(numTotalHits.value, 1000);
        for (int i = start; i < end; i++) {
            Document doc = searcher.doc(hits[i].doc);
            String docno = doc.get("docno");
            // There are duplicate document numbers in the FR collection, so only output a given
            // docno once.
            if (seen.containsKey(docno)) {
                continue;
            }
            seen.put(docno, docno);
            result+=qid+" Q0 "+docno+" "+i+" "+hits[i].score+" "+runtag + "\n";
        }
        return result;
    }
}
