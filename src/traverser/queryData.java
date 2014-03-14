package traverser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class queryData {
	//Jobs
	public ArrayList<condition> queries = new ArrayList<condition>();
	//Resumes
	public ArrayList<String> keywords = new ArrayList<String>();
	private boolean companyNamePrefer = false;
	private boolean companyTypePrefer = false;
	private boolean areaPrefer = false;

	
	public void addCondition(String path, String value){
		condition con = new condition(path, value);
		queries.add(con);
	}
	
	public void setCompanyNamePrefer(){
		companyNamePrefer = true;
	}
	
	public void setCompanyTypePrefer(){
		companyTypePrefer = true;
	}
	
	public void setAreaPrefer(){
		areaPrefer = true;
	}
	
	
	
	public boolean getCompanyNamePrefer(){
		return companyNamePrefer;
	}
	
	public boolean getCompanyTypePrefer(){
		return companyTypePrefer;
	}
	
	public boolean getAreaPrefer(){
		return areaPrefer;
	}
	
	public HashSet<String> query(ArrayList<condition> cons, HttpSolrServer server) throws SolrServerException{
		HashMap<String, ArrayList<String>> combination = new HashMap<String, ArrayList<String>>();
		
		SolrQuery query = new SolrQuery();
	    query.setStart(0);    
	    query.set("defType", "edismax");
	    query.setRows(1000);
//	    System.out.println("query:" + query.getQuery());
	    QueryResponse response = new QueryResponse();
		
		
		for(condition con : cons){
			if(combination.containsKey(con.getPath())){
				combination.get(con.getPath()).add(con.getValue());			
			} else{			
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(con.getValue());
				combination.put(con.getPath(), arr);
			}
		}
		String q = "";
		HashSet<String> candidateDocs = new HashSet<String>();
	//	boolean first = true;
		for(Entry<String, ArrayList<String>> con : combination.entrySet()){
//			if(con.getValue().size() == 1){
//				HashSet<String> tmpDocs = new HashSet<String>();
//				
//				String value = con.getValue().get(0);
//				q = "path:" + con.getKey().replace("dataset", datasetName) + " AND value:" + value;	
//				query.setQuery(q);
//				response = server.query(query);
//				SolrDocumentList results = response.getResults();			
//			    for (int i = 0; i < results.size(); ++i) {
//			    	tmpDocs.add((String) results.get(i).getFieldValue("doc_id"));
//			    }	
//			    if(first){
//			    	candidateDocs = tmpDocs;
//			    	first = false;
//			    }
//			    else{
//			    	HashSet<String> tmp = (HashSet<String>) candidateDocs.clone();
//			    	for(String s : candidateDocs){
//			    		if(!tmpDocs.contains(s)){
//			    			tmp.remove(s);
//			    		}
//			    	}
//			    	if(tmp.size() == 0)
//			    		return tmp;
//			    	candidateDocs = tmp;
//			    }
//			} else{
//				HashSet<String> unionDocs = new HashSet<String>();
//				for(int i = 0; i < con.getValue().size(); ++i){
//					String value = con.getValue().get(i);
//					q = "path:\"" + con.getKey().replace("dataset", datasetName) + "\" AND value:\"" + value + "\"";
//					query.setQuery(q);
//					response = server.query(query);
//					SolrDocumentList results = response.getResults();
//					for (int j = 0; j < results.size(); ++j) {
//						unionDocs.add((String) results.get(j).getFieldValue("doc_id"));
//					}	
//				}
//				if(first){
//				    candidateDocs = unionDocs;
//				    first = false;
//				}
//				else{
//					HashSet<String> tmp = (HashSet<String>) candidateDocs.clone();
//				    for(String s : candidateDocs){
//				    	if(!unionDocs.contains(s)){
//				    		tmp.remove(s);
//				    	}
//				    }
//				    if(tmp.size() == 0)
//			    		return tmp;
//				    candidateDocs = tmp;
//				}
//			}	
			
			if(con.getValue().size() == 1){
				String value = con.getValue().get(0);
				if(value.charAt(0) != '"')
					value = "\"" + value + "\"";
				q += " AND " + con.getKey() + ":" + value;				
			} else{				
				q += " AND ( ";
				for(int i = 0; i < con.getValue().size(); ++i){
					String value = con.getValue().get(i);
					if(value.charAt(0) != '"')
						value = "\"" + value + "\"";
					if(i == 0)
						q += con.getKey() + ":" + value;	
					else
						q += " OR " + con.getKey() + ":" + value;
				}
				q += ")";
			}
		}
		if(q.startsWith(" AND "))
			q = q.substring(5);
		query.setQuery(q);
		response = server.query(query);
		SolrDocumentList results = response.getResults();
		for (int j = 0; j < results.size(); ++j) {
			candidateDocs.add((String) results.get(j).getFieldValue("doc_id"));
		}	

		for(String s : candidateDocs){
			System.out.println(s);
		}
		
		return candidateDocs;
	}
	
//	public HashSet<String> query(ArrayList<String> keywords, HttpSolrServer server) throws SolrServerException{
//		HashSet<String> docs = new HashSet<String>();
//		SolrQuery query = new SolrQuery();
//	    query.setStart(0);    
//	    query.set("defType", "edismax");
//	    query.setRows(1000);
//	    QueryResponse response = new QueryResponse();
//	    String q = "";
//	    boolean first = true;
//	    for(String s: keywords){
//	    	if(!first)
//	    		q += " OR ";
//	    	else{
//	    		q += "full_text:(";
//	    		first = false;
//	    	}
//	    	q +=  "\""+ s + "\"";
//	    }
//	    q += ")";
//	    query.setQuery(q);
//		response = server.query(query);
//		SolrDocumentList results = response.getResults();
//		for (int j = 0; j < results.size(); ++j) {
//			docs.add((String) results.get(j).getFieldValue("doc_id"));
//		}
//		return docs;
//	}
	
	
	
	
}
