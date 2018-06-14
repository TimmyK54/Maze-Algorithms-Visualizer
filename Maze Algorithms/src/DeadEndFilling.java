import java.util.ArrayList;

public class DeadEndFilling extends MazeSearch {

	private ArrayList<Cell> deadend_list = new ArrayList<Cell>();
	private Cell current_cell;
	private boolean solution_drawn = false;
	
	public DeadEndFilling(int[][] generated_maze) {
		super(generated_maze);
		prepareSearch();
	}

	@Override
	public boolean isDone() {
		return solution_drawn;
	}

	@Override
	protected void prepareSearch() {
		current_cell = start_cell;
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[0].length; x++) {
				Cell cell = new Cell(y, x);
				if (isDeadEnd(cell)) 
					deadend_list.add(cell);
			}
		}
	}
	
	@Override
	protected void expandSearch() {
		if (deadend_list.size() > 0)
			fillCell(deadend_list.remove(random.nextInt(deadend_list.size())));
		else 
			drawPath(current_cell);
	}
	
	private boolean isDeadEnd(Cell c) {
		int y = c.y, x = c.x;
		if (maze[y][x] != PASSAGE)
			return false;
		int neighbors = 0;
		if (y > 1 && (maze[y-1][x] == PASSAGE || maze[y-1][x] == END || maze[y-1][x] == START))
			neighbors++;
		if (x > 1 && (maze[y][x-1] == PASSAGE || maze[y][x-1] == END || maze[y][x-1] == START))
			neighbors++;
		if (y < maze.length-2 && (maze[y+1][x] == PASSAGE || maze[y+1][x] == END || maze[y+1][x] == START))
			neighbors++;
		if (x < maze[0].length-2 && (maze[y][x+1] == PASSAGE || maze[y][x+1] == END || maze[y][x+1] == START))
			neighbors++;
		
		if (neighbors > 1) return false;
		return true;
	}
	
	private void fillCell(Cell c) {
		int y = c.y, x = c.x;
		maze[y][x] = CLOSED;
		Cell neighbor;
		if (y > 1 && (maze[y-1][x] == PASSAGE || maze[y-1][x] == END || maze[y-1][x] == START)) {
			neighbor = new Cell(y-1,x);
			if (isDeadEnd(neighbor)) 
				deadend_list.add(neighbor);
		}
		if (x > 1 && (maze[y][x-1] == PASSAGE || maze[y][x-1] == END || maze[y][x-1] == START)) {
			neighbor = new Cell(y,x-1);
			if (isDeadEnd(neighbor)) 
				deadend_list.add(neighbor);
		}
		if (y < maze.length-2 && (maze[y+1][x] == PASSAGE || maze[y+1][x] == END || maze[y+1][x] == START)) {
			neighbor = new Cell(y+1,x);
			if (isDeadEnd(neighbor)) 
				deadend_list.add(neighbor);
		}
		if (x < maze[0].length-2 && (maze[y][x+1] == PASSAGE || maze[y][x+1] == END || maze[y][x+1] == START)) {
			neighbor = new Cell(y,x+1);
			if (isDeadEnd(neighbor)) 
				deadend_list.add(neighbor);
		}
	}
	
	private void drawPath(Cell c) {
		int y = c.y, x = c.x;
		Cell path_neighbor = null;
		if (y > 1 && (maze[y-1][x] == PASSAGE || maze[y-1][x] == END))
			path_neighbor = new Cell(y-1, x);
		else if (x > 1 && (maze[y][x-1] == PASSAGE || maze[y][x-1] == END))
			path_neighbor = new Cell(y, x-1);
		else if (y < maze.length-2 && (maze[y+1][x] == PASSAGE || maze[y+1][x] == END))
			path_neighbor = new Cell(y+1, x);
		else if (x < maze[0].length-2 && (maze[y][x+1] == PASSAGE || maze[y][x+1] == END))
			path_neighbor = new Cell(y, x+1);
		
		distance++;
		steps--;
		if (maze[y][x] != START)
			maze[y][x] = PATH;
		if (path_neighbor == null) {
			distance = -1;
			maze[current_cell.y][current_cell.x] = END;
			solution_drawn = true;
		}
		else if(maze[path_neighbor.y][path_neighbor.x] != END) { 
			current_cell = path_neighbor;
			maze[path_neighbor.y][path_neighbor.x] = PATH;
		}
		else
			solution_drawn = true;
	}

}
