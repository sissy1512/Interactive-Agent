package agent;



import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

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

	question question;

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
        			   nextQuestion(); 
        		   }
        		   else{       			   
        			   ag.onePass(ag.sortedPaths.get(index++), ans.getText());
        			   if(index == ag.sortedPaths.size()){
							try {
								ag.filterJob();
								rp.intermediate.setText(ag.tr.queryConditionsJob.q);
								String output = "";
						  	  	for(String s: ag.docs){
						  	  		output += s + "\n";
						  	  	}
						  	  	ans.setText("");
						  	  	rp.result.setText(output);
								ag.restart();
								que.setText("Please input a query:");
								ans.setText("");
								isQuery = true;
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
  	  	String output = "";
  	  	for(String s: ag.docs){
  	  		output += s + "\n";
  	  	}
  	  	ans.setText("");
  	  	rp.result.setText(output);
  	  	rp.intermediate.setText(ag.tr.queryConditionsJob.q);  	  	
    }
    
    private void nextQuestion(){
    	String curQue = question.formQuestion(ag.sortedPaths.get(index));
    	que.setText(curQue);
    	ans.setText("");
    	rp.intermediate.setText(ag.sortedPaths.get(index));
    }
}
