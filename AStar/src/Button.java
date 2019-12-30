import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Button extends JButton implements ActionListener{

	public Button() {
		
		super();
		setFont(new Font(Font.SERIF,Font.BOLD, 125));
		setText(" ");
		addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		
		
	}

}
