import java.util.ArrayList;

public class EllersAlgorithm extends MazeGeneration {

	private int current_row = 1, current_col = 1;
	private boolean done = false;
	private double horizontal_join_chance = 0.5, vertical_join_chance = 0.5;
	private boolean[] vertical_join; // Used to create at least one connection to the next row for each set
	private Cell[][] disjoint_set;
	
	public EllersAlgorithm(int rows, int columns) {
		super(rows, columns);
		resetMaze();
	}

	@Override
	public void generateStep() {
		super.generateStep();
		expandMaze();
	}
	
	@Override
	public boolean isDone() {
		return done;
	}

	private void resetMaze() {
		super.resetMaze(WALL);
		vertical_join = new boolean[maze[0].length];
		
		disjoint_set = new Cell[maze.length][maze[0].length];
		for (int y = 1; y < maze.length; y += 2)
			for (int x = 1; x < maze[0].length; x += 2)
				makeSet(new Cell(y, x));
	}
	
	@Override
	protected void expandMaze() {
		boolean completed;
		do {
			completed = true;
			if (current_row >= maze.length - 2 && current_col > maze[0].length - 2) {
				int top_node = (random.nextInt(2) == 0) ? START : END;
				int bottom_node = (top_node == START) ? END : START;
				maze[1][1 + 2 * random.nextInt((maze[0].length - 1)/2)] = top_node;
				maze[maze.length-2][1 + 2 * random.nextInt((maze[0].length - 1)/2)] = bottom_node;
				done = true;
			}
			else if (current_row == maze.length - 2) {			// Final row
				if (maze[current_row][current_col] == WALL) {		// New set
					completed = false;
					maze[current_row][current_col] = PASSAGE;
					if (current_col < 2)
						current_col += 2;
				}
				else if (current_col > 2) {							// Horizontal join
					Cell current_cell = disjoint_set[current_row][current_col];
					Cell previous_cell = disjoint_set[current_row][current_col - 2];
					if (union(current_cell, previous_cell))
						maze[current_row][(current_cell.x + previous_cell.x)/2] = PASSAGE;
					else 
						completed = false;
					
					if (current_col == maze[0].length - 2)
						completed = true;
					current_col += 2;
				}
				else {
					completed = false;
					current_col += 2;
				}
			}
			else if (current_row % 2 == 1) {					// Iterate horizontally
				if (current_col >= maze[0].length - 1) {					// End of row
					createNewRow();
					completed = false;
				}
				else if (maze[current_row][current_col] == WALL) {		// New set
					maze[current_row][current_col] = PASSAGE;
					if (current_col > 2) {
						Cell current_cell = disjoint_set[current_row][current_col];
						Cell previous_cell = disjoint_set[current_row][current_col - 2];
						if (Math.random() < horizontal_join_chance && union(current_cell, previous_cell))
							maze[current_row][(current_cell.x + previous_cell.x)/2] = PASSAGE;
						current_col += 2;
					}
				}
				else if (current_col > 2) {								// Horizontal join
					Cell current_cell = disjoint_set[current_row][current_col];
					Cell previous_cell = disjoint_set[current_row][current_col - 2];
					if (Math.random() < horizontal_join_chance && union(current_cell, previous_cell))
						maze[current_row][(current_cell.x + previous_cell.x)/2] = PASSAGE;
					else 
						completed = false;
					current_col += 2;
				}
				else {
					completed = false;
					current_col += 2;
				}
			}
			else if (current_row < maze.length - 1) {			// Vertical join
				if (current_col >= 1 && vertical_join[current_col]) {
					Cell current_cell = disjoint_set[current_row+1][current_col];
					Cell previous_cell = disjoint_set[current_row-1][current_col];
					union(current_cell, previous_cell);
					maze[current_row][current_col] = PASSAGE;
					maze[current_row + 1][current_col] = PASSAGE;
				}
				else 
					completed = false;
				current_col -= 2;
				if (current_col < 1) {								// End of row
					current_col = 1;
					current_row++;
				}
			}
		} while (!completed);
	}
	
	private void createNewRow() {
		ArrayList<Cell> unconnected_sets = new ArrayList<Cell>();
		ArrayList<Cell> sets = new ArrayList<Cell>();
		ArrayList<Integer> frequency = new ArrayList<Integer>();
		
		for (int i = maze[current_row].length - 2; i >= 0; i -= 2) {
			int set_index = sets.indexOf(find(disjoint_set[current_row][i]));
			if (set_index != -1)
				frequency.set(set_index, frequency.get(set_index) + 1);
			else {
				unconnected_sets.add(find(disjoint_set[current_row][i]));
				sets.add(find(disjoint_set[current_row][i]));
				frequency.add(1);
			}
		}
		
		for (int i = maze[current_row].length - 2; i >= 0; i -= 2) {
			vertical_join[i] = false;
			int set_index = sets.indexOf(find(disjoint_set[current_row][i]));
			if (frequency.get(set_index) == 1 || Math.random() < vertical_join_chance) {
				vertical_join[i] = true;
				if (unconnected_sets.contains(find(disjoint_set[current_row][i])))
					unconnected_sets.remove(find(disjoint_set[current_row][i]));
			}
		}
		
		for (Cell root : unconnected_sets) {
			int num = 0;
			for (int i = 1; i < maze[0].length; i += 2)
				if (find(disjoint_set[current_row][i]) == root)
					num++;
			num = random.nextInt(num);
			for (int i = 1; i < maze[0].length; i += 2) {
				if (find(disjoint_set[current_row][i]) == root) {
					if (num == 0) {
						vertical_join[i] = true;
						break;
					}
					num--;
				}
			}
		}
		
		current_col = maze[0].length-2;
		current_row++;
	}
	
	// DISJOINT-SET DATA STRUCTURE OPERATIONS:
	
	// Adds cell to disjoint_set if it is not in it already
	public void makeSet(Cell c) {
		if (disjoint_set[c.y][c.x] == null) {
			c.parent = c;
			c.rank = 0;
			disjoint_set[c.y][c.x] = c;
		}
	}
	
	// Finds the root cell of the given cell, with path compression
	public Cell find(Cell c) {
		if (c.parent != c)
			c.parent = find(c.parent);
		return c.parent;
	}
	
	// Unites two sets if they have different roots, returns true if operation was successful
	public boolean union(Cell c1, Cell c2) {
		Cell c1_root = find(c1);
		Cell c2_root = find(c2);
		
		if (c1_root == c2_root) 
			return false;
		
		if 		(c1_root.rank < c2_root.rank) 
			c1_root.parent = c2_root;
		else if (c2_root.rank < c1_root.rank) 
			c2_root.parent = c1_root;
		else {
			c2_root.parent = c1_root;
			c1_root.rank++;
		}
		return true;
	}

}
