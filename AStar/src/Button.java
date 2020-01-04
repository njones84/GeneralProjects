import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Button extends JButton implements ActionListener {
	
	public Button() {
		
		super();
		setFont(new Font(Font.SERIF, Font.BOLD, 125));
		setText(" ");
		setContentAreaFilled(false);
		setSize(new Dimension(5, 5));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// create functionality for the A* algorithm to show it moving
		
	}
}