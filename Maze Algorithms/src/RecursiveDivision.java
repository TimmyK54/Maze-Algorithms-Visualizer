import java.util.ArrayList;

public class RecursiveDivision extends MazeGeneration {
	
	private ArrayList<Area> areas_list = new ArrayList<Area>();
	private boolean first_iteration = true;
	private final int
		HORIZONTAL = 5,
		VERTICAL = 10;
	
	public RecursiveDivision(int rows, int columns) {
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
		return areas_list.size() == 0;
	}
	
	private void resetMaze() {
		super.resetMaze(PASSAGE);
		for (int y = 0; y < maze.length; y++) {
			maze[y][0] = WALL;
			maze[y][maze[0].length-1] = WALL;
			if (y == 0 || y == maze.length-1)
				for (int x = 1; x < maze[0].length-1; x++)
					maze[y][x] = WALL;
		}
		createStartEndNodes();
		areas_list.add(new Area(1, 1, maze.length-2, maze[0].length-2));
	}
	
	private void createStartEndNodes() {
		maze[1][1] = END;
		maze[maze.length-2][maze[0].length-2] = START;
	}

	@Override
	protected void expandMaze() {
		if (first_iteration) {
			first_iteration = false;
			return;
		}
		
		Area area = areas_list.remove(0);
		
		// Split area
		int wall_y, wall_x;
		wall_y = area.y1 + 2 * random.nextInt((area.height-1)/2) + 1;
		wall_x = area.x1 + 2 * random.nextInt((area.length-1)/2) + 1;
			
		if (area.getOrientation() == HORIZONTAL) {
			for (int i = 0; i < area.height; i++)
				maze[area.y1+i][wall_x] = WALL;
			maze[area.y1 + 1 + 2 * random.nextInt((area.height + 1)/2) - 1][wall_x] = PASSAGE;
			if (wall_x - area.x1 > 1)
				areas_list.add(0, new Area(area.y1, area.x1, area.height, wall_x - area.x1));
			if (area.x1 + area.length - wall_x - 1 > 1)
				areas_list.add(0, new Area(area.y1, wall_x + 1, area.height, area.x1 + area.length - wall_x - 1));
		}
		else {
			for (int i = 0; i < area.length; i++)
				maze[wall_y][area.x1+i] = WALL;
			maze[wall_y][area.x1 + 1 + 2 * random.nextInt((area.length + 1)/2) - 1] = PASSAGE;
			if (wall_y - area.y1 > 1)
				areas_list.add(0, new Area(area.y1, area.x1, wall_y - area.y1, area.length));
			if (area.y1 + area.height - wall_y - 1 > 1)
				areas_list.add(0, new Area(wall_y + 1, area.x1, area.y1 + area.height - wall_y - 1, area.length));
		}
		
	}
	
	private class Area {
		
		public int y1, x1;
		public int height, length;
		
		public Area(int y1, int x1, int height, int length) {
			this.y1 = y1;
			this.x1 = x1;
			this.height = height;
			this.length = length;
		}
		
		private int getOrientation() {
			if (height == length) 
				return (random.nextInt(2) == 0) ? HORIZONTAL : VERTICAL;
			return (height < length) ? HORIZONTAL : VERTICAL;
		}
		
	}

}
