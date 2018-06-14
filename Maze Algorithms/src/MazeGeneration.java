
public abstract class MazeGeneration extends Maze {
	
	protected int steps; 	// Number of steps to generate maze
	
	public MazeGeneration(int rows, int columns) {
		rows = rows + 2 + (1 - rows % 2);
		columns = columns + 2 + (1 - columns % 2);
		if (rows < 5) rows = 5;
		if (columns < 5) columns = 5;
		maze = new int[rows][columns];
	}
	
	public boolean containsStart() {			// Returns whether or not maze contains a start node
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				if (maze[y][x] == START) return true;
			}
		}
		return false;
	}
	
	public int getSteps() {;
		return steps;
	}
	
	public void setMaze(int[][] new_maze) {		// Only used for transposing nodes
		maze = new_maze;
	}
	
	public void generateAuto() {				// Automatically generates entire maze
		while (!isDone()) {
			generateStep();
		}
	}
	
	public void generateStep() {				// Generates next step of maze (must be overridden)
		steps++;
	}
	
	public void removeNodes() {					// Remove starting and ending nodes
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				if (maze[y][x] == START || maze[y][x] == END) maze[y][x] = PASSAGE;
			}
		}
	}
	
	protected void resetMaze(int type) {		// Populates array with specified type	
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				maze[y][x] = type;
			}
		}
	}
	
	abstract public boolean isDone();			// Returns true if maze is finished generating
	abstract protected void expandMaze();		// Contains maze generation algorithm
	
	protected class Cell {
		
		public int y, x, rank, set;
		public boolean visited = false;
		public Cell parent;
		
		public Cell (int row, int col) {
			y = row;
			x = col;
		}
		
	}
	
}
