public class Node {

	private String info;
	private int h;
	private int g;
	private int f;
	private int gridX;
	private int gridY;
	private boolean navigable;
	private Node parent;
	
	public Node() {
		
		h = 0;
		g = 0;
		f = 0;
		navigable = true;
		info = "";
		
	}
	
	public Node(int x, int y) {
		
		//h = 0;
		//g = 0;
		//f = 0;
		gridX = x;
		gridY = y;
		navigable = true;
		info = "";
		
	}
	
	// get functions
	public int GetHeursticCost() { return h; }
	public int GetPathCost() { return g; }
	public int GetSumCost() { return f; }
	public int GetX() { return gridX; }
	public int GetY() { return gridY; }
	public boolean GetTraversable() { return navigable; }
	public String GetInfo() { return info; }
	public Node GetParent( ) { return parent; }
	
	// set functions
	public void SetHeuristicCost(int other) { h = other; }
	public void SetPathCost(int other) { g = other; }
	public void SetSumCost(int other) { f = other; }
	public void SetTraversable(boolean other) { navigable = other; }
	public void SetInfo(String other) { info = other; }
	public void SetParent(Node other) { parent = other; }

}
