import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
	
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
				consoleVisualization();
				
			}
		});
		
		// create button for JFrame visualization
		JButton gridButton = new JButton("Grid Visualization");
		gridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				gridVisualization();
				
			}
		});
		
		// add the buttons
		
		frame.getContentPane().add(gridButton);
		frame.getContentPane().add(consoleButton);
		frame.pack();
		frame.setLocation(screenDim.width / 2 - frame.getWidth() / 2, screenDim.height / 2 - frame.getHeight() / 2);
		frame.setVisible(true);
		
	}
	
	private static void consoleVisualization() {
		
		Pathfinder pathfinder = new Pathfinder();
		
		pathfinder.consoleSetUp();
		
		boolean found = pathfinder.aStar();
		
		if (found)
			pathfinder.constructPath();
		else
			System.out.println("Path not found! (Due to blockages)");
		
		DisplayChoice();
		
	}

	private static void gridVisualization() {
		
		new Frame("A* Visualization");

	}
}
