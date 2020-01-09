import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

	private static Node startNode;
	private static Node goalNode;
	private static Grid grid;
	private static Scanner input = new Scanner(System.in);
	
	private static int startX;
	private static int startY;
	private static int goalX;
	private static int goalY;
	private static int rows;
	private static int cols;
	private static int obstacleCount;
	private static boolean found = false;
	
	public static void main(String[] args) {

		DisplayChoice();
	
	}
	
	public static boolean AStarSearch(Node goal, Node start) {

		// variables to keep track of nodes
		PriorityQueue<Node> frontier = new PriorityQueue<Node>();
		PriorityQueue<Node> fringe = new PriorityQueue<Node>();
		PriorityQueue<Node> neighbors = new PriorityQueue<Node>();
		Node currentNode = start;

		// set the current node's distance and visual information
		// (Manhattan Distance)
		currentNode.SetHeuristicCost(Math.max(Math.abs(currentNode.GetX() - goalX), Math.abs(currentNode.GetY() - goalY)));
		currentNode.SetPathCost(0);
		currentNode.SetSumCost(currentNode.GetHeursticCost() + currentNode.GetPathCost());
		currentNode.SetInfo("S");

		// add current node to the frontier
		frontier.add(currentNode);

		// while the frontier is not empty
		while (!frontier.isEmpty()) {

			// get the new node each loop and add it to the fringe
			// clear the frontier and neighbors so the current node has a new start
			currentNode = frontier.poll();
			fringe.add(currentNode);

			// iteration after we pop the first one off
			frontier.clear();
			neighbors.clear();
			
			// get neighbors
			neighbors = grid.AddNeighbors(currentNode);

			// loop through the neighbors array and add possible nodes to expand on
			for (Node node : neighbors) {

				// check if goal node
				if (node == goal) {
					
					node.SetParent(currentNode);
					return true;
					
				}

				// check if the fringe contains this node or if the node can be navigated, if it does skip it
				if (fringe.contains(node) || !node.GetTraversable())
					continue;

				// set the child's respective H, G, and F costs and check for diagonal
				node.SetHeuristicCost(Math.max(Math.abs(node.GetX() - goalX), Math.abs(node.GetY() - goalY)));
				node.SetPathCost(currentNode.GetPathCost() + 1);
				node.SetSumCost(node.GetHeursticCost() + node.GetPathCost());

				if (frontier.contains(node))
					if (node.GetSumCost() > currentNode.GetSumCost())
						continue;

				// add the child to the frontier
				frontier.add(node);
				node.SetParent(currentNode);
				
				// debug statement
				System.out.println("Adding child: " + node.GetSumCost() + " [" + node.GetX() + ", " + node.GetY() + "]");

			}
			System.out.println();
		}

		// if all else fails, return false
		return false;

	}

	private static boolean CheckBounds(int x, int y, int rows, int cols) {

		if (x >= rows || y >= cols || x < 0 || y < 0)
			return true;

		return false;

	}
	
	private static void ConstructPath(Node node) {
		
		// create new path
		Stack<Node> path = new Stack<Node>();
		
		// while the nodes have parents, add them to the path
		while (node.GetParent() != null) {
			
			node = node.GetParent();
			path.push(node);
			
		}
		
		// pop first node off since its the start node
		path.pop();
		
		// keep track of iteration number
		int count = 1;
		
		// traverse through the stack to show the path in iterations
		while (!path.isEmpty()) {
			
			System.out.println("Current iteration of A* - " + count);
			count++;
			
			node = path.pop();
			node.SetInfo("E");
			grid.PrintGrid();
			
		}
		
	}
	
	private static void DisplayChoice() {
		
		// initialize the frame to show user visualization options
		JFrame frame = new JFrame("Visualization Options");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();		
		frame.setSize(screenDim.height / 2, screenDim.height / 2);
		frame.setLocation(screenDim.width / 2 - frame.getWidth() / 2, screenDim.height / 2 - frame.getHeight() / 2);
		
		// create button for console visualization
		JButton consoleButton = new JButton("Console Visualization");
		consoleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				ConsoleVisualization();
				
			}
		});
		
		// create button for JFrame visualization
		JButton gridButton = new JButton("Grid Visualization");
		gridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				GridInformation();
				
			}
		});
		
		// add the buttons
		frame.getContentPane().add(consoleButton);
		frame.getContentPane().add(gridButton);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private static void ConsoleVisualization() {
		
		int choice = 1;

		while (choice == 1) {
			
			// get amount of rows
			System.out.println("How many rows would you like in your grid? (Please be generous cause memory can be an issue!): ");
			rows = input.nextInt();
			
			// get amount of columns
			System.out.println("How many columns would you like in your grid? (Please be generous cause memory can be an issue!): ");
			cols = input.nextInt();
			
			// get amount of obstacles
			System.out.println("How many obstacles would you like in your grid?: ");
			obstacleCount = input.nextInt();
			
			// generate a new grid at the start of every loop
			System.out.println("Generating new grid to navigate.");
			grid = new Grid(rows, cols, obstacleCount);
			grid.GenerateGrid();

			// block the grid after it is generated
			System.out.println("Adding roadblocks to cells.");
			grid.BlockGrid();
			
			// ask the user to enter the start node's X and Y value
			System.out.print("Please enter a starting node X value (0-" + (rows - 1) + "): ");
			startX = input.nextInt();

			System.out.print("Please enter a starting node Y value (0-" + (cols - 1) + "): ");
			startY = input.nextInt();
			System.out.println();

			while (CheckBounds(startX, startY, rows, cols)) {

				System.out.println("Invalid entry.");

				System.out.print("Please enter a new X value for your start node (0-" + (rows - 1) + "...): ");
				startX = input.nextInt();

				System.out.print("Please enter a new Y value for your start node (0-" + (cols - 1) + "...): ");
				startY = input.nextInt();

			}

			startNode = grid.GetNode(startX, startY);
			startNode.SetInfo("S");

			// ask the user to enter the goal node's X and Y value
			System.out.print("Please enter a goal node X value (0-" + (rows - 1) + "): ");
			goalX = input.nextInt();

			System.out.print("Please enter a goal node Y value (0-" + (cols - 1) + "): ");
			goalY = input.nextInt();
			System.out.println();

			while (CheckBounds(goalX, goalY, rows, cols)) {

				System.out.println("Invalid entry.");

				System.out.print("Please enter a new X value for your goal node (0-" + (rows - 1) + "...): ");
				goalX = input.nextInt();

				System.out.print("Please enter a new Y value for your goal node (0-" + (cols - 1) + "...): ");
				goalY = input.nextInt();

			}

			goalNode = grid.GetNode(goalX, goalY);
			goalNode.SetInfo("G");

			// print update grid
			System.out.println("Displaying grid. Cells with an X cannot be traversed.");
			grid.PrintGrid();

			// A* search algorithm
			if (goalNode == startNode)
				System.out.println("Already on goal node...");
			else {
				System.out.println("Starting A* search algorithm.");
				found = AStarSearch(goalNode, startNode);

				if (found)
					ConstructPath(goalNode);
				else
					System.out.println("Path not found! (Due to blockages)");
			}

			// ask to keep going or not
			System.out.print("Want to continue going? Enter 1 to continue or any other integer to stop: ");
			choice = input.nextInt();

		}
	}
	
	private static void GridInformation() {
		
		// initialize the frame to gather board information
		JFrame frame = new JFrame("Board Information");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();		
		frame.setSize(screenDim.height / 2, screenDim.height / 2);
		frame.setLocation(screenDim.width / 2 - frame.getWidth() / 2, screenDim.height / 2 - frame.getHeight() / 2);
		
		// create a panel for the display information
		int infoRows = 4;
		int infoCols = 4;
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(infoRows, infoCols, 2, 2));
		infoPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(infoPanel, BorderLayout.CENTER);
		
		// panel just for start button
		JPanel startPanel = new JPanel();
		startPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(startPanel, BorderLayout.SOUTH);
		
		// create 2D panel holder to be able to slap labels in where we want!
		JPanel[][] panelHolder = new JPanel[infoRows][infoCols];
		for (int i = 0; i < infoRows; i++)
			for (int j = 0; j < infoCols; j++) {
				
				panelHolder[i][j] = new JPanel();
				panelHolder[i][j].setBackground(Color.WHITE);
				infoPanel.add(panelHolder[i][j]);
				
			}
		
		// add labels for the text fields
		JLabel gridSizeLabel = new JLabel("Size of Grid [x, y]:");
		JLabel startLabel = new JLabel("Start Node [x, y]:");
		JLabel goalLabel = new JLabel("Goal Node [x, y]:");
		JLabel obstaclesLabel = new JLabel("Obstacles:");
		Dimension dim = new Dimension(24, 24);
		
		// add text fields to enter A* grid information
		JTextField gridRows = new JTextField();
		JTextField gridCols = new JTextField();
		gridRows.setPreferredSize(dim);
		gridCols.setPreferredSize(dim);
		
		JTextField startXText = new JTextField();
		JTextField startYText = new JTextField();
		startXText.setPreferredSize(dim);
		startYText.setPreferredSize(dim);
		
		JTextField goalXText = new JTextField();
		JTextField goalYText = new JTextField();
		goalXText.setPreferredSize(dim);
		goalYText.setPreferredSize(dim);
		
		JTextField obstacles = new JTextField();
		obstacles.setPreferredSize(new Dimension(48, 24));
		
		// initialize the button to start the visualization
		JButton button = new JButton("Start Visualization");
		button.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				
				// if user hasn't entered anything, have user enter something
				if (gridRows.getText().isEmpty() || gridCols.getText().isEmpty() || startXText.getText().isEmpty() || startYText.getText().isEmpty() || goalXText.getText().isEmpty() || goalYText.getText().isEmpty() || obstacles.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Not all information fields were filled out. Please make sure you have entered information for the size of the grid, start node, and goal node.", "Error - Information missing", JOptionPane.ERROR_MESSAGE);
				
				rows = Integer.parseInt(gridRows.getText());
				cols = Integer.parseInt(gridCols.getText());
				
				startX = Integer.parseInt(startXText.getText());
				startY = Integer.parseInt(startYText.getText());
				
				goalX = Integer.parseInt(goalXText.getText());
				goalY = Integer.parseInt(goalYText.getText());
				
				obstacleCount = Integer.parseInt(obstacles.getText());
				
				// if start node or goal node is out of bounds, have user enter valid information
				if (CheckBounds(startX, startY, rows, cols) && !CheckBounds(goalX, goalY, rows, cols)) {
					
					JOptionPane.showMessageDialog(null, "Start node is out of bounds, please enter a new start node.", "Error - Out of bounds", JOptionPane.ERROR_MESSAGE);
					startXText.setText("");
					startYText.setText("");
					
				}
				else if (!CheckBounds(startX, startY, rows, cols) && CheckBounds(goalX, goalY, rows, cols)) {
					
					JOptionPane.showMessageDialog(null, "Goal node is out of bounds, please enter a new goal node.", "Error - Out of bounds", JOptionPane.ERROR_MESSAGE);
					goalXText.setText("");
					goalYText.setText("");
					
				}
				else if (CheckBounds(startX, startY, rows, cols) && CheckBounds(goalX, goalY, rows, cols)) {
					
					JOptionPane.showMessageDialog(null, "Both nodes are out of bounds, please enter new nodes.", "Error - Out of bounds", JOptionPane.ERROR_MESSAGE);
					startXText.setText("");
					startYText.setText("");
					goalXText.setText("");
					goalYText.setText("");
					
				}
				else {
					
					frame.dispose();
					GridVisualization();
					
				}
			}
		});
		
		GridBagLayout gbl = new GridBagLayout();
		
		panelHolder[0][1].setLayout(gbl);
		panelHolder[0][1].add(gridSizeLabel);
		
		panelHolder[0][2].setLayout(gbl);
		panelHolder[0][2].add(gridRows);
		panelHolder[0][2].add(gridCols);
		
		panelHolder[1][1].setLayout(gbl);
		panelHolder[1][1].add(startLabel);
		
		panelHolder[2][1].setLayout(gbl);
		panelHolder[2][1].add(goalLabel);
		
		panelHolder[3][1].setLayout(gbl);
		panelHolder[3][1].add(obstaclesLabel);
		
		panelHolder[1][2].setLayout(gbl);
		panelHolder[1][2].add(startXText);
		panelHolder[1][2].add(startYText);
		
		panelHolder[2][2].setLayout(gbl);
		panelHolder[2][2].add(goalXText);
		panelHolder[2][2].add(goalYText);
		
		panelHolder[3][2].setLayout(gbl);
		panelHolder[3][2].add(obstacles);
		startPanel.add(button);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private static void GridVisualization() {
		
		// initialize the frame for the board
		JFrame frame = new JFrame("A* Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();		
		frame.setSize(screenDim.height + 200, screenDim.height - 100);
		frame.setLocation(screenDim.width / 2 - frame.getWidth() / 2, screenDim.height / 2 - frame.getHeight() / 2);
		
		// create a panel for the display information
		int infoRows = 5;
		int infoCols = 3;
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(infoRows, infoCols, 2, 2));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
		frame.getContentPane().add(infoPanel, BorderLayout.WEST);
		
		// create 2D panel holder to be able to slap labels in where we want!
		JPanel[][] panelHolder = new JPanel[infoRows][infoCols];
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
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new GridLayout(rows, cols));
		pathPanel.setBackground(Color.WHITE);
		pathPanel.setSize(screenDim.height - 100, screenDim.height - 100);
		frame.getContentPane().add(pathPanel, BorderLayout.CENTER);
		
		// create buttons array
		JButton[][] buttons = new JButton[rows][cols];
		
		// each tile will be a button for more functionality
		for (int i = 0; i < rows; i++)
			for (int k = 0; k < cols; k++) {
						
				buttons[i][k] = new Button();
				pathPanel.add(buttons[i][k]);
						
			}
		
		frame.setVisible(true);
		
	}
}
