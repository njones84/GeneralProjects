public class Node implements Comparable<Node>{

	private String info;
	private int h;
	private int g;
	private int f;
	private int gridX;
	private int gridY;
	private boolean traversable;
	
	private Node parent;
	
	public Node() {
		
		this.h = 0;
		this.g = 0;
		this.f = 0;
		this.gridX = -1;
		this.gridY = -1;
		this.traversable = true;
		this.info = "";
		this.parent = null;
		
	}
	
	public Node(int x, int y) {
		
		this.h = 0;
		this.g = 0;
		this.f = 0;
		this.gridX = x;
		this.gridY = y;
		this.traversable = true;
		this.info = "";
		this.parent = null;
		
	}
	
	@Override
	public int compareTo(Node other) {
		
		return this.f - other.f;
		
	}
	
	// get functions
	public int getHeursticCost() { return h; }
	public int getPathCost() { return g; }
	public int getSumCost() { return f; }
	public int getX() { return gridX; }
	public int getY() { return gridY; }
	public boolean getTraversable() { return traversable; }
	public String getInfo() { return info; }
	public Node getParent( ) { return parent; }
	
	// set functions
	public void setHeuristicCost(int other) { h = other; }
	public void setPathCost(int other) { g = other; }
	public void setSumCost(int other) { f = other; }
	public void setTraversable(boolean other) { traversable = other; }
	public void setInfo(String other) { info = other; }
	public void setParent(Node other) { parent = other; }

}
