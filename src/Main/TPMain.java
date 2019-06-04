package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;

public class TPMain extends JFrame {
	
	private static Graph graph;
	
	private GraphOverlay worldMap;
	
	private JTextField graphNodesName;
	private JTextField graphEdgesName;
	private JButton loadGraph;
	
	
	private JLabel shortestPath;
	private JButton shortestPathButton;
	private JTextField shortestPathFrom;
	private JTextField shortestPathTo;
	
	private JLabel cheapestFare;
	private JButton cheapestFareButton;
	private JTextField cheapestFareFrom;
	private JTextField cheapestFareTo;
	
	private JButton aroundTheWorldButton;
	
	TPMain() {
		setLayout(new FlowLayout());

		
		// Around the world
		graphNodesName = new JTextField("nodes1.txt", 20);
		graphEdgesName = new JTextField("edges1.txt", 20);
		loadGraph = new JButton("Load graph");
		add(loadGraph);
		add(graphNodesName);
		add(graphEdgesName);
		LoadGraphAction loadAction = new LoadGraphAction(this);
		loadGraph.addActionListener(loadAction);
		
		// Shortest path
		shortestPath = new JLabel("Shortest path");
		shortestPathFrom = new JTextField(5);
		shortestPathTo = new JTextField(5);
		shortestPathButton = new JButton("Go");
		ShortestPathAction shortestPathAction = new ShortestPathAction(this);
		shortestPathButton.addActionListener(shortestPathAction);
		add(shortestPath);
		add(shortestPathFrom);
		add(shortestPathTo);
		add(shortestPathButton);
		
		// Cheapest fare
		cheapestFare = new JLabel("Cheapest fare");
		cheapestFareFrom = new JTextField(5);
		cheapestFareTo = new JTextField(5);
		cheapestFareButton = new JButton("Go");
		CheapestFareAction cheapestFareAction = new CheapestFareAction(this);
		cheapestFareButton.addActionListener(cheapestFareAction);
		add(cheapestFare);
		add(cheapestFareFrom);
		add(cheapestFareTo);
		add(cheapestFareButton);
		
		// Around the world
		aroundTheWorldButton = new JButton("Around the world");
		add(aroundTheWorldButton);
		
		// World map
//		worldMap = new ImageIcon(getClass().getResource("worldmap.jpg"));
//		worldMapLabel = new JLabel(worldMap);
		worldMap = new GraphOverlay(this);
		add(worldMap);
	}
	
	// Actions
	private class ShortestPathAction implements ActionListener {
		
		private TPMain tp;
		
		public ShortestPathAction(TPMain main) {
			tp = main;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (graph == null) return;
			String from = tp.shortestPathFrom.getText();
			String to = tp.shortestPathTo.getText();
			String result = graph.shortestWayBetween(from, to);
			
			JOptionPane.showMessageDialog(tp, result);
		}
		
	}
	
	private class CheapestFareAction implements ActionListener {
		
		private TPMain tp;
		
		public CheapestFareAction(TPMain main) {
			tp = main;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (graph == null) return;
			String from = tp.cheapestFareFrom.getText();
			String to = tp.cheapestFareTo.getText();
			String result = graph.cheapestWayBetween(from, to);
			
			JOptionPane.showMessageDialog(tp, result);
		}
	}
	
	private class LoadGraphAction implements ActionListener {
		private TPMain tpMain;
		
		public LoadGraphAction (TPMain tpmain) {
			this.tpMain = tpmain;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String nodesFileName = graphNodesName.getText();
			String edgesFileName = graphEdgesName.getText();
			try {
				graph = initializeGraph(nodesFileName, edgesFileName);
				tpMain.worldMap.repaint();
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(tpMain, "Erro ao carregar o grafo: " + exception.getLocalizedMessage());
			}
		}
	}
	
	private class GraphOverlay extends JPanel {
		private BufferedImage image;
		private String fileName = "worldmap.jpg";
		
		public GraphOverlay(TPMain tpMain) {
			try {
				File file = new File(fileName);
				image = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
	    public Dimension getPreferredSize() {
	        return new Dimension(image.getWidth(), image.getHeight());
	    }
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.drawImage(image, 0, 0, null);
			g.setColor(Color.black);
			if (graph != null) {
				for (Node node: graph.nodes) {
					int actualX = getActualX(image.getWidth()/2, node.longitude);
					int actualY = getActualY(image.getHeight()/2, node.latitude);
					g.fillOval(actualX - 30, actualY - 30, 30, 30);
					g.drawString(node.name, actualX - 30, actualY - 30);
				}
				
				for(Edge edge: graph.edgesList) {
					int fromX = getActualX(image.getWidth()/2, edge.from.longitude);
					int fromY = getActualY(image.getHeight()/2, edge.from.latitude);
					int toX = getActualX(image.getWidth()/2, edge.to.longitude);
					int toY = getActualY(image.getHeight()/2, edge.to.latitude);
					
					g.drawLine(fromX - 15, fromY - 15, toX - 15, toY - 15);
					
					g.drawString("" + edge.value, 
								(fromX > toX ? ((fromX - toX) / 2) + toX : ((toX -fromX) / 2) + fromX) - 10,
								(fromY > toY ? ((fromY - toY) / 2) + toY : ((toY -fromY) / 2) + fromY) - 10);
					g.drawString(String.format("%.3f", edge.distance), 
							(fromX > toX ? ((fromX - toX) / 2) + toX : ((toX -fromX) / 2) + fromX) - 30,
							(fromY > toY ? ((fromY - toY) / 2) + toY : ((toY -fromY) / 2) + fromY) - 30);
				}
			}
		}
		
		private int getActualX(double frameX, double coordinateX) {
			int result = (int) frameX + (int) ((frameX * coordinateX) / 180) - (coordinateX < 0 ? 30 : 20);
			return result;
		}
		
		private int getActualY(double frameY, double coordinateY) {
			int result = (int) frameY - (int) ((frameY * coordinateY) / 180) + (coordinateY < 0 ? 150 : 10);
			return result;
		}
		
	}

	public static void main(String[] args) {
		TPMain tp = new TPMain();
		tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tp.setVisible(true);
		tp.pack();
		tp.setTitle("Trabalho de Grafos");
	}
	
	private static Graph initializeGraph(String nodesName, String edgesName) throws NumberFormatException, IOException {
		graph = new Graph();
		
		File nodesFile = new File(nodesName);
		File edgesFile = new File(edgesName);
		Scanner nodesIn = new Scanner(nodesFile);
		Scanner edgesIn = new Scanner(edgesFile);
		
		int nodesCount = Integer.parseInt(nodesIn.nextLine());
		for (int j = 0; j < nodesCount; j++) {
			String line = nodesIn.nextLine();
			String[] nodeInfo = line.split(" ");
			Node node = new Node(nodeInfo[0], Double.parseDouble(nodeInfo[1]), Double.parseDouble(nodeInfo[2]));
			
			graph.addNode(node);
		}
		
		int edgesCount = Integer.parseInt(edgesIn.nextLine());
		for (int i = 0; i < edgesCount; i++) {
			String line = edgesIn.nextLine();
			String[] edgesInfo = line.split(" ");
			Node from = graph.nodeNamed(edgesInfo[0]);
			Node to = graph.nodeNamed(edgesInfo[1]);
			double value = Double.parseDouble(edgesInfo[2]);
			
			Edge edge = new Edge(from, to, value);
			graph.addEdge(edge);
		}
		
		nodesIn.close();
		edgesIn.close();
		
		return graph;
	}

}
