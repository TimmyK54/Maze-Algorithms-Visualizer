
public class NullSearch extends MazeSearch {

	public NullSearch(int[][] maze) {
		super(maze);
	}

	@Override
	public void generateStep() {
		return;
	}

	// This method serves the entire and only purpose of this class
	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	protected void prepareSearch() {
		return;
	}
	
	@Override
	protected void expandSearch() {
		return;
	}

}
