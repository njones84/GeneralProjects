import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Scanner;

public class Pathfinder {
	
	private int startX;
	private int startY;
	private int goalX;
	private int goalY;

	private PriorityQueue<Node> frontier;
	private PriorityQueue<Node> fringe;
	private PriorityQueue<Node> neighbors;
	private Stack<Node> path;
	private ArrayList<Node> obstacles;
	
	private Node start;
	private Node goal;
	private Grid grid;
	private GridPanel panel;
	
	private Scanner input;
	
	public Pathfinder() {
		
		this.startX = -1;
		this.startY = -1;
		this.goalX = -1;
		this.goalY = -1;
		
		this.start = null;
		this.goal = null;
		this.grid = null;
		this.panel = null;
		
		this.input = new Scanner(System.in);
		
	}
	
	public Pathfinder(GridPanel panel) {
		
		this.startX = -1;
		this.startY = -1;
		this.goalX = -1;
		this.goalY = -1;
		
		obstacles = new ArrayList<Node>();
		
		this.start = null;
		this.goal = null;
		this.grid = null;
		this.panel = panel;
		
	}
	
	public void ConsoleSetUp() {
		
		// get amount of rows
		System.out.println("How many rows would you like in your grid? (Please be generous cause memory can be an issue!): ");
		int rows = input.nextInt();
		
		// get amount of columns
		System.out.println("How many columns would you like in your grid? (Please be generous cause memory can be an issue!): ");
		int cols = input.nextInt();
		
		// get amount of obstacles
		System.out.println("How many obstacles would you like in your grid?: ");
		int obstacleCount = input.nextInt();
		
		// generate a new grid at the start of every loop
		System.out.println("Generating new grid to navigate.");
		setGrid(new Grid(rows, cols, obstacleCount));
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

		while (grid.CheckBounds(startX, startY)) {

			System.out.println("Invalid entry.");

			System.out.print("Please enter a new X value for your start node (0-" + (rows - 1) + "...): ");
			startX = input.nextInt();

			System.out.print("Please enter a new Y value for your start node (0-" + (cols - 1) + "...): ");
			startY = input.nextInt();

		}

		setStartNode(grid.getNode(startX, startY));
		start.setInfo("S");

		// ask the user to enter the goal node's X and Y value
		System.out.print("Please enter a goal node X value (0-" + (rows - 1) + "): ");
		goalX = input.nextInt();

		System.out.print("Please enter a goal node Y value (0-" + (cols - 1) + "): ");
		goalY = input.nextInt();
		System.out.println();

		while (grid.CheckBounds(goalX, goalY)) {

			System.out.println("Invalid entry.");

			System.out.print("Please enter a new X value for your goal node (0-" + (rows - 1) + "...): ");
			goalX = input.nextInt();

			System.out.print("Please enter a new Y value for your goal node (0-" + (cols - 1) + "...): ");
			goalY = input.nextInt();

		}

		setGoalNode(grid.getNode(goalX, goalY));
		goal.setInfo("G");
		goal.setTraversable(true);

		// print update grid
		System.out.println("Displaying grid. Cells with an X cannot be traversed.");
		grid.PrintGrid();
	
	}
	
	public void FrameSetUp() {
		
		this.grid = new Grid(this.panel.getWidth() / this.panel.getNodeSize() - 1, this.panel.getHeight() / this.panel.getNodeSize() - 1, 0);
		grid.GenerateGrid();
		
	}
	
 	public boolean AStarSearch() {
 		
		// variables to keep track of nodes
		frontier = new PriorityQueue<Node>();
		fringe = new PriorityQueue<Node>();
		neighbors = new PriorityQueue<Node>();
		Node currentNode = start;

		// set the current node's distance and visual information
		// (Diagonal Distance)
		currentNode.setHeuristicCost(Math.abs(currentNode.getX() - goal.getX()) + Math.abs(currentNode.getY() - goal.getY()));
		currentNode.setPathCost(0);
		currentNode.setSumCost(currentNode.getHeursticCost() + currentNode.getPathCost());

		// add current node to the frontier
		frontier.add(currentNode);

		// while the frontier is not empty
		while (!frontier.isEmpty()) {

			// get the new node each loop and add it to the fringe
			currentNode = frontier.poll();
			fringe.add(currentNode);
			
			// get neighbors
			neighbors = grid.AddNeighbors(currentNode);
			
			// loop through the neighbors queue
			for (Node node : neighbors) {

				// check if goal node
				if (node == goal) {

					// repaint the panel if one exists
					if (panel != null)
						panel.repaint();
					
					node.setParent(currentNode);
					return true;
					
				}
				
				// check if the fringe contains this node, if it does skip it
				if (fringe.contains(node))
					continue;
						
				// if frontier contains this neighbor, check if its path cost is greater than or equal to the current node (the most optimal so far), and if so, then skip it since it is not as optimal
				if (frontier.contains(node))
					if (node.getPathCost() >= currentNode.getPathCost())
						continue;
				
				// set the child's respective H, G, and F costs and check for diagonal
				node.setHeuristicCost(Math.abs(node.getX() - goal.getX()) + Math.abs(node.getY() - goal.getY()));
				node.setPathCost(currentNode.getPathCost() + 1);
				node.setSumCost(node.getHeursticCost() + node.getPathCost());
				
				// add the child to the frontier
				frontier.add(node);
				node.setParent(currentNode);
				
				// debug statement
				System.out.println("Adding child: " + node.getSumCost() + " [" + node.getX() + ", " + node.getY() + "]");

			}
			System.out.println();
		}

		// repaint the panel if one exists
		if (panel != null)
			panel.repaint();
		
		// if all else fails, return false
		return false;

	}
	
	public void ConstructPath() {

		Node node = goal;
		path = new Stack<Node>();
		
		if (panel != null) {
			
			// while the nodes have parents, add them to the path
			while (node.getParent() != null) {
				
				node = node.getParent();
				path.push(node);
				panel.repaint();
				
			}
			
		}
		else {
			
			// while the nodes have parents, add them to the path
			while (node.getParent() != null) {
				
				node = node.getParent();
				path.push(node);
				
			}
			
			// pop first node off since its the start node
			path.pop();
			
			// keep track of iteration number
			int count = 0;
			
			// traverse through the stack to show the path in iterations
			while (!path.isEmpty()) {
				
				System.out.println("Current iteration of A* - " + ++count);
				
				node = path.pop();
				node.setInfo("E");
				grid.PrintGrid();
				
			}
			
		}
			
	}

	// getters
	public PriorityQueue<Node> getFrontier() { return frontier; }
	public PriorityQueue<Node> getFringe() { return fringe; }
	public PriorityQueue<Node> getNeighbors() { return neighbors; }
	public Stack<Node> getPath() { return path; }
	public ArrayList<Node> getObstacles() { return obstacles; }
	public Grid getGrid() { return grid; }
	public Node getStartNode() { return start; }
	public Node getGoalNode() { return goal; }
	
	// setters
	public void setStartNode(Node other) { this.start = other; }
	public void setGoalNode(Node other) { this.goal = other; }
	public void setGrid(Grid other) { this.grid = other; }
	public void setStartX(int other) { this.startX = other; }
	public void setStartY(int other) { this.startY = other; }
	public void setGoalX(int other) { this.goalX = other; }
	public void setGoalY(int other) { this.goalY = other; }
	
	// adders
	public void addObstacle(Node other) { obstacles.add(other);}
	
	// deleters
	public void deleteFrontier() { frontier = new PriorityQueue<Node>(); }
	public void deleteFringe() { fringe = new PriorityQueue<Node>(); }
	public void deletePath() { path = new Stack<Node>(); }
	public void deleteObstacle(Node other) { obstacles.remove(other); }
	public void deleteAllObstacles() { obstacles = new ArrayList<Node>(); }
	
}
