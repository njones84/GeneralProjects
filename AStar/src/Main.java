import java.util.*;

public class Main {

	static Grid map;
	static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {

		int choice = 1;

		while (choice == 1) {

			int x;
			int y;
			int rows;
			int cols;
			int obstacleCount;
			boolean found = false;
			Node startNode;
			Node goalNode;
			
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
			map = new Grid(rows, cols, obstacleCount);
			map.GenerateMap();

			// block the map after it is generated
			System.out.println("Adding roadblocks to cells.");
			map.BlockMap();

			// ask the user to enter the start node's X and Y value
			System.out.print("Please enter a starting node X value (0-" + (rows - 1) + "): ");
			x = input.nextInt();

			System.out.print("Please enter a starting node Y value (0-" + (cols - 1) + "): ");
			y = input.nextInt();
			System.out.println();

			while (CheckBounds(x, y, rows, cols)) {

				System.out.println("Invalid entry.");

				System.out.print("Please enter a new X value for your start node (0-" + (rows - 1) + "...): ");
				x = input.nextInt();

				System.out.print("Please enter a new Y value for your start node (0-" + (cols - 1) + "...): ");
				y = input.nextInt();

			}

			startNode = map.GetNode(x, y);
			startNode.SetInfo("S");

			// ask the user to enter the goal node's X and Y value
			System.out.print("Please enter a goal node X value (0-" + (rows - 1) + "): ");
			x = input.nextInt();

			System.out.print("Please enter a goal node Y value (0-" + (cols - 1) + "): ");
			y = input.nextInt();
			System.out.println();

			while (CheckBounds(x, y, rows, cols)) {

				System.out.println("Invalid entry.");

				System.out.print("Please enter a new X value for your goal node (0-" + (rows - 1) + "...): ");
				x = input.nextInt();

				System.out.print("Please enter a new Y value for your goal node (0-" + (cols - 1) + "...): ");
				y = input.nextInt();

			}

			goalNode = map.GetNode(x, y);
			goalNode.SetInfo("G");

			// print update map
			System.out.println("Displaying map. Cells with an X cannot be traversed.");
			map.PrintMap();

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

	public static boolean AStarSearch(Node goal, Node current) {

		// variables to keep track of nodes
		ArrayList<Node> frontier = new ArrayList();
		ArrayList<Node> fringe = new ArrayList();
		ArrayList<Node> neighbors = new ArrayList();
		Node currentNode = current;
		int goalX = goal.GetX();
		int goalY = goal.GetY();

		// set the current node's distance and visual information
		// (Manhattan Distance)
		currentNode.SetHeuristicCost(Math.abs(current.GetX() - goalX) + Math.abs(current.GetY() - goalY));
		currentNode.SetPathCost(0);
		currentNode.SetSumCost(current.GetHeursticCost() + current.GetPathCost());
		currentNode.SetInfo("S");

		// add current node to the frontier
		frontier.add(current);

		// while the frontier is not empty
		while (!frontier.isEmpty()) {

			// get the new node each loop and add it to the fringe
			// clear the frontier and neighbors so the current node has a new start
			currentNode = GetBestNode(frontier);
			fringe.add(currentNode);

			// iteration after we pop the first one off
			frontier.clear();
			neighbors.clear();
			
			// get neighbors
			neighbors = map.AddNeighbors(currentNode);

			// loop through the neighbors array and add possible nodes to expand on
			for (Node node : neighbors) {

				// check if goal node
				if (node == goal) {
					
					node.SetParent(currentNode);
					return true;
					
				}

				// check if the fringe contains this node or if the node can be navigated, if it
				// does skip it
				if (fringe.contains(node) || !node.GetTraversable())
					continue;

				// set the child's respective H, G, and F costs
				node.SetHeuristicCost(Math.abs(node.GetX() - goalX) + Math.abs(node.GetY() - goalY));
				node.SetPathCost(fringe.size() + 1);
				node.SetSumCost(node.GetHeursticCost() + node.GetPathCost());

				// if the frontier contains this child, check if the child is greater than the
				// current nodes path cost, then skip it
				if (frontier.contains(node))
					if (node.GetSumCost() > currentNode.GetSumCost())
						continue;
				
				// add the child to the frontier
				System.out.println("Adding child: " + node.GetSumCost() + " [" + node.GetX() + ", " + node.GetY() + "]");
				frontier.add(node);
				node.SetParent(currentNode);

			}
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
		Stack<Node> path = new Stack();
		
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
			map.PrintMap();
			
		}
		
	}
	
	private static Node GetBestNode(ArrayList<Node> frontier) {
		
		// initialize a worst node and worst cost
        Node best = null;
        int bestSumCost = Integer.MAX_VALUE;

        // loop through each node in the frontier and determine which one is the best to expand on
        for (Node x : frontier)
        {
            if (x.GetSumCost() < bestSumCost)
            {
                bestSumCost = x.GetSumCost();
                best = x;
            }
        }

        // clear the frontier for incoming better nodes and return the best current node
        frontier.clear();
        return best;
		
	}
}
