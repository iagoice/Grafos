package Main;
import java.util.ArrayList;
import java.util.Random;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;

public class TPMain {

	public static void main(String[] args) {
		Graph graph = new Graph();
		
		String[] nodes = {"GRU", "EZE", "LAX", "JAP", "BRA", "US"};
		int i = 0;
		
		while(i < nodes.length) {
			Node node = new Node(nodes[i]);
			graph.addNode(node);
			i++;
 		}
		
		String [] edges = {"GRU EZE 200", "EZE LAX 1000", "LAX GRU 900", "JAP LAX 200", "GRU JAP 500", "BRA JAP 5000", "EZE BRA 500", "LAX US 600"}; // simulating file, remove later
//		String [] edges = generateRandomGraph(nodes);
		for (int j = 0; j < edges.length; j++) {
			String[] edgeInfo = edges[j].split(" ");
			Node nodeFrom = graph.nodeNamed(edgeInfo[0]);
			Node nodeTo= graph.nodeNamed(edgeInfo[1]);
			double value = Double.parseDouble(edgeInfo[2]);
			
			Edge edge = new Edge(nodeFrom, nodeTo, value);
			graph.addEdge(edge);
		}
		
//		
//		System.out.println(graph.shortestWayBetween("JAP", "US"));
//		System.out.println(graph.cheapestWayBetween("JAP", "US"));
//		System.out.println(graph.aroundTheWorldWithoutGoingBackToBeginning());
		graph.prim();
	}
	
	private static String[] generateRandomGraph(String[] nodes) {
		Random random = new Random();
		int numberOfEdges = random.nextInt((nodes.length*(nodes.length-1))/2);
		
		String[] edges = new String[numberOfEdges];
		for (int i = 0; i < numberOfEdges; i++) {
			String fromEdge = nodes[random.nextInt(nodes.length)];
			String toEdge = nodes[random.nextInt(nodes.length)];
			int value = random.nextInt(5000);
			
			edges[i] = fromEdge + " " + toEdge + " " + value;
		}
		
		return edges;
	}

}
