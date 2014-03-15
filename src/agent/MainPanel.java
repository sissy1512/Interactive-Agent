package agent;



import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
/**
 *
 * @author Xixi Xiao
 */
public class MainPanel extends JPanel{
    
    public String fileFolder;               //File Path
    public String[] IDF;                           //IDF[0]:minIdf; IDF[1]:maxIdf;
    public String[] scMethod;           //scMethod[0]:cosine,simrank;  scMethod[1]:vec scMethod[2]:weight
    public String ndoc;                            //number of docs
    public String nclass;                          //number of class
    public String method;                 //classification or clusterer method
    public String output = System.getProperty("user.dir");

    public String para;         
    public String imgPath;

    JButton fp, it, sc;
    JButton confirm1;
    JButton OK;
    
    JLabel que = new JLabel("Please input a query:");  
    JTextPane ans = new JTextPane();
    JButton confirm = new JButton("confirm");
    
    rightPanel rp = new rightPanel();
    FileWriter fw;
    BufferedWriter bw;
    Font fo = new Font("ErasITC", Font.BOLD, 23);

//    JLabel pathLabel = new JLabel(); 
//    JLabel idfLabel = new JLabel();
//    JLabel simLabel = new JLabel();
    
    
    public MainPanel()
    {
 //           imgPath = "pic/background.jpg";
 //           Color c = new Color(0,0,0,0);
           // idf.setVisible(false);
            //sp.setVisible(false);
    	   //this.setVisible(true);
           
           
           //que.setVisible(true); 
           que.setBounds(50, 60, 300, 50);
           que.setFont(fo);
//           ans.setVisible(true);
           ans.setBounds(50, 140, 300, 50);
           
           confirm.setBounds(260, 210, 80, 40);
           
           confirm.addMouseListener(new MouseAdapter(){
        	   public void mousePressed(MouseEvent e)
        	   {
                     System.out.print("confirm!");
        	   }
           });
           
           this.add(que);
           this.add(ans);
           this.add(confirm);
           this.add(rp);
           
//            //File Path Button
////            ImageIcon iconFP = new ImageIcon("pic/FP.png");
////            fp = new JButton(){
////                    protected void paintComponent(Graphics g) {
////                    
////                        ImageIcon icon;
//// /*                       if(FPstate == 0)
////                             icon = new ImageIcon("pic/FP.png");
////                        else
////                             icon = new ImageIcon("pic/FPc.png"); */
////                        icon = new ImageIcon("pic/FP.png");
////                        Image img = icon.getImage();
////                        g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
////                        
////                    }
////            };
////            pathLabel.setBounds(30, 75, iconFP.getIconWidth()-10, 30);
//            fp.setBounds(16, 90, iconFP.getIconWidth(), iconFP.getIconHeight());
//            fp.setOpaque(false);
//            fp.setBackground(c);
//            fp.setBorderPainted(false);
//            fp.addMouseListener(new MouseAdapter(){
//                        public void mousePressed(MouseEvent e)
//                        {
//                                if(fp.isEnabled() == true)
//                                {
// //                                   FPstate = (FPstate + 1) % 2;
//                                    fp.repaint();
//                                    fileFolder = chooseFolder();
//                                    pathLabel.setText(fileFolder);
//                                }
//                        }
//                }
//            );
//
//            //Idf Threshold Button
//            ImageIcon iconIT = new ImageIcon("pic/IT.png");
//            it = new JButton(){
//                    protected void paintComponent(Graphics g) {
//                    ImageIcon icon;
// /*                       if(ITstate == 0)
//                             icon = new ImageIcon("pic/IT.png");
//                        else
//                             icon = new ImageIcon("pic/ITc.png");*/
//                    icon = new ImageIcon("pic/IT.png");
//                    Image img = icon.getImage();
//                    g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
//                     }
//            };
//            idfLabel.setBounds(210, 75, iconIT.getIconWidth(), 30);
//            it.setBounds(180, 90, iconIT.getIconWidth(), iconIT.getIconHeight());
//            it.setOpaque(false);
//            it.setBackground(c);
//            it.setBorderPainted(false);
//            it.addMouseListener(new MouseAdapter(){
//                        public void mousePressed(MouseEvent e)
//                        {
//                            if(it.isEnabled() == true)
//                            {
//                                idf.setVisible(true);
// //                               ITstate = (ITstate + 1) % 2;
//                                 it.repaint();
//                                 idf();       
//                                 
//                            }
//                        }
//                }
//             );
//             
//
//            
//            //Similarity Calculation Button
//            ImageIcon iconSC = new ImageIcon("pic/SC.png");
//            sc = new JButton(){
//                    protected void paintComponent(Graphics g) {
//                            ImageIcon icon;
// /*                           if(SCstate == 0)
//                                icon = new ImageIcon("pic/SC.png");
//                            else
//                                icon = new ImageIcon("pic/SCc.png");*/
//                            icon = new ImageIcon("pic/SC.png");
//                            Image img = icon.getImage();
//                            g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
//                     }
//            };
//            simLabel.setBounds(150, 205, iconSC.getIconWidth(), 30);
//            sc.setBounds(16, 220, iconSC.getIconWidth(), iconSC.getIconHeight());
//            sc.setOpaque(false);
//            sc.setBackground(c);
//            sc.setBorderPainted(false);
//            sc.addMouseListener(new MouseAdapter(){
//                        public void mousePressed(MouseEvent e)
//                        {
//                             if(sc.isEnabled() == true)
//                             {
//                                 sp.setVisible(true);
////                                 SCstate = (SCstate + 1) % 2;
//                               
//                                 sc.repaint();
//                                 sc();
//                                 
//                             }
//                        }
//                }
//             );
//            
//            prepare.addMouseListener(new MouseAdapter(){
//                public void mousePressed(MouseEvent e)
//                {
//                     if(prepare.isEnabled() == true)
//                     {
//                    	 	fp.setEnabled(false);
//                    	 	sc.setEnabled(false);
//                    	 	it.setEnabled(false);
//                    	 	prepare.setEnabled(false);
//                    	 	System.out.println("again");
//                            if(fileFolder != null && IDF != null)
//                            {                                                                       
//                                    new Thread(new Runnable() {
//                                        public void run() {                                       	
//                                        	try{                                                                                       
//                                        		confirm1.setEnabled(true);                       	   
//                                        		process.setVisible(true);
//                                        		WVToolExample wvt = new WVToolExample(fileFolder, output, process);
//                                        		wvt.setIDF(Integer.parseInt(IDF[0]), Integer.parseInt(IDF[1]));
//                                        		process.setText("������������������������");
//                                        		Thread.sleep(500);
//                                        		wvt.process();								                       	   
//                                        		process.setText("finish!");
//                                        		prepare.setEnabled(true);
//                                        		fp.setEnabled(true);
//                                        		sc.setEnabled(true);
//                                        		it.setEnabled(true);
//                                        		System.out.println("finish!");
//                                        	}
//                                        	catch(Exception e){e.printStackTrace();};
//                                         }
//                                     }).start();                           													
//                            }
//                            else
//                            {
//                                    JOptionPane.showMessageDialog( null,"������ !");
//                                    prepare.setEnabled(true);
//                            }
//                     	}
//                	}
//            	}
//            );
//
//            OK = new JButton("���������������");
//            OK.setBounds(160, 300, 100, 40);
//            OK.addMouseListener(new MouseAdapter(){
//                        public void mousePressed(MouseEvent e)
//                        {
//                             if(OK.isEnabled() == true)
//                             {
//                            	 	OK.setEnabled(false)  ;
//                            	 	fp.setEnabled(false);
//                            	 	sc.setEnabled(false);
//                            	 	it.setEnabled(false);
//                            	 	process.setVisible(true);
//                                    if(scMethod!= null)
//                                    {
//                                            /*if(scMethod[0] == "Cosine")
//                                                para += "\n���������������" + scMethod[1];
//                                            else
//                                                para += "\n������C: "+ scMethod[1];
////                                            new Thread(new Runnable() {                                               
////                                            	public void run() {                                              	
////                                            		try{
//////                                            			System.out.println(para);                                        
////                                            			rp.jtp.setText(para);
////                                            		}
////                                            		catch(Exception e){};
////                                            	}
////                                            }).start();  
//*/                                            
//                                            new Thread(new Runnable() {
//                                                public void run() {
//                                                	
//                                                	try{                                                                                   	   				                                                   
//                                                		if(scMethod[0] == "Cosine"){                                                    
//                                                			if(scMethod[1] =="word vector")
//                                                				a.vecPath = "wv.txt";
//                                                			else if(scMethod[1] =="concept vector")
//                                                				a.vecPath = "cv.txt";
//                                                			else{
//                                                				process.setText("������������������"); 
//                                                				new wordConcept(Double.parseDouble(scMethod[2]));
//                                                				a.vecPath = "wcv.txt";
//                                                			}                                                       
//                                                		process.setText("������������������������");                                                  
//                                                		new readData(a);                                                                                                              
//                                                		}
//                                                		else if(scMethod[0] == "Simrank"){
//                                                			SimRank test = new SimRank();
//                                                			test.LoadData("concept.txt", "dc.txt");                                               		
//                                                			process.setText("���������������������"); 
//                                                			test.SimRankIterator(Double.parseDouble(scMethod[1]));                                               		
//                                                			process.setText("������������������������"); 
//                                                			test.OutPut("MySimRankSimilarity.txt");
//                                                		}
//                                                		process.setText("finish!");
//                                                		OK.setEnabled(true);
//                                                		fp.setEnabled(true);
//                                                		sc.setEnabled(true);
//                                                		it.setEnabled(true);
//                                                		System.out.println("finish!");
//                                                	}
//                                                	catch(Exception e){e.printStackTrace();};
//                                                }
//                                             }).start();                           				
//                                    }
//                                    else
//                                    {
//                                            JOptionPane.showMessageDialog( null,"��������������� !");
//                                            OK.setEnabled(true);
////                                            OKstate = (OKstate + 1) % 2;
////                                            OK.repaint(); 
//                                    }
//                             }
//                        }
//                }
//            );
//
//             //confirm button
//             ImageIcon iconCF1 = new ImageIcon( "pic/confirm1.png");
//             confirm1 = new JButton(){
//                    protected void paintComponent(Graphics g) {
//                            ImageIcon icon;
//                            if(CFstate1 == 0)
//                                icon = new ImageIcon("pic/confirm1.png");
//                            else
//                                icon = new ImageIcon("pic/confirm1C.png");
//                        Image img = icon.getImage();
//                        g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
//                    }
//            };
//            confirm1.setBounds(335, 540, iconCF1.getIconWidth(), iconCF1.getIconHeight());
//            confirm1.setOpaque(false);
//            confirm1.setBackground(c);
//            confirm1.setBorderPainted(false);
//            confirm1.addMouseListener(new MouseAdapter(){
//                        public void mousePressed(MouseEvent e)
//                        {                          
//                            	 new Thread(new Runnable() {
//                                        public void run() {
//                                        	
//                                    try {  
//                                    	a.getOriginClass();
//                                    	String []temp = cc.re();                                    
//                                    	para = new String();
//                                    	System.out.println("start!");
//                                            ndoc = temp[0];
//                                            nclass = temp[1];
//                                            method = temp[2];
//                                            a.nclass = Integer.parseInt(nclass);
//                                            a.count = Integer.parseInt(ndoc);
//                                            if(method == "Hierarchical Clustering"){
//                                            	                                             	  	
//                                                	process.setText("������������");
//													Thread.sleep(500);                                           	                                            
//													try{														
//														new HierClustering.Cluster(a);
//													}
//													catch(Exception e){
//														JOptionPane.showMessageDialog( null,"������������ !");
//													}
//													para = "            ������\t";
//													for(int i = 0; i < a.nclass; ++i)
//														para += i + "\t";
//													para +="\n������\n\n";
//													for(int i =0; i < a.nclass; ++i){
//														para += "  " + i + "\t";
//														for(int j = 0; j < a.nclass; ++j){
//															para += Integer.toString(a.result[i][j]) + "\t";
//														}
//														para += "\n\n";
//													}	
//													para += "\n\n" + "���������:"
//                                                    	+ "\nprecision:\t" + a.pre
//                                                    	+"\npurity:\t" + a.pur
//                                                    	+ "\nentropy:\t" + a.e
//                                                    	+ "\nF_measure:\t" + a.f
//                                                    	+ "\nnmi:\t" + a.nmi;
//													para += "\n\n";
//													rp.jtp.setText(para);
//													process.setText("finish!");
//                                            }
//                                            else if(method == "KNN"){
//                                            	KnnAlglorith knn = new KnnAlglorith();
//                                        		knn.Initial(Integer.parseInt(ndoc), Integer.parseInt(nclass));
//                                        		knn.KnnCore(15);
//                                        		knn.CatagoryEvaluate();
//                                        		para += "\n\n" + "���������:"
//                                                + "\nprecision:\t" + knn.precision
//                                                +"\nrecall:\t" + knn.recall                                              
//                                                + "\nF_measure:\t" + knn.F_measure;
//                                        		//para += "\n\n" + para2;
//												rp.jtp.setText(para);
//												process.setText("finish!");
//                                            }                                                          
//                                    } catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//                                	
//                                        }
//                                    }).start(); 
//                             }
//                }
//            );
//
//            
//            this.add(pathLabel);
//            this.add(idfLabel);
//            this.add(simLabel);
//            this.add(prepare);
//            this.add(fp);
//            this.add(it);
//            this.add(sc);
//            this.add(OK);
//            this.add(idf);
//            this.add(sp);
//            this.add(cc);
//            this.add(rp);
//            this.add(confirm1);
//            this.add(process);
//    }
//
//    private String chooseFolder()
//    {
//            String folderPath = null;
//            JFileChooser jfc = new JFileChooser();
//            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
//                public boolean accept(File f) {
//                         return true;
//                }
//                public String getDescription() {
//                        return null;
//                }
//            }
//        );
//        int returnVal = jfc.showOpenDialog(null);
//        if(returnVal == JFileChooser.APPROVE_OPTION) {
//           folderPath = jfc.getSelectedFile().getPath();
//        }
////        FPstate = (FPstate + 1) % 2;
//        return folderPath;
//    }
//    
//    private void idf()
//    {      
//        fp.setEnabled(false);
//        it.setEnabled(false);
//        sc.setEnabled(false);
//        cc.setEnabled(false);
//
//        idf.addComponentListener(new ComponentAdapter(){
//                    public void componentHidden(ComponentEvent e)
//                    {
// //                           ITstate = (ITstate + 1) % 2;
//                            it.repaint();
//                            fp.setEnabled(true);
//                            it.setEnabled(true);
//                            sc.setEnabled(true);       
//                            cc.setEnabled(true);
//                            IDF = idf.re();
//                            idfLabel.setText("idf������������" + IDF[0] + ",������������" + IDF[1]);
//                    }
//        });     
//    }
//
//    private void sc()
//    {      
//        fp.setEnabled(false);
//        it.setEnabled(false);
//        sc.setEnabled(false);
//        cc.setEnabled(false);
//
//        sp.addComponentListener(new ComponentAdapter(){
//                    public void componentHidden(ComponentEvent e)
//                    {
////                            SCstate = (SCstate + 1) % 2;
//                            sc.repaint();
//                            fp.setEnabled(true);
//                            it.setEnabled(true);
//                            sc.setEnabled(true);
//                            cc.setEnabled(true);
//                            scMethod =sp.re();
//                            simLabel.setText("���" + scMethod[0] +"���������������");
//                    }
//        });
//    }
//
//    protected void paintComponent(Graphics g) {
//             ImageIcon icon = new ImageIcon(imgPath);
//             Image img = icon.getImage();
//             g.drawImage(img, 0, 0, 1000, 780, this);
//             if(idf != null && idf.isVisible())
//             {
//                 idf.repaint();
//             }
//    }

    }
}
