import java.util.ArrayList;

public class ComputerMoveGenerator {
	
	//DATA
	public static final int OFFENSE = 1;
	public static final int DEFENSE = -1;
	
	GameBoard myGame;
	int myStone;
	
	ArrayList<CMObject> oMoves = new ArrayList<CMObject>();
	ArrayList<CMObject> dMoves = new ArrayList<CMObject>();

	//CONSTRUCTOR
	public ComputerMoveGenerator(GameBoard gb, int stoneColor) {
		myStone = stoneColor;
		myGame = gb;
		
		//System.out.println("Computer is playing as player " + myStone);
	}
	
	//BEHAVIORS
	public int[] getComputerMove() {
		int[] newMove;
		
		findOffMoves();
		findDefMoves();
		
		newMove = generateRandomMove();
		
		try {
			sleepForAMove();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return newMove;
	}
	
	public void findDefMoves() {
		findOneDef();
	}
	
	public void findOffMoves() {
		
	}
	
	public void findOneDef() {
		
	}
	
	public int[] generateRandomMove() {
		 int[] move = new int[2];
		 boolean done = false;
		 int newR, newC;
		
		 do {
			 newR = (int)(Math.random() * GameBoard.NUM_SQUARE_SIDE);
			 newC = (int)(Math.random() * GameBoard.NUM_SQUARE_SIDE);
			 
			 if(myGame.getBoard()[newR][newC].getState() == GameBoard.EMPTY) {
				 done = true;
				 move[0] = newR;
				 move[1] = newC;
			 }
					
		 } while(!done);
		 
		 return move;
	}
	
	public void sleepForAMove() throws InterruptedException {
		Thread currThread = Thread.currentThread();
		currThread.sleep(GameBoard.SLEEP_TIME);
	}
}
