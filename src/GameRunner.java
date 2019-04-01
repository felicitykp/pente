import javax.swing.JFrame;

public class GameRunner {

	public static void main(String[] args) {
		
		int gWidth = 700;
		int gHeight = 700;
		
		JFrame theGame = new JFrame("Play Pente!");
		theGame.setSize(gWidth, gHeight);
		theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GameBoard gb = new GameBoard(gWidth, gHeight-20);
		theGame.add(gb);
		
		theGame.setVisible(true);
		gb.startNewGame();
	}

}