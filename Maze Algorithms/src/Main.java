import javax.swing.JFrame;

/**
 * 
 * Project started on January 24, 2018
 *
 */

public class Main {

	public static void main(String[] args) {
		
		final int maze_size = 785;
		final int ui_length = 300;
		
		JFrame GUI = new JFrame("Maze Generation and Search Algorithms Visualizer");
		GUI.getContentPane().add(new MazePanel(maze_size, ui_length));	// Adds maze panel
		
		GUI.pack();
		GUI.setResizable(false);
		GUI.setLocationRelativeTo(null);
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUI.setVisible(true);
		
	}

}
