import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/** 
 * 
 *	Adding a maze generation algorithm:
 * 		1. New algorithm should extend MazeGeneration class
 * 		2. Add global maze generation radio button under "Create Global UI Elements" section
 * 		3. Add button to list with the method addMazeButton(JRadioButton) in the constructor
 * 		4. Add algorithm to createNewMaze() method
 * 		5. Add button to getGenerationName() method in the "Maze generation progress box methods" section
 * 
 * 	Adding a maze search algorithm:
 * 		1. New algorithm should extend MazeSearch class
 * 		2. Add global maze search radio button under "Create Global UI Elements" section
 * 		3. Add button to list with the method addSearchButton(JRadioButton) in the constructor
 * 		4. Add algorithm to createNewSearch() method
 * 
 */

@SuppressWarnings("serial")
public class MazePanel extends JPanel {
	
	private int rows, columns, height;
	private int[][] maze_grid;				// Maze array
	private MazeGeneration maze_generation;	// Maze generation object
	private MazeSearch maze_search;			// Maze search object
	ArrayList<JRadioButton> generation_button_list = new ArrayList<JRadioButton>();
	Timer generation_timer, search_timer;
	
	public MazePanel() {
	}

	public MazePanel(LayoutManager layout) {
		super(layout);
	}

	public MazePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public MazePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}
	
	// CREATE GLBOBAL UI ELEMENTS:
	JSpinner 
		row_spinner = new JSpinner(new SpinnerNumberModel(30,0,1000,1)),
		col_spinner = new JSpinner(new SpinnerNumberModel(30,0,1000,1));
	JSlider delay_slider = new JSlider(0, 200, 50);
	
	// Fonts
	Font box_title_font = new Font("American Typewriter", Font.PLAIN, 17);
	Font buttons_font 	= new Font("Sans Serif", Font.PLAIN, 13);
	Font label_font 	= new Font("American Typewriter", Font.PLAIN, 14);
	Font progress_font	= new Font("Sans Serif", Font.PLAIN, 11);
	
	// Algorithm selection radio buttons
	Box generation_selection_box = Box.createVerticalBox();
	ButtonGroup generation_button_group = new ButtonGroup();
	JRadioButton 
		generation_radiobutton_prim 		= new JRadioButton("Prim's Algorithm"), 
		generation_radiobutton_kruskal		= new JRadioButton("Kruskal's Algorithm"), 
		generation_radiobutton_eller		= new JRadioButton("Eller's Algorithm"), 
		generation_radiobutton_wilson		= new JRadioButton("Wilson's Algorithm"),
		generation_radiobutton_huntkill		= new JRadioButton("Hunt-and-Kill Algorithm"),
		generation_radiobutton_dfs			= new JRadioButton("Depth-First Search"), 
		generation_radiobutton_division		= new JRadioButton("Recursive Division");
	
	Box search_selection_box = Box.createVerticalBox();
	ButtonGroup search_button_group = new ButtonGroup(); 
	JRadioButton 
		search_radiobutton_astar 			= new JRadioButton("A* Algorithm"), 
		search_radiobutton_ida 				= new JRadioButton("Iterative Deepening A*"),	// may change to (or add) Dynamic Weighting
		search_radiobutton_jps				= new JRadioButton("Jump-Point Search"), 
		search_radiobutton_dfs 				= new JRadioButton("Depth-First Search"), 
		search_radiobutton_breadth 			= new JRadioButton("Breadth-First Search"),
		search_radiobutton_best 			= new JRadioButton("Best-First Search"), 
		search_radiobutton_bi 				= new JRadioButton("Bidirectional Search"),
		search_radiobutton_deadend 			= new JRadioButton("Dead-End Filling"),
		search_radiobutton_backtracker 		= new JRadioButton("Recursive Backtracker");
	
	// Transpose buttons
	ButtonGroup transpose_button_group = new ButtonGroup();
	JRadioButton 
		transpose_radiobutton_random 		= new JRadioButton("Randomize"),
		transpose_radiobutton_maximum		= new JRadioButton("Maximize");
	
	// Progress panel
	JPanel progress_panel = new JPanel();
	JLabel progress_label1 = new JLabel(), progress_label2 = new JLabel(), progress_label3 = new JLabel();
	double start_time, end_time;
	
	// Customization options
	private boolean boxes, perfect_boxes;
	JCheckBox 
		enable_boxes_button 		= new JCheckBox("Enable Boxes"),
		enable_perfect_boxes_button = new JCheckBox("Perfect Boxes");
	
	// Maze colors
	private Color 
	wall_color 		= Color.BLACK,
	passage_color 	= Color.WHITE,
	start_color		= Color.GREEN,
	end_color		= Color.MAGENTA,
	frontier_color	= Color.BLUE,
	open_color		= new Color(255, 60, 60),	// Red
	closed_color	= new Color(110, 110, 110),	// Gray
	path_color		= Color.CYAN;
	
	public MazePanel(int maze_size, int ui_length) {
		
		// Initialize maze generation and search objects
		maze_generation = new NullGeneration(rows, columns);
		maze_grid = maze_generation.getMaze();
		maze_search = new NullSearch(maze_grid);
		
		// SETUP PANEL PROPERTIES:
		height = maze_size;
		setPreferredSize(new Dimension(height+ui_length, height));
		setLayout(null);
		
		// CREATE/SETUP UI ELEMENTS:
		//Labels
		JLabel grid_size_label = new JLabel("Grid Size:");
		JLabel generation_delay_slider_label = new JLabel("Delay (ms):");
		JLabel search_delay_slider_label = new JLabel("Delay (ms):");
		
		grid_size_label.setFont(label_font);
		generation_delay_slider_label.setFont(label_font);
		search_delay_slider_label.setFont(label_font);
		
		// Grid size spinners
		Box grid_spinner_box = Box.createHorizontalBox();
		grid_spinner_box.add(grid_size_label);
		grid_spinner_box.add(row_spinner);
		grid_spinner_box.add(col_spinner);
		
		// Maze generation algorithm selection radio buttons
		addMazeButton(generation_radiobutton_prim);
		addMazeButton(generation_radiobutton_kruskal);
		addMazeButton(generation_radiobutton_eller);
//		addMazeButton(generation_radiobutton_wilson);
		addMazeButton(generation_radiobutton_huntkill);
		addMazeButton(generation_radiobutton_dfs);
		addMazeButton(generation_radiobutton_division);
		
		TitledBorder generation_selection_border = new TitledBorder(new EtchedBorder());
		generation_selection_border.setTitleFont(box_title_font);
		generation_selection_border.setTitle("Maze Generation Algorithm");
		generation_selection_box.setPreferredSize(new Dimension(ui_length - 10, generation_button_group.getButtonCount() * 28 + 28));
		generation_selection_box.setBorder(generation_selection_border);
			
		// Maze search algorithm selection radio buttons
		addSearchButton(search_radiobutton_astar);
//		addSearchButton(search_radiobutton_ida);
		addSearchButton(search_radiobutton_jps);
		addSearchButton(search_radiobutton_dfs);
		addSearchButton(search_radiobutton_breadth);
		addSearchButton(search_radiobutton_best);
//		addSearchButton(search_radiobutton_bi);
		addSearchButton(search_radiobutton_deadend);
//		addSearchButton(search_radiobutton_backtracker);
		
		TitledBorder search_selection_border = new TitledBorder(new EtchedBorder());
		search_selection_border.setTitleFont(box_title_font);
		search_selection_border.setTitle("Maze Search Algorithm");
		search_selection_box.setPreferredSize(new Dimension(ui_length - 10, search_button_group.getButtonCount() * 28 + 28));
		search_selection_box.setBorder(search_selection_border);
		
		// Generation buttons
		JButton instant_generate_button = new JButton("Instant");
		instant_generate_button.addActionListener(InstantGenerateListener);
		
		JButton auto_generate_button = new JButton("Generate Maze");
		auto_generate_button.addActionListener(AutoGenerateListener);
		
		JButton step_generate_button = new JButton("Step");
		step_generate_button.addActionListener(StepGenerateListener);
		
		Box generate_button_box = Box.createHorizontalBox();
		generate_button_box.add(instant_generate_button);
		generate_button_box.add(auto_generate_button);
		generate_button_box.add(step_generate_button);
		generate_button_box.setSize(ui_length, 200);
		
		// Search buttons
		JButton instant_search_button = new JButton("Instant");
		instant_search_button.addActionListener(InstantSearchListener);
		
		JButton auto_search_button = new JButton("Search Maze");
		auto_search_button.addActionListener(AutoSearchListener);
		
		JButton step_search_button = new JButton("Step");
		step_search_button.addActionListener(StepSearchListener);
		
		Box search_button_box = Box.createHorizontalBox();
		search_button_box.add(instant_search_button);
		search_button_box.add(auto_search_button);
		search_button_box.add(step_search_button);
		search_button_box.setSize(ui_length, 200);
		
		// Transpose Nodes Buttons
		JButton transpose_button = new JButton("Transpose");
		transpose_button.addActionListener(TransposeListener);
		
		Box transpose_radiobutton_box = Box.createVerticalBox();
		transpose_radiobutton_box.add(transpose_radiobutton_random);
		transpose_button_group.add(transpose_radiobutton_random);
		transpose_radiobutton_random.setFont(buttons_font);
		transpose_radiobutton_box.add(transpose_radiobutton_maximum);
		transpose_button_group.add(transpose_radiobutton_maximum);
		transpose_radiobutton_maximum.setFont(buttons_font);
		
		Box transpose_box = Box.createHorizontalBox();
		transpose_box.add(transpose_radiobutton_box);
		transpose_box.add(Box.createHorizontalGlue());
		transpose_box.add(transpose_button);
		transpose_box.add(Box.createHorizontalGlue());
		
		TitledBorder transpose_selection_border = new TitledBorder(new EtchedBorder());
		transpose_selection_border.setTitleFont(box_title_font);
		transpose_selection_border.setTitle("Transpose Nodes");
		transpose_box.setPreferredSize(new Dimension(ui_length - 10, 90));
		transpose_box.setBorder(transpose_selection_border);
		
		// Delay Sliders
		delay_slider.setMinorTickSpacing(10);
		delay_slider.setMajorTickSpacing(50);
		delay_slider.setPaintTicks(true);
		delay_slider.setPaintLabels(true);
		
		Box delay_box = Box.createHorizontalBox();
		delay_box.add(generation_delay_slider_label);
		delay_box.add(delay_slider);	
		
		// Customization Options
		enable_boxes_button.addActionListener(ToggleBoxes);
		enable_perfect_boxes_button.addActionListener(TogglePerfectBoxes);
		
		enable_boxes_button.setFont(buttons_font);
		enable_perfect_boxes_button.setFont(buttons_font);
		
		Box customization_box = Box.createHorizontalBox();
		customization_box.add(Box.createHorizontalGlue());
		customization_box.add(enable_boxes_button);
		customization_box.add(Box.createHorizontalGlue());
		customization_box.add(enable_perfect_boxes_button);
		customization_box.add(Box.createHorizontalGlue());
		customization_box.setPreferredSize(new Dimension(ui_length - 10, 24));
		
		// Progress Panel
		Box progress_box = Box.createVerticalBox();
		
		progress_box.add(progress_label1);
		progress_box.add(progress_label2);
		progress_box.add(progress_label3);
		
		progress_label1.setFont(progress_font);
		progress_label2.setFont(progress_font);
		progress_label3.setFont(progress_font);
		
		progress_label1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		progress_label2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		progress_label3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		progress_panel.setPreferredSize(new Dimension(ui_length - 10, 60));
		progress_panel.add(progress_box);
		progress_panel.setBorder(new EtchedBorder());
		defaultProgBox();
		
		// ADD UI ELEMENTS:
		JPanel ui_panel = new JPanel();
		
		ui_panel.add(grid_size_label);
		ui_panel.add(grid_spinner_box);
		ui_panel.add(generation_selection_box);
		ui_panel.add(generate_button_box);
		ui_panel.add(search_selection_box);
		ui_panel.add(search_button_box);
		ui_panel.add(transpose_box);
		ui_panel.add(delay_box);
		ui_panel.add(customization_box);
		ui_panel.add(progress_panel);
		
		add(ui_panel);
		ui_panel.setBounds(maze_size, 0, ui_length, maze_size);
		
		// CREATE TIMERS:
		generation_timer = new Timer(delay_slider.getValue(), new RepaintGeneration());
		search_timer = new Timer(delay_slider.getValue(), new RepaintSolution());
		generation_timer.setInitialDelay(0);
		search_timer.setInitialDelay(0);
		
		// ALGORITHMS TO BE IMPLEMENTED:
		generation_radiobutton_wilson.setEnabled(false);
		
		search_radiobutton_ida.setEnabled(false);
		search_radiobutton_backtracker.setEnabled(false);
		search_radiobutton_bi.setEnabled(false);
		
	}	// End of constructor
	
	// CONSTRUCTOR HELPER METHODS:
	private void addMazeButton(JRadioButton rb) {
		generation_button_group.add(rb);
		generation_selection_box.add(rb);
		rb.setFont(buttons_font);
	}
	
	private void addSearchButton(JRadioButton rb) {
		search_button_group.add(rb);
		search_selection_box.add(rb);
		rb.setFont(buttons_font);
	}
	
	// NEW MAZE GENERATION:
	private void createNewMaze() {
		rows = (int) row_spinner.getValue();
		columns = (int) col_spinner.getValue();
		if (generation_radiobutton_prim.isSelected()) {
			maze_generation = new PrimsAlgorithm(rows, columns);
			progress_label1.setText(generation_radiobutton_prim.getText());
		}
		else if (generation_radiobutton_kruskal.isSelected()) {
			maze_generation = new KruskalsAlgorithm(rows, columns);
			progress_label1.setText(generation_radiobutton_kruskal.getText());
		}
		else if (generation_radiobutton_eller.isSelected()) {
			maze_generation = new EllersAlgorithm(rows, columns);
			progress_label1.setText(generation_radiobutton_eller.getText());
		}
		else if (generation_radiobutton_wilson.isSelected()) {
			maze_generation = new NullGeneration(rows, columns);
			progress_label1.setText(generation_radiobutton_wilson.getText());
		}
		else if (generation_radiobutton_huntkill.isSelected()) {
			maze_generation = new HuntAndKillAlgorithm(rows, columns);
			progress_label1.setText(generation_radiobutton_huntkill.getText());
		}
		else if (generation_radiobutton_dfs.isSelected()) {
			maze_generation = new DepthFirstGeneration(rows, columns);
			progress_label1.setText(generation_radiobutton_dfs.getText());
		}
		else if (generation_radiobutton_division.isSelected())	{
			maze_generation = new RecursiveDivision(rows, columns);
			progress_label1.setText(generation_radiobutton_division.getText());
		}
		else {
			generation_radiobutton_prim.setSelected(true);
			createNewMaze();
		}
	}
	
	// NEW MAZE SEARCH:
	private void createNewSearch() {
		if (!maze_generation.containsStart()) 
			return;
		maze_grid = maze_generation.getMaze();
		if (search_radiobutton_astar.isSelected()) { 
			maze_search = new AStarAlgorithm(maze_grid); 
			progress_label1.setText(search_radiobutton_astar.getText());
		}
		else if (search_radiobutton_ida.isSelected()) { 
			maze_search = new IterativeDeepeningAStar(maze_grid); 
			progress_label1.setText(search_radiobutton_ida.getText());
		}
		else if (search_radiobutton_jps.isSelected()) { 
			maze_search = new JumpPointSearch(maze_grid); 
			progress_label1.setText(search_radiobutton_jps.getText());
		}
		else if (search_radiobutton_dfs.isSelected()) { 
			maze_search = new DepthFirstSearch(maze_grid); 
			progress_label1.setText(search_radiobutton_dfs.getText());
		}
		else if (search_radiobutton_breadth.isSelected()) { 
			maze_search = new BreadthFirstSearch(maze_grid); 
			progress_label1.setText(search_radiobutton_breadth.getText());
		}
		else if (search_radiobutton_best.isSelected()) { 
			maze_search = new BestFirstSearch(maze_grid); 
			progress_label1.setText(search_radiobutton_best.getText());
		}
		else if (search_radiobutton_bi.isSelected()) { 
			maze_search = new BidirectionalSearch(maze_grid); 
			progress_label1.setText(search_radiobutton_bi.getText());
		}
		else if (search_radiobutton_deadend.isSelected()) { 
			maze_search = new DeadEndFilling(maze_grid); 
			progress_label1.setText(search_radiobutton_deadend.getText());
		}
		else if (search_radiobutton_backtracker.isSelected()) { 
			maze_search = new NullSearch(maze_grid); 
			progress_label1.setText(search_radiobutton_backtracker.getText());
		}
		else {
			search_radiobutton_astar.setSelected(true);
			createNewSearch();
		}
	}
	
	// ACTION LISTENERS:
	// Maze Generation action listeners
	ActionListener InstantGenerateListener = new ActionListener() {	// Runs when instant_generate_button is activated
		@Override
		public void actionPerformed (ActionEvent e) {
			maze_search = new NullSearch(maze_grid); 
			
			start_time = System.nanoTime();
			if (maze_generation.isDone()) 
				createNewMaze();
			maze_generation.generateAuto();
			end_time = System.nanoTime();
			
			if (generation_timer.isRunning())
				setGProgBoxDone();
			else
				setGProgBoxDone((end_time - start_time) * Math.pow(10,-9));
			
			maze_grid = maze_generation.getMaze();
			repaint();
		}
	};
	
	ActionListener AutoGenerateListener = new ActionListener() {	// Runs when auto_generate_button is activated
		@Override
		public void actionPerformed (ActionEvent e) {
			if (maze_generation.isDone()) 
				createNewMaze();
			
			if (generation_timer.isRunning()) {
				generation_timer.stop();
				pauseGProgBox();
			}
			else {
				generation_timer.setDelay(delay_slider.getValue());
				generation_timer.start();
				updateGProgBox();
			}
		}
	};
	
	ActionListener StepGenerateListener = new ActionListener() {	// Runs when step_generate_button is activated
		@Override
		public void actionPerformed (ActionEvent e) {
			maze_search = new NullSearch(maze_grid); 
			generation_timer.stop();
			
			if (maze_generation.isDone()) 
				createNewMaze();
			maze_generation.generateStep();
			maze_grid = maze_generation.getMaze();
			repaint();
			
			if (maze_generation.isDone()) {
				maze_search = new NullSearch(maze_grid);
				setGProgBoxDone();
			}
			else 
				stepGProgBox();
		}
	};
	
	private class RepaintGeneration implements ActionListener {		// Paints the maze with a delay via Timer class
		@Override
		public void actionPerformed(ActionEvent e) {
			maze_search = new NullSearch(maze_grid);
			updateGProgBox();
			
			if (maze_generation.isDone()) {
				generation_timer.stop();
				setGProgBoxDone();
			}
			else {
				maze_generation.generateStep();
				maze_grid = maze_generation.getMaze();
				repaint();
				generation_timer.setDelay(delay_slider.getValue());
			}
		}
	};
	
	// Maze Search action listeners
	ActionListener InstantSearchListener = new ActionListener() {	// Runs when instant_search_button is activated 
		public void actionPerformed (ActionEvent e) {
			generation_timer.stop();
			
			start_time = System.nanoTime();
			if (maze_search.isDone()) 
				createNewSearch();
			maze_search.generateAuto();
			end_time = System.nanoTime();
			
			if (generation_timer.isRunning())
				setSProgBoxDone();
			else
				setSProgBoxDone((end_time - start_time) * Math.pow(10,-9));
			
			maze_grid = maze_search.getMaze();
			repaint();
		}
	};
	
	ActionListener AutoSearchListener = new ActionListener() {		// Runs when auto_search_button is activated
		public void actionPerformed (ActionEvent e) {
			generation_timer.stop();
			
			if (maze_search.isDone()) 
				createNewSearch();
			if (search_timer.isRunning()) {
				search_timer.stop();
				pauseSProgBox();
			}
			else {
				search_timer.setDelay(delay_slider.getValue());
				search_timer.start();
				updateSProgBox();
			}
		}
	};
	
	ActionListener StepSearchListener = new ActionListener() {		// Runs when step_search_button is activated
		public void actionPerformed (ActionEvent e) {
			search_timer.stop();
			generation_timer.stop();
			
			if (maze_search.isDone()) 
				createNewSearch();
			maze_search.generateStep();
			maze_grid = maze_search.getMaze();
			repaint();
			updateSProgBox();
			
			if (maze_search.isDone()) 
				setSProgBoxDone();
			else 
				stepSProgBox();
		}
	};
	
	private class RepaintSolution implements ActionListener {		// Paints the maze with a delay via Timer class
		@Override
		public void actionPerformed(ActionEvent e) {
			if (maze_search.isDone()) {
				search_timer.stop();
				setSProgBoxDone();
			}
			else {
				maze_search.generateStep();
				maze_grid = maze_search.getMaze();
				repaint();
				updateSProgBox();
				search_timer.setDelay(delay_slider.getValue());
			}
		}
	};
	
	// Transpose Nodes action listeners
	ActionListener TransposeListener = new ActionListener() {		// Runs when a transpose_radiobutton is activated
		@Override
		public void actionPerformed(ActionEvent e) {
			search_timer.stop();
			generation_timer.stop();
			maze_search = new NullSearch(maze_grid); 
			
			boolean transposed = false;
			TransposeNodes node_transpose = new TransposeNodes(maze_grid);
			if (transpose_radiobutton_maximum.isSelected()) {
				start_time = System.nanoTime();
				if (node_transpose.maximize())
					transposed = true;
				end_time = System.nanoTime();
			}
			else {
				if (!transpose_radiobutton_random.isSelected())
					transpose_radiobutton_random.setSelected(true);
				start_time = System.nanoTime();
				if (node_transpose.randomize())
					transposed = true;
				end_time = System.nanoTime();
			}
			
			if (transposed) {
				setTProgBoxDone((end_time - start_time) * Math.pow(10,-9));
				maze_generation.setMaze(node_transpose.getMaze());
				createNewSearch();
				if (transpose_radiobutton_maximum.isSelected())
					progress_label1.setText("Maximize Nodes");
				else
					progress_label1.setText("Randomize Nodes");
			}
			else
				noMazeError();
			repaint();
		}
	};
	
	// Customization Options action listeners
	ActionListener ToggleBoxes = new ActionListener() {				// Runs when enable_boxes_button is activated
		public void actionPerformed (ActionEvent e) {
			boxes = !boxes;
			repaint();
		}
	};
	
	ActionListener TogglePerfectBoxes = new ActionListener() {		// Runs when enable_perfect_boxes_button is activated
		public void actionPerformed (ActionEvent e) {
			perfect_boxes = !perfect_boxes;
			repaint();
		}
	};
	
	// PROGRESS BOX METHODS
	// Maze generation
	private void defaultProgBox() {
		progress_label1.setText("Maze Generation and Search");
		progress_label2.setText("Algorithms Visualizer");
		progress_label3.setText("by Tushar Khan");
	}
	
	private void setGProgBoxDone() {
		progress_label2.setText("Done");
		progress_label3.setText(maze_generation.getSteps() + " steps");
	}
	
	private void setGProgBoxDone(double seconds) {
		NumberFormat duration_format = new DecimalFormat("#0.0000000");
		progress_label2.setText("Done in " + duration_format.format(seconds) + " seconds");
		progress_label3.setText(maze_generation.getSteps() + " steps");
	}
	
	private void updateGProgBox() {
		progress_label2.setText("Generating maze...");
		progress_label3.setText(maze_generation.getSteps() + " steps");
	}
	
	private void pauseGProgBox() {
		progress_label2.setText("Paused");
		progress_label3.setText(maze_generation.getSteps() + " steps");
	}
	
	private void stepGProgBox() {
		progress_label2.setText("Step generated");
		progress_label3.setText(maze_generation.getSteps() + " steps");
	}
	
	// Maze search
	private void setSProgBoxDone() {
		if (!maze_generation.containsStart()) {
			noStartNodeError();
			return;
		}
		if (maze_search.getDistance() <= 0) 
			noEndNodeError();
		else {
			progress_label2.setText("Done");
			progress_label3.setText(maze_search.getSteps() + " steps, " + maze_search.getDistance() + " path distance");
		}
	}
	
	private void setSProgBoxDone(double seconds) {
		if (!maze_generation.containsStart()) {
			noStartNodeError();
			return;
		}
		if (maze_search.getDistance() <= 0) 
			noEndNodeError();
		else {
			NumberFormat duration_format = new DecimalFormat("#0.0000000");
			progress_label2.setText("Done in " + duration_format.format(seconds) + " seconds");
			progress_label3.setText(maze_search.getSteps() + " steps, " + maze_search.getDistance() + " path distance");
		}
	}
	
	private void updateSProgBox() {
		progress_label2.setText("Searching maze...");
		progress_label3.setText(maze_search.getSteps() + " steps, " + maze_search.getDistanceString());
	}
	
	private void pauseSProgBox() {
		progress_label2.setText("Paused");
		progress_label3.setText(maze_search.getSteps() + " steps, " + maze_search.getDistanceString());
	}
	
	private void stepSProgBox() {
		progress_label2.setText("Step generated");
		progress_label3.setText(maze_search.getSteps() + " steps, " + maze_search.getDistanceString());
	}
	
	private void noStartNodeError() {
		progress_label2.setText("Error: Start node not found");
		progress_label3.setText("");
	}
	
	private void noEndNodeError() {
		progress_label2.setText("Error: No reachable end node found");
		progress_label3.setText("");
	}
	
	// Transpose Nodes
	private void setTProgBoxDone(double seconds) {
		NumberFormat duration_format = new DecimalFormat("#0.0000000");
		progress_label2.setText("Done in " + duration_format.format(seconds) + " seconds");
		progress_label3.setText("");
	}
	
	private void noMazeError() {
		progress_label2.setText("Error: Maze not generated");
		progress_label3.setText("");
	}
	
	// MAZE PAINTING:
	public void setWallColor(Color c) 		{ wall_color 		= c; }
	public void setPassageColor(Color c) 	{ passage_color 	= c; }
	public void setStartColor(Color c) 		{ start_color 		= c; }
	public void setEndColor(Color c) 		{ end_color 		= c; }
	public void setFrontierColor(Color c) 	{ frontier_color 	= c; }
	public void setOpenSetColor(Color c) 	{ open_color 		= c; }
	public void setClosedSetColor(Color c) 	{ closed_color 		= c; }
	public void setPathColor(Color c) 		{ path_color 		= c; }
	
	// Maze paint method
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		rows = maze_grid.length;
		columns = maze_grid[0].length;
		
		// Paints background color
		Graphics2D g2 = (Graphics2D) g;
		double gradient_multiplier = 0.6;	// Value between 0.0 (darker) and 1.0 (lighter)
		Color gradient_color = new Color(
				(int)(wall_color.getRed()*gradient_multiplier), 
				(int)(wall_color.getGreen()*gradient_multiplier), 
				(int)(wall_color.getBlue()*gradient_multiplier));
        GradientPaint gp = new GradientPaint(0, 0, wall_color, 0, height, gradient_color);
        g2.setPaint(gp);
        g2.fillRect(0, 0, height, height);
		
		// Evaluates general cell values
		double cell_pos = ((double)height/columns < (double)height/rows) ? (double)height/columns : (double)height/rows;
		double cell_size = ((double)height/(columns-1)+1 < (double)height/(rows-1)+1) ? (double)height/(columns-1)+1 : (double)height/(rows-1)+1;
		
		// Centers rectangular mazes
		double x_offcenter = ((rows-columns)*(cell_size-1)/2 > 0) ? (rows-columns)*(cell_size-1)/2 : 0;
		double y_offcenter = ((columns-rows)*(cell_size-1)/2 > 0) ? (columns-rows)*(cell_size-1)/2 : 0;
		
		// Makes all cells equal sizes
		if (perfect_boxes) {
			x_offcenter += maze_grid[0].length * (cell_pos - (int)cell_pos)/2;
			y_offcenter += maze_grid.length * (cell_pos - (int)cell_pos)/2;
			cell_pos = (int)cell_pos;
		}
		
		// Paints maze grid
        for (int y = 0; y < maze_grid.length; y++) {
        	for (int x = 0; x < maze_grid[0].length; x++) {
        		switch (maze_grid[y][x]) {
        		case Maze.WALL:
        			g.setColor(wall_color);
        			break;
        		case Maze.PASSAGE:
        			g.setColor(passage_color);
        			break;
        		case Maze.START:
        			g.setColor(start_color);
        			break;
        		case Maze.END:
        			g.setColor(end_color);
        			break;
        		case Maze.FRONTIER:
        			g.setColor(frontier_color);
        			break;
        		case Maze.OPEN:
        			g.setColor(open_color);
        			break;
        		case Maze.CLOSED:
        			g.setColor(closed_color);
        			break;
        		case Maze.PATH:
        			g.setColor(path_color);
        			break;
        		}
        		
        		// Paints cell
        		if (maze_grid[y][x] != Maze.WALL || wall_color == Color.BLACK) {
        			g.fillRect((int)(x*cell_pos+x_offcenter),(int)(y*cell_pos+y_offcenter),(int)cell_size,(int)cell_size);
        			if (boxes) {
        				double border_multiplier = 0.75;	// Value between 0.0 (darker) and 1.0 (lighter)
        				g.setColor(new Color(
        						(int)(g.getColor().getRed()*border_multiplier),
        						(int)(g.getColor().getGreen()*border_multiplier),
        						(int)(g.getColor().getBlue()*border_multiplier)));
        				g.drawRect((int)(x*cell_pos+x_offcenter),(int)(y*cell_pos+y_offcenter),(int)cell_size-1,(int)cell_size-1);
        			}
        		}
        	}
        }
        
	}
	
}

/*
 * TO DO LIST:
 * 
 * 		- Optimize maximize trapnspose nodes
 * 		- Figure out where to put the end node in the depth-first generation algorithm
 * 		- Add a color picker to change the color of maze elements
 * 		- Make search buttons fit within ui_panel constraints
 * 
 */
