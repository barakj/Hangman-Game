import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 * A class that will use Hangman object to generate a game
 * It will create a component that will contain the letters gueesed, lines, logo and the hangman drawing
 */
public class HangmanComponent extends JComponent{

	//Number of wrong guesses allowed before loss
	public static final int MAX_GUESSES = 10;
	public static final int WORDS_PER_CATEGORY = 8;
	public static final int HIDDEN_COORDINATE = -100;
	public static final int LETTER_POSITION_X = 175;
	public static final int LETTER_POSITION_Y = 175;
	public static final int LINE_POSITION_Y = 180;
	public static final int SPACE_BETWEEN_LETTERS = 25;
	public static final int WRONG_KEY_SPACE = 110;
	//Arrays of categories and words
	private String[] SPORTS= {"Tennis", "Football","Soccer","Hockey", "Basketball", "Judo", "Handball", "Volleyball"};
	private String[] COUNTRIES = {"Israel","Canada","Germany","Japan","China","Russia","Turkey","Brazil"};
	private String[] FOODS = {"Pasta", "Pizza", "Steak","Sushi","Soup","Salad","Fish","Chicken"};
	//Array that keeps track of keys that were guessed wrong
	private char[] wrongKeys;
	private String category;
	private String key;
	private int xPos;
	private int yPos;
	private int wrongGuesses;
	private String word;
	//Boolean array to check which letters of the word were revealed 
	private boolean[] wasGuessed;
	private Random r;
	//color for the hangman drawing
	private Color hangColor;
	

	/**
     * Constructs the component, will not be shown yet
     * Initializes all instance variables
     */
	public HangmanComponent(){
		category = "";
		wrongGuesses = 0;
		wrongKeys = new char[10];
		xPos = HIDDEN_COORDINATE;
		yPos = HIDDEN_COORDINATE;
		wrongGuesses = 0;
		r = new Random();
		key = "";
		hangColor = Color.BLACK;
	}
	
	/**
     * Paints the component with logo,letters,lines and hangman
     */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		//adding the logo at the top
		addLogo(g2);
		Hangman hangman = new Hangman();
		//drawing parts of the hangman based on number of wrong guesses
		drawWrongKeys(g2);
		hangman.draw(g2, wrongGuesses, hangColor);
		int x = LETTER_POSITION_X;
		int yLetter = LETTER_POSITION_Y;;
		int yLine = LINE_POSITION_Y;;
		//Frame's color (will be hidden)
		Color unknownLetter = new Color(0,0,0,0);
		Color knownLetter = Color.BLACK;
		Color actualColor;
		for(int i=0;i<word.length();i++){
			//if this letter matches the key pressed, or this letter was guessed already
			if(key.equalsIgnoreCase(word.charAt(i)+"") || wasGuessed[i]){
				actualColor = knownLetter;
				wasGuessed[i] = true;
			}
			//if letter wasn't guessed by the user, it would be hidden
			else
				actualColor = unknownLetter;
			
			//draw the words and the lines underneath
			drawWord(actualColor,g2,word.charAt(i)+"",x,yLetter);
			drawLine(Color.BLACK,g2,x,yLine);
			x+=SPACE_BETWEEN_LETTERS;
		}
		
		//checking cases of win and loss
		
		if(checkIfWon()){
			//Winner is runnable to make sure the JComponent is fully painted before JOptionPane appears
			java.awt.EventQueue.invokeLater(new Winner());
			
		}
		
		if(wrongGuesses == MAX_GUESSES){
			//Loser is runnable to make sure the JComponent is fully painted before JOptionPane appears
			java.awt.EventQueue.invokeLater(new Loser());
		}
		
	}
	
	/**
     * Paints logo at the middle/top of the component using a its url
     */
	private void addLogo(Graphics2D g2) {
		Image img = null;
		URL url = null;
		try {
			url = this.getClass().getResource("HangmanLogo2.png");
			img = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(img,getWidth()/8,-10,null);
		
	}

	/**
     * Draws the list of wrong keys/tries
     */
	private void drawWrongKeys(Graphics2D g2) {
		int x = LETTER_POSITION_X;
		int y = LINE_POSITION_Y + SPACE_BETWEEN_LETTERS;
			drawWord(Color.BLACK,g2,"Wrong Guesses: ",x,y);		
			for(int i=0;i<wrongGuesses;i++){
				drawWord(Color.RED,g2,(wrongKeys[i]+"").toUpperCase(),x+WRONG_KEY_SPACE,y);
				x+=25;
		}
		
	}
	/**
     * Rests the boolean letters array to false
     * Used when a new game is created
     */
	private void resetArray() {
		for(int i=0;i<wasGuessed.length;i++){
			wasGuessed[i] = false;
		}
	}
	
	/**
     * Increases the wrong guesses total
     * Used as part of key listener when the user guesses wrong
     */
	public void increaseWrong(char wrongKey) {
		wrongKeys[wrongGuesses] = wrongKey;
		wrongGuesses++;
		//if lost, draw hangman in red
		if(wrongGuesses == MAX_GUESSES)
			hangColor = Color.RED;
		else
			hangColor = Color.BLACK;
		repaint();
		
	}
	
	/**
     * Returns the word used in the current game
     * @return the word used in the current game
     */
	public String getWord(){
		return word;
	}
	
	/**
     * Draws the letters of the world in a certain position and color
     */
	private void drawWord(Color c, Graphics2D g2, String message, int xPos, int yPos){
		g2.setColor(c);
		g2.drawString(message, xPos, yPos);
			
	}
	
	/**
     * Sets the key with the letter pressed by the user
     */
	public void setKey(String k){
		key = k;
		repaint();
	}
	
	/**
     * Draws lines beneath the letters of the word
     */
	private void drawLine(Color c, Graphics2D g2, int xPos, int yPos){
		g2.setColor(c);
		Line2D.Double l = new Line2D.Double(xPos-5, yPos, xPos+10, yPos);
		g2.draw(l);
	}
	
	/**
     * Checks whether the user won or not
     *@return true if user won, false otherwise
     */
	private boolean checkIfWon(){
		for(int i=0;i<wasGuessed.length;i++){
			//if there is a letter that wasnt guessed, return false
			if(!wasGuessed[i])
				return false;
		}
		return true;
	}
	
	/**
	 * A class that uses the Runnable interface
	 * The run method will create the menu if the user win (popup)
	 * Runnable is used in order to delay the JOptionPane execution until component is fully painted
	 */
	class Winner implements Runnable{

		@Override
		public void run() {
			Object[] choices = {"Yes, continue", "No, quit"};
			int n = JOptionPane.showOptionDialog(null,
					"YOU WON!!\n"+
					"Would you like to play again?",
					"Winner",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					choices,
					choices[0]);
			//User wants to play again
			if(n==0){
				//create the drop down list menu 
				Object[] types = {"Sports", "Countries", "Foods"};
				String s = (String)JOptionPane.showInputDialog(
				                    null,
				                    "Choose a Category:\n",
				                    "Categories",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    types,
				                    "Sports");

				//If user chose a category
				if ((s != null) && (s.length() > 0)) {
					//Start a new game
				    setCategoryAndWord(s);
				    resetArray();
				    wrongGuesses = 0;
				    key = "";
				    repaint();
				}
			else
				System.exit(0);
		
		}
			else
				System.exit(0);
	}
	}
	
	/**
	 * A Runnable class with a run method
	 * The run method creates the menu if the user losses
	 * Runnable is used in order to delay the JOptionPane execution until component is fully painted
	 */
	class Loser implements Runnable{
		
		@Override
		public void run() {
			Object[] choices = {"Yes, continue", "No, quit"};
			int n = JOptionPane.showOptionDialog(null,
					"YOU LOST!!\n"+
					"Would you like to play again?",
					"Game Over",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					choices,
					choices[0]);
			//User wants to play again
			if(n==0){
				Object[] types = {"Sports", "Countries", "Foods"};
				String s = (String)JOptionPane.showInputDialog(
				                    null,
				                    "Choose a Category:\n",
				                    "Categories",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    types,
				                    "Sports");
				
				//If user chose a category
				if ((s != null) && (s.length() > 0)) {
					//Start a new game
				    setCategoryAndWord(s);
				    resetArray();
				    wrongGuesses = 0;
				    key = "";
				    repaint();
				}
				else
					System.exit(0);
			}
			else
			{
				System.exit(0);
			}
		
		}
	}
		

		
	/**
	 * Sets the category chosen by the user and a random word corresponding to category
	 */
	public void setCategoryAndWord(String s) {
		this.category = s;
		int index = r.nextInt(WORDS_PER_CATEGORY);
		if(category.equalsIgnoreCase("Sports")){
			//use sports
			word = SPORTS[index];
		}
		else if(category.equalsIgnoreCase("Countries")){
			//use countries
			word = COUNTRIES[index];
		}
		else if(category.equalsIgnoreCase("Foods")){
			//use foods
			word = FOODS[index];
		}
		//new word, new boolean array 
		wasGuessed = new boolean[word.length()];
		repaint();
	}
	


		
	
	
	
}
