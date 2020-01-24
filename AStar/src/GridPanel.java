import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GridPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

	private int width;
	private int height;
	private int nodeSize;
	
	private Node start;
	private Node goal;
	private Pathfinder pathfinder;
	private Frame frame;
	
	public GridPanel(Frame frame, int width, int height) {
		
		this.width = width;
		this.height = height;
		this.nodeSize = 20;
		
		this.start = null;
		this.goal = null;
		this.pathfinder = new Pathfinder(this);
		this.frame = frame;
		
		// set up grid
		pathfinder.FrameSetUp();
		
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);

		this.repaint();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		// draw grid
		g.setColor(Color.BLACK);
		for (int i = 0; i < width / nodeSize - 1; i++)
			for (int j = 0; j < height / nodeSize - 1; j++)
				g.drawRect(i * nodeSize, j * nodeSize, nodeSize, nodeSize);
		
		// draw borders
		g.setColor(Color.WHITE);
		for (int i = 0; i < width / nodeSize - 1; i++)
			for (int j = 0; j < height / nodeSize - 1; j++)
				g.fillRect(i * nodeSize + 1, j * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		
		// draw frontier
		g.setColor(Color.BLUE);
		if (pathfinder.getFrontier() != null)
			for (Node node : pathfinder.getFrontier())
				g.fillRect(node.getX() * nodeSize + 1, node.getY() * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		else
			System.out.print("Frontier is null.");
		
		// draw fringe
		g.setColor(Color.ORANGE);
		if (pathfinder.getFringe() != null)
			for (Node node : pathfinder.getFringe())
				g.fillRect(node.getX() * nodeSize + 1, node.getY() * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		else
			System.out.print("Fringe is null.");
		
		// draw path
		g.setColor(Color.YELLOW);
		if (pathfinder.getPath() != null)
			for (Node node : pathfinder.getPath())
				g.fillRect(node.getX() * nodeSize + 1, node.getY() * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		else
			System.out.print("Path is null.");
		
		// draw obstacles
		g.setColor(Color.BLACK);
		if (pathfinder.getObstacles() != null)
			for (Node node : pathfinder.getObstacles())
				g.fillRect(node.getX() * nodeSize + 1, node.getY() * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		
		// draw start node
		g.setColor(Color.GREEN);
		start = pathfinder.getStartNode();
		if (start != null)
			g.fillRect(start.getX() * nodeSize + 1, start.getY() * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		
		// draw goal node
		g.setColor(Color.RED);
		goal = pathfinder.getGoalNode();
		if (goal != null)
			g.fillRect(goal.getX() * nodeSize + 1, goal.getY() * nodeSize + 1, nodeSize - 1, nodeSize - 1);
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {


		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		NodePlacement(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {


		
	}

	@Override
	public void mouseReleased(MouseEvent e) {


		
	}

	@Override
	public void mouseEntered(MouseEvent e) {


		
	}

	@Override
	public void mouseExited(MouseEvent e) {


		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		NodePlacement(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
		
	}
	
	public void NodePlacement(MouseEvent e) {
		
		// get node that was selected
		int x = e.getX() / nodeSize;
		int y = e.getY() / nodeSize;
		Node node = pathfinder.getGrid().getNode(x, y);
	
		// left click, set start node
		if (SwingUtilities.isLeftMouseButton(e)) {
			
			System.out.println("Start Node = [" + node.getX() + ", " + node.getY() + "]");
			pathfinder.setStartNode(node);
			repaint();
			
		}
		
		// right click, set goal node
		if (SwingUtilities.isRightMouseButton(e)) {
			
			System.out.println("Goal Node = [" + node.getX() + ", " + node.getY() + "]");
			pathfinder.setGoalNode(node);
			repaint();
			
		}
		
		// middle click, set obstacle
		if (SwingUtilities.isMiddleMouseButton(e)) {
			
			if (node.getTraversable() == true) {
				
				node.setTraversable(false);
				pathfinder.addObstacle(node);
				pathfinder.getGrid().setObstacleCount(pathfinder.getObstacles().size());
				repaint();
				
				System.out.println(pathfinder.getGrid().getObstacleCount());
				
			}
			else {
				
				node.setTraversable(true);
				pathfinder.deleteObstacle(node);
				pathfinder.getGrid().setObstacleCount(pathfinder.getObstacles().size());
				repaint();
				
			}
			
		}
		
	}
	
	// getters
	public JFrame getFrame() { return frame; }
	public Pathfinder getPathfinder() { return pathfinder; }
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	public int getNodeSize() { return nodeSize; }
	
}
