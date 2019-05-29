package Main;
import javax.swing.JOptionPane;

import Graph.*;

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
		
		String [] edges = {"GRU EZE 200", "EZE LAX 1000", "LAX GRU 900", "JAP LAX 200", "GRU JAP 500", "BRA JAP 5000", "EZE BRA 500"}; // simulating file, remove later
		
		for (int j = 0; j < edges.length; j++) {
			String[] edgeInfo = edges[j].split(" ");
			Node nodeFrom = graph.nodeNamed(edgeInfo[0]);
			Node nodeTo= graph.nodeNamed(edgeInfo[1]);
			double value = Double.parseDouble(edgeInfo[2]);
			
			Edge edge = new Edge(nodeFrom, nodeTo, value);
			graph.addEdge(edge);
		}
		
		
		System.out.println(graph.shortestWayBetween("JAP", "US"));
		System.out.println(graph.cheapestWayBetween("JAP", "US"));
	}

}
