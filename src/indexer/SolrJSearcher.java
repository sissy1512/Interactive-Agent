package indexer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.net.MalformedURLException;

public class SolrJSearcher {
	HttpSolrServer server;
	public SolrJSearcher(HttpSolrServer server){
		this.server = server;
	}
  
  
  //Find all sub-contents starting from the same level as the given path p
  public QueryResponse findSilblings(String q) throws MalformedURLException, SolrServerException {
	  int index = q.lastIndexOf(".");
	  String prefix = q.substring(0, index);
	  return subContent(prefix);
  }
  
  //Find all sub-contents of the given path p
  public QueryResponse subContent(String p) throws MalformedURLException, SolrServerException{
	  String query = "path_prefix:" + p;
	  return search(query);
  }
  
  public QueryResponse search(String q) throws MalformedURLException, SolrServerException {
	  	SolrQuery query = new SolrQuery();
	    query.setQuery(q);
	    //query.addFilterQuery("price:1005.0");
	    query.setStart(0);    
	    query.set("defType", "edismax");
	    //query.set("deftype", "func");
	    query.setRows(100);
	    System.out.println("query:" + query.getQuery());

	    QueryResponse response = server.query(query);
	    return response;
//	    SolrDocumentList results = response.getResults();
//	    System.out.println("results:" +  results.size());
//	    for (int i = 0; i < results.size(); ++i) {
//	      System.out.println(results.get(i));
//	    }
  }
}