package Graph;
import java.util.ArrayList;

public class Graph {
	
	private final int DEFAULT_GRAPH_SIZE = 3;
	private final String DIJKSTRA_ERROR_MESSAGE = "There is no available path between those two locations!";
	/**
	 * List of nodes.
	 */
	public ArrayList<Node> nodes;
	/**
	 * Edges matrix.
	 */
	public Edge [][] edgesGrid;
	
	public ArrayList<Edge> edgesList;
	
	/**
	 * If the graph is directed or not.
	 */
	public boolean isDirected;
	
	
	public Graph () {
		edgesGrid = new Edge [DEFAULT_GRAPH_SIZE][DEFAULT_GRAPH_SIZE];
		edgesList = new ArrayList<Edge>();
		isDirected = false;
		nodes = new ArrayList<Node>(DEFAULT_GRAPH_SIZE);
	}
	
	
	/** Instantiates a new graph defining its direction and number of nodes
	 * @param directed if the graph is directed or not
	 * @param sizeIn number of nodes
	 */
	public Graph (boolean directed, int sizeIn) {
		edgesGrid = new Edge [sizeIn][sizeIn];
		edgesList = new ArrayList<Edge>();
		isDirected = directed;
		nodes = new ArrayList<Node>(DEFAULT_GRAPH_SIZE);
	}//end constructor
	
	@Override
	protected Graph clone() {
		try{  
	        return (Graph) super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	
	/** Verifies if there is an edge between two nodes, given their position.
	 * Note that the verification differs, depending on its isDirected property.
	 * @param index1 index of first node
	 * @param index2 index of second node
	 * @return result of the verification, true if there is an edge, false if not.
	 */
	public boolean isEdge(int index1, int index2) {
		boolean result = false;
		Edge edge = edgesGrid[index1][index2];
		Edge reverseEdge = edgesGrid[index2][index1];
		if (!isDirected)
			result = ((edge != null && edge.value != 0) || (reverseEdge!= null && reverseEdge.value != 0))? true : false;
		else if (isDirected)
			result = (edge != null && edge.value != 0) ? true : false;
			
		return result;
	}//end isEdge()
	
	
	/** Calculates the edge count of the graph.
	 * @return the edge count.
	 */
	public int totalEdges() {
		return edgesList.size();
	}//end totalEdges()
	
	/** If the graph is complete, there should be and edge connecting every node to every other node.
	 * @return if the graph is complete or not.
	 */
	public boolean isComplete() {
		boolean result = true;
		int n = nodes.size();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (edgesGrid[i][j] != null && edgesGrid[i][j].value == 0) {
					result = false;
					break;
				}//end if
			}//end for2
		}//end for1
		return result;
	}//end isComplete()
	
	public boolean isConnected() {
		DijkstraResult result = dijsktra(nodeAt(0).name, false);
		for (String path: result.paths) {
			if (path.isEmpty()) return false;
		}
		return true;
	}
	
	//remove for tp 2, as you'll have to add multiple entries for the search, will also have to resize array everytime something gets added
	public void addNode(Node node) {
		nodes.add(node);
	}//end addNode()
	
	public void addEdge(int index1, int index2, double value) {
		Node from = nodeAt(index1);
		Node to = nodeAt(index2);
			
		Edge edge = new Edge(from, to, value);
		
		edgesGrid[index1][index2] = edge;
	}//end addEdge()
	
	public boolean addEdge(Edge edge) {
		boolean result = false;
		if (nodeNamed(edge.from.name) != null &&
			nodeNamed(edge.to.name) != null) {
			edgesList.add(edge);
			edge.from.addEdge(edge);
			if(!isDirected) {
				edge.to.addEdge(edge.reverseEdge());
			}
		}
		return result;
	}
	
	public void showGraphByIndex() {
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				if (edgesGrid[i][j] != null && edgesGrid[i][j].value != 0 && i < j)
					System.out.println(i+","+j+","+edgesGrid[i][j]);
			}//end for2
		}//end for1
	}
	
	public void showGraphByNames() {
		for (Edge edge: edgesList) {
			System.out.println(edge.from.name + ", " + edge.to.name + ", " + edge.value);
		}
	}
	
	
	public Node nodeNamed(String name) {
		for (Node node: nodes) {
			if (node.name.equals(name)) {
				return node;
			}
		}
		return null;
	}
	
	private int indexOfNodeNamed(String name) {
		int index = -1;
		for (Node node: nodes) {
			if(node.name.equals(name)) {
				return nodes.indexOf(node);
			}
		}
		return index;
	}
	
	private Node nodeAt(int index) {
		return nodes.get(index);
	}
	
	/* 
	 * Search methods
	 * */
	
	private void clearVisited() {
		for (Node node: nodes) {
			node.unvisit();
		}
	}
	
	private boolean areAllVisited() {
		for (Node node: nodes) {
			if (!node.visited) return false; 
		}
		return true;
	}
	
	private boolean areAllBlack() {
		for (Node node: nodes) {
			if (!node.isBlack()) return false;
		}
		return true;
	}
	
	public Graph minimalGeneratingTree() {
		clearVisited();
		return prim();
	}
	
	private Graph prim() {
		Graph resultingGraph = new Graph(isDirected, nodes.size());
		
		ArrayList<Edge> currentTouchingEdges = new ArrayList<>();
		
		Node currentNode = nodeAt(0);
		resultingGraph.addNode(currentNode);
		
		while (resultingGraph.nodes.size() < nodes.size()) {
			currentNode.visit();
			currentTouchingEdges.addAll(notVisitedEdges(currentNode.edges));
			currentTouchingEdges = notVisitedEdges(currentTouchingEdges);
			Edge shortestEdge = shortestEdge(currentTouchingEdges);
			Node nodeToAdd = shortestEdge.to.clone();
			resultingGraph.addNode(nodeToAdd);
			resultingGraph.addEdge(shortestEdge);
			currentNode = shortestEdge.to;
			
		}
		clearVisited();
		return resultingGraph;
	}
	
	private Edge shortestEdge(ArrayList<Edge> edges) {
		Edge shortest = new Edge(Double.MAX_VALUE);
		for(Edge edge: edges) {
			if (edge.value < shortest.value &&
				!edge.to.visited)
				shortest = edge;
		}
		return shortest;
	}
	
	private ArrayList<Edge> notVisitedEdges(ArrayList<Edge> edges) {
		ArrayList<Edge> result = new ArrayList<Edge>();
		for(Edge edge: edges) {
			if (!edge.to.visited)
				result.add(edge);
		}
		return result;
	}
	
	public String aroundTheWorld() {
		clearVisited();
		ArrayList<String> paths = new ArrayList<String>();
		for (Node node: nodes) {
			paths.add(aroundTheWorld(node, node, ""));
			clearVisited();
		}
		String shortest = paths.get(0);
		for (String path: paths) {
			if (path.length() < shortest.length())
				shortest = path;
 		}
		return shortest;
	}

	public String aroundTheWorld(Node current, Node searched, String result) {
		current.colorGray();
		for(Edge edge: current.edges) {
			if (edge.to.isWhite())
				aroundTheWorld(edge.to, searched, result);
		}
		
		if (current.name.equals(searched.name) && areAllBlack())
			result += " " + current.name;
		else 
			current.colorBlack();
		return result;
	}
	public String aroundTheWorldWithoutGoingBackToBeginning() {
		ArrayList<Node> oneDegreeNodes = oneDegreeNodes();
		if (!isConnected() || oneDegreeNodes.size() > 1) // if the graph is not connected or there are more than one 1 degree node
			return "Não é possível viajar ao redor do mundo!";
		
		ArrayList<Double> listOfResults = new ArrayList<>();
		if (oneDegreeNodes().size() == 0) {
			for (Node node: nodes) {
				listOfResults.add(aroundTheWorldWithoutGoingBackToBeginning(node.name));	 
			}
		} else {
			listOfResults.add(aroundTheWorldWithoutGoingBackToBeginning(oneDegreeNodes.get(0).name));
		}
		double shortest = -1.0;
		for (Double value: listOfResults) {
			if (value > shortest) shortest = value;
		}
		return "O menor preço para viajar ao redor do mundo é: " + shortest;
	}
	
	private double aroundTheWorldWithoutGoingBackToBeginning(String from) {
		clearVisited();
		Node currentNode = nodeNamed(from);
		Graph resultingGraph = new Graph(isDirected, nodes.size());
		for (Node node: nodes) {
			Node newNode = node.clone();
			newNode.edges = new ArrayList<Edge>();
			resultingGraph.addNode(newNode);
		}
		boolean failure = false;
		int failureCount = 0;
		
		while(!areAllVisited()) {
			currentNode.visit();
			Edge shortestEdge = currentNode.shortestEdgeNotVisited();
			if (shortestEdge == null) {
				failureCount++;
				failure = failureCount > 1; // if fails more than one time, it means the search failed
				break;
			}
			resultingGraph.addEdge(shortestEdge);
			currentNode = shortestEdge.to;
		}
		
		double result = 0.0;
		if (!failure) {
			for (Edge edge: resultingGraph.edgesList) {
				result += edge.value;
			}
		}
		clearVisited();
		
		return result;
	}
	
	private ArrayList<Node> oneDegreeNodes() {
		ArrayList<Node> oneDegreeNodes = new ArrayList<>();
		for (Node node: nodes) {
			if (node.degree == 1) {
				oneDegreeNodes.add(node);
			}
		}
		
		return oneDegreeNodes;
	}
	
	// Dijsktra algorithm
	public String shortestWayBetween(String fromName, String toName) {
		DijkstraResult result = dijsktra(fromName, true);
		
		int index = indexOfNodeNamed(toName);
		if (index < 0) return DIJKSTRA_ERROR_MESSAGE;
		String resultingPath = result.paths[index]; 
		if (resultingPath.isEmpty())
			return DIJKSTRA_ERROR_MESSAGE;
		else 
			return resultingPath; 
	}
	
	public String cheapestWayBetween(String fromName, String toName) {
		DijkstraResult result = dijsktra(fromName, false);
		
		int index = indexOfNodeNamed(toName);
		if (index < 0) return DIJKSTRA_ERROR_MESSAGE;
		double resultDistance = result.distances[index];
		String resultPath = result.paths[index];
		if (resultDistance == Integer.MAX_VALUE)  
			return DIJKSTRA_ERROR_MESSAGE;
		else
			return resultPath + " " + resultDistance;
	}


	private DijkstraResult dijsktra(String fromName, boolean distance) {
		
		clearVisited();
		
		Node from = nodeNamed(fromName);
		int indexOfFrom = indexOfNodeNamed(fromName);
		
		String[] paths = new String[nodes.size()];
		double[] distances = new double[nodes.size()];
		
		if (from == null) return new DijkstraResult(distances, paths);
		// Initializations ------------------------------------------------------
		// Visists origin node
		from.visit();
		
		// Initializes paths
		for (int i = 0; i < paths.length; i++) {
			paths[i] = "";
		}
		// Sets all distances to max distance
		for (int i = 0; i < distances.length; i++) {
			distances[i] = Double.MAX_VALUE;
		}
		
		// distance to self is zero
		distances[indexOfFrom] = 0;
		paths[indexOfFrom] = from.name;
		// Sets the distance from the origin edge to its adjacent nodes
		for(Edge edge: from.edges) {
			int indexOfToNode = nodes.indexOf(edge.to);
			distances[indexOfToNode] = distance ? edge.distance : edge.value;
			paths[indexOfToNode] = from.name + " " + edge.to.name;
		}
		
		while (!areAllVisited()) {
			Node currentNode = nodes.get(shortestNotVisited(distances));
			int indexOfCurrentNode = nodes.indexOf(currentNode);
			
			currentNode.visit();
			
			for(Edge edge: currentNode.edges) {
				Node neighbour = edge.to;
				int indexOfNeighbour = nodes.indexOf(neighbour);
				 if (!edge.to.visited) {
					 if(distances[indexOfCurrentNode] + (distance ? edge.distance : edge.value) < distances[indexOfNeighbour]) {
						 distances[indexOfNeighbour] = distances[indexOfCurrentNode] + (distance ? edge.distance : edge.value);
						 paths[indexOfNeighbour] = paths[indexOfCurrentNode] + " " + neighbour.name; 
					 }
				 }
			}
		}
		
		clearVisited();
		return new DijkstraResult(distances, paths);
	}
	
	private int shortestNotVisited(double[] distances) {
		Integer shortest = null;
		for(int i = 0; i < distances.length; i++) {
			if (!nodes.get(i).visited) {
				if (shortest != null) {
					if(distances[i] < distances[shortest])
						shortest = i;
				} else {
					shortest = i;
				}
			}
		}
		return shortest;
	}
	
	public void defineAltitude() {
		long startTime = System.nanoTime();
		int length = edgesList.size();
		for(int i = 0; i < length; i++) {
			for (int j = i + 1; j < length - 1; j++) {
				Edge currentEdge = edgesList.get(i);
				Edge edgeToCompare = edgesList.get(j);
				if (currentEdge.cross(edgeToCompare)) {
					edgeToCompare.altitude += 1000;
				}
			}
		}
		long endTime = System.nanoTime();
		System.out.println("Tempo de execução em milisegundos: " + ((endTime - startTime)/100000.0));
	}
	
	private Graph reduce() {
		Graph result = clone();
		ArrayList<Node> nodesToRemove = new ArrayList<Node>();
		for(Node node: nodes) {
			for(Edge firstEdge: node.edges) {
				Node secondNode = firstEdge.to;
				for(Edge secondEdge: secondNode.edges) {
					if(secondEdge.to.name.equals(node.name) && !secondEdge.to.visited){
						nodesToRemove.add(secondEdge.to);
						secondEdge.to.visit();
					}
				}
			}
		}
		nodes.removeAll(nodesToRemove);
		return result;
	}
}
