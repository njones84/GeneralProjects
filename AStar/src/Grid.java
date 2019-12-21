import java.util.*;

public class Grid {

	private Node[][] grid;
	private int rows;
	private int cols;
	private int obstacleCount;

	public Grid() {

		grid = null;
		rows = 0;
		cols = 0;
		obstacleCount = 0;

	}

	public Grid(int rows, int cols, int obstacleCount) {

		grid = null;
		this.rows = rows;
		this.cols = cols;
		this.obstacleCount = obstacleCount;

	}

	public void GenerateMap() {

		grid = new Node[rows][cols];

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				grid[i][j] = new Node(i, j);

	}

	public void BlockMap() {

		int count = 0;
		Random rng = new Random();

		while (count < obstacleCount) {

			int randomX = rng.nextInt(rows);
			int randomY = rng.nextInt(cols);

			if (grid[randomX][randomY].GetTraversable() == true) {

				grid[randomX][randomY].SetTraversable(false);
				count++;

			}
		}
	}

	public void PrintMap() {

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (grid[i][j].GetInfo().equals("S"))
					System.out.print("[S] ");
				else if (grid[i][j].GetInfo().equals("G"))
					System.out.print("[G] ");
				else if (grid[i][j].GetInfo().equals("E"))
					System.out.print("[E] ");
				else if (grid[i][j].GetTraversable())
					System.out.print("[ ] ");
				else
					System.out.print("[X] ");

			}
			System.out.println();
		}
	}

	public ArrayList<Node> AddNeighbors(Node node) {

		// create new arraylist to hold neighbors
		ArrayList<Node> neighbors = new ArrayList();

		// access and store the current nodes position
		int x = node.GetX();
		int y = node.GetY();

		// check for neighbors
		if (x + 1 < rows)
			if (grid[x + 1][y].GetTraversable())
				neighbors.add(grid[x + 1][y]);

		// up
		if (x - 1 >= 0)
			if (grid[x - 1][y].GetTraversable())
				neighbors.add(grid[x - 1][y]);

		// left
		if (y - 1 >= 0)
			if (grid[x][y - 1].GetTraversable())
				neighbors.add(grid[x][y - 1]);

		// right
		if (y + 1 < cols)
			if (grid[x][y + 1].GetTraversable())
				neighbors.add(grid[x][y + 1]);

		// up-right
		if (x - 1 >= 0 && y + 1 < cols)
			if (grid[x - 1][y + 1].GetTraversable())
				neighbors.add(grid[x - 1][y + 1]);

		// up-left
		if (x - 1 >= 0 && y - 1 >= 0)
			if (grid[x - 1][y - 1].GetTraversable())
				neighbors.add(grid[x - 1][y - 1]);

		// down-left
		if (x + 1 < rows && y - 1 >= 0)
			if (grid[x + 1][y - 1].GetTraversable())
				neighbors.add(grid[x + 1][y - 1]);

		// down-right
		if (x + 1 < rows && y + 1 < cols)
			if (grid[x + 1][y + 1].GetTraversable())
				neighbors.add(grid[x + 1][y + 1]);

		return neighbors;
	}

	public Node GetNode(int x, int y) { return grid[x][y]; }

}
