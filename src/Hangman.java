import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * A hangman class that is drawn depending on number of wrong answers
 */
public class Hangman {
	//lots of position variables
	//array of coordinates
	private int[] xPoints;
	private int[] yPoints;
	public static final int STARTING_X = 50;
	public static final int STARTING_Y = 200;
	public static final int POLE_HEIGHT = 100;
	public static final int POLE_WIDTH = 50;
	public static final int FACE_DIAMETER = 10;
	public static final int FACE_RADIUS = 5;
	private int xStart;
	private int yStart;
	private int x2nd;
	private int y2nd;
	private int x3rd;
	private int y3rd;
	
	/**
     * Constructs a hangman and initializes all instance variables
     */
	public Hangman(){
		xPoints = new int[11];
		yPoints = new int[11];
		initializePoints();
		//some positioning
		xStart = 50;
		yStart = 200;
		x2nd = 50;
		y2nd = 100;
		x3rd = 150;
		y3rd = 100;
	}
	
	/**
     * Initializes all the coordinates in the array
     */
	private void initializePoints() {
		xPoints[0] = STARTING_X;
		yPoints[0] = STARTING_Y;
		
		xPoints[1] = STARTING_X;
		yPoints[1] = STARTING_Y - POLE_HEIGHT;
		
		xPoints[2] = STARTING_X + POLE_WIDTH;
		yPoints[2] = STARTING_Y - POLE_HEIGHT;
		
		xPoints[3] = STARTING_X + POLE_WIDTH;
		yPoints[3] = (int) (STARTING_Y - (POLE_HEIGHT/(3.0/2)));
		
		xPoints[4] = STARTING_X + POLE_WIDTH;
		yPoints[4] = (int) (STARTING_Y - (POLE_HEIGHT/3));
		
		xPoints[5] = STARTING_X + POLE_WIDTH;
		yPoints[5] = (int) (STARTING_Y - (POLE_HEIGHT/2.5));
		
		xPoints[6] = STARTING_X + POLE_WIDTH;
		yPoints[6] = (int) (STARTING_Y - (POLE_HEIGHT/3)) + 15;
		
	}

	/**
     * Draws a hangman depending on the number of wrong answers
     * Every wrong answer, another part will be added to the hangman drawing
     */
	public void draw(Graphics2D g2, int numWrong, Color c){
		g2.setColor(c);
		switch(numWrong){
		//no break so that every necessary part will be drawn
		case 10:
			Line2D.Double leftLeg = new Line2D.Double(xPoints[4], yPoints[4], xPoints[6], yPoints[6]);
			AffineTransform at = 
			        AffineTransform.getRotateInstance(
			            Math.toRadians(45), leftLeg.getX1(), leftLeg.getY1());
			g2.draw(at.createTransformedShape(leftLeg));
		case 9:
			Line2D.Double rightLeg = new Line2D.Double(xPoints[4], yPoints[4], xPoints[6], yPoints[6]);
			AffineTransform at2 = 
			        AffineTransform.getRotateInstance(
			            Math.toRadians(315), rightLeg.getX1(), rightLeg.getY1());
			g2.draw(at2.createTransformedShape(rightLeg));
		case 8:
			Line2D.Double leftArm = new Line2D.Double(xPoints[3], yPoints[3]+15, xPoints[5], yPoints[5]);
			AffineTransform at3 = 
			        AffineTransform.getRotateInstance(
			            Math.toRadians(45), leftArm.getX1(), leftArm.getY1());
			g2.draw(at3.createTransformedShape(leftArm));
		case 7:
			Line2D.Double rightArm = new Line2D.Double(xPoints[3], yPoints[3]+15, xPoints[5], yPoints[5]);
			AffineTransform at4 = 
			        AffineTransform.getRotateInstance(
			            Math.toRadians(315), rightArm.getX1(), rightArm.getY1());
			g2.draw(at4.createTransformedShape(rightArm));
		case 6:
			Line2D.Double body = new Line2D.Double(xPoints[3], yPoints[3]+FACE_DIAMETER, xPoints[4], yPoints[4]);
			g2.draw(body);
		case 5:
			Ellipse2D.Double face = new Ellipse2D.Double(xPoints[3]-FACE_RADIUS, yPoints[3], FACE_DIAMETER, FACE_DIAMETER);
			g2.draw(face);
		case 4:
			Line2D.Double third = new Line2D.Double(xPoints[2], yPoints[2], xPoints[3], yPoints[3]);
			g2.draw(third);
		case 3:
			Line2D.Double second = new Line2D.Double(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
			g2.draw(second);
		case 2:
			Line2D.Double first = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
			g2.draw(first);
		case 1:
			Line2D.Double base = new Line2D.Double(xPoints[0]-20, yPoints[0], xPoints[1]+20, yPoints[0]);
			g2.draw(base);
		case 0:
			break;
		}
	}
}
