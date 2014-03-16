package agent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class dictionary {
	HashSet<String> dic = new HashSet<String>();
	public int maxLen = 4;
	BufferedReader br;
	
	public dictionary(){
		String filePath = new File("").getAbsolutePath() + "/src/dic.txt";
		try {
			br = new BufferedReader(new FileReader(filePath));		
			String curLine;
			while((curLine = br.readLine()) != null){
				dic.add(curLine.toLowerCase());
			}
			br.close();			
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
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
	
	public static void main(String[] args){
		//ArrayList<String> re = new ArrayList<String>();
		dictionary diction = new dictionary();
//		String[] words = "software development with \"java\" and c++, or python software engineer".replace(',', ' ').replace('\"', ' ').split(" ");
//		re = diction.decomposeText(words);
//		for(String s: re){
//			System.out.println(s);
//		}
	}
}
