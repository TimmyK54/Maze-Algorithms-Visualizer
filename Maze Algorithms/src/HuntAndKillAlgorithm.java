import java.util.ArrayList;

public class HuntAndKillAlgorithm extends MazeGeneration {

	private ArrayList<Cell> frontier_list = new ArrayList<Cell>();
	private Cell[][] cell_list;
	private Cell current_cell;
	private boolean start_created = false;
	
	public HuntAndKillAlgorithm(int rows, int columns) {
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
		return start_created && frontier_list.size() == 0;
	}
	
	private void resetMaze() {
		super.resetMaze(WALL);
		cell_list = new Cell[maze.length][maze[0].length];
		
		for (int y = 1; y < maze.length; y += 2)
			for (int x = 1; x < maze[0].length; x += 2)
				cell_list[y][x] = new Cell(y, x);
	}
	
	private void createStartingCell() {
		int y_length = maze.length, x_length = maze[0].length;
		int y, x;
		do { y = random.nextInt(y_length); } while (y % 2 == 0);
		do { x = random.nextInt(x_length); } while (x % 2 == 0);
		
		maze[y][x] = START;
		current_cell = cell_list[y][x];
		current_cell.parent = current_cell;
		start_created = true;
		computeFrontier(current_cell);
	}

	@Override
	protected void expandMaze() {
		ArrayList<Cell> connection_list = getNeighborList(current_cell);
		
		if (connection_list.isEmpty()) {	// Hunt (Find frontier)
			current_cell = frontier_list.get(random.nextInt(frontier_list.size()));
			connectCell(current_cell);
		}
		else {								// Kill (Expand)
			Cell neighbor = connection_list.get(random.nextInt(connection_list.size()));
			if (maze[neighbor.y][neighbor.x] != START && maze[neighbor.y][neighbor.x] != END)
				maze[neighbor.y][neighbor.x] = PASSAGE;
			maze[(current_cell.y + neighbor.y) / 2][(current_cell.x + neighbor.x) / 2] = PASSAGE;
			neighbor.parent = current_cell;
			current_cell = neighbor;
		}
		
		frontier_list.remove(current_cell);
		computeFrontier(current_cell);
		if (frontier_list.size() == 0) maze[current_cell.y][current_cell.x] = END;
	}
	
	private void computeFrontier(Cell c) {
		int y = c.y, x = c.x;
		if (y > 2 					&& maze[y-2][x] == WALL) { frontier_list.add(cell_list[y-2][x]); maze[y-2][x] = FRONTIER; }
		if (x > 2 					&& maze[y][x-2] == WALL) { frontier_list.add(cell_list[y][x-2]); maze[y][x-2] = FRONTIER; }
		if (y < maze.length-3 		&& maze[y+2][x] == WALL) { frontier_list.add(cell_list[y+2][x]); maze[y+2][x] = FRONTIER; }
		if (x < maze[0].length-3 	&& maze[y][x+2] == WALL) { frontier_list.add(cell_list[y][x+2]); maze[y][x+2] = FRONTIER; }
	}
	
	private void connectCell(Cell c) {
		int y = c.y, x = c.x;
		ArrayList<Cell> connection_list = new ArrayList<Cell>();
		if (y > 2 					&& maze[y-2][x] != WALL && maze[y-2][x] != FRONTIER) connection_list.add(new Cell(y-1,x));
		if (x > 2 					&& maze[y][x-2] != WALL && maze[y][x-2] != FRONTIER) connection_list.add(new Cell(y,x-1));
		if (y < maze.length-3 		&& maze[y+2][x] != WALL && maze[y+2][x] != FRONTIER) connection_list.add(new Cell(y+1,x));
		if (x < maze[0].length-3 	&& maze[y][x+2] != WALL && maze[y][x+2] != FRONTIER) connection_list.add(new Cell(y,x+1));
		Cell connection = connection_list.get(random.nextInt(connection_list.size()));
		maze[connection.y][connection.x] = PASSAGE;
		maze[y][x] = PASSAGE;
	}
	
	private ArrayList<Cell> getNeighborList(Cell c) {
		int y = c.y, x = c.x;
		ArrayList<Cell> connection_list = new ArrayList<Cell>();
		if (y > 1 					&& (maze[y-2][x] == WALL || maze[y-2][x] == FRONTIER)) connection_list.add(cell_list[y-2][x]);
		if (x > 1 					&& (maze[y][x-2] == WALL || maze[y][x-2] == FRONTIER)) connection_list.add(cell_list[y][x-2]);
		if (y < maze.length-2 		&& (maze[y+2][x] == WALL || maze[y+2][x] == FRONTIER)) connection_list.add(cell_list[y+2][x]);
		if (x < maze[0].length-2 	&& (maze[y][x+2] == WALL || maze[y][x+2] == FRONTIER)) connection_list.add(cell_list[y][x+2]);
		return connection_list;
	}

}
