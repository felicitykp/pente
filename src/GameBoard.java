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
	public static final int PLAYER1_TURN = 1; //assumed that player 1 is white stone
	public static final int PLAYER2_TURN = -1;
	public static final int MAX_CAPTURES = 10;
	public static final int SLEEP_TIME = 600;
	
	//variables for setup
	private int bWidth, bHeight;
	private Color bColor = new Color(153, 178, 221); //light blue
	private int squareW, squareH;
	
	//variables for game
	private int playerTurn;
	private boolean player1IsComp = false;
	private boolean player2IsComp = false;
	private String p1Name, p2Name;
	private int p1Score, p2Score;
	
	//variables for specialized issues
	private boolean secondMoveTaken = false;
	private boolean gameOver = false;
	
	//variables for components
	private BoardSquare[][] gameBoard; //holds board pieces
	private ScoreBoard scoreBoard; 
	
	//variables for computer move generator
	private ComputerMoveGenerator p1ComputerPlayer = null;
	private ComputerMoveGenerator p2ComputerPlayer = null;
	
	
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
				gameBoard[row][col].setWinningSquare(false);
			}
		}
		//this.paintImmediately(0, 0, bWidth, bHeight);
		this.repaint();
	}
	
	public void startNewGame(boolean firstGame) {
		p1Score = 0;
		p2Score = 0;
		gameOver = false;
		
		if(firstGame) {
			p1Name = JOptionPane.showInputDialog("Name of Player 1: (or type 'c' for computer ");
			if(p1Name.toLowerCase().equals("c") || p1Name.toLowerCase().equals("computer") || p1Name.toLowerCase().equals("comp")) {
				player1IsComp = true;
				//System.out.println("comp is gonna play for 1");
				p1ComputerPlayer = new ComputerMoveGenerator(this, WHITESTONE);
			} 
		}
		
		scoreBoard.setName(p1Name, WHITESTONE);
		scoreBoard.setCaptures(p1Score, WHITESTONE);
		
		if(firstGame) {
			p2Name = JOptionPane.showInputDialog("Name of Player 2: (or type 'c' for computer ");
			if(p2Name.toLowerCase().equals("c") || p2Name.toLowerCase().equals("computer") || p2Name.toLowerCase().equals("comp")) {
				player2IsComp = true;
				//System.out.println("comp is gonna play for 2");
				p2ComputerPlayer = new ComputerMoveGenerator(this, BLACKSTONE);
			}
		}
		
		scoreBoard.setCaptures(p2Score, BLACKSTONE);
		scoreBoard.setName(p2Name, BLACKSTONE);
		
		resetBoard();
		
		//first stone placed
		playerTurn = PLAYER1_TURN;
		this.gameBoard[NUM_SQUARE_SIDE / 2][NUM_SQUARE_SIDE / 2].setState(WHITESTONE);
		this.secondMoveTaken = false;
		changePlayerTurn();
		checkForComputerMove(playerTurn);	
		
		this.repaint();
	}
	
	public void changePlayerTurn() {
		playerTurn *= -1;
		scoreBoard.setPlayerTurn(playerTurn);
	}
	
	public void checkClick(int clickX, int clickY) {
		if(!gameOver) {
			for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
				for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
					boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
					if(squareClicked) {
						if(gameBoard[row][col].getState() == EMPTY) {
							if(!darkSquareProblem(row, col)) {
								gameBoard[row][col].setState(playerTurn);
								checkForCaptures(row, col, playerTurn);
								this.paintImmediately(0, 0, bWidth, bHeight );
								checkForWin(row, col, playerTurn);
								repaint();
								if(!gameOver) {
									this.changePlayerTurn();
									checkForComputerMove(playerTurn);
								}
							} else {
								JOptionPane.showMessageDialog(null, "This is an Invalid Move");
							}
						} else {
							JOptionPane.showMessageDialog(null, "This Square is Taken");
						}
					}
				}
			}
		}
	}
	
	public void checkForComputerMove(int whichPlayer) {
		if(whichPlayer == PLAYER1_TURN && this.player1IsComp == true) {
			//System.out.println("check for player 1 comp move");
			
			int[] nextMove = this.p1ComputerPlayer.getComputerMove();
			int newR = nextMove[0];
			int newC = nextMove[1];
			
			gameBoard[newR][newC].setState(whichPlayer);
			this.paintImmediately(0, 0, bWidth, bHeight);
			checkForCaptures(newR, newC, whichPlayer);
			this.repaint();
			checkForWin(newR, newC, whichPlayer);
			if(!gameOver) {
				this.changePlayerTurn();
				checkForComputerMove(playerTurn);
			}
			
		} else if (whichPlayer == PLAYER2_TURN && this.player2IsComp == true) {
			//System.out.println("check for player 2 comp move");
			
			int[] nextMove = this.p2ComputerPlayer.getComputerMove();
			int newR = nextMove[0];
			int newC = nextMove[1];
			
			gameBoard[newR][newC].setState(whichPlayer);
			this.paintImmediately(0, 0, bWidth, bHeight);
			checkForCaptures(newR, newC, whichPlayer);
			this.repaint();
			checkForWin(newR, newC, whichPlayer);
			if(!gameOver) {
				this.changePlayerTurn();
				checkForComputerMove(playerTurn);
			}
		}
		
		this.repaint();
	}
	
	public boolean darkSquareProblem(int r, int c) {
		boolean dSP = false;
		
		if(secondMoveTaken == false && playerTurn == WHITESTONE) {
			if((r >= INNER_START && r <= INNER_END) && (c >= INNER_START && c <= INNER_END)) {
				dSP = true;
			} else {
				secondMoveTaken = true;
			}
		}
		
		return dSP;
	}
	
	//big routine for check captures
	public void checkForCaptures(int r, int c, int pt) {
		//vertical
		checkForCapture(r, c, pt, 1, 0);
		checkForCapture(r, c, pt, -1, 0);
		
		//horizontal
		checkForCapture(r, c, pt, 0, 1);
		checkForCapture(r, c, pt, 0, -1);
		
		//diagonal
		checkForCapture(r, c, pt, 1, -1);
		checkForCapture(r, c, pt, -1, 1);
		checkForCapture(r, c, pt, 1, 1);
		checkForCapture(r, c, pt, -1, -1);
	}
	
	public boolean checkForCapture(int r, int c, int pt, int upDown, int rightLeft) {
		
		try {
			boolean capture = false;
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
			//System.out.println("error" + e.toString());
			return false;
		}
	}
	
	public boolean fiveInARow(int whichPlayer) {
		boolean isFive = false;
		
		for(int col = 0; col < NUM_SQUARE_SIDE; col++) {
			for(int row = 0; row < NUM_SQUARE_SIDE; row++) {
				for(int rL = -1; rL <= 1; rL++) {
					for(int uD = -1; uD <= 1; uD++) {
						if(fiveCheck(row, col, whichPlayer, uD, rL)) {
							isFive = true;
							//System.out.println("found a 5");
						}
					}
				}
			}
		}
		return isFive;
	}
	
	public boolean fiveCheck (int r, int c, int pt, int upDown, int rightLeft) {
		boolean yesFive = false;
		
		try {
			if(!(upDown == 0 && rightLeft == 0)) {
				if(gameBoard[r][c].getState() == pt) {
					if(gameBoard[r+(rightLeft)][c+(upDown)].getState() == pt) {
						if(gameBoard[r+(rightLeft*2)][c+(upDown*2)].getState() == pt) {
							if(gameBoard[r+(rightLeft*3)][c+(upDown*3)].getState() == pt) {
								if(gameBoard[r+(rightLeft*4)][c+(upDown*4)].getState() == pt) {
									yesFive = true;
									gameBoard[r][c].setWinningSquare(true);
									gameBoard[r+(rightLeft)][c+(upDown)].setWinningSquare(true);
									gameBoard[r+(rightLeft*2)][c+(upDown*2)].setWinningSquare(true);
									gameBoard[r+(rightLeft*3)][c+(upDown*3)].setWinningSquare(true);
									gameBoard[r+(rightLeft*4)][c+(upDown*4)].setWinningSquare(true);
								}
							}
						}
					}
				}
			}
			return yesFive;
		} catch(ArrayIndexOutOfBoundsException e) {
			return yesFive;
		}
	}
	
	public void checkForWin(int r, int c, int whichPlayer) {
		if(whichPlayer == WHITESTONE) {
			if(this.p1Score >= MAX_CAPTURES) {
				JOptionPane.showMessageDialog(null, "Congratulations " + p1Name+ " Wins by Capture!");
				gameOver = true;
			} else if(fiveInARow(playerTurn)) {
				JOptionPane.showMessageDialog(null, "Congratulations " + p1Name+ " Wins by 5 in a Row!");
			}
		} else if(whichPlayer == BLACKSTONE){
			if(this.p2Score >= MAX_CAPTURES) {
				JOptionPane.showMessageDialog(null, "Congratulations " + p2Name+ " Wins!");
				gameOver = true;
			} else if(fiveInARow(playerTurn)) {
				JOptionPane.showMessageDialog(null, "Congratulations " + p2Name+ " Wins by 5 in a Row!");
			}
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
	
	//ACESSOR
	public BoardSquare[][] getBoard() {
		return gameBoard;
	}
}
