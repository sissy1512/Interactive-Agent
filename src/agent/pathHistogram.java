package agent;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;


public class pathHistogram extends histogram{
	public String path;
	
	public pathHistogram(String path) throws SolrServerException{
		this.path = path;
//		getWordsList();		
	}
	
	//Get all words corresponding to the given path, construct the histogram
	private void getWordsList(HttpSolrServer server) throws SolrServerException{
		String q = "path:" + path;
		SolrQuery query = new SolrQuery();
	    query.setStart(0);    
	    query.set("defType", "edismax");
	    query.setRows(100);

	    query.setQuery(q);
	    QueryResponse response = server.query(query);

	    SolrDocumentList results = response.getResults();
//	    System.out.println("results:" +  results.size());
	    for (int i = 0; i < results.size(); ++i) {
	    	String word = (String) results.get(i).getFieldValue("word");
	    	addValue(word);
	    }
	}
	
	public void addValue(String word){
		if(distribute.containsKey(word)){
			int count  = distribute.get(word);
			distribute.put(word, count+1);
		} else{
			distribute.put(word, 1);
		}
	}
}
