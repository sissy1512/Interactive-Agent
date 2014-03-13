package indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import AsterixDB.restAPI;
import agent.dictionary;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SolrjPopulator {
	HttpSolrServer server;
	
//	//Record the value distribution of Job data
//	public jobHistogram jh = new jobHistogram();
	private dictionary dict = new dictionary();
	
	//All data sets in AsterixDB
	private ArrayList<Dataset> datasets = new ArrayList<Dataset>();
	private ArrayList<String> prefix = new ArrayList<String>();
	private JsonParser jsonParser = new JsonParser();
	private static final String query = "use%20dataverse%20Metadata;for%20$l%20in%20dataset%20Dataset%20where%20$l.DataverseName%20!=%20'Metadata'%20return{\"verseName\":%20$l.DataverseName,%20\"setName\":%20$l.DatasetName}";
	//AsterixDB
	private restAPI res = new restAPI();
	
	public SolrjPopulator(HttpSolrServer httpServer) throws SolrServerException, IOException{
		buildDataSet();
		server = httpServer;
		server.deleteByQuery( "*:*" );
	}	
	
	public void index() throws SolrServerException, IOException{
		for(Dataset set: datasets){
			prefix = new ArrayList<String>();
			//prefix.add(set.dataverse);
			prefix.add(set.dataset);
			HashMap<String, Set<Entry<String, JsonElement>>> docs = set.docs;
			for(Entry<String, Set<Entry<String, JsonElement>>> doc : docs.entrySet()){
				String id = doc.getKey();
				if(set.dataset.equals("Jobs")){
					SolrInputDocument solrDoc = new SolrInputDocument();
				//solrDoc.addField("doc_id", id);  
					solrDoc.addField("id", id+set.dataset);
					solrDoc.addField("doc_id", id);
					proceedJobPath(prefix, doc.getValue(), solrDoc, set.dataset);
//				if(set.dataset.equals("Resumes")){
//					String text = "";
//					for(Entry<String, JsonElement> e: doc.getValue()){
//						text += e.getKey() + ":" + e.getValue().toString() + " ";
//					}
//					solrDoc.addField("full_text", text);
//					server.add(solrDoc);
//				} 
					System.out.println(solrDoc.toString());
					server.add(solrDoc);
				} else if(set.dataset.equals("Resumes")){
					proceedResumePath(prefix, doc.getValue(), id, set.dataset);
				}
			}
		}
		server.commit(); 
	}
	
	
	public void buildDataSet(){
		res.setInfoPath("query");
		String response = res.query(query);
		JsonParser jsonParser = new JsonParser();
		JsonArray dataverses = jsonParser.parse(new String(response))
		        .getAsJsonObject().getAsJsonArray("results");
		for (JsonElement result : dataverses) {
			JsonElement jse = jsonParser.parse(result.getAsString());
			//jse.getAsJsonObject().entrySet();
			String verseName = jse.getAsJsonObject().get("verseName").getAsString();
			String setName = jse.getAsJsonObject().get("setName").getAsString();
			Dataset set = new Dataset(verseName, setName);
			datasets.add(set);
		    //System.out.println(verseName + " " + setName);
		    addDocs(set);
		}
	}
	
	private void addDocs(Dataset set){
		String query = "use%20dataverse%20"+set.dataverse+";for%20$l%20in%20dataset%20"+set.dataset+"%20return%20$l";
		String response = res.query(query);
		JsonParser jsonParser = new JsonParser();
//		System.out.println(jsonParser.parse("{\"skills\": {{ \"Nanomaterials\", \"Nanoparticles\", \"MATLAB\" }}}"));
//		System.out.println(response);
		JsonArray docs = jsonParser.parse(response)
		        .getAsJsonObject().getAsJsonArray("results");
		for (JsonElement doc : docs) {
//			System.out.println(doc.getAsString());
			JsonElement jse = jsonParser.parse(doc.getAsString());
			String name = jse.getAsJsonObject().get("id").getAsString();
			Set<Map.Entry<String, JsonElement>> entries = jse.getAsJsonObject().entrySet();
	       // System.out.println(name);
	        set.docs.put(name, entries);   
		}
	}	
	
	private void proceedJobPath(ArrayList<String> prefix, Set<Entry<String, JsonElement>> entries, SolrInputDocument solrDoc, String dataSet) throws SolrServerException, IOException{
		for(Entry<String, JsonElement> field : entries){
//			SolrInputDocument solrDoc = new SolrInputDocument();
//			solrDoc.addField("doc_id", id);  

			String fieldName = field.getKey();
			prefix.add(fieldName);
			JsonElement fieldValue = field.getValue();
			if(fieldValue instanceof JsonObject){
				//System.out.println(fieldName);
				//System.out.println(fieldValue.toString());				
				JsonElement jse = jsonParser.parse(fieldValue.toString());
				Set<Map.Entry<String, JsonElement>> nextEntries = jse.getAsJsonObject().entrySet();
				proceedJobPath(prefix, nextEntries, solrDoc, dataSet);
//				prefix.remove(prefix.size()-1);
			} else if(fieldValue instanceof JsonArray){
				//System.out.println("Array:" + fieldName);
				for(JsonElement jse: fieldValue.getAsJsonArray()){
					if(jse instanceof JsonObject){
						Set<Map.Entry<String, JsonElement>> nextEntries = jse.getAsJsonObject().entrySet();
						proceedJobPath(prefix, nextEntries, solrDoc, dataSet);
					} else{
						StringBuilder sb = new StringBuilder();
						for(String s: prefix){
							sb.append(s);
							sb.append(".");
						}
						String path = sb.substring(0, sb.length()-1).toLowerCase();
						String[] words = fieldValue.toString().split(" ");
						if(words.length > dict.maxLen){
							ArrayList<String> re = dict.decomposeText(words);
							for(String s: re){
									solrDoc.addField(path, s.toLowerCase());
							}
						}
						else{
								solrDoc.addField(path, jse.toString().toLowerCase());
						}				
//						prefix.remove(prefix.size()-1);
//						server.add(solrDoc);
					}
				}
		//		JsonElement jse = jsonParser.parse(fieldValue.getAsJsonArray().get(0).toString());
				
//				prefix.remove(prefix.size()-1);
			} else{
				StringBuilder sb = new StringBuilder();
				for(String s: prefix){
					sb.append(s);
					sb.append(".");
				}
				String path = sb.substring(0, sb.length()-1).toLowerCase();
		//		System.out.println(path);
//				solrDoc.addField("id", id + path);
//				solrDoc.addField("text", fieldValue);
//				solrDoc.addField("path_prefix", path);
//				solrDoc.addField("path_substring", path);
//				solrDoc.addField("value", fieldValue);
//				solrDoc.addField("path", path);
				String[] words = fieldValue.toString().split(" ");
				if(words.length > dict.maxLen){
					ArrayList<String> re = dict.decomposeText(words);
					for(String s: re){
						solrDoc.addField(path, s.toLowerCase());
					}
				}
				else{
					solrDoc.addField(path, fieldValue.toString().toLowerCase());
				}				
				
			}
			prefix.remove(prefix.size()-1);
		}
	}
	
	
	private void proceedResumePath(ArrayList<String> prefix, Set<Entry<String, JsonElement>> entries, String id, String dataSet) throws SolrServerException, IOException{
		for(Entry<String, JsonElement> field : entries){
			String fieldName = field.getKey();
			prefix.add(fieldName);
			JsonElement fieldValue = field.getValue();
			if(fieldValue instanceof JsonObject){
				//System.out.println(fieldName);
				//System.out.println(fieldValue.toString());				
				JsonElement jse = jsonParser.parse(fieldValue.toString());
				Set<Map.Entry<String, JsonElement>> nextEntries = jse.getAsJsonObject().entrySet();
				proceedResumePath(prefix, nextEntries, id, dataSet);
				prefix.remove(prefix.size()-1);
			} else if(fieldValue instanceof JsonArray){
				//System.out.println("Array:" + fieldName);
				for(JsonElement jse: fieldValue.getAsJsonArray()){
					if(jse instanceof JsonObject){
						Set<Map.Entry<String, JsonElement>> nextEntries = jse.getAsJsonObject().entrySet();
						proceedResumePath(prefix, nextEntries, id, dataSet);
					} else{
						StringBuilder sb = new StringBuilder();
						for(String s: prefix){
							sb.append(s);
							sb.append(".");
						}
						String path = sb.substring(0, sb.length()-1).toLowerCase();
						String[] words = fieldValue.toString().split(" ");
						if(words.length > dict.maxLen){
							ArrayList<String> re = dict.decomposeText(words);
							for(String s: re){
									SolrInputDocument solrDoc = new SolrInputDocument();
									solrDoc.addField("id", id+path+s);  
									solrDoc.addField("doc_id", id); 
									solrDoc.addField("word",s.toLowerCase());
									solrDoc.addField("path",path);
									server.add(solrDoc);
									System.out.println(solrDoc.toString());
							}
						}
						else{
							SolrInputDocument solrDoc = new SolrInputDocument();
							solrDoc.addField("id", id+path+jse.toString());  
							solrDoc.addField("doc_id", id); 
							solrDoc.addField("word",jse.toString().toLowerCase());
							solrDoc.addField("path",path);
							server.add(solrDoc);
							System.out.println(solrDoc.toString());
						}				
//						prefix.remove(prefix.size()-1);
						
					}
				}
		//		JsonElement jse = jsonParser.parse(fieldValue.getAsJsonArray().get(0).toString());
				
				prefix.remove(prefix.size()-1);
			} else{
				StringBuilder sb = new StringBuilder();
				for(String s: prefix){
					sb.append(s);
					sb.append(".");
				}
				String path = sb.substring(0, sb.length()-1).toLowerCase();
				String[] words = fieldValue.toString().split(" ");
				if(words.length > dict.maxLen){
					ArrayList<String> re = dict.decomposeText(words);
					for(String s: re){
						SolrInputDocument solrDoc = new SolrInputDocument();
						solrDoc.addField("id", id+path+s);  
						solrDoc.addField("doc_id", id); 
						solrDoc.addField("word",s.toLowerCase());
						solrDoc.addField("path",path);
						server.add(solrDoc);
						System.out.println(solrDoc.toString());
					}
				}
				else{
					SolrInputDocument solrDoc = new SolrInputDocument();
					solrDoc.addField("id", id+path+fieldValue.toString());  
					solrDoc.addField("doc_id", id);  
					solrDoc.addField("word",fieldValue.toString().toLowerCase());
					solrDoc.addField("path",path);
					server.add(solrDoc);
					System.out.println(solrDoc.toString());
				}				
				prefix.remove(prefix.size()-1);
			}			
		}
	}
}
