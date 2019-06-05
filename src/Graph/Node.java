package Graph;
import java.util.ArrayList;

public class Node implements Cloneable {
	public String name;
	public int degree;
	public ArrayList<Edge> edges;
	public double latitude;
	public double longitude;
	
	/*
	 * Search parameters
	 * */
	
	// Dijkstra
	public boolean visited;
	
	//DFS
	public int color;
	
	public Node (String nameIn, double lat, double lon) {
		latitude = lat;
		longitude = lon;
		name = nameIn;
		edges = new ArrayList<Edge>();
		degree = edges.size();
		color = 0;
	}
	
	public Node (String nameIn) {
		name = nameIn;
		edges = new ArrayList<Edge>();
		degree = edges.size();
		color = 0;
	}
	
	public Node clone(){  
	    try{  
	        return (Node) super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	
	public void addEdge(Edge edgeIn) {
		edges.add(edgeIn);
		degree = edges.size();
	}//end addEdge()
	
	public void removeEdge(int index) {
		if (index < edges.size()) {
			edges.remove(index);
			degree = edges.size();
		}
	}//end removeEdge()
	
	public Edge shortestEdgeNotVisited() {
		Edge shortest = new Edge(Double.MAX_VALUE);
		for (Edge edge: edges) {
			if (edge.value < shortest.value &&
				!edge.to.visited)
				shortest = edge;
		}
		return shortest.value == Double.MAX_VALUE ? null : shortest;
	}
	
	public Edge shortestEdge() {
		Edge shortest = new Edge(Double.MAX_VALUE);
		for (Edge edge: edges) {
			if (edge.value < shortest.value)
				shortest = edge;
		}
		return shortest.value == Double.MAX_VALUE ? null : shortest;
	}
	
	public void removeEdgesExcept(Edge edgeToKeep) {
			edges.removeIf( edge ->
				edge.value != edgeToKeep.value &&
				!edge.to.name.equals(edgeToKeep.to.name) &&
				!edge.from.name.equals(edgeToKeep.from.name)
			);
	}
	
	public void showNode() {
		System.out.println("Value: "+name);
		System.out.println("Degree: "+degree);
		int n = edges.size();
		for(int i = 0; i < n; i++ ){
			edges.get(i).showEdge();
		}//end for
	}//end showNode()
	
	
	//needs to be rearranged depending on the problem
	public boolean equals(Node nodeIn) {
		return (name == nodeIn.name ? true : false);
	}
	
	public void visit() {
		visited = true;
	}
	
	public void unvisit() {
		visited = false;
	}
	
	public void colorWhite() {
		color = 0;
	}
	
	public void colorGray() {
		color = 1;
	}
	
	public void colorBlack() {
		color = 2;
	}
	
	public boolean isWhite() {
		return color == 0;
	}
	public boolean isGray() {
		return color == 1;
	}	
	
	public boolean isBlack() {
		return color == 2;
	}
}//end Node
