package agent;



import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import org.apache.solr.client.solrj.SolrServerException;

import AsterixDB.restAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Xixi Xiao
 */
public class MainPanel extends JPanel{
    
	//JLabel que = new JLabel(); 
	JTextArea que = new JTextArea();
    JTextPane ans = new JTextPane();
    JButton confirm = new JButton("confirm");
    JScrollPane spQue =new JScrollPane(que);
    JScrollPane spAns =new JScrollPane(ans);
    
    rightPanel rp = new rightPanel();
    Font fo1 = new Font("ErasITC", Font.BOLD, 16);
    Font fo2 = new Font("ErasITC", Font.BOLD, 16);
    Color c = new Color(0,0,0,0);
    agent ag;
	
    boolean isQuery = true;
    boolean isName = true;

	question question;
	String userName;

	int index = 0;
   	
//	HashMap<String, ArrayList<String>> answers = ag.interact();
//	profile p = new profile(answers);
//	HashMap<String, HashMap<String, ArrayList<String>>> results = p.combineItems(p.items);
//	String s = p.genereateProfile(results, 0);
//	System.out.print(s);
    
    public MainPanel()
    {
    	
        spQue.setBounds(50, 50, 300, 70);

       	que.setEditable(false);  
        que.setBounds(50, 50, 300, 70);
        que.setFont(fo1);
        que.setText("Please input a query:");

        spAns.setBounds(50, 140, 300, 50);
        ans.setBounds(50, 140, 300, 50);
        ans.setFont(fo2);
           
        confirm.setBounds(260, 210, 80, 40);
           
           
           
           confirm.addMouseListener(new MouseAdapter(){
        	   public void mousePressed(MouseEvent e)
        	   {
        		   if(isQuery){
        			   initialize(ans.getText());
        			   isQuery = false;
        			   que.setText("What is your name?");
        			   ans.setText("");
        			   //nextQuestion(); 
        			   printDocs();
        		   } else if(isName){
        			   userName = ans.getText();
        			   ArrayList<String> tmp = new ArrayList<String>();
        			   tmp.add(userName);
        			   ag.pairs.put("name", tmp);
        			   isName = false;
        			   nextQuestion(); 
        		   }
        		   else{       			   
        			   ag.onePass(ag.sortedPaths.get(index++), ans.getText());
        			   if(index == ag.sortedPaths.size()){
							try {
								ag.filterJob();
								rp.intermediate.setText(ag.tr.queryConditionsJob.q);
								profile p = new profile(ag.pairs);							
								p.writeProfileFile(userName, p.genereateProfile(p.combineItems(p.items), 1));
								
								
								
//								String output = "";
//						  	  	for(String s: ag.docs){
//						  	  		output += s + "\n";
//						  	  	}
						  	  	ans.setText("");
						  	  	printDocs();
								ag.restart();
								que.setText("Please input a query:");
								ans.setText("");
								isQuery = true;
								isName = true;
								index = 0;								
							} catch (SolrServerException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 						
        			   }else{
        				   nextQuestion(); 
        			   }
        		   }
        	   }
           });
           
           this.add(spQue);
           this.add(spAns);
           this.add(confirm);
           this.add(rp);
           
           try {
	       		ag = new agent();
	       	} catch (SolrServerException | IOException e1) {
	       		// TODO Auto-generated catch block
	       		e1.printStackTrace();
	       	}
           question = new question(ag.asked);

    }
    
    private void initialize(String q){
  	  	try {
			ag.initializeData(q);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//  	  	String output = "";
//  	  	for(String s: ag.docs){
//  	  		output += s + "\n";
//  	  	}
//  	  	ans.setText("");
//  	  	rp.result.setText(output);
  	  	rp.intermediate.setText(ag.tr.queryConditionsJob.q);  	  	
    }
    
    private void nextQuestion(){
    	String curQue = question.formQuestion(ag.sortedPaths.get(index));
    	que.setText(curQue);
    	ans.setText("");
    	rp.intermediate.setText(ag.sortedPaths.get(index));
    }
    
    private void printDocs(){
    	JsonParser jsonParser = new JsonParser();
    	restAPI res = new restAPI();
    	String output = "results: " + ag.docs.size() + "\n";
    	for(String s: ag.docs){
    		s = s.substring(0, s.length()-3);
    		String query = "use%20dataverse%20searchAgent;for%20$l%20in%20dataset%20Jobs%20where%20$l.\"id\"%20=%20" + "int16(\"" + s + "\")%20return%20$l";
    		res.setInfoPath("query");
    		String response = res.query(query);
    		JsonArray docs = jsonParser.parse(response)
    		        .getAsJsonObject().getAsJsonArray("results");
    		output += docs.get(0).getAsString() + "\n";    		
    	}
    	rp.result.setText(output);
    }
}
