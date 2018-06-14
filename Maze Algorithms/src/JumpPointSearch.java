import java.util.ArrayList;

public class JumpPointSearch extends MazeSearch {

	private ArrayList<Cell> open_list = new ArrayList<Cell>();
	private ArrayList<Integer> fcosts_list = new ArrayList<Integer>();
	private Cell path_cell = null;
	public boolean path_drawn = false;
	private String distance_substring = " estimated path distance";
	
	public JumpPointSearch(int[][] generated_maze) {
		super(generated_maze);
		prepareSearch();
	}

	@Override
	public boolean isDone() {
		return path_drawn;
	}

	@Override
	protected void prepareSearch() {
		start_cell.parent = start_cell;
		open_list.add(start_cell);
		fcosts_list.add(0);
	}

	@Override
	protected void expandSearch() {
		if (end_cell == null || open_list.size() <= 0) {
			distance = -1;
			maze[start_cell.y][start_cell.x] = END;
			path_drawn = true;
		}
		else if (path_cell != null) {	// Backtrack
			steps--;
			do {
				path_cell = super.backtrackPath(path_cell);
				if (maze[path_cell.y][path_cell.x] == START) path_drawn = true;
			} while (maze[path_cell.y][path_cell.x] == PASSAGE && !path_drawn);
			if (!path_drawn)
				path_cell = super.backtrackPath(path_cell);
		}
		else {							// Search
			Cell cell = open_list.remove(0);
			fcosts_list.remove(0);
			computeNeighbors(cell);
			if (maze[cell.y][cell.x] != START) maze[cell.y][cell.x] = CLOSED;
			distance = cell.getFunctionalCost();
		}
	}
	
	private void computeNeighbors(Cell c) {
		int y = c.y, x = c.x;
		boolean[] directions = new boolean[4]; // up, down, left, right
		final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
		
		if (y > 1 && (maze[y-1][x] == PASSAGE || maze[y-1][x] == END) && c.parent.y >= y) 
			directions[UP - 1] = true;
		if (y < maze.length-2 && (maze[y+1][x] == PASSAGE || maze[y+1][x] == END) && c.parent.y <= y) 
			directions[DOWN - 1] = true;
		if (x > 1 && (maze[y][x-1] == PASSAGE || maze[y][x-1] == END) && c.parent.x >= x) 
			directions[LEFT - 1] = true;
		if (x < maze[0].length-2 && (maze[y][x+1] == PASSAGE || maze[y][x+1] == END) && c.parent.x <= x) 
			directions[RIGHT - 1] = true;
		
		if (directions[UP - 1] == true) {
			Cell current = c;
			while (current.y > 1 && maze[current.y-1][current.x] == PASSAGE && 
				  (maze[current.y-2][current.x] == PASSAGE || (maze[current.y-2][current.x] == END))) {
				Cell connection_cell = new Cell(current.y - 1, current.x, current.getCost() + 1);
				Cell jump_cell = new Cell(current.y - 2, current.x, current.getCost() + 2);
				connection_cell.parent = current;
				jump_cell.parent = connection_cell;
				current = jump_cell;
				if (current.y == end_cell.y && current.x == end_cell.x) {
					path_cell = current;
					path_cell = super.backtrackPath(path_cell);
					distance_substring = " path distance";
					break;
				}
				if ((maze[current.y][current.x] == PASSAGE) &&																// Cell is a passage
					((current.x > 1 && (maze[current.y][current.x-1] == PASSAGE || maze[current.y][current.x-1] == END) && 	// Passage on the left AND...
					(maze[current.y-1][current.x-1] == WALL || maze[current.y+1][current.x-1] == WALL)) ||		// walls on either side of said passsage
					(current.x < maze[0].length-2 && (maze[current.y][current.x+1] == PASSAGE || maze[current.y][current.x+1] == END) &&
					(maze[current.y-1][current.x+1] == WALL || maze[current.y+1][current.x+1] == WALL)))) {
					addOpenCell(current);
					maze[current.y][current.x] = OPEN;
					break;
				}
			}
		}
		if (directions[DOWN - 1] == true) {
			Cell current = c;
			while (current.y < maze.length-2 && maze[current.y+1][current.x] == PASSAGE && 
				  (maze[current.y+2][current.x] == PASSAGE || (maze[current.y+2][current.x] == END))) {
				Cell connection_cell = new Cell(current.y + 1, current.x, current.getCost() + 1);
				Cell jump_cell = new Cell(current.y + 2, current.x, current.getCost() + 2);
				connection_cell.parent = current;
				jump_cell.parent = connection_cell;
				current = jump_cell;
				if (current.y == end_cell.y && current.x == end_cell.x) {
					path_cell = current;
					path_cell = super.backtrackPath(path_cell);
					distance_substring = " path distance";
					break;
				}
				if ((maze[current.y][current.x] == PASSAGE) &&
					((current.x > 1 && (maze[current.y][current.x-1] == PASSAGE || maze[current.y][current.x-1] == END) && 
					(maze[current.y-1][current.x-1] == WALL || maze[current.y+1][current.x-1] == WALL)) ||
					(current.x < maze[0].length-2 && (maze[current.y][current.x+1] == PASSAGE || maze[current.y][current.x+1] == END) &&
					(maze[current.y-1][current.x+1] == WALL || maze[current.y+1][current.x+1] == WALL)))) {
					addOpenCell(current);
					maze[current.y][current.x] = OPEN;
					break;
				}
			}
		}
		if (directions[LEFT - 1] == true) {
			Cell current = c;
			while (current.x > 1 && maze[current.y][current.x-1] == PASSAGE && 
				  (maze[current.y][current.x-2] == PASSAGE || (maze[current.y][current.x-2] == END))) {
				Cell connection_cell = new Cell(current.y, current.x - 1, current.getCost() + 1);
				Cell jump_cell = new Cell(current.y, current.x - 2, current.getCost() + 2);
				connection_cell.parent = current;
				jump_cell.parent = connection_cell;
				current = jump_cell;
				if (current.y == end_cell.y && current.x == end_cell.x) {
					path_cell = current;
					path_cell = super.backtrackPath(path_cell);
					distance_substring = " path distance";
					break;
				}
				if ((maze[current.y][current.x] == PASSAGE) &&
					((current.y > 1 && (maze[current.y-1][current.x] == PASSAGE || maze[current.y-1][current.x] == END) &&
					(maze[current.y-1][current.x-1] == WALL || maze[current.y-1][current.x+1] == WALL)) ||
					(current.y < maze.length-2 && (maze[current.y+1][current.x] == PASSAGE || maze[current.y+1][current.x] == END) &&
					(maze[current.y+1][current.x-1] == WALL || maze[current.y+1][current.x+1] == WALL)))) {
					addOpenCell(current);
					maze[current.y][current.x] = OPEN;
					break;
				}
			}
		}
		if (directions[RIGHT - 1] == true) {
			Cell current = c;
			while (current.x < maze[0].length-2 && maze[current.y][current.x+1] == PASSAGE && 
				  (maze[current.y][current.x+2] == PASSAGE || (maze[current.y][current.x+2] == END))) {
				Cell connection_cell = new Cell(current.y, current.x + 1, current.getCost() + 1);
				Cell jump_cell = new Cell(current.y, current.x + 2, current.getCost() + 2);
				connection_cell.parent = current;
				jump_cell.parent = connection_cell;
				current = jump_cell;
				if (current.y == end_cell.y && current.x == end_cell.x) {
					path_cell = current;
					path_cell = super.backtrackPath(path_cell);
					distance_substring = " path distance";
					break;
				}
				if ((maze[current.y][current.x] == PASSAGE) &&
					((current.y > 1 && (maze[current.y-1][current.x] == PASSAGE || maze[current.y-1][current.x] == END) &&
					(maze[current.y-1][current.x-1] == WALL || maze[current.y-1][current.x+1] == WALL)) ||
					(current.y < maze.length-2 && (maze[current.y+1][current.x] == PASSAGE || maze[current.y+1][current.x] == END) &&
					(maze[current.y+1][current.x-1] == WALL || maze[current.y+1][current.x+1] == WALL)))) {
					addOpenCell(current);
					maze[current.y][current.x] = OPEN;
					break;
				}
			}
		}
	}
	
	private void addOpenCell(Cell c) {
		int functional_cost = c.getFunctionalCost();
		for (int i = 0; i < open_list.size(); i++) {
			if (functional_cost < fcosts_list.get(i)) {
				open_list.add(i, c);
				fcosts_list.add(i, functional_cost);
				return;
			}
		}
		open_list.add(c);
		fcosts_list.add(functional_cost);
	}
	
	@Override
	public String getDistanceString() {
		return distance + distance_substring;
	}

}
