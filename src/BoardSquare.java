import java.awt.Color;
import java.awt.Graphics;

public class BoardSquare {

	//VARIABLES
	private int xLoc, yLoc;
	private int sWidth, sHeight;
	private int sState; //open, player1, player 2
	private Color sColor; //square
	private Color sColorB; //shadow for black
	private Color sColorW; //shadow for white
	private Color lColor; //cross lines
	private Color bColor; //border
	private Color iColor; //inner section
	private Color darkStone; //outer
	private Color darkStoneIn; //innner
	private Color darkStoneH; //highlight
	private Color lightStone; //outer
	private Color lightStoneIn; //inner
	boolean isInner = false;
	
	//CONSTRUCTOR
	public BoardSquare(int x, int y, int w, int h) {
		//assigning
		xLoc = x;
		yLoc = y;
		sWidth = w;
		sHeight = h;
		
		sColor = new Color(234, 209, 190); //beige
		sColorB = new Color(181, 172, 166); //beige gray
		sColorW = new Color(206, 196, 187); //lighter beige gray
		iColor = new Color(221, 197, 179); //darker beige
		lColor = new Color(233, 175, 163); //light pink 
		bColor = new Color(104, 80, 68); //brown
		darkStone = new Color(58, 64, 90); //dark blue
		darkStoneIn = new Color(93, 99, 124); //lighter dark blue
		darkStoneH = new Color(213, 215, 224);
		lightStone = new Color(170, 170, 170); //white-ish
		lightStoneIn = new Color(219, 219, 219); //lighter white-ish

		
		sState = GameBoard.EMPTY;
		
	}
	
	public void setInner() {
		isInner = true;
	}
	
	//BEHAVIORS / METHODS
	public void drawMe(Graphics g) {
		
		//square
		if(isInner) {
			g.setColor(iColor);
		} else {
			g.setColor(sColor);
		}
		g.fillRect(xLoc, yLoc, sWidth, sHeight);
		
		//border
		g.setColor(bColor);
		g.drawRect(xLoc, yLoc, sWidth, sHeight);
		
		
		//lines
		g.setColor(lColor);
		g.drawLine(xLoc, yLoc + (sHeight/2), xLoc + sWidth, yLoc + (sHeight/2)); //horizontal
		g.drawLine(xLoc + (sWidth/2), yLoc, xLoc + (sWidth/2), yLoc + sHeight); //vertical
		
		if(sState == GameBoard.BLACKSTONE) {
			g.setColor(sColorB);
			g.fillOval(xLoc, yLoc + 6, sWidth - 8, sHeight - 8);
			g.setColor(darkStone);
			g.fillOval(xLoc + 3, yLoc + 3, sWidth - 6, sHeight - 6);
			g.setColor(darkStoneIn);
			g.fillOval(xLoc + 6, yLoc + 3, sWidth - 10, sHeight - 8);
			g.setColor(darkStoneH);
			g.drawArc(xLoc +(int)(sWidth*0.55), yLoc + 8, (int)(sWidth * 0.3), (int)(sHeight * 0.35), 0, 90);
		}
		
		if(sState == GameBoard.WHITESTONE) {
			g.setColor(sColorW);
			g.fillOval(xLoc, yLoc + 6, sWidth - 8, sHeight - 8);
			g.setColor(lightStone);
			g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
			g.setColor(lightStoneIn);
			g.fillOval(xLoc + 6, yLoc + 3, sWidth - 10, sHeight - 8);
		}
	}
	
	//accessor methods
	
	public void setState(int newState) { //empty = 0; black = 1; white = -1;
		if(newState < -1 || newState > 1) {
			System.out.println(newState + " is an illegal state");
		} else {
			this.sState = newState;
		}
	}
	
	public int getState() {
		return sState;
	}
	
	public void setXLoc(int newX) {
		xLoc = newX;
	}
	
	public void setYLoc(int newY) {
		yLoc = newY;
	}
	
	public boolean isClicked(int clickX, int clickY) {
		boolean didYouClick = false;
		
		if(xLoc < clickX && clickX < xLoc + sWidth) {
			if(yLoc < clickY && clickY < yLoc + sHeight) {
				didYouClick = true;
			}
		}
		return didYouClick;
	}
	
}
