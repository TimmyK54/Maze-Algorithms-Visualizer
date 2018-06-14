
public class NullGeneration extends MazeGeneration {

	public NullGeneration(int rows, int columns) {
		super(rows, columns);
	}

	@Override
	public void generateStep() {
		return;
	}
	
	// This methods serves the entire and only purpose of this class
	@Override
	public boolean isDone() {
		return true;
	}
	
	@Override
	protected void expandMaze() {
		return;
	}

}
