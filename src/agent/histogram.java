package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class histogram {
	//The number of occurrences for each value
	public HashMap<String, Integer> distribute = new HashMap<String, Integer>();
	//Order the values according to their occurrences
	public String[] sortedValue;
	//Total occurrences
	int sum = 0;

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
}
