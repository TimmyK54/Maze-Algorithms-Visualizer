import java.util.ArrayList;

public class KruskalsAlgorithm extends MazeGeneration {

	private ArrayList<Cell> potential_frontiers = new ArrayList<Cell>();
	private Cell[][] disjoint_set;
	private int remaining_sets = 0, total_sets;
	
	public KruskalsAlgorithm(int rows, int columns) {
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
		if (remaining_sets == 1) return true;
		return false;
	}
	
	private void resetMaze() {
		super.resetMaze(WALL);
		
		disjoint_set = new Cell[maze.length][maze[0].length];
		for (int y = 1; y < maze.length; y += 2) {
			for (int x = 1; x < maze[0].length; x += 2) {
				Cell cell = new Cell(y, x);
				makeSet(cell);
				potential_frontiers.add(cell);
			}
		}
		
		total_sets = remaining_sets;
	}
	
	@Override
	protected void expandMaze() {
		Cell frontier = null, neighbor;
		
		// Get frontier cell
		do {
			frontier = potential_frontiers.get(random.nextInt(potential_frontiers.size()));
		} while (!checkNeighborCells(frontier));
		
		// Get neighbor and unite with frontier 
		ArrayList<Cell> connection_list = getNeighborList(frontier);
		neighbor = connection_list.get(random.nextInt(connection_list.size()));
		union(frontier, neighbor);
		if 		(remaining_sets + 1 == total_sets || maze[frontier.y][frontier.x] == START) maze[frontier.y][frontier.x] = START;
		else if (remaining_sets == 1) 														maze[frontier.y][frontier.x] = END;
		else																				maze[frontier.y][frontier.x] = PASSAGE;
		if (maze[neighbor.y][neighbor.x] == WALL) 											maze[neighbor.y][neighbor.x] = PASSAGE;
		maze[(frontier.y + neighbor.y) / 2][(frontier.x + neighbor.x) / 2] = PASSAGE;
		checkNeighborCells(frontier);
		checkNeighborCells(neighbor);
	}
	
	private ArrayList<Cell> getNeighborList(Cell c) {
		Cell neighbor, neighbor_root, c_root = find(c);
		ArrayList<Cell> connection_list = new ArrayList<Cell>();
		if (c.y > 2) {
			neighbor = disjoint_set[c.y - 2][c.x];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) connection_list.add(neighbor);
			else checkNeighborCells(neighbor);
		}
		if (c.x > 2) {
			neighbor = disjoint_set[c.y][c.x - 2];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) connection_list.add(neighbor);
			else checkNeighborCells(neighbor);
		}
		if (c.y < maze.length - 3) {
			neighbor = disjoint_set[c.y + 2][c.x];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) connection_list.add(neighbor);
			else checkNeighborCells(neighbor);
		}
		if (c.x < maze[0].length - 3) {
			neighbor = disjoint_set[c.y][c.x + 2];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) connection_list.add(neighbor);
			else checkNeighborCells(neighbor);
		}
		return connection_list;
	}
	
	private boolean checkNeighborCells(Cell c) {
		Cell neighbor, neighbor_root, c_root = find(c);
		if (c.y > 2) {
			neighbor = disjoint_set[c.y - 2][c.x];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) return true;
		}
		if (c.x > 2) {
			neighbor = disjoint_set[c.y][c.x - 2];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) return true;
		}
		if (c.y < maze.length - 3) {
			neighbor = disjoint_set[c.y + 2][c.x];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) return true;
		}
		if (c.x < maze[0].length - 3) {
			neighbor = disjoint_set[c.y][c.x + 2];
			neighbor_root = find(neighbor);
			if (c_root != neighbor_root) return true;
		}
		potential_frontiers.remove(c);
		return false;
	}
	
	// DISJOINT-SET DATA STRUCTURE OPERATIONS:
	
	// Adds cell to disjoint_set if it is not in it already
	public void makeSet(Cell c) {
		if (disjoint_set[c.y][c.x] == null) {
			c.parent = c;
			c.rank = 0;
			disjoint_set[c.y][c.x] = c;
			remaining_sets++;
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
		remaining_sets--;
		return true;
	}
	
}
