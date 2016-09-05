import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * A class that is used to launch the program with a certain size and title
 * It will also generate the first pop up windows(rules and categories)
 */
public class MyFrame {
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(600,350);
		frame.setTitle("Hangman game");
		final HangmanComponent man = new HangmanComponent();
		man.setFocusable(true);
		
		
		//Creating the first popup window, explaining the rules (ok/cancel window)
		int input = JOptionPane.showOptionDialog(null,  "Welcome to Barak's Hangman game!\n\n"+
				"Rules:\n"+
				"1. You will be given a word based on a category you choose.\n"+
				"2. You need to guess that word by sugggesting letters using the keyboard.\n"+
				"3. You have 10 guesses to reveal the word.\n\n"+
				"Ready to start?", 
				"The title", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, null, null, null);

		//if user pressed cancel or exit
		if(input != JOptionPane.OK_OPTION){
			System.exit(0);
		}
		
		
		//if here, user pressed ok
		//creating the second popup window, choosing a category (drop down list)
		Object[] types = {"Sports", "Countries", "Foods"};
		String s = (String)JOptionPane.showInputDialog(
		                    frame,
		                    "Choose a Category:\n",
		                    "Categories",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    types,
		                    "Sports");

		//If a category was chosen
		if ((s != null) && (s.length() > 0)) {
		    man.setCategoryAndWord(s);
		}

		//If user pressed cancel or exit
		else
			System.exit(0);
		


	
		/**
	     * Listener class for the keyboard keys (step 3)
	     * Checks what key the user pressed, if its valid, and whether it a right or wrong guess
	     */
	class MyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent event) {
			boolean rightGuess = false;
			//Getting the key from the event and removing the word press from it
			String key = KeyStroke.getKeyStrokeForEvent(event).toString();
			key = key.replace("pressed ", "");
			key = key.toLowerCase();
			//using regular expression, checking whether the key is a letter
			boolean b = Pattern.matches("{1}[a-z]", key);
			//not a letter
			if(!b)
				return;
			
			//If here, key is a valid letter
			String word = man.getWord();
			for(int i=0;i<word.length();i++){
				//if the key is in the word
				if((word.charAt(i)+"").equalsIgnoreCase(key)){
					rightGuess = true;
					break;
				}
			
			}
			
			//If key is not part of the word
			if(!rightGuess){
				man.increaseWrong(key.charAt(0));
			}
			//If key is in the word
			else{
				man.setKey(key);	
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	man.addKeyListener(new MyListener());
	frame.add(man);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setVisible(true);
	
	
}
	
}
