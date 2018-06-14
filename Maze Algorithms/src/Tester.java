import java.text.DecimalFormat;

public class Tester {

	public static void main(String[] args) {

		int iterations = 100, size = 1;

		System.out.println("Iterations: " + iterations + "\n");
		
		 DecimalFormat f = new DecimalFormat("##0.000000");

		while (size < 500) {
			double total_time = 0;
			System.out.println("     Size: " + size + " x " + size);
			for (int i = 0; i < iterations; i++) {
				PrimsAlgorithm maze_generation = new PrimsAlgorithm(size, size);
				maze_generation.generateAuto();
				int[][] maze_grid = maze_generation.getMaze();
	
				TransposeNodes transpose = new TransposeNodes(maze_grid);
				double start_time = System.nanoTime();
				transpose.maximize();
				double end_time = System.nanoTime();
				double time = (end_time - start_time) * Math.pow(10, -9);
				total_time += time;
			}
			System.out.println("Tot. time: " + f.format(total_time));
			System.out.println("Avg. time: " + f.format(total_time/iterations));
			System.out.println();
			size += 2;
		}

		System.out.println("\nDone.");
	}

}
