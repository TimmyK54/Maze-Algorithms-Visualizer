import java.util.ArrayList;

public class DepthFirstSearch extends MazeSearch {
	
	private Cell[][] cell_list;
	private Cell current_cell;
	
	public DepthFirstSearch(int[][] generated_maze) {
		super(generated_maze);
		prepareSearch();
	}

	@Override
	public boolean isDone() {
		if (maze[current_cell.y][current_cell.x] == END) 
			return true;
		return false;
	}
	
	@Override
	protected void prepareSearch() {
		cell_list = new Cell[maze.length][maze[0].length];
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				cell_list[y][x] = new Cell(y, x);
			}
		}
		current_cell = cell_list[start_cell.y][start_cell.x];
		current_cell.parent = current_cell;
	}
	
	@Override
	protected void expandSearch() {
		ArrayList<Cell> connection_list = getNeighborList(current_cell);
		
		if (connection_list.isEmpty()) {	// Backtrack
			if (current_cell == current_cell.parent)
				maze[current_cell.y][current_cell.x] = END;
			if (maze[current_cell.y][current_cell.x] != START && maze[current_cell.y][current_cell.x] != END)
				maze[current_cell.y][current_cell.x] = CLOSED;
			current_cell = current_cell.parent;
			distance--;
		}
		else {								// Expand
			Cell neighbor = connection_list.get(random.nextInt(connection_list.size()));
			if (maze[current_cell.y][current_cell.x] != START && maze[current_cell.y][current_cell.x] != END)
				maze[current_cell.y][current_cell.x] = PATH;
			if (maze[neighbor.y][neighbor.x] != START && maze[neighbor.y][neighbor.x] != END)
				maze[neighbor.y][neighbor.x] = PATH;
			neighbor.parent = current_cell;
			current_cell = neighbor;
			distance++;
		}
	}
	
	private ArrayList<Cell> getNeighborList(Cell c) {
		ArrayList<Cell> connection_list = new ArrayList<Cell>();
		if (c.y > 1 				&& (maze[c.y-1][c.x] == PASSAGE || maze[c.y-1][c.x] == END)) connection_list.add(cell_list[c.y-1][c.x]);
		if (c.x > 1 				&& (maze[c.y][c.x-1] == PASSAGE || maze[c.y][c.x-1] == END)) connection_list.add(cell_list[c.y][c.x-1]);
		if (c.y < maze.length-2 	&& (maze[c.y+1][c.x] == PASSAGE || maze[c.y+1][c.x] == END)) connection_list.add(cell_list[c.y+1][c.x]);
		if (c.x < maze[0].length-2 	&& (maze[c.y][c.x+1] == PASSAGE || maze[c.y][c.x+1] == END)) connection_list.add(cell_list[c.y][c.x+1]);
		return connection_list;
	}
	
}
