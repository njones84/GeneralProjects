import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Button extends JButton implements ActionListener, Comparable<Button> {

	private int gridX;
	private int gridY;
	private int h;
	private int g;
	private int f;
	private boolean traversable;
	private Button parent;
	private Color color;
	
	public Button() {
		
		super();
		addActionListener(this);
		this.gridX = -1;
		this.gridY = -1;
		this.h = 0;
		this.g = 0;
		this.f = 0;
		this.traversable = true;
		this.parent = null;
		this.color = Color.WHITE;
		
	}

	public Button(int x, int y) {
		
		super();
		addActionListener(this);
		this.gridX = x;
		this.gridY = y;
		this.h = 0;
		this.g = 0;
		this.f = 0;
		this.traversable = true;
		this.parent = null;
		this.color = Color.WHITE;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

	@Override
	public int compareTo(Button other) {
		
		return this.f - other.f;
		
	}
	
	// get functions
	public int GetHeursticCost() { return h; }
	public int GetPathCost() { return g; }
	public int GetSumCost() { return f; }
	public int GetX() { return gridX; }
	public int GetY() { return gridY; }
	public boolean GetTraversable() { return traversable; }
	public Button GetParent( ) { return parent; }
	
	// set functions
	public void SetHeuristicCost(int other) { h = other; }
	public void SetPathCost(int other) { g = other; }
	public void SetSumCost(int other) { f = other; }
	public void SetTraversable(boolean other) { traversable = other; }
	public void SetParent(Button other) { parent = other; }
	
}
