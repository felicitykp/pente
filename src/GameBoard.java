import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements MouseListener{

	//VARIABLES
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
	int p1Score, p2Score;
	
	//variables for components
	private BoardSquare[][] gameBoard; //holds board pieces
	private ScoreBoard scoreBoard; 
	
	
	//CONSTRUCTOR
	public GameBoard(int w, int h, ScoreBoard sb) {
		//assigning
		bWidth = w;
		bHeight = h;
		scoreBoard = sb;
		
		this.setSize(w, h);
		this.setBackground(bColor);
		
		squareW = bWidth/GameBoard.NUM_SQUARE_SIDE;
		squareH = bHeight/GameBoard.NUM_SQUARE_SIDE;
		
		//allocating space for board
		gameBoard = new BoardSquare[NUM_SQUARE_SIDE][NUM_SQUARE_SIDE]; 

		//create board squares
		for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
			for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
				gameBoard[row][col] = new BoardSquare(row*squareW+8, col*squareH+8, squareW, squareH);
				if(col >= INNER_START && col <= INNER_END && row >= INNER_START && row <= INNER_END) {
					gameBoard[row][col].setInner();
				}
			}
		}
		
		//setup the initial screen
		startScreen();
		repaint();
		
		//mouse listener stuff
		this.addMouseListener(this);
		this.setFocusable(true);
	}
	
	//BEHAVIORS / METHODS
	public void paintComponent(Graphics g) { //override
		
		//setup
		g.setColor(bColor);
		g.fillRect(0, 0, bWidth, bHeight);
		
		//board square paint
		for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
			for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
				gameBoard[row][col].drawMe(g);
			}
		}
		
		this.repaint();
	}
	
	public void resetBoard() {
		for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
			for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
				gameBoard[row][col].setState(EMPTY);
			}
		}
	}
	
	public void startNewGame() {
		
		p1Name = JOptionPane.showInputDialog("Name of Player 1: (or type 'c' for computer ");
		if(p1Name.equals("c") || p1Name.equals("computer")) {
			player1IsComp = true;
		} 
		
		
		
		p2Name = JOptionPane.showInputDialog("Name of Player 2: (or type 'c' for computer ");
		if(p2Name.equals("c") || p2Name.equals("computer")) {
			player2IsComp = true;
		}
		
		resetBoard();
		
		playerTurn = PLAYER1_TURN;
		this.gameBoard[NUM_SQUARE_SIDE / 2][NUM_SQUARE_SIDE / 2].setState(WHITESTONE);
		playerTurn = PLAYER2_TURN;
		
		scoreBoard.setName(p1Name, WHITESTONE);
		scoreBoard.setName(p2Name, BLACKSTONE);
		
		this.repaint();
	}
	
	public void changePlayerTurn() {
		playerTurn *= -1;
		scoreBoard.setPlayerTurn(playerTurn);
	}
	
	public void checkClick(int clickX, int clickY) {
		for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
			for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
				boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
				if(squareClicked) {
					if(gameBoard[row][col].getState() == EMPTY) {
						System.out.println("You clicked the square at [" + row + ", " + col + "]");
						gameBoard[row][col].setState(playerTurn);
						checkForCaptures(row, col, playerTurn);
						this.repaint();
						this.changePlayerTurn();
					} else {
						JOptionPane.showMessageDialog(null, "This Square is Taken");
					}
				}
			}
		}
	}
	
	//big routine for check captures
	public void checkForCaptures(int r, int c, int pt) {
		boolean didCapture;
		
		//vertical
		didCapture = checkForCapture(r, c, pt, 1, 0);
		didCapture = checkForCapture(r, c, pt, -1, 0);
		
		//horizontal
		didCapture = checkForCapture(r, c, pt, 0, 1);
		didCapture = checkForCapture(r, c, pt, 0, -1);
		
		//diagonal
		didCapture = checkForCapture(r, c, pt, 1, -1);
		didCapture = checkForCapture(r, c, pt, -1, 1);
		didCapture = checkForCapture(r, c, pt, 1, 1);
		didCapture = checkForCapture(r, c, pt, -1, -1);
	}
	
	public boolean checkForCapture(int r, int c, int pt, int upDown, int rightLeft) {
		
		try {
			boolean capture = false;
		
			System.out.println("got in");
			if(gameBoard[r+(rightLeft)][c+(upDown)].getState() == pt*-1) {
				if(gameBoard[r+(rightLeft*2)][c+(upDown*2)].getState() == pt*-1) {
					if(gameBoard[r+(rightLeft*3)][c+(upDown*3)].getState() == pt) {
					
						gameBoard[r+(rightLeft)][c+(upDown)].setState(EMPTY);
						gameBoard[r+(rightLeft*2)][c+(upDown*2)].setState(EMPTY);
						capture = true;
						if(pt == this.PLAYER1_TURN) {
							p1Score += 2;
							scoreBoard.setCaptures(p1Score, playerTurn);
						} else {
							p2Score += 2;
							scoreBoard.setCaptures(p2Score, playerTurn);
						}
					}
				}
			}
			return capture;
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("error" + e.toString());
			return false;
		}
	}

	

	//mouse listener methods
	public void mouseClicked(MouseEvent e) { //override
		//no...
	}
	
	
	public void mousePressed(MouseEvent e) { //override
		this.checkClick(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) { //override
		
	}

	public void mouseEntered(MouseEvent e) { //override
		
	}

	public void mouseExited(MouseEvent e) { //override
		
	}
	
	//the beast
	public void startScreen() {
		//letter P
		this.gameBoard[1][7].setState(BLACKSTONE);
		this.gameBoard[1][8].setState(BLACKSTONE);
		this.gameBoard[1][9].setState(BLACKSTONE);
		this.gameBoard[1][10].setState(BLACKSTONE);
		this.gameBoard[1][11].setState(BLACKSTONE);
		this.gameBoard[2][7].setState(BLACKSTONE);
		this.gameBoard[2][9].setState(BLACKSTONE);
		this.gameBoard[3][7].setState(BLACKSTONE);
		this.gameBoard[3][8].setState(BLACKSTONE);
		this.gameBoard[3][9].setState(BLACKSTONE);
		
		//letter e
		this.gameBoard[4][7].setState(WHITESTONE);
		this.gameBoard[4][8].setState(WHITESTONE);
		this.gameBoard[4][9].setState(WHITESTONE);
		this.gameBoard[4][10].setState(WHITESTONE);
		this.gameBoard[4][11].setState(WHITESTONE);
		this.gameBoard[5][7].setState(WHITESTONE);
		this.gameBoard[5][9].setState(WHITESTONE);
		this.gameBoard[5][11].setState(WHITESTONE);
		this.gameBoard[6][7].setState(WHITESTONE);
		this.gameBoard[6][11].setState(WHITESTONE);
		
		//letter n
		this.gameBoard[7][7].setState(BLACKSTONE);
		this.gameBoard[7][8].setState(BLACKSTONE);
		this.gameBoard[7][9].setState(BLACKSTONE);
		this.gameBoard[7][10].setState(BLACKSTONE);
		this.gameBoard[7][11].setState(BLACKSTONE);
		this.gameBoard[8][8].setState(BLACKSTONE);
		this.gameBoard[9][9].setState(BLACKSTONE);
		this.gameBoard[10][7].setState(BLACKSTONE);
		this.gameBoard[10][8].setState(BLACKSTONE);
		this.gameBoard[10][9].setState(BLACKSTONE);
		this.gameBoard[10][10].setState(BLACKSTONE);
		this.gameBoard[10][11].setState(BLACKSTONE);
		
		//letter t
		this.gameBoard[11][7].setState(WHITESTONE);
		this.gameBoard[12][7].setState(WHITESTONE);
		this.gameBoard[12][8].setState(WHITESTONE);
		this.gameBoard[12][9].setState(WHITESTONE);
		this.gameBoard[12][10].setState(WHITESTONE);
		this.gameBoard[12][11].setState(WHITESTONE);
		this.gameBoard[13][7].setState(WHITESTONE);
		
		//letter e
		this.gameBoard[14][7].setState(BLACKSTONE);
		this.gameBoard[14][8].setState(BLACKSTONE);
		this.gameBoard[14][9].setState(BLACKSTONE);
		this.gameBoard[14][10].setState(BLACKSTONE);
		this.gameBoard[14][11].setState(BLACKSTONE);
		this.gameBoard[15][7].setState(BLACKSTONE);
		this.gameBoard[15][9].setState(BLACKSTONE);
		this.gameBoard[15][11].setState(BLACKSTONE);
		this.gameBoard[16][7].setState(BLACKSTONE);
		this.gameBoard[16][11].setState(BLACKSTONE);
		
		//!
		this.gameBoard[17][7].setState(WHITESTONE);
		this.gameBoard[17][8].setState(WHITESTONE);
		this.gameBoard[17][9].setState(WHITESTONE);
		this.gameBoard[17][11].setState(WHITESTONE);
		
		//overline
		this.gameBoard[1][5].setState(BLACKSTONE);
		this.gameBoard[2][5].setState(BLACKSTONE);
		this.gameBoard[3][5].setState(BLACKSTONE);
		this.gameBoard[4][5].setState(BLACKSTONE);
		this.gameBoard[5][5].setState(BLACKSTONE);
		this.gameBoard[6][5].setState(BLACKSTONE);
		this.gameBoard[7][5].setState(BLACKSTONE);
		this.gameBoard[8][5].setState(BLACKSTONE);
		this.gameBoard[9][5].setState(BLACKSTONE);
		this.gameBoard[10][5].setState(BLACKSTONE);
		this.gameBoard[11][5].setState(BLACKSTONE);
		this.gameBoard[12][5].setState(BLACKSTONE);
		this.gameBoard[13][5].setState(BLACKSTONE);
		this.gameBoard[14][5].setState(BLACKSTONE);
		this.gameBoard[15][5].setState(BLACKSTONE);
		this.gameBoard[16][5].setState(BLACKSTONE);
		this.gameBoard[17][5].setState(BLACKSTONE);
		
		//underline
		this.gameBoard[1][13].setState(BLACKSTONE);
		this.gameBoard[2][13].setState(BLACKSTONE);
		this.gameBoard[3][13].setState(BLACKSTONE);
		this.gameBoard[4][13].setState(BLACKSTONE);
		this.gameBoard[5][13].setState(BLACKSTONE);
		this.gameBoard[6][13].setState(BLACKSTONE);
		this.gameBoard[7][13].setState(BLACKSTONE);
		this.gameBoard[8][13].setState(BLACKSTONE);
		this.gameBoard[9][13].setState(BLACKSTONE);
		this.gameBoard[10][13].setState(BLACKSTONE);
		this.gameBoard[11][13].setState(BLACKSTONE);
		this.gameBoard[12][13].setState(BLACKSTONE);
		this.gameBoard[13][13].setState(BLACKSTONE);
		this.gameBoard[14][13].setState(BLACKSTONE);
		this.gameBoard[15][13].setState(BLACKSTONE);
		this.gameBoard[16][13].setState(BLACKSTONE);
		this.gameBoard[17][13].setState(BLACKSTONE);
	}
}
