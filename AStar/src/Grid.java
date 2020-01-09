import java.util.Random;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
					System.out.print("[S]");
				else if (grid[i][j].GetInfo().equals("G"))
					System.out.print("[G]");
				else if (grid[i][j].GetInfo().equals("E"))
					System.out.print("[E]");
				else if (grid[i][j].GetTraversable())
					System.out.print("[ ]");
				else
					System.out.print("[X]");

			}
			System.out.println();
		}
	}

	public PriorityQueue<Node> AddNeighbors(Node node) {

		// create new queue to hold neighbors
		PriorityQueue<Node> neighbors = new PriorityQueue<Node>();

		// access and store the current nodes position
		int x = node.GetX();
		int y = node.GetY();

		// check for neighbors
		// down
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
