
public class CMObject {
	//DATA
	private int priority;
	private int row = -1;
	private int col = -1;
	private int moveType = 0;
	
	//CONSTRUCTOR
	
	//BEHAVIORS / METHODS
	
	//setters
	public void setPriority(int newP){
		priority = newP;
	}
	
	public void setRow (int newR){
		row = newR;
	}
	
	public void setCol (int newC){
		col = newC;
	}
	
	public void setMoveType(int newT){
		moveType = newT;
	}
	
	//getters
	public int getPriority(){
		return priority;
	}
	
	public int getRow (){
		return row;
	}
	
	public int getCol (){
		return col;
	}
	
	public int getMoveType(){
		return moveType;
	}
}
