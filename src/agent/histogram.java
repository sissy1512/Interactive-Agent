package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class histogram {
		String key;
		//The number of occurrences for each value
		public HashMap<String, Integer> distribute = new HashMap<String, Integer>();
		//Order the values according to their occurrences
		public String[] sortedValue;
		//Total occurrences
		int sum = 0;
		
		double threshold = 0.02;

		public histogram(String key){
			this.key = key;
		}
		
		public void addValue(String word){
			
			if(distribute.containsKey(word)){
				int count  = distribute.get(word);
				distribute.put(word, count+1);
			} else{
				distribute.put(word, 1);
			}
		}
		
		public void sortValue(){
			int size = distribute.size();
			ArrayList<String> tmp = new ArrayList<String>();
			for(Entry<String, Integer> e: distribute.entrySet()){
				int curCount = e.getValue();
				String curPath = e.getKey();
				size = tmp.size();
				int i;
				for(i = 0; i < size; ++i){
					if(curCount > distribute.get(tmp.get(i))){
						tmp.add(i, curPath);
						break;
					}
				}
				if(i == size){
					tmp.add(curPath);
				}
			}
			size = distribute.size();
			sortedValue = new String[size];
			for(int i = 0; i < size; ++i){
				sortedValue[i] = tmp.get(i);
			}
		}
		
		public void sumUp(){
			sum = 0;
			for(Entry<String, Integer> e: distribute.entrySet()){
				sum += e.getValue();
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
