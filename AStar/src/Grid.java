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
		frame.setSize(screenDim.height + 200, screenDim.height - 100);
		frame.setLocation(screenDim.width / 2 - frame.getWidth() / 2, screenDim.height / 2 - frame.getHeight() / 2);
		
		// create a panel for the display information
		int infoRows = 5;
		int infoCols = 3;
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(infoRows, infoCols, 2, 2));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
		frame.getContentPane().add(infoPanel, BorderLayout.WEST);
		
		// create 2D panel holder to be able to slap labels in where we want!
		JPanel[][] panelHolder = new JPanel[5][3];
		for (int i = 0; i < infoRows; i++)
			for (int j = 0; j < infoCols; j++) {
				
				panelHolder[i][j] = new JPanel();
				infoPanel.add(panelHolder[i][j]);
				
			}
		
		// create instructions labels
		JLabel[] instructions = {new JLabel("Instructions"), new JLabel("Left Click = Start Node"), new JLabel("Right Click = Goal Node"), new JLabel("Middle Click = Obstacle")};
		
		// create displaying information labels
		JLabel[] information = {new JLabel("Information"), new JLabel("Start Node: "), new JLabel("Goal Node: "), new JLabel("Number of Obstacles: ")};
	
		// add labels where we want them! make it look nice and readable
		panelHolder[0][1].add(instructions[0]);
		panelHolder[1][0].add(instructions[1]);
		panelHolder[1][1].add(instructions[2]);
		panelHolder[1][2].add(instructions[3]);
		panelHolder[3][1].add(information[0]);
		panelHolder[4][0].add(information[1]);
		panelHolder[4][1].add(information[2]);
		panelHolder[4][2].add(information[3]);
		
		// create a panel for the A* algorithm
		pathPanel = new JPanel();
		pathPanel.setLayout(new GridLayout(rows, cols));
		pathPanel.setBackground(Color.WHITE);
		pathPanel.setSize(screenDim.height - 100, screenDim.height - 100);
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
