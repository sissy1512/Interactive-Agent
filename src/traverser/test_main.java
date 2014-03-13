package traverser;

import indexer.SolrjPopulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import parser.ASTStart;
import parser.QueryParser;

public class test_main {

	public static void main(String[] args) throws SolrServerException, IOException {
		// TODO Auto-generated method stub
		HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
		SolrjPopulator pop = new SolrjPopulator(server);
		pop.index();
		traverser tr = new traverser();
	//	String prefix = "";
	//	queryData data = new queryData();
	//	data.setPath(prefix);
		String data = "searchAgent.dataset";
		
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
			root.jjtAccept(tr, data);
//			for(condition q : tr.queryConditionsJob.queries){
//				System.out.print("Path: searchAgent.Jobs" + q.getPath());
//				System.out.println(", Value: " + q.getValue());			
//			}
//			for(condition q : tr.queryConditionsResume.queries){
//				System.out.print("Path: searchAgent.Resumes" + q.getPath());
//				System.out.println(", Value: " + q.getValue());			
//			}
			
//			String q1 = tr.queryConditionsJob.formQuery(tr.queryConditionsJob.queries, "Jobs");
//			System.out.println(q1);
//			String q2 = tr.queryConditionsResume.formQuery(tr.queryConditionsResume.queries, "Resumes");
//			System.out.println(q2);		
			tr.queryConditionsJob.query(tr.queryConditionsJob.queries, "Jobs", server);
			tr.queryConditionsResume.query(tr.queryConditionsJob.queries, "Resumes", server);
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
