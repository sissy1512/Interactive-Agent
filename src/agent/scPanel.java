/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package agent;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
/**
 *  Panel for setting similarity calculation args
 * @author student
 */
public class scPanel extends JPanel{
    String scp = "pic/scp0.png";
    ImageIcon icon = new ImageIcon(scp);
    int CFstate = 0;
    Color c = new Color(0,0,0,0);
    JButton confirm;
    String result[] = new String[3];
    JLabel vec = new JLabel();

    public scPanel()
    {
             this.setBounds(300, 290, icon.getIconWidth(), icon.getIconHeight());
             this.setLayout(null);
             String[] s = {"word vector", "concept vector", "word_concept vector","graph"};
             final JComboBox jc = new JComboBox(s);
             jc.setBounds(130,25, 120, 30);
             String [] ss = {"0.1","0.2","0.3","0.4","0.5","0.6","0.7","0.8","0.9"};
             final JComboBox jcb = new JComboBox(ss);
             jcb.setBounds(95, 90, 200, 30);
             vec.setBounds(140, 90, 200, 30);
             final JTextPane jt = new JTextPane();
             jt.setBounds(208, 95, 104, 20);
             jcb.setVisible(false);
             jt.setVisible(false);
             vec.setVisible(true);
             this.add(jcb);  //  down JComboBox
             this.add(vec);
             this.add(jt);     // JTextPane
             this.add(jc);    //  Up JComboBox
             

            ImageIcon iconCF = new ImageIcon( "pic/OK2.png");
            confirm = new JButton(){
                    protected void paintComponent(Graphics g) {
                        ImageIcon icon = new ImageIcon("pic/OK2.png");    
                        Image img = icon.getImage();
                        g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
                    }
            };

            confirm.setBounds(310, 50, iconCF.getIconWidth(), iconCF.getIconHeight());
            confirm.setOpaque(false);
            confirm.setBackground(c);
            confirm.setBorderPainted(false);
            confirm.addMouseListener(new MouseAdapter(){
                        public void mousePressed(MouseEvent e)
                        {                                                                       
                               
                                if(jc.getSelectedIndex() == 0)
                                {
                                	result[0] = "Cosine";
                                	result[1] = "word vector";
                                }
                                else if(jc.getSelectedIndex() == 1)
                                {
                                	result[0] = "Cosine";
                                	result[1] = "concept vector";
                                }
                                else if(jc.getSelectedIndex() == 2)
                                {
                                	result[0] = "Cosine";                               	
                                	result[1] = "word_concept vector";
                                	result[2] = (String)jcb.getSelectedItem();
                                }
                                else
                                {  	
                                	result[0] = "Simrank";   
                                	result[1] = jt.getText();
                                }
                               setVisible(false);         
                        }
             });


             jc.addPopupMenuListener(new PopupMenuListener(){
                public void popupMenuCanceled(PopupMenuEvent e){}
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                {
                        if(jc.getSelectedIndex() == 0 || jc.getSelectedIndex() == 1)
                        {
                            scp = "pic/scp0.png";
                            jcb.setVisible(false);
                            jt.setVisible(false);
                            vec.setVisible(true);
                            vec.setText("");
                            repaint();
                        }                     
                        else if(jc.getSelectedIndex() == 2)
                        {
                            scp = "pic/scp0.png";
                            jt.setVisible(false);
                            jcb.setVisible(true);
                            vec.setVisible(false);
                            repaint();
                        }
                        else
                        {
                            scp = "pic/scp1.png";                         
                            jt.setVisible(true);
                            jcb.setVisible(false);
                            vec.setVisible(false);
                            repaint();
                        }
                }
                 public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
      });

      this.add(confirm);
}

    protected void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon(scp);
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
     }

    public String[] re()
    {
        return result;
    }
}
