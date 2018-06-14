import java.util.ArrayList;

public class DepthFirstGeneration extends MazeGeneration {
	
	private Cell[][] cell_list;
	private Cell current_cell, start_cell;
	private int unvisited_cells = 0;
	private boolean start_created = false;
	
	public DepthFirstGeneration(int rows, int columns) {
		super(rows, columns);
		resetMaze();
	}

	@Override
	public void generateStep() {
		super.generateStep();
		if (!start_created) createStartingCell();
		else expandMaze();
	}

	@Override
	public boolean isDone() {
		return start_created && unvisited_cells == 0 && current_cell == start_cell;
	}
	
	private void resetMaze() {
		super.resetMaze(WALL);
		cell_list = new Cell[maze.length][maze[0].length];
		
		for (int y = 1; y < maze.length; y += 2) {
			for (int x = 1; x < maze[0].length; x += 2) {
				cell_list[y][x] = new Cell(y, x);
				unvisited_cells++;
			}
		}
	}
	
	private void createStartingCell() {
		int y_length = maze.length, x_length = maze[0].length;
		int y, x;
		do { y = random.nextInt(y_length); } while (y % 2 == 0);
		do { x = random.nextInt(x_length); } while (x % 2 == 0);
		
		maze[y][x] = START;
		start_cell = cell_list[y][x];
		start_cell.visited = true;
		start_cell.parent = start_cell;
		current_cell = start_cell;
		unvisited_cells--;
		start_created = true;
	}
	
	@Override
	protected void expandMaze() {
		ArrayList<Cell> connection_list = getNeighborList(current_cell);
		
		if (connection_list.isEmpty()) {	// Backtrack
			if (maze[current_cell.y][current_cell.x] != START && maze[current_cell.y][current_cell.x] != END) {
				maze[current_cell.y][current_cell.x] = PASSAGE;
			}
			maze[(current_cell.y + current_cell.parent.y) / 2][(current_cell.x + current_cell.parent.x) / 2] = PASSAGE;
			current_cell = current_cell.parent;
		}
		else {								// Expand
			Cell neighbor = connection_list.get(random.nextInt(connection_list.size()));
			if (maze[neighbor.y][neighbor.x] != START && maze[neighbor.y][neighbor.x] != END)
				maze[neighbor.y][neighbor.x] = FRONTIER;
			maze[(current_cell.y + neighbor.y) / 2][(current_cell.x + neighbor.x) / 2] = FRONTIER;
			unvisited_cells--;
			cell_list[neighbor.y][neighbor.x].visited = true;
			neighbor.parent = current_cell;
			current_cell = neighbor;
		}
	}
	
	private ArrayList<Cell> getNeighborList(Cell c) {
		ArrayList<Cell> connection_list = new ArrayList<Cell>();
		if (c.y > 1 				&& !cell_list[c.y-2][c.x].visited) connection_list.add(cell_list[c.y-2][c.x]);
		if (c.x > 1 				&& !cell_list[c.y][c.x-2].visited) connection_list.add(cell_list[c.y][c.x-2]);
		if (c.y < maze.length-2 	&& !cell_list[c.y+2][c.x].visited) connection_list.add(cell_list[c.y+2][c.x]);
		if (c.x < maze[0].length-2 	&& !cell_list[c.y][c.x+2].visited) connection_list.add(cell_list[c.y][c.x+2]);
		return connection_list;
	}
	
}
