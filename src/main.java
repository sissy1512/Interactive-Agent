import indexer.Query;
import indexer.SolrJSearcher;
import indexer.SolrjPopulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import agent.agent;
import agent.profile;
import agent.question;
import parser.ASTStart;
import parser.QueryParser;
import traverser.queryData;
import traverser.traverser;


public class main {

	public static void main(String[] args) throws IOException, SolrServerException {
		// TODO Auto-generated method stub
		HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
//		Query query = new Query();
//		query.addCondition("text", "Software Engineer", "AND");
//		query.addCondition("path_substring", "experience", "OR");
//		query.addCondition("path_substring", "Jobs", "AND");
		String q = "word:\"yang liu\"";
//		String q = query.toQueryString();

//		String q = "path_prefix:searchAgent.Resumes AND (path_substring:summary OR path_substring:location)";
		SolrjPopulator pop = new SolrjPopulator(server);
		pop.index();
		SolrJSearcher searcher = new SolrJSearcher(server);
		QueryResponse response = searcher.search(q);
		
	    SolrDocumentList results = response.getResults();
	    System.out.println("results:" +  results.size());
	    for (int i = 0; i < results.size(); ++i) {
	      System.out.println(results.get(i));
	    }
//		String query = "use%20dataverse%20Metadata;for%20$l%20in%20dataset%20Dataset%20where%20$l.DataverseName%20!=%20'Metadata'%20return{\"verseName\":%20$l.DataverseName,%20\"setName\":%20$l.DatasetName}";
//		String query = "http://localhost:19002/ddl?ddl=drop%20dataverse%20company%20if%20exists;create%20dataverse%20company;use%20dataverse%20company;create%20type%20Emp%20as%20open%20{id%20:%20int32,name%20:%20string};create%20dataset%20Employee(Emp)%20primary%20key%20id;";
//		restAPI rest = new restAPI(query);
		
		
//		HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
//		SolrjPopulator pop = new SolrjPopulator(server);
//		traverser tr = new traverser();
//		
//	//	String prefix = "";
//	//	queryData data = new queryData();
//	//	data.setPath(prefix);
//		pop.index();
//				
//		String fileName;
//		if (args.length == 1) {
//			fileName = args[0];
//		} else {
//			fileName = "/Users/xixi/Documents/workspace/TestSolr/src/parser/test.txt";
//		}
//		try {
//			QueryParser t = new QueryParser(new FileInputStream(new File(fileName)));
//			ASTStart root = t.Start();
//			root.dump(">");
//			String data = "searchAgent.dataset";
//			root.jjtAccept(tr, data);	
//			HashSet<String> docs1 = tr.queryConditionsJob.query(tr.queryConditionsJob.queries, "Jobs", server);
////			HashSet<String> docs2 = tr.queryConditionsResume.query(tr.queryConditionsResume.queries, "Resumes", server);
//			HashSet<String> docs2 = tr.queryConditionsResume.query(tr.queryConditionsResume.keywords, server);
//			
//			//Jobs
//			agent jobAgent = new agent(server,tr.asked);
//			jobAgent.freqAttr(docs1);
//			jobAgent.sortAttr();
//			question que = new question(jobAgent.rankedAttr,tr.asked);
//			docs1 = jobAgent.jobFilter(docs1, que);
			
//			agent ag = new agent(server);
//			docs1.addAll(docs2);
//			ag.freqAttr(docs1);
//			ag.sortAttr();
//			for(String s: ag.rankedAttr){
//				System.out.println(s + ":" + ag.attributes.get(s));
//			}
//			que = new question(ag.rankedAttr);
//			profile pro = new profile(ag.interact(que));
//			HashMap<String, HashMap<String, String>> results = pro.combineItems(pro.items);
//			String s = pro.genereateProfile(results, 0);
//			System.out.print(s);
//			System.out.print("over");
//		} catch (Exception e) {
//			System.out.println("Oops.");
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}	
	}

}
