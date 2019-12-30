import java.util.Random;
import java.util.ArrayList;

import java.awt.*;
import javax.swing.*;

public class Grid {

	private Node[][] grid;
	private int rows;
	private int cols;
	private int obstacleCount;
	
	private JButton[][] buttons;
	private JPanel panel;
	private JFrame frame;

	public Grid() {

		this.grid = null;
		this.buttons = null;
		this.panel = null;
		this.frame = null;
		
		this.rows = 0;
		this.cols = 0;
		this.obstacleCount = 0;

	}

	public Grid(int rows, int cols, int obstacleCount) {

		this.grid = null;
		this.buttons = null;
		this.panel = null;
		this.frame = null;
		
		this.rows = rows;
		this.cols = cols;
		this.obstacleCount = obstacleCount;

	}

	public void GenerateGrid() {

		grid = new Node[rows][cols];

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				grid[i][j] = new Node(i, j);

	}

	public void BlockGrid() {

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

	public void PrintGrid() {

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
	
	public void VisualizeGrid() {
		
		// initialize the frame for the board
		frame = new JFrame("A* Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// creates a table for the A* algorithm
		panel = new JPanel();
		panel.setLayout(new GridLayout(rows, cols));
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
		
		buttons = new JButton[rows][cols];
		// each tile will be a button for more functionality
		for (int i = 0; i < rows; i++)
			for (int k = 0; k < cols; k++) {
						
				buttons[i][k] = new JButton();
				buttons[i][k].setSize(new Dimension(5, 5));
				panel.add(buttons[i][k]);
						
			}
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		frame.setSize(screenDim.height - 200, screenDim.height - 200);
		frame.setLocation(screenDim.width / 2 - frame.getSize().width / 2, screenDim.height / 2 - frame.getSize().height / 2);
		
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
