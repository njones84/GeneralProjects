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
	private JPanel pathPanel;
	private JPanel infoPanel;
	private JFrame frame;

	public Grid() {

		this.grid = null;
		this.buttons = null;
		this.pathPanel = null;
		this.infoPanel = null;
		this.frame = null;
		
		this.rows = 0;
		this.cols = 0;
		this.obstacleCount = 0;

	}

	public Grid(int rows, int cols, int obstacleCount) {

		this.grid = null;
		this.buttons = null;
		this.pathPanel = null;
		this.infoPanel = null;
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
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();			
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setLocation(screenDim.width / 2 - frame.getSize().width / 2, screenDim.height / 2 - frame.getSize().height / 2);
		
		// create a panel for the display information
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3, 5));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		frame.getContentPane().add(infoPanel, BorderLayout.WEST);
		
		// create instructions labels
		JLabel[] instructions = {new JLabel("Instructions"), new JLabel("Left Click = Start Node"), new JLabel("Right Click = Goal Node"), new JLabel("Middle Click = Obstacle")};
		infoPanel.add(instructions[0]);
		infoPanel.add(instructions[1]);
		infoPanel.add(instructions[2]);
		infoPanel.add(instructions[3]);
		
		// create displaying information labels
		JLabel[] information = {new JLabel("Information"), new JLabel("Start Node: "), new JLabel("Goal Node: "), new JLabel("Number of Obstacles: ")};
		infoPanel.add(information[0]);
		infoPanel.add(information[1]);
		infoPanel.add(information[2]);
		infoPanel.add(information[3]);
		
		// create a panel for the A* algorithm
		pathPanel = new JPanel();
		pathPanel.setLayout(new GridLayout(rows, cols));
		pathPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(pathPanel, BorderLayout.CENTER);
		
		// create buttons array
		buttons = new JButton[rows][cols];
		
		// each tile will be a button for more functionality
		for (int i = 0; i < rows; i++)
			for (int k = 0; k < cols; k++) {
						
				buttons[i][k] = new JButton();
				buttons[i][k].setSize(new Dimension(5, 5));
				pathPanel.add(buttons[i][k]);
						
			}
		
		frame.setVisible(true);
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
