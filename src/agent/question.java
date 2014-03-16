package agent;

import java.util.HashMap;
import java.util.HashSet;

public class question {
//	public String[] paths;
	public HashMap<String, String> asked = new HashMap<String, String>();
	
    public question(HashMap<String,String> asked){
    	this.asked = asked;
    	
	}
	
	
	public String formQuestion(String path){
		if(asked.containsKey(path)){			
			return "";
		}
		String[] nouns = path.split("\\.");
		String question = "";
		int startIndex = 1;
		if(nouns.length == (startIndex + 1)){
			question = "What " + nouns[startIndex] + " do you have?";
		} else if(nouns.length == (startIndex + 2)){
			question += "Could you tell me more about your " + nouns[startIndex] + ",";
			switch(nouns[startIndex]){
				case "location": question += " what " + nouns[startIndex+1] + "are you living in?"; break;
				case "education": {
					switch(nouns[startIndex+1]){
						case "univ_name": question += " what university did you attend?"; break;
						case "degree" : question += " what university did you get?"; break;
						default: question += " what is your " + nouns[startIndex+1] + " ?"; break;
					}
					break;
				}
				case "experience": {
					switch(nouns[startIndex+1]){
						case "organization_name": 
					}
				}
			}
			question += " what is the " + nouns[startIndex+1] + "?";
		} else if(nouns.length == (startIndex + 3)){
			question += "Could you tell me more about your " + nouns[startIndex] + ",";
			question += " about your " + nouns[startIndex+1] + ",";
			question += " What is the " + nouns[startIndex+2] + "?";
		}
		return question;
	}	
}
