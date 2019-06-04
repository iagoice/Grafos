package Graph;

public class Edge implements Cloneable {
	public Node from;
	public Node to;
	public double value;
	public double distance;
	
	public Edge (Node a, Node b, double valueIn) {
		from = a;
		to = b;
		value = valueIn;
		if (a != null && 
			b != null) {
			distance = calculateDistance(a, b);
		}
		
	}//end constructor
	
	private double calculateDistance(Node a, Node b) {
		return Math.sqrt(Math.pow((a.longitude - b.longitude), 2) + Math.pow((a.latitude - b.latitude), 2)); 
	}
	
	public Edge(double valueIn) {
		value = valueIn;
	}
	
	public void showEdge() {
		System.out.println(from.name+"-"+to.name+":"+value);
	}//end showEdge()
	
	public Edge reverseEdge() {
		return new Edge(to, from, value);
	}
	
}//end Edge
