package Graph;

import java.awt.geom.Line2D;

public class Edge implements Cloneable {
	public Node from;
	public Node to;
	public double value;
	public double distance;
	public int altitude;
	
	public Edge (Node a, Node b, double valueIn) {
		from = a;
		to = b;
		value = valueIn;
		altitude = 10000;
		if (a != null && 
			b != null) {
			distance = calculateDistance(a, b);
		}
		
	}
	
	public Edge(double valueIn) {
		value = valueIn;
		altitude = 10000;
	}
	
	private double calculateDistance(Node a, Node b) {
		return Math.sqrt(Math.pow((a.longitude - b.longitude), 2) + Math.pow((a.latitude - b.latitude), 2)); 
	}
	
	public void showEdge() {
		System.out.println(from.name+"-"+to.name+":"+value);
	}//end showEdge()
	
	public Edge reverseEdge() {
		return new Edge(to, from, value);
	}
	
	public boolean cross (Edge edge) {
		if (from.name.equals(edge.from.name) ||
			from.name.equals(edge.to.name)	||
			to.name.equals(edge.from.name)	||
			to.name.equals(edge.to.name)) {
				return false;
		} else {
			return Line2D.linesIntersect(from.longitude, from.latitude, to.longitude, to.latitude, edge.from.longitude, edge.from.latitude, edge.to.longitude, edge.to.latitude) && altitude == edge.altitude;
		}
	}
	
}//end Edge
