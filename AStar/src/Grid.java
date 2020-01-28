import java.util.Random;
import java.util.PriorityQueue;

public class Grid {

	private int rows;
	private int cols;
	private int obstacleCount;
	
	private Node[][] grid;

	public Grid() {

		this.grid = null;	
		this.rows = 0;
		this.cols = 0;
		this.obstacleCount = 0;

	}

	public Grid(int rows, int cols, int obstacleCount) {

		this.grid = null;	
		this.rows = rows;
		this.cols = cols;
		this.obstacleCount = obstacleCount;

	}

	public void generateGrid() {

		grid = new Node[rows][cols];

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				grid[i][j] = new Node(i, j);

	}

	public void blockGrid() {

		int count = 0;
		Random rng = new Random();

		while (count < obstacleCount) {

			int randomX = rng.nextInt(rows);
			int randomY = rng.nextInt(cols);

			if (grid[randomX][randomY].getTraversable() == true) {

				grid[randomX][randomY].setTraversable(false);
				count++;

			}
		}
	}

	public void printGrid() {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (grid[i][j].getInfo().equals("S"))
					System.out.print("[S]");
				else if (grid[i][j].getInfo().equals("G"))
					System.out.print("[G]");
				else if (grid[i][j].getInfo().equals("E"))
					System.out.print("[E]");
				else if (grid[i][j].getTraversable())
					System.out.print("[ ]");
				else
					System.out.print("[X]");

			}
			System.out.println();
		}
	}

	public PriorityQueue<Node> addNeighbors(Node node) {

		// create new queue to hold neighbors
		PriorityQueue<Node> neighbors = new PriorityQueue<Node>();

		// access and store the current nodes position
		int x = node.getX();
		int y = node.getY();

		// check for neighbors
		// down
		if (x + 1 < rows)
			if (grid[x + 1][y].getTraversable())
				neighbors.add(grid[x + 1][y]);

		// up
		if (x - 1 >= 0)
			if (grid[x - 1][y].getTraversable())
				neighbors.add(grid[x - 1][y]);

		// left
		if (y - 1 >= 0)
			if (grid[x][y - 1].getTraversable())
				neighbors.add(grid[x][y - 1]);

		// right
		if (y + 1 < cols)
			if (grid[x][y + 1].getTraversable())
				neighbors.add(grid[x][y + 1]);

		// up-right
		if (x - 1 >= 0 && y + 1 < cols)
			if (grid[x - 1][y + 1].getTraversable())
				neighbors.add(grid[x - 1][y + 1]);

		// up-left
		if (x - 1 >= 0 && y - 1 >= 0)
			if (grid[x - 1][y - 1].getTraversable())
				neighbors.add(grid[x - 1][y - 1]);

		// down-left
		if (x + 1 < rows && y - 1 >= 0)
			if (grid[x + 1][y - 1].getTraversable())
				neighbors.add(grid[x + 1][y - 1]);

		// down-right
		if (x + 1 < rows && y + 1 < cols)
			if (grid[x + 1][y + 1].getTraversable())
				neighbors.add(grid[x + 1][y + 1]);
		
		return neighbors;
		
	}

	public boolean checkBounds(int x, int y) {

		if (x >= rows || y >= cols || x < 0 || y < 0)
			return true;

		return false;

	}
	
	// getters
	public Node getNode(int x, int y) { return grid[x][y]; }
	public int getRows() { return rows; }
	public int getCols() { return cols; }
	public int getObstacleCount() { return obstacleCount; }
	
	// setters
	public void setRows(int other) { this.rows = other; }
	public void setCols(int other) { this.cols = other; }
	public void setObstacleCount(int other) { this.obstacleCount = other; }
	public void resetNodes() {
		
		// set all parents to null
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				
				grid[i][j].setParent(null);
				grid[i][j].setTraversable(true);
				
			}
		
	}

}
