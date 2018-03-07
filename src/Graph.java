import java.util.ArrayList;
/*
 * To do: create the removeEdge and removeNode, not needed for this project
*/
public class Graph {
	public ArrayList<Node> nodes;
	public int [][] edges;
	public int isDirected;
	public int capacity;
	public int size;
	
	public Graph () {
		edges = new int [3][3];
		isDirected = 0;
		nodes = new ArrayList<Node>(size);
		capacity = 0;
		size = 3;
	}
	
	public Graph (int directed, int sizeIn) {
		edges = new int [sizeIn][sizeIn];
		isDirected = directed;
		nodes = new ArrayList<Node>(size);
		capacity = 0;
		size = sizeIn;
	}//end constructor
	
	public boolean isEdge( int index1, int index2 ) {
		boolean result = false;
		if ( isDirected == 0 )
			result = ((edges[index1][index2] != 0 || edges[index2][index1] != 0)? true : false);
		else if ( isDirected == 1 )
			result = (edges[index1][index2] != 0 ? true : false);
			
		return result;
	}//end isEdge()
	
	public int totalEdges() {
		int n = nodes.size(), count = 0;
		for ( int i = 0; i < n; i++ ) {
			for ( int j = 0; j < n; j++ ) {
				if (isDirected == 0) {
					if (i < j && edges[i][j] != 0) {
						count++;
					}//end if2
				}else if (isDirected == 1) {
					if (edges[i][j] != 0) {
						count++;
					}//end if3
				}//end else if
			}//end for2
		}//end for1
		return count;
	}//end totalEdges()
	
	public boolean isComplete() {
		boolean result = true;
		int n = nodes.size();
		for ( int i = 0; i < n; i++ ) {
			for ( int j = 0; j < n; j++ ) {
				if (edges[i][j] == 0) {
					result = false;
					break;
				}//end if
			}//end for2
		}//end for1
		return result;
	}//end isComplete()
	
	//remove for tp 2, as you'll have to add multiple entries for the search, will also have to resize array everytime something gets added
	public void addNode(Node node) {
		if (capacity < size) {
			nodes.add(node);
			capacity++;
		}
	}//end addNode()
	
	public void addEdge(int index1, int index2, int value) {
		if(edges[index1][index2] == 0) {//verify if position is available, as to not repeat any edges
			edges[index1][index2] = value;
			nodes.get(index1).addEdge(new Edge(nodes.get(index1), nodes.get(index2), value));//adds the edge to the node passed by parameter
			if (isDirected == 0) {//if is not directed, then add the opposite edge
				edges[index2][index1] = value;
				nodes.get(index2).addEdge(new Edge(nodes.get(index2), nodes.get(index1), value));
			}
			
		}//end if
	}//end addEdge()
	
	public Graph complementary() {
		Graph result = new Graph (isDirected, size);
		result.nodes.addAll(nodes);
		for ( int i = 0; i < size; i++ ) {
			for ( int j = 0; j < size; j++ ) {
				if(edges[i][j] == 0) {
					result.edges[i][j] = 1;
				} else {
					result.edges[i][j] = 0;
				}//end if else
			}//end for2
		}//end for1
		return result;
	}//end complementary()
	
	public void showGraph() {
		for ( int i = 0; i < size; i++ ) {
			for ( int j = 0; j < size; j++ ) {
				if (edges[i][j] != 0 && i < j)
					System.out.println(i+","+j+","+edges[i][j]);
			}//end for2
		}//end for1
	}
}
