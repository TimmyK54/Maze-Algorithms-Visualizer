import java.util.Random;

public abstract class Maze {
	
	protected int[][] maze;
	protected Random random = new Random();
	
	public final static int 
		// Used for maze generation
		WALL 		= 0, 
		PASSAGE 	= 1, 
		START 		= 2, 
		END 		= 3, 
		FRONTIER 	= 4,
		
		// Used for maze searching
		OPEN		= 5,	// Open nodes
		CLOSED	 	= 6,	// Closed nodes
		PATH		= 7;	// Maze solution path
	
	public int[][] getMaze() { 
		return maze; 
	}
	
}
