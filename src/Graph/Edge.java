package Graph;

public class Edge implements Cloneable {
	public Node from;
	public Node to;
	public double value;
	
	public Edge (Node a, Node b, double valueIn) {
		from = a;
		to = b;
		value = valueIn;
	}//end constructor
	
	public void showEdge() {
		System.out.println(from.name+"-"+to.name+":"+value);
	}//end showEdge()
	
	public Edge reverseEdge() {
		return new Edge(to, from, value);
	}
	
}//end Edge
