package agent;

import indexer.SolrjPopulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

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
	public jobHistogram jh = new jobHistogram();
	public HashMap<String, resumeHistogram> rhs = new HashMap<String,resumeHistogram>();
	public ArrayList<String> sortedPaths = new ArrayList<String>();
	static SolrjPopulator pop;
	static traverser tr;	
	double threshold = 0.01;
	
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
    
//	public void freqAttr(HashSet<String> candidateDocs) throws SolrServerException{
//		for(String s: candidateDocs){
//			String q = "doc_id:" + s;	
//			query.setQuery(q);
//			response = server.query(query);
//			
//			SolrDocumentList results = response.getResults();			
//		    for (int i = 0; i < results.size(); ++i) {
//		    	for(Entry<String, Object> e: results.get(i).entrySet()){
////		    		String path = (String) results.get(i).getFieldValue("path");
//		    		String path = e.getKey();
//			    	if(attributes.containsKey(path)){
//			    		int count = attributes.get(path);
//			    		attributes.put(path, count+1);
//			    	} else{
//			    		attributes.put(path, 1);
//			    	}
//		    	}
//		    	
//		    }	
//		}
//	}
//	
//	public void sortAttr(){
//		int size = attributes.size();
//		ArrayList<String> tmp = new ArrayList<String>();
//		for(Entry<String, Integer> e: attributes.entrySet()){
//			int curCount = e.getValue();
//			String curPath = e.getKey();
//			size = tmp.size();
//			int i;
//			for(i = 0; i < size; ++i){
//				if(curCount > attributes.get(tmp.get(i))){
//					tmp.add(i, curPath);
//					break;
//				}
//			}
//			if(i == size){
//				tmp.add(curPath);
//			}
//		}
//		size = attributes.size();
//		rankedAttr = new String[size];
//		for(int i = 0; i < size; ++i){
//			rankedAttr[i] = tmp.get(i);
//		}
//	}
	
	public HashMap<String, ArrayList<String>> interact() throws SolrServerException{
		//questions and answers
		HashMap<String, ArrayList<String>> pairs = new HashMap<String, ArrayList<String>>();
		question que = new question(asked);
		HashSet<String> answerWords = new HashSet<String>();
		
		for(String path: sortedPaths){
			String curQue = que.formQuestion(path);
			System.out.println(curQue);
			try{
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    String ans = bufferRead.readLine().toLowerCase();
			    String[] words = ans.split(",");
			    int len = words.length;
			    resumeHistogram rh = rhs.get(path);
			  //drop suffix Resumes.
			    path = path.substring(8);
			    for(int i = 0; i < len; ++i){
			    	words[i] = "\"" + words[i] + "\"";
			    	answerWords.add(words[i]);
			    	if(rh.distribute.containsKey(words[i]) && (double)rh.distribute.get(words[i])/rh.sum > threshold){
			    		if(pairs.containsKey(path)){
			    			pairs.get(path).add(words[i]);
			    		}else{
			    			ArrayList<String> tmp = new ArrayList<String>();
			    			tmp.add(words[i]);
			    			pairs.put(path, tmp);
			    		}
			    	}
			    }
			    
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		filterJob(answerWords);
		return pairs;
	}
	
	private void filterJob(HashSet<String> words) throws SolrServerException{
	//	ArrayList<condition> cons = new ArrayList<condition>();
		for(Entry<String, HashSet<String>> e: jh.valuePath.entrySet()){
			if(words.contains(e.getKey())){
				for(String s: e.getValue()){
					if(!asked.containsKey(s)){
						condition con = new condition(s, e.getKey());
						tr.queryConditionsJob.queries.add(con);
					}
				}
			}
		}
		docs = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, "jobs", server);
	}
	
	//read and parse input query, decide whether to start the interactive action or not, construct job and resume histograms
	//return sorted resume paths(to form question)
	public void initializeData(String[] args) throws SolrServerException{
		
		//Parse input user query
		String fileName;
		if (args.length == 1) {
			fileName = args[0];
		} else {
			fileName = "/Users/xixi/Documents/workspace/TestSolr/src/parser/test.txt";
		}
		try {
			QueryParser t = new QueryParser(new FileInputStream(new File(fileName)));
			ASTStart root = t.Start();
			root.dump(">");
			String data = "dataset";
			root.jjtAccept(tr, data);	
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//Decide whether to start the interactive action or not
		docs = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, "Jobs".toLowerCase(), server);		
		//Too few result
		if(docs.size() < 3){
			ArrayList<condition> newQuery = new ArrayList<condition>();
			for(condition con: tr.queryConditionsJob.queries){
				if(!con.getPath().equals("Jobs.title")){
					newQuery.add(con);
				}
			}
			tr.queryConditionsJob.queries = newQuery;
			docs = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, "Jobs".toLowerCase(), server);
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
		    for (int i = 0; i < results.size(); ++i) {
		    	for(Entry<String, Object> e: results.get(i).entrySet()){
		    		String path = (String) e.getKey();
		    		if(path.equals("jobs.id") || path.equals("doc_id") || path.equals("id"))
		    			continue; 		
		    		if(e.getValue() instanceof ArrayList<?>){
		    			ArrayList<String> values = (ArrayList<String>) e.getValue();
			    		for(String cur: values){
			    			jh.addValue(cur, path);
			    		}
		    		}else if(e.getValue() instanceof String){
		    			jh.addValue((String)e.getValue(), path);
		    		}
		    		
		    	}	    	
		    }	
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
//		ArrayList<resumeHistogram> rhs = new ArrayList<resumeHistogram>();
		HashMap<String, Integer> paths = new HashMap<String, Integer>();
		
		//Get all related paths and their occurrences(one path may corresponds to several words)
		for(String word : words){
			String q = "word:" + word;	
			query.setQuery(q);
			response = server.query(query);
			
			SolrDocumentList results = response.getResults();			
		    for (int i = 0; i < results.size(); ++i) {
//		    	for(Entry<String, Object> e: results.get(i).entrySet()){
//		    		if(e.getKey().equals("path")){
//			    		String path = (String) e.getValue();
//				    	if(paths.containsKey(path)){
//				    		int count = paths.get(path);
//				    		paths.put(path, count+1);
//				    	} else
//				    		paths.put(path, 1);
//		    		}
//		    	}
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
//		ArrayList<String> tmp = new ArrayList<String>();
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
			resumeHistogram rh = new resumeHistogram(s, server);
			rh.sumUp();
			rhs.put(s, rh);
		}
		
//		return tmp;
	}
	
	public static void main(String[] args) throws IOException, SolrServerException {
		// TODO Auto-generated method stub
		agent ag = new agent();
		ag.initializeData(args);	
		HashMap<String, ArrayList<String>> answers = ag.interact();
		profile p = new profile(answers);
		HashMap<String, HashMap<String, ArrayList<String>>> results = p.combineItems(p.items);
		String s = p.genereateProfile(results, 0);
		System.out.print(s);
		
		
		
		//		HashSet<String> docs2 = tr.queryConditionsResume.query(tr.queryConditionsResume.queries, "Resumes", server);
//		HashSet<String> docs2 = tr.queryConditionsResume.query(tr.queryConditionsResume.keywords, server);
//		
//		//Jobs
//		agent jobAgent = new agent(server,tr.asked);
//		jobAgent.freqAttr(docs1);
//		jobAgent.sortAttr();
//		question que = new question(jobAgent.rankedAttr,tr.asked);
//		docs1 = jobAgent.jobFilter(docs1, que);
	}
	
	
	
}