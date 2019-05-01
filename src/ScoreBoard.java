import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScoreBoard extends JPanel implements ActionListener{
	//VARIABLES
	private int width, height;
	private int xLoc, yLoc;
	private Color bColor; //background
	private Color p1Color;
	private Color p2Color;
	private Color lColor; //label
	private Font myFont = new Font("Times New Roman", Font.BOLD, 20);
	
	private JLabel p1Name, p2Name;
	private JLabel p1Score, p2Score;
	private JLabel whoseTurn;
	private JButton resetButton;

	private GameBoard myBoard = null;
	
	private boolean firstGame = true;
	
	//CONSTRUCTORS
	public ScoreBoard(int w, int h) {
		width = w;
		height = h;
		bColor = new Color(233, 175, 163); //pink
		p1Color = new Color(235, 235, 235); //off white
		p2Color = new Color(58, 64, 90); //navy
		lColor = new Color(252, 244, 244);
		
		this.setSize(width, height);
		this.setBackground(bColor);
		
		this.setVisible(true);
		
		addInfoPlaces();
		addTurnLabel();
		addButton();
	}
	
	//BEHAVIORS / METHODS
	
	public void addInfoPlaces() {
		//player info panel
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
		playerPanel.setSize(width, (int)(height * 0.4));
		playerPanel.setOpaque(false);
		
			//player 1 name
			p1Name = new JLabel("<html><div style='text-align: center'>Player 1 Name<br>name</div></html>");
			p1Name.setFont(myFont);
			p1Name.setForeground(p1Color);
			p1Name.setFocusable(false);
			p1Name.setPreferredSize(new Dimension(130, 70));
		
			//player 1 score
			p1Score = new JLabel("<html><div style='text-align: center'>Player 1 Score<br>0</div></html>");
			p1Score.setFont(myFont);
			p1Score.setForeground(p1Color);
			p1Score.setFocusable(false);
			p1Score.setPreferredSize(new Dimension(130, 70));
			
			//player 2 name
			p2Name = new JLabel("<html><div style='text-align: center'>Player 2 Name<br>name</div></html>");
			p2Name.setFont(myFont);
			p2Name.setForeground(p2Color);
			p2Name.setFocusable(false);
			p2Name.setPreferredSize(new Dimension(130, 70));
			
			//player 2 score
			p2Score = new JLabel("<html><div style='text-align: center'>Player 2 Name<br>0</div></html>");
			p2Score.setFont(myFont);
			p2Score.setForeground(p2Color);
			p2Score.setFocusable(false);
			p2Score.setPreferredSize(new Dimension(130, 70));
		
		playerPanel.add(p1Name);
		playerPanel.add(Box.createRigidArea(new Dimension(20,0)));
		playerPanel.add(p1Score);
		playerPanel.add(Box.createRigidArea(new Dimension(20,0)));
		playerPanel.add(p2Name);
		playerPanel.add(Box.createRigidArea(new Dimension(20,0)));
		playerPanel.add(p2Score);
		this.add(playerPanel);
	}
	
	public void addTurnLabel() {
		//add turn label
		whoseTurn = new JLabel("It is Player ? turn");
		whoseTurn.setOpaque(true);
		whoseTurn.setPreferredSize(new Dimension(170, 40));
		whoseTurn.setHorizontalAlignment(SwingConstants.CENTER);
		whoseTurn.setBackground(lColor);
		whoseTurn.setFont(myFont);
		whoseTurn.setForeground(p2Color);
		whoseTurn.setFocusable(false);
		
		this.add(whoseTurn);	
	}
	
	public void addButton() {
		//add a button
		resetButton = new JButton("New Game");
		resetButton.setFont(myFont);
		resetButton.setBackground(lColor);
		resetButton.setOpaque(true);
		resetButton.setForeground(p2Color);
		resetButton.setPreferredSize(new Dimension(170, 40));
		resetButton.addActionListener(this);
		resetButton.setBorderPainted(false);
		

		this.add(resetButton);
	}
	
	public void setName(String n, int whichPlayer) {
		if(whichPlayer == GameBoard.WHITESTONE) {
			p1Name.setText("<html><div style='text-align: center'>Player 1 Name<br>" + n + "</div></html>");
		} else if (whichPlayer == GameBoard.BLACKSTONE){
			p2Name.setText("<html><div style='text-align: center'>Player 2 Name<br>" + n + "</div></html>");
		}
	
		
		repaint();
	}
	
	public void setPlayerTurn(int whichPlayer) {
		if(whichPlayer == GameBoard.WHITESTONE) {
			whoseTurn.setText("It's Player 1's Turn");
		} else if (whichPlayer == GameBoard.BLACKSTONE) {
			whoseTurn.setText("It's Player 2's Turn");
		}
		
		Rectangle r = whoseTurn.getVisibleRect();
		if(firstGame == true) {
			whoseTurn.repaint();
		} else {
			whoseTurn.paintImmediately(r);
		}
	}
	
	public void setCaptures(int c, int whichPlayer) {
		if(whichPlayer == GameBoard.WHITESTONE) {
			p1Score.setText("<html><div style='text-align: center'>Player 1 Score<br>" + c + "</div></html>");
		} else if (whichPlayer == GameBoard.BLACKSTONE) {
			p2Score.setText("<html><div style='text-align: center'>Player 2 Name<br>" + c + "</div></html>");
		}
		
		Rectangle r1 = p1Score.getVisibleRect();
		Rectangle r2 = p2Score.getVisibleRect();
		
		if(firstGame == true) {
			p1Score.repaint();
			p2Score.repaint();
		} else {
			p1Score.paintImmediately(r1);
			p2Score.paintImmediately(r2);
		}
	}

	public void setGameBoard(GameBoard gb) {
		myBoard = gb;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		firstGame = false;
		JOptionPane.showMessageDialog(null, "Starting New Game");
		if(myBoard != null) myBoard.startNewGame(false);
		
	}

}
