package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class profile {
	public HashMap<String, ArrayList<String>> items = new HashMap<String, ArrayList<String>>();
	public profile(HashMap<String,ArrayList<String>> items){
		this.items = items;
	}
	
	public HashMap<String, HashMap<String, ArrayList<String>>> combineItems(HashMap<String, ArrayList<String>> pairs){
		HashMap<String, HashMap<String, ArrayList<String>>> results = new HashMap<String, HashMap<String, ArrayList<String>>>();
		for(Entry<String, ArrayList<String>> e: pairs.entrySet()){
			String first, sec;
			int index = e.getKey().indexOf(".");
			if(index != -1){
				first = e.getKey().substring(0, index);
				sec = e.getKey().substring(index+1);
			}else{
				first = e.getKey();
				sec = "";
			}
			if(results.containsKey(first)){
				HashMap<String, ArrayList<String>> tmp = results.get(first);
				tmp.put(sec, e.getValue());
			}else{
				HashMap<String, ArrayList<String>> tmp = new HashMap<String, ArrayList<String>>();
				tmp.put(sec, e.getValue());
				results.put(first, tmp);
			}
		}
		return results;
	}
	
	public String genereateProfile(HashMap<String, HashMap<String, ArrayList<String>>> pairs, int level){
		String profile = "";
		for(Entry<String, HashMap<String, ArrayList<String>>> e: pairs.entrySet()){
			for(int i = 0; i < level; ++i){
				profile += "\t";
			}
			profile += e.getKey() + "\t";
			HashMap<String, ArrayList<String>> tmp = e.getValue();
			if(tmp.size() == 1){
				for(Entry<String,ArrayList<String>> en: tmp.entrySet()){
					if(en.getKey().equals("")){
						String value = "";
						for(String s: en.getValue())
							value += s.substring(1,s.length()-1) + ", ";
						value = value.substring(0, value.length() - 2);
						profile += en.getKey() + "\t" + value + "\n";
					} else{
						profile += "\n";
						HashMap<String, HashMap<String, ArrayList<String>>> results = combineItems(tmp);
						profile += genereateProfile(results, level+1);
					}
				}
			}else{
				profile += "\n";
				HashMap<String, HashMap<String, ArrayList<String>>> results = combineItems(tmp);
				profile += genereateProfile(results, level+1);
			}
		}
		return profile;
	}
	
	public static void main(String[] args){
//		profile p = new profile();
//		p.items.put("requirement.experience.year", "3");
//		p.items.put("requirement.minimum_degree", "BS");
//		p.items.put("location", "SF");
//		p.items.put("company_name", "LinkedIn");
//		p.items.put("discipline", "CS");
//		p.items.put("requirement.experience.area", "SD");
//		p.items.put("title", "software engineer");
//		p.items.put("compensation", "$50/hour");
//		p.items.put("description", "haha");
//		HashMap<String, HashMap<String, String>> results = p.combineItems(p.items);
//		String s = p.genereateProfile(results, 1);
//		System.out.print(s);
	}
}
