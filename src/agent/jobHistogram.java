package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class jobHistogram extends histogram{
	//Record the corresponding paths for each value
	HashMap<String, pathHistogram> valuePath = new HashMap<String, pathHistogram>();
	double threshold = 0.02;
	public void addValue(String word, String path){
		
		if(distribute.containsKey(word)){
			int count  = distribute.get(word);
			distribute.put(word, count+1);
			pathHistogram ph = valuePath.get(word);
			if(ph.distribute.containsKey(path)){
				int c = ph.distribute.get(path);
				ph.distribute.put(path, c+1);
			}else{
				ph.distribute.put(path, 1);
			}
//			if(!valuePath.get(word).contains(path))
//				valuePath.get(word).add(path);
		} else{
			distribute.put(word, 1);
			pathHistogram ph = new pathHistogram(word, server);
			HashSet<String> tmp = new HashSet<String>();
			tmp.add(path);
			valuePath.put(word, tmp);
		}
	}
	
	public ArrayList<String> getTopWords(){
		ArrayList<String> words = new ArrayList<String>();
		//TODO: Find top k words
		sumUp();
		for(Entry<String, Integer> e: distribute.entrySet()){
			if((double)e.getValue()/sum > threshold){
				words.add(e.getKey());
				//System.out.println(e.getKey() + ":" + (double)e.getValue()/sum);
			}
		}
		return words;
	}
}
