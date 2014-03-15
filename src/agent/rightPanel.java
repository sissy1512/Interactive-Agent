/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *  the right panel
 * @author student
 */
package agent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
/**
 *  the right panel
 * @author student
 */
public class rightPanel extends JTabbedPane{
//        String imgPath = "pic/right.png";
//        ImageIcon icon = new ImageIcon(imgPath);
       // ImageIcon tab1 = new ImageIcon("pic/tab1.png");
        Color c = new Color(0,0,0,0);
        Font f = new Font("ErasITC", Font.BOLD, 16);
        JTextArea intermediate = new JTextArea();
        JTextArea result = new JTextArea();
        
        

        public rightPanel()
        {
                this.setBounds(400, 30, 600, 300);
                JScrollPane sp1 =new JScrollPane(intermediate);
                intermediate.setBounds(0, 5, 550, 300);
                intermediate.setFont(f); 
                intermediate.setText("heihei\nxixi\nhaha\nhuhu\nwuwu\nyiyi\naa\noo\nyep!\n");
                result.setText("a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nyes!\n");
                JScrollPane sp2 =new JScrollPane(result);
                result.setBounds(0, 5, 550, 300);
                result.setFont(f); 
                this.addTab("intermediate", sp1);
                this.addTab("result", sp2);
        }

}
