package Graph;
import java.util.ArrayList;

public class Node {
	public String name;
	public int degree;
	public ArrayList<Edge> edges;
	private double latitude;
	private double longitude;
	
	/*
	 * Search parameters
	 * */
	
	// Dijkstra
	public boolean visited;
	
	public Node (String nameIn) {
		name = nameIn;
		edges = new ArrayList<Edge>();
		degree = edges.size();
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
}//end Node
