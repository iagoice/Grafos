
public class Edge implements Cloneable {
	public Node from;
	public Node to;
	public int value;
	
	public Edge (Node a, Node b, int valueIn) {
		from = a;
		to = b;
		value = valueIn;
	}//end constructor
	
	public void showEdge() {
		System.out.println(from.value+"-"+to.value+":"+value);
	}//end showEdge()
	
}//end Edge
