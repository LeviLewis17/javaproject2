//Levi Lewis
//Java Project #2
//CSCI 3381

package twitterProjectGUI;

import java.awt.*; 
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

import sentimentanalysis.Tweet;
import sentimentanalysis.TweetCollection;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder; 
 
public class TwitterPanel extends JPanel { 
	private int delay;
	private TweetCollection tc;
	private JTextField username;
	private JComboBox comboBox;
	private Set<Long> tweetIDs;
	private TweetCollection tweetSubset;
	private JTextArea tweetData;
	private JTextField txtPredictionfield;

    public TwitterPanel() 
    { 
    	//creates a tweet collection object to be used in the gui
    	tc = new TweetCollection(".\\trainingProcessed.txt", 1600000);

    	//sets the size, background color, and layout of the panel
    	setPreferredSize (new Dimension(800, 500)); 
    	setBackground(Color.WHITE);
    	setLayout(null);
    	
    	//This code creates the label that accompanies the tweet collection text area
    	JLabel lblTweets = new JLabel("Tweets");
    	lblTweets.setFont(new Font("Stencil", Font.PLAIN, 18));
    	lblTweets.setBounds(80, 42, 101, 28);
    	add(lblTweets);
    	
    	//This code creates a scroll pane for the tweet collection text area
    	JScrollPane scrollPane = new JScrollPane();
    	scrollPane.setBounds(80, 80, 260, 286);
    	add(scrollPane);
    	
    	//This code creates the text area in which the tweet collection will be displayed
    	JTextArea textArea = new JTextArea();
    	textArea.setEditable(false); //This text area will be read-only
    	textArea.setText(tc.toString());
    	scrollPane.setViewportView(textArea); //Connects the text area to the scroll pane
    	
    	//Creates the label that accompanies the input text field that the user will
    	//use to enter a username
    	JLabel lblUsername = new JLabel("Enter Username");
    	lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
    	lblUsername.setBounds(471, 85, 110, 13);
    	add(lblUsername);
    	
    	//Creates the text field the user will use to enter a username for searching
    	username = new JTextField();
    	username.setBounds(471, 104, 177, 19);
    	add(username);
    	username.setColumns(10);
    	
    	//Creates a button that will generates a list of tweet IDs from the username
    	//that the user enters in the username text field
    	JButton btnGetTweetIds = new JButton("Get Tweet IDs");
    	btnGetTweetIds.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {  			
    			Set<Long> ids = tc.getTweetIdsByUser(username.getText());
    			//If the username entered does not return any tweet IDs, a dialog box
    			//will appear that notifies the user that the username could not be found
    			if (ids.isEmpty()) {
    				JOptionPane.showMessageDialog(btnGetTweetIds, "The username value you input could not be found.");
    				tweetData.setText("");
    				return;
    			}
    			
    			//If the user does not enter username value but presses the button, a
    			// dialog box will appear and notify the user of their error.
    			if (username.getText().isBlank()) {
    				JOptionPane.showMessageDialog(btnGetTweetIds, "You did not enter a username.");
    				tweetData.setText("");
    				return;
    			}
    				
    			String[] idsStr = new String[ids.size()];
    			int index = 0;
    			//creates a string array consisting of the IDs from the ids set
    			for (Long l : ids) {
    				idsStr[index] = l.toString();
    				index++;
    			}
    			//sets the drop-down list of IDs to contain all of the IDs in the set
    	    	comboBox.setModel(new DefaultComboBoxModel(idsStr));
    		}
    	});
    	btnGetTweetIds.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	btnGetTweetIds.setBounds(471, 133, 177, 21);
    	add(btnGetTweetIds);
    	
    	//Creates a drop-down list box to display the tweet IDs of the username that
    	//the user enters
    	comboBox = new JComboBox();
    	comboBox.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//Retrieves the ID that the user selects from the drop-down list
    			//and sets the tweetData text area to display the text of the
    			//tweet that corresponds to the ID
    			String id = (String)comboBox.getSelectedItem();
    			Tweet t = tc.getTweetById(Long.parseLong(id));
    			tweetData.setText(t.getText());
    		}
    	});
    	comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	comboBox.setBounds(471, 164, 177, 21);
    	add(comboBox);
    	
    	//Creates a button group for the radio buttons that will be created
    	ButtonGroup polarities = new ButtonGroup();
    	
    	//Creates the first radio button, which will be used to display every
    	//Tweet in the collection with a polarity of zero
    	JRadioButton rdbtnZero = new JRadioButton("0");
    	rdbtnZero.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			tweetSubset = new TweetCollection("", 1000000);
    			for (Long l : tc.getAllTweetIds()) {
    				if (tc.getTweetById(l).getPolarity() == 0)
    					tweetSubset.addTweet(tc.getTweetById(l));
    			}
    			
    			textArea.setText(tweetSubset.toString());
    		}
    	});
    	polarities.add(rdbtnZero); //adds the radio button to the button group
    	rdbtnZero.setBounds(80, 380, 36, 21);
    	add(rdbtnZero);
    	
    	//Creates the second radio button, which will be used to display every
    	//Tweet in the collection with a polarity of two
    	JRadioButton rdbtnTwo = new JRadioButton("2");
    	rdbtnTwo.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			tweetSubset = new TweetCollection("", 1000000);
    			for (Long l : tc.getAllTweetIds()) {
    				if (tc.getTweetById(l).getPolarity() == 2)
    					tweetSubset.addTweet(tc.getTweetById(l));
    			}
    			
    			textArea.setText(tweetSubset.toString());
    		}
    	});
    	polarities.add(rdbtnTwo); //adds the radio button to the button group
    	rdbtnTwo.setBounds(118, 380, 34, 21);
    	add(rdbtnTwo);
    	
    	//Creates the third radio button, which will be used to display every
    	//Tweet in the collection with a polarity of four
    	JRadioButton rdbtnFour = new JRadioButton("4");
    	rdbtnFour.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			tweetSubset = new TweetCollection("", 1000000);
    			for (Long l : tc.getAllTweetIds()) {
    				if (tc.getTweetById(l).getPolarity() == 4)
    					tweetSubset.addTweet(tc.getTweetById(l));
    			}
    			
    			textArea.setText(tweetSubset.toString());
    		}
    	});
    	polarities.add(rdbtnFour); //adds the radio button to the button group
    	rdbtnFour.setBounds(154, 380, 36, 21);
    	add(rdbtnFour);
    	
    	//Creates the final radio button, which will be used to display every
    	//Tweet in the collection
    	JRadioButton rdbtnAll = new JRadioButton("All");
    	rdbtnAll.setSelected(true);
    	rdbtnAll.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			textArea.setText(tc.toString());
    		}
    	});
    	polarities.add(rdbtnAll); //adds the radio button to the button group
    	rdbtnAll.setBounds(192, 380, 47, 21);
    	add(rdbtnAll);
    	
    	//Label that corresponds to the text area used to display some tweet by an
    	//input username that the user has chosen from the drop-down list
    	JLabel lblTweet = new JLabel("Tweet:");
    	lblTweet.setFont(new Font("Tahoma", Font.PLAIN, 14));
    	lblTweet.setBounds(395, 200, 45, 13);
    	add(lblTweet);
    	
    	//Creates a new panel that will be the environment for the tweetData
    	//text area
    	JPanel panel = new JPanel();
    	panel.setBorder(new LineBorder(new Color(0, 0, 0)));
    	panel.setBounds(395, 216, 343, 79);
    	add(panel);
    	panel.setLayout(null);
    	
    	//Creates a text area that will display a tweet of the user's choosing.
    	//This tweet will correspond to the tweet ID that the user chooses from the
    	//drop-down list after entering a username
    	tweetData = new JTextArea();
    	tweetData.setBounds(6, 15, 331, 48);
    	tweetData.setEditable(false);
    	panel.add(tweetData);
    	tweetData.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	tweetData.setLineWrap(true);
    	
    	//Creates a new buffered image object that will take the twitter logo
    	//as its image file
    	BufferedImage img = null;
    	try {
    	    img = ImageIO.read(new File(".\\twitter_bird.png"));
    	} catch (Exception e) {
    	    System.err.println("Image File could not be found.");
    	}
    	
    	//Creates a new Label object for the purpose of displaying the twitter logo
    	//in the corner of the program
        JLabel twitterLabel = new JLabel();
        twitterLabel.setBounds(20, 30, 50, 50);
        
        //Resizes the image to fit into the dimensions of the label
        Image dimg = img.getScaledInstance(twitterLabel.getWidth(), twitterLabel.getHeight(), Image.SCALE_SMOOTH);
        twitterLabel = new JLabel(new ImageIcon(dimg));
        twitterLabel.setBounds(20, 30, 50, 50);
        add(twitterLabel);
        
        //Creates the button that will be used to display a dialog box in order for
        //the user to enter new tweet information so the tweet can be added to the
        //collection
        JButton btnAddTweet = new JButton("Add Tweet");
        btnAddTweet.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//creates the text fields that will be used to input tweet data
        		JTextField idField = new JTextField(5);
        	    JTextField userField = new JTextField(5);
        	    JTextField tweetField = new JTextField(5);
        	    long id = 0;
        	    String username;
        	    String tweetText;
        	    
        	    //Creates the panel that will serve as the environment for the add tweet
        	    //dialog box
        	    JPanel myPanel = new JPanel();
        	    myPanel.add(new JLabel("ID:"));
        	    myPanel.add(idField);
        	    myPanel.add(Box.createHorizontalStrut(15)); //acts as a spacer for the text fields
        	    myPanel.add(new JLabel("Username:"));
        	    myPanel.add(userField);
        	    myPanel.add(Box.createHorizontalStrut(15)); //acts as a spacer for the text fields
        	    myPanel.add(new JLabel("Tweet Text: "));
        	    myPanel.add(tweetField);
        	    
        	    //Opens the dialog box as a dialog box with OK/Cancel button options
        	    //The option you choose will be saved in "result"
        	    int result = JOptionPane.showConfirmDialog(null, myPanel, "Please enter Tweet Data", JOptionPane.OK_CANCEL_OPTION);
        	    
        	    //This try catch ensures that the idea value inserted into the dialog box 
        	    //is actually a Long value and is greater than 0
        	    try {
        	    	id = Long.parseLong(idField.getText());
        	    	if (id < 0)
        	    		throw new IllegalArgumentException();
        	    } catch(Exception e1) {
        	    	JOptionPane.showMessageDialog(null, "The ID you entered is invalid.");
        	    }
        	    
        	    //This try catch ensures that the user actually enters a value for
        	    //ID in the dialog box
        	    try {
        	    	if (idField.getText().isBlank())
        	    		throw new IllegalArgumentException();
        	    } catch(Exception e2) {
        	    	JOptionPane.showMessageDialog(null, "You did not enter an ID value.");
        	    }
        	    
        	    //This try catch ensures that the user does not enter a tweet ID that already
        	    //exists in the collection
        	    try {
        	    	if (tc.getTweetById(id) != null)
        	    		throw new IllegalArgumentException();
        	    } catch(Exception ea) {
        	    	JOptionPane.showMessageDialog(null, "The ID you entered already exists.");
        	    }
        	    
        	    //This try catch ensures that the username box is not left blank
        	    try {
        	    	if (userField.getText().isBlank())
        	    		throw new IllegalArgumentException();
        	    } catch(Exception e3) {
        	    	JOptionPane.showMessageDialog(null, "You did not enter a username.");
        	    }
        	    
        	    //This try catch ensures that the tweet text box is not left blank
        	    try {
        	    	if (tweetField.getText().isBlank())
        	    		throw new IllegalArgumentException();
        	    } catch(Exception e4) {
        	    	JOptionPane.showMessageDialog(null, "You did not enter text for the tweet.");
        	    }
        	    
        	    //Creates a new temporary Tweet object to be added to the tweet collection
        	    username = userField.getText();
        	    tweetText = tweetField.getText();
        	    Tweet temp = new Tweet(0, id, username, tweetText);
        	    temp.setPolarity(tc.predict(temp));
        	    tc.addTweet(temp);
        	    tc.rewriteFile(); //rewrites the input text file to include the new tweet
        	    //Sets the txtPredictionfield text field to display the predicted polarity of the added
        	    //tweet
        	    txtPredictionfield.setText(Integer.toString(temp.getPolarity()));  	    
        	}
        });
        btnAddTweet.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnAddTweet.setBounds(395, 368, 150, 33);
        add(btnAddTweet);
        
        //Creates the button that will launch the dialog box that a user can use to remove
        //a tweet from the collection
        JButton btnRemoveTweet = new JButton("Remove Tweet");
        btnRemoveTweet.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//Creates a text field for the user to enter the ID of the tweet
        		JTextField idField = new JTextField(5);        	  
        	    long id = 0;

        	    //Creates the panel that will serve as the environment for the remove tweet
        	    //dialog box
        	    JPanel myPanel = new JPanel();
        	    myPanel.setPreferredSize (new Dimension(300, 50));
        	    myPanel.add(new JLabel("ID:"));
        	    myPanel.add(idField);

        	    //Opens the dialog box as a dialog box with OK/Cancel button options
        	    //The option you choose will be saved in "result"
        	    int result = JOptionPane.showConfirmDialog(null, myPanel, "Please enter the ID of the Tweet to be removed.", JOptionPane.OK_CANCEL_OPTION);
        	    
        	    //This try catch ensures that the idea value inserted into the dialog box 
        	    //is actually a Long value and is greater than 0
        	    try {
        	    	id = Long.parseLong(idField.getText());
        	    	if (id < 0)
        	    		throw new IllegalArgumentException();
        	    	if (tc.getTweetById(id) == null)
        	    		throw new IllegalArgumentException();
        	    } catch(Exception e1) {
        	    	JOptionPane.showMessageDialog(null, "The ID you entered is invalid.");
        	    }
        	    
        	    //This try catch ensures that the user actually enters a value for
        	    //ID in the dialog box
        	    try {
        	    	if (idField.getText().isBlank())
        	    		throw new IllegalArgumentException();
        	    } catch(Exception e2) {
        	    	JOptionPane.showMessageDialog(null, "You did not enter an ID value.");
        	    }
        	    
        	    //removes the tweet from the collection and then rewrites the input text file
        	    tc.removeTweet(id);
        	    tc.rewriteFile();
        	}
        });
        btnRemoveTweet.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnRemoveTweet.setBounds(577, 368, 150, 33);
        add(btnRemoveTweet);
        
        //Creates the label that corresponds to the Add and Remove buttons
        JLabel lblAddOrRemove = new JLabel("Add or Remove Tweets from the Collection:");
        lblAddOrRemove.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblAddOrRemove.setBounds(395, 339, 332, 19);
        add(lblAddOrRemove);
        
        //Creates the label that corresponds to the txtPredictionfield text field
        JLabel lblPrediction = new JLabel("Predicted Polarity of Added Tweet:");
        lblPrediction.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblPrediction.setBounds(341, 413, 197, 13);
        add(lblPrediction);
        
        //Creates the text field that will display the predicted polarity of the added tweet
        txtPredictionfield = new JTextField();
        txtPredictionfield.setBounds(539, 411, 28, 19);
        txtPredictionfield.setEditable(false);
        add(txtPredictionfield);
        txtPredictionfield.setColumns(10);
        
        //Creates the check box that will be used to toggle the Dark Mode of the program
        JCheckBox chckbxDarkmode = new JCheckBox("Dark Mode");
        chckbxDarkmode.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//if the check box is selected, the background becomes black and all labels
        		//become white
        		if (chckbxDarkmode.isSelected()) {
        			setBackground(Color.BLACK);
        			lblAddOrRemove.setForeground(Color.WHITE);
        			lblTweets.setForeground(Color.WHITE);
        			lblTweet.setForeground(Color.WHITE);
        			lblUsername.setForeground(Color.WHITE);
        			lblPrediction.setForeground(Color.WHITE);
        		}
        		//if the check box is not selected, the background reverts to white and all labels
        		//revert to black
        		else if (!chckbxDarkmode.isSelected()) {
        			setBackground(Color.WHITE);
        			lblAddOrRemove.setForeground(Color.BLACK);
        			lblTweets.setForeground(Color.BLACK);
        			lblTweet.setForeground(Color.BLACK);
        			lblUsername.setForeground(Color.BLACK);
        			lblPrediction.setForeground(Color.BLACK);
        		}
        	}
        });
        chckbxDarkmode.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chckbxDarkmode.setBounds(80, 453, 93, 21);
        add(chckbxDarkmode);
    } 
  }