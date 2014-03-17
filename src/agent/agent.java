package agent;

import indexer.SolrjPopulator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import parser.ASTStart;
import parser.QueryParser;
import traverser.condition;
import traverser.traverser;

public class agent {
	
	public static HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
	public static HashMap<String, String> asked = new HashMap<String, String>();
	//Matching Jobs
	public HashSet<String> docs = new HashSet<String>();
	//Histogram for all field values in job docs
	public histogram jh = new histogram("job");
	//A histogram for all paths related to a specific value in jobs
	public HashMap<String, histogram> jhs = new HashMap<String, histogram>();
	//A histogram for all values related to a specific path in resumes
	public HashMap<String, histogram> rhs = new HashMap<String,histogram>();
	public ArrayList<String> sortedPaths = new ArrayList<String>();
	QueryParser t = new QueryParser(new ByteArrayInputStream("".getBytes("UTF-8")));
	static SolrjPopulator pop;
	static traverser tr;	
	double threshold = 0.02;
	
	//The sum of number of occurrences of all filed values in qualified job docs
	int occurrences = 0;
	
	HashSet<String> answerWords = new HashSet<String>();
    HashMap<String, ArrayList<String>> pairs = new HashMap<String, ArrayList<String>>();
//	public HashMap<String, Integer> attributes = new HashMap<String, Integer>();
//	public String[] rankedAttr;

	SolrQuery query = new SolrQuery();
    QueryResponse response = new QueryResponse();
    
    //index data
    public agent() throws SolrServerException, IOException{
    	pop = new SolrjPopulator(server);
		pop.index();
    	tr = new traverser(asked);
    	query.setStart(0);    
        query.set("defType", "edismax");
        query.setRows(1000);
    }
   
	
	public void restart(){
		asked = new HashMap<String, String>();
		docs = new HashSet<String>();
		jh = new histogram("job");
		jhs = new HashMap<String,histogram>();
		rhs = new HashMap<String,histogram>();
		occurrences = 0;
		sortedPaths = new ArrayList<String>();
		tr = new traverser(asked);
	}
	
    
	public boolean onePass(String path, String answer){
		    String[] words = answer.split(",");
		    int len = words.length;
		    histogram rh = rhs.get(path);
		  //drop suffix Resumes.
		    path = path.substring(8);
		    boolean find = false;
		    for(int i = 0; i < len; ++i){
		    	if(!words[i].startsWith("\""))
		    		words[i] = "\"" + words[i] + "\"";
		    	if(rh.distribute.containsKey(words[i]) && (double)rh.distribute.get(words[i])/rh.sum > threshold){
			    	answerWords.add(words[i]);
		    		if(pairs.containsKey(path)){
		    			pairs.get(path).add(words[i]);
		    		}else{
		    			ArrayList<String> tmp = new ArrayList<String>();
		    			tmp.add(words[i]);
		    			pairs.put(path, tmp);
		    		}
		    		find = true;
		    	}
		    }
		    return find;
	}
	
	public void filterJob() throws SolrServerException{
	//	ArrayList<condition> cons = new ArrayList<condition>();
		for(String s: answerWords){
			if(jhs.containsKey(s) && (double)jhs.get(s).sum/occurrences > threshold){
				int curSum = jhs.get(s).sum;
				histogram tmp = jhs.get(s);
				for(Entry<String, Integer> e: tmp.distribute.entrySet()){
					if((double)e.getValue()/tmp.sum >= tmp.threshold){
						if(!asked.containsKey(e.getKey())){
							condition con = new condition(e.getKey(), s);
							tr.queryConditionsJob.queries.add(con);
						}
					}
				}
			}
		}
		
		
//		for(Entry<String, HashSet<String>> e: jh.valuePath.entrySet()){
//			if(answerWords.contains(e.getKey())){
//				for(String s: e.getValue()){
//					if(!asked.containsKey(s)){
//						condition con = new condition(s, e.getKey());
//						tr.queryConditionsJob.queries.add(con);
//					}
//				}
//			}
//		}
		docs = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, server);
	}
	
	//read and parse input query, decide whether to start the interactive action or not, construct job and resume histograms
	public void initializeData(String inputQuery) throws SolrServerException{
		try {
			t.ReInit(new ByteArrayInputStream(inputQuery.getBytes("UTF-8")));
			ASTStart root = t.Start();
	//		root.dump(">");
			String data = "jobs";
			root.jjtAccept(tr, data);	
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//Decide whether to start the interactive action or not
		docs = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, server);		
		//Too few result
		if(docs.size() < 3){
			ArrayList<condition> newQuery = new ArrayList<condition>();
			for(condition con: tr.queryConditionsJob.queries){
				if(!con.getPath().equals("Jobs.title")){
					newQuery.add(con);
				}
			}
			tr.queryConditionsJob.queries = newQuery;
			docs = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, server);
			if(docs.size() < 3){
				System.out.println("We just have a few jobs with the position you're looking for.");
				return;
			}
		}
		if(docs.size() >= 3 && docs.size() <= 5){
			System.out.println("Output result:");
			for(String s: docs)
				System.out.println(s);
			return;
		}
		
		//Too much matching, construct job histogram, prepare to interact
		String q = "";
		for(String s: docs){
			q = "doc_id:\"" + s + "\"";
			query.setQuery(q);
			response = server.query(query);
			
			SolrDocumentList results = response.getResults();			

	    	for(Entry<String, Object> e: results.get(0).entrySet()){
	    		String path = (String) e.getKey();
	    		if(path.equals("jobs.id") || path.equals("doc_id") || path.equals("id"))
	    			continue; 		
	    		if(e.getValue() instanceof ArrayList<?>){
	    			ArrayList<String> values = (ArrayList<String>) e.getValue();
		    		for(String cur: values){
		    			jh.addValue(cur);
		    			if(jhs.containsKey(cur)){
			    			histogram tmp = jhs.get(cur);
			    			tmp.addValue(path);
			    		}else{
			    			histogram tmp = new histogram(cur);
			    			tmp.distribute.put(path, 1);
			    			jhs.put(cur, tmp);
			    		}
		    			occurrences++;
		    		}
	    		}else if(e.getValue() instanceof String){
	    			jh.addValue((String)e.getValue());
	    			if(jhs.containsKey((String)e.getValue())){
		    			histogram tmp = jhs.get((String)e.getValue());
		    			tmp.addValue(path);
		    		}else{
		    			histogram tmp = new histogram((String)e.getValue());
		    			tmp.distribute.put(path, 1);
		    			jhs.put((String)e.getValue(), tmp);
		    		}
	    			occurrences++;
	    		}
	    		
	    	}	
	    	
		}
		
		for(Entry<String, histogram> e: jhs.entrySet()){
			e.getValue().sortValue();
			e.getValue().sumUp();
		}
		
		
		jh.sortValue();
		int size = jh.sortedValue.length;
		for(int i = 0; i < size; ++i){
			System.out.println(jh.sortedValue[i] + ": " + jh.distribute.get(jh.sortedValue[i]));
		}
		
		
		ArrayList<String> words = jh.getTopWords();
//		System.out.println("Sum: " + jh.sum);
		System.out.println("Job Words");
		for(String word: words){
			System.out.println(word);
		}
		
		constructResumeHistograms(words);
	}
	
	//Find related resume paths, sort them by occurrences, construct the resume histograms, return the sorted paths 
	public void constructResumeHistograms(ArrayList<String> words) throws SolrServerException{
		//HashMap<String, histogram> rhs = new HashMap<String, histogram>();
		HashMap<String, Integer> paths = new HashMap<String, Integer>();
		
		//Get all related paths and their occurrences(one path may corresponds to several words)
		for(String word : words){
			String q = "word:" + word;	
			query.setQuery(q);
			response = server.query(query);
			
			SolrDocumentList results = response.getResults();	
				
		    for (int i = 0; i < results.size(); ++i) {
		    	String path = (String) results.get(i).getFieldValue("path");
		    	if(paths.containsKey(path)){
		    		int count = paths.get(path);
		    		paths.put(path, count+1);
		    	} else
		    		paths.put(path, 1);
		    }	
		}
		
		//Sort the paths according to their occurrences
		int size = paths.size();
		for(Entry<String, Integer> e: paths.entrySet()){
			int curCount = e.getValue();
			String curPath = e.getKey();
			size = sortedPaths.size();
			int i;
			for(i = 0; i < size; ++i){
				if(curCount > paths.get(sortedPaths.get(i))){
					sortedPaths.add(i, curPath);
					break;
				}
			}
			if(i == size){
				sortedPaths.add(curPath);
			}
		}
		
		//Create resumeHistogram for each path
		System.out.println("Resume");
		for(String s: sortedPaths){
			System.out.println(s + " : " + paths.get(s));
			histogram rh = new histogram(s);
		
			String q = "path:" + s;
			SolrQuery query = new SolrQuery();
		    query.setStart(0);    
		    query.set("defType", "edismax");
		    query.setRows(100);

		    query.setQuery(q);
		    QueryResponse response = server.query(query);

		    SolrDocumentList results = response.getResults();
		    for (int i = 0; i < results.size(); ++i) {
		    	String word = (String) results.get(i).getFieldValue("word");
		    	rh.addValue(word);
		    }
		    rh.sortValue();
			rh.sumUp();
//			if(rh.key.equals("resumes.experience.title")){
//				for(String st: rh.distribute.keySet()){
//			    	System.out.println(st + ":" + (double)rh.distribute.get(st)/rh.sum);
//				}
//			}
			rhs.put(s, rh);
		}
		
//		return tmp;
	}
	
	public static void main(String[] args) throws IOException, SolrServerException {
		// TODO Auto-generated method stub
		new GUI();			
	}
	
	
	
}
