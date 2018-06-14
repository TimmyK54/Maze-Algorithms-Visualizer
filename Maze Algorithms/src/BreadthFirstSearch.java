import java.util.ArrayList;

public class BreadthFirstSearch extends MazeSearch {
	
	Cell path_cell;
	private ArrayList<Cell> open_list = new ArrayList<Cell>();
	private ArrayList<Cell> closed_list = new ArrayList<Cell>();
	private boolean end_found = false, solution_drawn = false;
	
	public BreadthFirstSearch(int[][] generated_maze) {
		super(generated_maze);
		prepareSearch();
	}

	@Override
	public boolean isDone() {
		if (solution_drawn) 
			return true;
		return false;
	}

	@Override
	protected void prepareSearch() {
		open_list.add(start_cell);
		start_cell.parent = start_cell;
		start_cell.setCost(0);
	}
	
	@Override
	protected void expandSearch() {
		if (end_found) {					// Backtrack
			if (path_cell.y == end_cell.y && path_cell.x == end_cell.x) 
				path_cell = super.backtrackPath(path_cell);
			path_cell = super.backtrackPath(path_cell);
			if (maze[path_cell.y][path_cell.x] == START) solution_drawn = true;
			steps--;
		}
		else if (open_list.size() == 0) {	// End node not reachable
			distance = -1;
			maze[start_cell.y][start_cell.x] = END;
			solution_drawn = true;
		}
		else {								// Expand
			Cell open_cell = open_list.remove(0);
			computeNeighbors(open_cell);
			closeCell(open_cell);
			open_cell.setCost(open_cell.parent.getCost()+1);
			distance = open_cell.getCost();
		}
	}
	
	private void computeNeighbors(Cell c) {
		int y = c.y, x = c.x;
		Cell neighbor;
		if (y > 1 && (maze[y-1][x] == PASSAGE || maze[y-1][x] == END)) { 
			neighbor = new Cell(y-1,x);
			neighbor.parent = c;
			open_list.add(neighbor);
			if (maze[y-1][x] == END) {
				end_found = true;
				path_cell = neighbor;
			}
			else
				maze[y-1][x] = OPEN; 
		}
		if (x > 1 && (maze[y][x-1] == PASSAGE || maze[y][x-1] == END)) { 
			neighbor = new Cell(y,x-1);
			neighbor.parent = c;
			open_list.add(neighbor);
			if (maze[y][x-1] == END) {
				end_found = true;
				path_cell = neighbor;
			}
			else 
				maze[y][x-1] = OPEN; 
		}
		if (y < maze.length-2 && (maze[y+1][x] == PASSAGE || maze[y+1][x] == END)) { 
			neighbor = new Cell(y+1,x);
			neighbor.parent = c;
			open_list.add(neighbor);
			if (maze[y+1][x] == END) {
				end_found = true;
				path_cell = neighbor;
			}
			else 
				maze[y+1][x] = OPEN; 
		}
		if (x < maze[0].length-2 && (maze[y][x+1] == PASSAGE || maze[y][x+1] == END)) { 
			neighbor = new Cell(y,x+1);
			neighbor.parent = c;
			open_list.add(neighbor);
			if (maze[y][x+1] == END) {
				end_found = true;
				path_cell = neighbor;
			}
			else 
				maze[y][x+1] = OPEN; 
		}
	}
	
	private void closeCell(Cell c) {
		closed_list.add(c);
		if (maze[c.y][c.x] != START) maze[c.y][c.x] = CLOSED;
	}
	
}
