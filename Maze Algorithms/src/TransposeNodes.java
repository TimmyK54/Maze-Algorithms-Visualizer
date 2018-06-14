import java.util.ArrayList;

public class TransposeNodes extends Maze {

	boolean valid_maze = false, contains_start = false, contains_end = false;
	ArrayList<Cell> valid_cells = new ArrayList<Cell>();
	
	public TransposeNodes(int[][] maze_grid) {
		maze = maze_grid;
		clearNodes();
	}
	
	public boolean randomize() {
		if (valid_maze) {
			if (contains_start && contains_end && valid_cells.size() <= 1)
				return false;
			if (((contains_start || contains_end) && valid_cells.size() < 1))
				return false;
			if (contains_start) {
				Cell start = valid_cells.remove(random.nextInt(valid_cells.size()));
				maze[start.y][start.x] = START;
			}
			if (contains_end) {
				Cell end = valid_cells.remove(random.nextInt(valid_cells.size()));
				maze[end.y][end.x] = END;
			}
			return true;
		}
		return false;
	}
	
	public boolean maximize() {
		if (valid_maze) {
			int max_distance = 0;
			ArrayList<Cell[]> new_nodes_list = new ArrayList<Cell[]>();
			Cell[][] maze_cells = new Cell[maze.length][maze[0].length];
			for (int row = 1; row < maze.length; row += 2)
				for (int col = 1; col < maze[0].length; col += 2)
					maze_cells[row][col] = new Cell(row, col);
			
			for (int row = 1; row < maze.length; row += 2) {
				for (int col = 1; col < maze[0].length; col += 2) {
					ArrayList<Cell> open_list = new ArrayList<Cell>();
					ArrayList<Cell> closed_list = new ArrayList<Cell>();
					maze_cells[row][col].cost = 0;
					open_list.add(maze_cells[row][col]);
					
					while(open_list.size() > 0) {
						Cell current_cell = open_list.remove(0);
						if (current_cell.y > 1 && maze[current_cell.y - 1][current_cell.x] == PASSAGE) { 						// Up
							Cell neighbor_cell = maze_cells[current_cell.y - 2][current_cell.x];
							if (!closed_list.contains(neighbor_cell) && !open_list.contains(neighbor_cell)) {
								neighbor_cell.cost = current_cell.cost + 1;
								open_list.add(neighbor_cell);
							}
						}
						if (current_cell.y < maze.length - 2 && maze[current_cell.y + 1][current_cell.x] == PASSAGE) {			// Down
							Cell neighbor_cell = maze_cells[current_cell.y + 2][current_cell.x];
							if (!closed_list.contains(neighbor_cell) && !open_list.contains(neighbor_cell)) {
								neighbor_cell.cost = current_cell.cost + 1;
								open_list.add(neighbor_cell);
							}
						}
						if (current_cell.x > 1 && maze[current_cell.y][current_cell.x - 1] == PASSAGE) {						// Left
							Cell neighbor_cell = maze_cells[current_cell.y][current_cell.x - 2];
							if (!closed_list.contains(neighbor_cell) && !open_list.contains(neighbor_cell)) {
								neighbor_cell.cost = current_cell.cost + 1;
								open_list.add(neighbor_cell);
							}
						}
						if (current_cell.x < maze[0].length - 2 && maze[current_cell.y][current_cell.x + 1] == PASSAGE) {		// Right
							Cell neighbor_cell = maze_cells[current_cell.y][current_cell.x + 2];
							if (!closed_list.contains(neighbor_cell) && !open_list.contains(neighbor_cell)) {
								neighbor_cell.cost = current_cell.cost + 1;
								open_list.add(neighbor_cell);
							}
						}
						closed_list.add(current_cell);
					}
					
					int distance = closed_list.get(closed_list.size() - 1).cost;
					if (distance > max_distance) {
						new_nodes_list.clear();
						max_distance = distance;
						for (int i = closed_list.size() - 1; i >= 0; i--) {
							if (closed_list.get(i).cost == max_distance)
								new_nodes_list.add(new Cell[] { maze_cells[row][col], closed_list.get(i) });
							else
								break;
						}
					}
					else if (distance == max_distance) {
						for (int i = closed_list.size() - 1; i >= 0; i--) {
							if (closed_list.get(i).cost == max_distance)
								new_nodes_list.add(new Cell[] { maze_cells[row][col], closed_list.get(i) });
							else
								break;
						}
					}
				}
			}
			
			int new_nodes_index = random.nextInt(new_nodes_list.size());
//			if (random.nextInt(2) == 0) {
				maze[new_nodes_list.get(new_nodes_index)[0].y][new_nodes_list.get(new_nodes_index)[0].x] = START;
				maze[new_nodes_list.get(new_nodes_index)[1].y][new_nodes_list.get(new_nodes_index)[1].x] = END;
/*			}
			else {
				maze[new_nodes_list.get(new_nodes_index)[0].y][new_nodes_list.get(new_nodes_index)[0].x] = END;
				maze[new_nodes_list.get(new_nodes_index)[1].y][new_nodes_list.get(new_nodes_index)[1].x] = START;
			} */
			return true;
		}
		return false;
	}
	
	private void clearNodes() {
		for (int y = 0; y < maze.length; y++)
			for (int x = 0; x < maze[0].length; x++)
				if (maze[y][x] != WALL && maze[y][x] != FRONTIER) {
					if (maze[y][x] == START)
						contains_start = true;
					if (maze[y][x] == END)
						contains_end = true;
					maze[y][x] = PASSAGE;
					valid_maze = true;
					if (maze[y][x] == PASSAGE && y % 2 == 1 && x % 2 == 1)
						valid_cells.add(new Cell(y, x));
				}
	}
	
	private class Cell {
		public int y, x, cost;
		
		public Cell(int y, int x, int cost) {
			this.y = y;
			this.x = x;
			this.cost = cost;
		}
		public Cell(int y, int x) {
			this(y, x, 0);
		}
	}

}
