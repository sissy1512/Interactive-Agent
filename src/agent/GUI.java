package agent;

import javax.swing.JFrame;

public class GUI {
	 JFrame mf = new JFrame();
	 public GUI(){
	     mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     mf.setBounds(100, 20, 1000, 360);
	     MainPanel mp= new MainPanel();
	     mp.setLayout(null);
	     mf.setContentPane(mp);
	     mf.setVisible(true);
	 }
}
