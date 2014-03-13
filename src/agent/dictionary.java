package agent;

import java.util.ArrayList;
import java.util.HashSet;

public class dictionary {
	HashSet<String> dic = new HashSet<String>();
	public int maxLen = 4;
	
	public dictionary(){
		dic.add("software engineer".toLowerCase());
		dic.add("JAVA".toLowerCase());
		dic.add("C++".toLowerCase());
		dic.add("Python".toLowerCase());
		dic.add("software development".toLowerCase());
		dic.add("software".toLowerCase());
		dic.add("Linux".toLowerCase());
		dic.add("JavaScript".toLowerCase());
	}
	
	//Extract meaningful words from long text(represented as words, split by " ")
	public ArrayList<String> decomposeText(String[] words){
		ArrayList<String> results = new ArrayList<String>();
	//	String[] words = text.split(" ");
		int size = words.length;
		for(int i = maxLen; i > 0; --i){
			for(int j = 0; j <= size - i; ++j){
				int k = 0;
				String tmp = "";
				while(k < i && !words[j+k].equals("")){
					if(k != 0)
						tmp += " ";
					tmp += words[j+k];
					k++;
				}
				if(k == i && dic.contains(tmp)){
					results.add(tmp);
					for(int m = 0; m < i; ++m){
						words[j+m] = "";
					}
				}
			}
		}
		return results;
	}
	
//	public static void main(String[] args){
//		ArrayList<String> re = new ArrayList<String>();
//		dictionary diction = new dictionary();
//		re = diction.decomposeText("software development with java and c++ or python software engineer");
//		for(String s: re){
//			System.out.println(s);
//		}
//	}
}
