import java.util.ArrayList;

public class BestFirstSearch extends MazeSearch {

	private ArrayList<Cell> open_list = new ArrayList<Cell>();
	private ArrayList<Integer> hcosts_list = new ArrayList<Integer>();
	private Cell path_cell = null;
	private boolean solution_drawn = false;
	private String distance_substring = " estimated path distance";
	
	public BestFirstSearch(int[][] generated_maze) {
		super(generated_maze);
		prepareSearch();
	}

	@Override
	public boolean isDone() {
		return solution_drawn;
	}
	
	@Override
	protected void prepareSearch() {
		open_list.add(start_cell);
		hcosts_list.add(0);
	}

	@Override
	protected void expandSearch() {
		if (end_cell == null) {
			distance = -1;
			maze[start_cell.y][start_cell.x] = END;
			solution_drawn = true;
		}
		else if (path_cell != null) {	// Backtrack
			steps--;
			path_cell = super.backtrackPath(path_cell);
			if (maze[path_cell.y][path_cell.x] == START) solution_drawn = true;
		}
		else {							// Search
			Cell cell = open_list.remove(0);
			hcosts_list.remove(0);
			computeNeighbors(cell);
			if (maze[cell.y][cell.x] != START) maze[cell.y][cell.x] = CLOSED;
			distance = cell.getFunctionalCost();
		}
		
	}
	
	private void computeNeighbors(Cell c) {
		int y = c.y, x = c.x;
		Cell neighbor;
		if (y > 1 && (maze[y-1][x] == PASSAGE || maze[y-1][x] == END)) { 
			neighbor = new Cell(y-1, x, c.getCost()+1);
			neighbor.parent = c;
			addOpenCell(neighbor);
			if (maze[y-1][x] != END) 
				maze[y-1][x] = OPEN; 
			else {
				path_cell = neighbor;
				path_cell = super.backtrackPath(path_cell);
				distance_substring = " path distance";
			}
		}
		if (x > 1 && (maze[y][x-1] == PASSAGE || maze[y][x-1] == END)) { 
			neighbor = new Cell(y, x-1, c.getCost()+1);
			neighbor.parent = c;
			addOpenCell(neighbor);
			if (maze[y][x-1] != END) 
				maze[y][x-1] = OPEN; 
			else {
				path_cell = neighbor;
				path_cell = super.backtrackPath(path_cell);
				distance_substring = " path distance";
			} 
		}
		if (y < maze.length-2 && (maze[y+1][x] == PASSAGE || maze[y+1][x] == END)) { 
			neighbor = new Cell(y+1, x, c.getCost()+1);
			neighbor.parent = c;
			addOpenCell(neighbor);
			if (maze[y+1][x] != END) 
				maze[y+1][x] = OPEN; 
			else {
				path_cell = neighbor;
				path_cell = super.backtrackPath(path_cell);
				distance_substring = " path distance";
			}
		}
		if (x < maze[0].length-2 && (maze[y][x+1] == PASSAGE || maze[y][x+1] == END)) { 
			neighbor = new Cell(y, x+1, c.getCost()+1);
			neighbor.parent = c;
			addOpenCell(neighbor);
			if (maze[y][x+1] != END)
				maze[y][x+1] = OPEN; 
			else {
				path_cell = neighbor;
				path_cell = super.backtrackPath(path_cell);
				distance_substring = " path distance";
			}
		}
	}
	
	private void addOpenCell(Cell c) {
		int heuristic_cost = c.getHeuristicCost();
		for (int i = 0; i < open_list.size(); i++) {
			if (heuristic_cost < hcosts_list.get(i)) {
				open_list.add(i, c);
				hcosts_list.add(i, heuristic_cost);
				return;
			}
		}
		open_list.add(c);
		hcosts_list.add(heuristic_cost);
	}
	
	@Override
	public String getDistanceString() {
		return distance + distance_substring;
	}
		
}
