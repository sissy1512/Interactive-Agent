package agent;

import java.util.HashMap;
import java.util.HashSet;

public class question {
//	public String[] paths;
	public HashMap<String, String> asked = new HashMap<String, String>();
	
    public question(HashMap<String,String> asked){
    	this.asked = asked;
//		this.paths = new String[paths.length];
//		for(int i = 0; i < paths.length; ++i)
//			this.paths[i] = paths[i].substring(12);
	}
	
	
	public String formQuestion(String path){
		if(asked.containsKey(path)){			
			return "";
		}
		String[] nouns = path.split("\\.");
		String question = "";
		int startIndex = 1;
		if(nouns.length == (startIndex + 1)){
			question = "What is your " + nouns[startIndex] + " ?";
		} else if(nouns.length == (startIndex + 2)){
			question += "Could you tell me more about your " + nouns[startIndex] + ",";
			question += " what is the " + nouns[startIndex+1] + "?";
		} else if(nouns.length == (startIndex + 3)){
			question += "Could you tell me more about your " + nouns[startIndex] + ",";
			question += " about your " + nouns[startIndex+1] + ",";
			question += " What is the " + nouns[startIndex+2] + "?";
		}
		return question;
	}	
}
