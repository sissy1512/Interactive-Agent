package indexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;

public class Dataset {
	public String dataverse = null;
	public String dataset = null;
	public HashMap<String, Set<Map.Entry<String, JsonElement>>> docs = null;
	
	public Dataset(String verseName, String setName){
		dataverse = verseName;
		dataset = setName;
		docs = new HashMap<String, Set<Map.Entry<String, JsonElement>>>();
	}
	
	public void addDoc(String id, Set<Map.Entry<String, JsonElement>> entries){
		docs.put(id, entries);
	}
}
