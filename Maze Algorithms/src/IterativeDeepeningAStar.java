import java.util.ArrayList;

public class IterativeDeepeningAStar extends MazeSearch {

	private Cell current_cell;
	private int threshold, min_cost;
	
	public IterativeDeepeningAStar(int[][] generated_maze) {
		super(generated_maze);
		prepareSearch();
		threshold = current_cell.getFunctionalCost();
		System.out.println(threshold);
	}

	@Override
	public boolean isDone() {
		if (maze[current_cell.y][current_cell.x] == END) 
			return true;
		return false;
	}

	@Override
	protected void prepareSearch() {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				if (maze[y][x] != START && maze[y][x] != END && maze[y][x] != WALL)
					maze[y][x] = PASSAGE;
			}
		}
		current_cell = start_cell;
		current_cell.parent = current_cell;
		min_cost = 0;
	}

	@Override
	protected void expandSearch() {	
		if (current_cell.getFunctionalCost() > threshold) {		// New branch (functional cost exceeds threshold)
			if (current_cell.getFunctionalCost() < min_cost || min_cost == 0) 
				min_cost = current_cell.getFunctionalCost();
			maze[current_cell.y][current_cell.x] = CLOSED;
			current_cell = current_cell.parent;
		}
		else {													// Continue branch
			if (getNeighbor(current_cell) == null) {	// Backtrack
				maze[current_cell.y][current_cell.x] = CLOSED;
				current_cell = current_cell.parent;
			}
			else {										// Expand
				current_cell = getNeighbor(current_cell);
				if (maze[current_cell.y][current_cell.x] != START && maze[current_cell.y][current_cell.x] != END)
					maze[current_cell.y][current_cell.x] = PATH;
			}
		}
		if (current_cell == current_cell.parent) {				// New iteration
			prepareSearch();
			threshold = min_cost;
		}
	}
	
	private Cell getNeighbor(Cell c) {
		Cell neighbor = null;
		if 		(c.y > 1 				&& (maze[c.y-1][c.x] == PASSAGE || maze[c.y-1][c.x] == END)) neighbor = new Cell(c.y-1,c.x);
		else if (c.x > 1 				&& (maze[c.y][c.x-1] == PASSAGE || maze[c.y][c.x-1] == END)) neighbor = new Cell(c.y,c.x-1);
		else if (c.y < maze.length-2 	&& (maze[c.y+1][c.x] == PASSAGE || maze[c.y+1][c.x] == END)) neighbor = new Cell(c.y+1,c.x);
		else if (c.x < maze[0].length-2 && (maze[c.y][c.x+1] == PASSAGE || maze[c.y][c.x+1] == END)) neighbor = new Cell(c.y,c.x+1);
		
		if (neighbor != null) neighbor.parent = c;
		return neighbor;
	}

}
