//Levi Lewis
//Java Project #2
//CSCI 3381

package twitterProjectGUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

public class Main {
	private static boolean showTitleScreen;
	
	public static void main(String[] args) {
		showTitleScreen = true;
		JFrame frame = new JFrame("Sentiment Analysis"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(800, 500); 
		frame.getContentPane().add(new TwitterPanel());
   
		frame.pack(); 
		frame.setVisible(true);

	}
}
