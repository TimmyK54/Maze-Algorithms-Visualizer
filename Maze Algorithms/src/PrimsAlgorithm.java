import java.util.ArrayList;

public class PrimsAlgorithm extends MazeGeneration {

	private ArrayList<int[]> frontier_list = new ArrayList<int[]>();
	private boolean start_created = false;
	
	public PrimsAlgorithm(int rows, int columns) {
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
		return start_created && frontier_list.isEmpty();
	}
	
	private void resetMaze() {
		super.resetMaze(WALL);
		frontier_list.clear();
		start_created = false;
	}
	
	// Pick a random starting cell and compute its frontier cells
	private void createStartingCell() {
		int y_length = maze.length, x_length = maze[0].length;
		int y, x;
		do { y = random.nextInt(y_length); } while (y % 2 == 0);
		do { x = random.nextInt(x_length); } while (x % 2 == 0);
		maze[y][x] = START;
		computeFrontier(y, x);
		start_created = true;
	}
	
	// Connect a random frontier cell and compute new frontier cells
	@Override
	protected void expandMaze() {
		int[] frontier_cell = frontier_list.remove(random.nextInt(frontier_list.size()));
		computeFrontier(frontier_cell[0], frontier_cell[1]);
		connectCell(frontier_cell[0], frontier_cell[1]);
	}
	
	private void computeFrontier(int y, int x) {
		if (y > 2 					&& maze[y-2][x] == WALL) { frontier_list.add(new int[] {y-2,x}); maze[y-2][x] = FRONTIER; }
		if (x > 2 					&& maze[y][x-2] == WALL) { frontier_list.add(new int[] {y,x-2}); maze[y][x-2] = FRONTIER; }
		if (y < maze.length-3 		&& maze[y+2][x] == WALL) { frontier_list.add(new int[] {y+2,x}); maze[y+2][x] = FRONTIER; }
		if (x < maze[0].length-3 	&& maze[y][x+2] == WALL) { frontier_list.add(new int[] {y,x+2}); maze[y][x+2] = FRONTIER; }
	}
	
	private void connectCell(int y, int x) {
		ArrayList<int[]> connection_list = new ArrayList<int[]>();
		if (y > 2 					&& maze[y-2][x] != WALL && maze[y-2][x] != FRONTIER) connection_list.add(new int[] {y-1,x});
		if (x > 2 					&& maze[y][x-2] != WALL && maze[y][x-2] != FRONTIER) connection_list.add(new int[] {y,x-1});
		if (y < maze.length-3 		&& maze[y+2][x] != WALL && maze[y+2][x] != FRONTIER) connection_list.add(new int[] {y+1,x});
		if (x < maze[0].length-3 	&& maze[y][x+2] != WALL && maze[y][x+2] != FRONTIER) connection_list.add(new int[] {y,x+1});
		int[] connection = connection_list.get(random.nextInt(connection_list.size()));
		maze[connection[0]][connection[1]] = PASSAGE;
		if (frontier_list.size() == 0) maze[y][x] = END;
		else maze[y][x] = PASSAGE;
	}

}
