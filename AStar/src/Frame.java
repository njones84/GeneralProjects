import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/** Useful references.
 *  - https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
 */

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JSplitPane frameSplitPane;
	private JSplitPane infoPanelSplitPane;
	private GridPanel gridPanel;
	private JPanel infoPanel;
	private JPanel instructionPanel;
	private JPanel optionPanel;
	private JButton runButton;
	private JButton setButton;
	private JButton clearButton;
	
	private Pathfinder pathfinder;
	
	public Frame() {
		
		frameSplitPane = null;
		infoPanelSplitPane = null;
		gridPanel = null;
		infoPanel = null;
		instructionPanel = null;
		optionPanel = null;
		runButton = null;
		setButton = null;
		clearButton = null;
		
	}
	
	public Frame(String name) {
		
		// set title of frame
		setTitle(name);
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();		
		setSize(screenDim.height - 100, screenDim.height - 100);
		setLocation(screenDim.width / 2 - getWidth() / 2, screenDim.height / 2 - getHeight() / 2);
		
		// set the layout of our frame
		getContentPane().setLayout(new GridLayout());
		
		// set up the left and right panel for our split pane and get the Pathfinder
		gridPanel = new GridPanel(this, (int) getHeight() / 4 * 3, screenDim.height - 100);
		pathfinder = gridPanel.getPathfinder();
		
		infoPanel = new JPanel(new GridLayout());
		instructionPanel = new JPanel();
		optionPanel = new JPanel();
		
		// set up the split pane for our frame so we can put stuff side by side
		frameSplitPane = new JSplitPane();
		frameSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		frameSplitPane.setDividerLocation((int) (getWidth() / 4));
		frameSplitPane.setLeftComponent(infoPanel);
		frameSplitPane.setRightComponent(gridPanel);
		getContentPane().add(frameSplitPane);
		
		// set up the split pane for the info panel
		infoPanelSplitPane = new JSplitPane();
		infoPanelSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		infoPanelSplitPane.setDividerLocation((int) getHeight() / 2);
		infoPanelSplitPane.setTopComponent(instructionPanel);
		infoPanelSplitPane.setBottomComponent(optionPanel);
		infoPanel.add(infoPanelSplitPane);
		
		// create buttons and add functionality to them
		runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// show errors, if no errors run the pathfinder
				if (pathfinder.getStartNode() == null && pathfinder.getGoalNode() == null)
					JOptionPane.showMessageDialog(null, "Please have a start and goal node before running the algorithm.", "Error - Start/Goal missing", JOptionPane.ERROR_MESSAGE);
				else if (pathfinder.getStartNode() == null)
					JOptionPane.showMessageDialog(null, "Please have a start node before running the algorithm.", "Error - Start missing", JOptionPane.ERROR_MESSAGE);
				else if (pathfinder.getGoalNode() == null)
					JOptionPane.showMessageDialog(null, "Please have a goal node before running the algorithm.", "Error - Goal missing", JOptionPane.ERROR_MESSAGE);
				else {
					
					boolean found = pathfinder.AStarSearch();
					
					// show only path if a solution is found
					if (found) {
					
						pathfinder.ConstructPath();
						
					}
					
					gridPanel.repaint();
				}
			}
		});
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// clear everything (start node, goal node, frontier, fringe, path, obstacles)
				pathfinder.setStartNode(null);
				pathfinder.setGoalNode(null);
				pathfinder.deleteFrontier();
				pathfinder.deleteFringe();
				pathfinder.deletePath();
				pathfinder.deleteAllObstacles();
				gridPanel.repaint();
				
			}
		});
		
		optionPanel.add(runButton);
		optionPanel.add(clearButton);
		
		setVisible(true);
		
	}
	
}