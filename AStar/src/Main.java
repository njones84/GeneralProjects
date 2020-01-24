import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
	
	static Pathfinder pathfinder;
	static boolean found;
	
	public static void main(String[] args) {

		DisplayChoice();
	
	}
	
	private static void DisplayChoice() {
		
		// initialize the frame to show user visualization options
		JFrame frame = new JFrame("Visualization Options");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		// get screen dimensions to display the JFrame appropriately
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();		
		
		// create button for console visualization
		JButton consoleButton = new JButton("Console Visualization");
		consoleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				ConsoleViz();
				
			}
		});
		
		// create button for JFrame visualization
		JButton gridButton = new JButton("Grid Visualization");
		gridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				GridVisualization();
				
			}
		});
		
		// add the buttons
		
		frame.getContentPane().add(gridButton);
		frame.getContentPane().add(consoleButton);
		frame.pack();
		frame.setLocation(screenDim.width / 2 - frame.getWidth() / 2, screenDim.height / 2 - frame.getHeight() / 2);
		frame.setVisible(true);
		
	}
	
	private static void ConsoleViz() {
		
		pathfinder = new Pathfinder();
		
		pathfinder.ConsoleSetUp();
		
		found = pathfinder.AStarSearch();
		
		if (found)
			pathfinder.ConstructPath();
		else
			System.out.println("Path not found! (Due to blockages)");
		
		DisplayChoice();
		
	}

	private static void GridVisualization() {
		
		new Frame("A* Visualization");

	}
}
