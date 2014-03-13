package indexer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Query {
	public HashMap<String, ArrayList<String>> conditions = new HashMap<String, ArrayList<String>>();
	//connect: could be AND/OR
	public void addCondition(String field, String value, String connect){
		value = connect + " " + value;
		if(conditions.containsKey(field)){
			ArrayList<String> arr = conditions.get(field);
			arr.add(value);
		} else{
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(value);
			conditions.put(field, arr);
		}
	}
	
	public String toQueryString(){
		String query = "";
		for(Entry<String, ArrayList<String>> con : conditions.entrySet()){
			if(con.getValue().size() == 1){
				String value = con.getValue().get(0);
				int index = value.indexOf(" ");
				String connect = value.substring(0, index);
				value = value.substring(index+1);
				query += " " + connect + " " + con.getKey() + ":" + value;				
			} else{				
				for(String value : con.getValue()){
					int index = value.indexOf(" ");
					String connect = value.substring(0, index);
					value = value.substring(index+1);
					query += " " + connect + " " + con.getKey() + ":" + value;	
				}
			}
		}
		if(query.startsWith(" AND ")){
			return query.substring(5);
		}else{
			int index = query.indexOf(" AND ");
			if( index != -1){
				String first = query.substring(0,index);
				String second = query.substring(index+5);
				index = second.indexOf(" ");
				if(index == -1)
					return second + first;
				else
					return second.substring(0,index) + first + second.substring(index);
			} else{
				return query.substring(4);
			}
		}
	}
}
