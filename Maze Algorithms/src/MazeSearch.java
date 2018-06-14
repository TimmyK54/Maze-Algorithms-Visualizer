
public abstract class MazeSearch extends Maze {
	
	protected int steps; 	// Number of steps to solve maze
	protected int distance; // Distance between start and end nodes
	protected Cell start_cell, end_cell;
	
	public MazeSearch(int[][] generated_maze) {
		maze = new int[generated_maze.length][generated_maze[0].length];
		for (int y = 0; y < maze.length; y++)
			for (int x = 0; x < maze[0].length; x++)
				maze[y][x] = generated_maze[y][x];
		steps = 0;
		distance = 0;
		start_cell = getStart();
		end_cell = getEnd();
	}
	
	public int getSteps() {;
		return steps;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public String getDistanceString() {
		return distance + " path distance";
	}
	
	public void generateAuto() {				// Automatically solves entire maze
		while (!isDone()) {
			generateStep();
		}
	}
	
	public void generateStep() {				// Searches next step of maze (must be overridden)
		steps++;
		expandSearch();
	}
	
	protected Cell backtrackPath(Cell c) {		// Backtrack from c to parent, marking cells as path
		if (c != c.parent) {
			if (maze[c.y][c.x] != END) maze[c.y][c.x] = PATH;
			c = c.parent;
		}
		return c;
	}
	
	private Cell getStart() {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				if (maze[y][x] == START) return new Cell(y, x);
			}
		}
		return null;
	}
	
	private Cell getEnd() {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				if (maze[y][x] == END) return new Cell(y, x);
			}
		}
		return null;
	}
	
	abstract public boolean isDone();			// Returns true if maze is finished generating
	abstract protected void prepareSearch();	// Prepares initial variables for search
	abstract protected void expandSearch();		// Contains maze search algorithm
	
	protected class Cell {
		public int y, x;
		public Cell parent;			// Needed for backtracking solution
		private int path_cost;		// Needed for heuristic algorithms
		
		public Cell (int row, int col, int cost) {
			y = row;
			x = col;
			path_cost = cost;
		}
		public Cell (int row, int col) {
			this(row, col, 0);
		}
		
		public int getCost() {
			return path_cost;
		}
		public int getHeuristicCost() {
			return Math.abs(end_cell.y-this.y) + Math.abs(end_cell.x-this.x);
		}
		public int getFunctionalCost() {
			return path_cost + getHeuristicCost();
		}
		public void setCost(int cost) {
			path_cost = cost;
		}
	}
	
}
