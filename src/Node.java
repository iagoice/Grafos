import java.util.ArrayList;

public class Node {
	public int value;
	public int degree;
	public ArrayList<Edge> edges;
	
	public Node (int valueIn) {
		value = valueIn;
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
		System.out.println("Value: "+value);
		System.out.println("Degree: "+degree);
		int n = edges.size();
		for(int i = 0; i < n; i++ ){
			edges.get(i).showEdge();
		}//end for
	}//end showNode()
	
	
	//needs to be rearranged depending on the problem
	public boolean equals(Node nodeIn) {
		return (value == nodeIn.value ? true : false);
	}
}//end Node
