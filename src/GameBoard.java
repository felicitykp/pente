import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameBoard extends JPanel{

	//variables
	public static final int EMPTY = 0;
	public static final int BLACKSTONE = -1;
	public static final int WHITESTONE = 1;
	public static final int NUM_SQUARE_SIDE = 19;
	public static final int INNER_START = 7;
	public static final int INNER_END = 11;
	public static final int PLAYER1_TURN = 1; //assumed that player 1 is dark stone
	public static final int PLAYER2_TURN = -1;
	
	//variables for setup
	private int bWidth, bHeight;
	private Color bColor = new Color(153, 178, 221); //light blue
	private int squareW, squareH;
	
	//variables for game
	int playerTurn;
	boolean player1IsComp = false;
	boolean player2IsComp = false;
	String p1Name, p2Name;
	
	
	private BoardSquare[][] gameBoard; //holds board pieces
	
	//constructor
	public GameBoard(int w, int h) {
		//assigning
		bWidth = w;
		bHeight = h;
		
		this.setSize(w, h);
		this.setBackground(bColor);
		
		squareW = bWidth/this.NUM_SQUARE_SIDE;
		squareH = bHeight/this.NUM_SQUARE_SIDE;
		
		gameBoard = new BoardSquare[NUM_SQUARE_SIDE][NUM_SQUARE_SIDE]; //allocating space for board
		
		for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
			for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
				gameBoard[row][col] = new BoardSquare(row*squareW+8, col*squareH+8, squareW, squareH);
				if(col >= INNER_START && col <= INNER_END && row >= INNER_START && row <= INNER_END) {
					gameBoard[row][col].setInner();
				}
				//to test
				/*if((row+col) % 2 == 0 ) {
					gameBoard[row][col].setState(BLACKSTONE);
				} else {
					gameBoard[row][col].setState(WHITESTONE);
				}  */
			}
		}
	}
	
	//BEHAVIORS / METHODS
	public void paintComponent(Graphics g) { //override
		g.setColor(bColor);
		g.fillRect(0, 0, bWidth, bHeight);
		
		for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
			for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
				gameBoard[row][col].drawMe(g);
			}
		}
	}
	
	public void resetBoard() {
		for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
			for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
				gameBoard[row][col].setState(EMPTY);
			}
		}
	}
	
	public void startNewGame() {
		resetBoard();
		
		p1Name = JOptionPane.showInputDialog("Name of Player 1: (or type 'c' for computer ");
		if(p1Name.equals("c") || p1Name.equals("computer")) {
			player1IsComp = true;
		} 
		
		p2Name = JOptionPane.showInputDialog("Name of Player 2: (or type 'c' for computer ");
		if(p2Name.equals("c") || p2Name.equals("computer")) {
			player2IsComp = true;
		}
		
		playerTurn = PLAYER1_TURN;
		this.gameBoard[NUM_SQUARE_SIDE / 2][NUM_SQUARE_SIDE / 2].setState(BLACKSTONE);
		changePlayerTurn();
		
		this.repaint();
	}
	
	public void changePlayerTurn() {
		playerTurn *= -1;
	}
}
