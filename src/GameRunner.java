import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class GameRunner {

	public static void main(String[] args) {
		
		int gWidth = 586;
		int gHeight = 752;
		int sbWidth = gWidth - 16;
		int sbHeight = 136;
		
		JFrame theGame = new JFrame("Play Pente!");
		theGame.setLayout(new BorderLayout());
		theGame.setSize(gWidth, gHeight);
		theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theGame.setResizable(false);
		
		//scoreboard setup
		ScoreBoard sb =  new ScoreBoard(sbWidth, sbHeight); //x - 8, y - gWidth - 16
		sb.setPreferredSize(new Dimension(sbWidth, sbHeight));
		sb.setMaximumSize(new Dimension(sbWidth, sbHeight));
		
		//gameboard setup
		GameBoard gb = new GameBoard(gWidth, gHeight-170, sb);
		gb.setPreferredSize(new Dimension(gWidth, gHeight-170));
		sb.setGameBoard(gb);
		
		//add components
		theGame.add(gb, BorderLayout.CENTER);
		theGame.add(sb, BorderLayout.PAGE_END);
		
		//final setup
		theGame.setVisible(true);
		gb.startNewGame(true);
	}

}