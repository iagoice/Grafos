package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	
	private static final long serialVersionUID = 1L;

	private static Graph graph;
	
	private String currentShortestPath;
	private String currentCheapestFare;
	private Graph currentGeneratingTree;
	
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
	
	private JButton minimalGeneratingTree;
	
	TPMain() {
		setLayout(new FlowLayout());

		
		// Around the world
		graphNodesName = new JTextField("nodes1.txt", 20);
		graphEdgesName = new JTextField("edges1.txt", 20);
		loadGraph = new JButton("Carregar");
		add(loadGraph);
		add(graphNodesName);
		add(graphEdgesName);
		LoadGraphAction loadAction = new LoadGraphAction(this);
		loadGraph.addActionListener(loadAction);
		
		// Shortest path
		shortestPath = new JLabel("Menor caminho");
		shortestPathFrom = new JTextField(5);
		shortestPathTo = new JTextField(5);
		shortestPathButton = new JButton("Ir");
		ShortestPathAction shortestPathAction = new ShortestPathAction(this);
		shortestPathButton.addActionListener(shortestPathAction);
		add(shortestPath);
		add(shortestPathFrom);
		add(shortestPathTo);
		add(shortestPathButton);
		
		// Cheapest fare
		cheapestFare = new JLabel("Menor tarifa");
		cheapestFareFrom = new JTextField(5);
		cheapestFareTo = new JTextField(5);
		cheapestFareButton = new JButton("Ir");
		CheapestFareAction cheapestFareAction = new CheapestFareAction(this);
		cheapestFareButton.addActionListener(cheapestFareAction);
		add(cheapestFare);
		add(cheapestFareFrom);
		add(cheapestFareTo);
		add(cheapestFareButton);
		
		// Around the world
		aroundTheWorldButton = new JButton("Ao redor do mundo");
		AroundTheWorldAction aroundTheWorldAction = new AroundTheWorldAction(this);
		aroundTheWorldButton.addActionListener(aroundTheWorldAction);
		add(aroundTheWorldButton);
		
		// Minimal generating tree
		minimalGeneratingTree = new JButton("Árvore geradora mínima");
		MinimalGeneratingTreeAction minimalGeneratingTreeAction = new MinimalGeneratingTreeAction(this);
		minimalGeneratingTree.addActionListener(minimalGeneratingTreeAction);
		add(minimalGeneratingTree);
		
		
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
			long startTime = System.nanoTime();
			String result = graph.shortestWayBetween(from, to);
			long endTime = System.nanoTime();
			System.out.println("Tempo de execução em milisegundos: " + ((endTime - startTime)/100000.0));
			currentShortestPath = result;
			currentCheapestFare = null;
			currentGeneratingTree = null;
			tp.worldMap.repaint();
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
			long startTime = System.nanoTime();
			String result = graph.cheapestWayBetween(from, to);
			long endTime = System.nanoTime();
			System.out.println("Tempo de execução em milisegundos: " + ((endTime - startTime)/100000.0));
			
			currentCheapestFare = result;
			currentShortestPath = null;
			currentGeneratingTree = null;
			tp.worldMap.repaint();
			String[] fares = currentCheapestFare.split(" ");
			String cheapestFare = fares[fares.length - 1];
			JOptionPane.showMessageDialog(tp, cheapestFare);
		}
	}
	
	private class AroundTheWorldAction implements ActionListener {
		private TPMain tp;
		
		public AroundTheWorldAction(TPMain main) {
			tp = main;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (graph == null) return;
			long startTime = System.nanoTime();
			String result = graph.aroundTheWorldWithoutGoingBackToBeginning();
			long endTime = System.nanoTime();
			System.out.println("Tempo de execução em milisegundos: " + ((endTime - startTime)/100000.0));
			JOptionPane.showMessageDialog(tp, result);
		}
	}
	
	private class MinimalGeneratingTreeAction implements ActionListener {
		private TPMain tp;
		
		public MinimalGeneratingTreeAction(TPMain main) {
			tp = main;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (graph == null) return;
			long startTime = System.nanoTime();
			currentGeneratingTree = graph.minimalGeneratingTree();
			long endTime = System.nanoTime();
			System.out.println("Tempo de execução em milisegundos: " + ((endTime - startTime)/100000.0));
			tp.worldMap.repaint();
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
			generateCompleteGraph();
			try {
				graph = initializeGraph(nodesFileName, edgesFileName);
				graph.defineAltitude();
				tpMain.worldMap.repaint();
				currentCheapestFare = null;
				currentShortestPath = null;
				currentGeneratingTree = null;
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(tpMain, "Erro ao carregar o grafo: " + exception.getLocalizedMessage());
				exception.printStackTrace();
			}
		}
	}
	
	private class GraphOverlay extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
					
					g.drawString("$: " + String.format("%.2f", edge.value), 
								(fromX > toX ? ((fromX - toX) / 2) + toX : ((toX -fromX) / 2) + fromX) - 30,
								(fromY > toY ? ((fromY - toY) / 2) + toY : ((toY -fromY) / 2) + fromY) - 30);
					g.drawString("D: " + String.format("%.3f", edge.distance), 
							(fromX > toX ? ((fromX - toX) / 2) + toX : ((toX -fromX) / 2) + fromX) - 20,
							(fromY > toY ? ((fromY - toY) / 2) + toY : ((toY -fromY) / 2) + fromY) - 20);
					
					g.drawString("A: " + edge.altitude, 
							(fromX > toX ? ((fromX - toX) / 2) + toX : ((toX -fromX) / 2) + fromX) - 10,
							(fromY > toY ? ((fromY - toY) / 2) + toY : ((toY -fromY) / 2) + fromY) - 10);
				}
				
				if(currentShortestPath != null) {
					g.setColor(Color.red);
					String[] path = currentShortestPath.split(" ");
					for (int i = 0; i < path.length - 1; i++) {
						Node from = graph.nodeNamed(path[i]);
						Node to = graph.nodeNamed(path[i + 1]);
						
						int fromX = getActualX(image.getWidth()/2, from.longitude);
						int fromY = getActualY(image.getHeight()/2, from.latitude);
						int toX = getActualX(image.getWidth()/2, to.longitude);
						int toY = getActualY(image.getHeight()/2, to.latitude);
						g.drawLine(fromX - 15, fromY - 15, toX - 15, toY - 15);
					}
				}
				
				if(currentCheapestFare != null) {
					g.setColor(Color.red);
					String[] path = currentCheapestFare.split(" ");
					for (int i = 0; i < path.length - 2; i++) {
						Node from = graph.nodeNamed(path[i]);
						Node to = graph.nodeNamed(path[i + 1]);
						
						int fromX = getActualX(image.getWidth()/2, from.longitude);
						int fromY = getActualY(image.getHeight()/2, from.latitude);
						int toX = getActualX(image.getWidth()/2, to.longitude);
						int toY = getActualY(image.getHeight()/2, to.latitude);
						g.drawLine(fromX - 15, fromY - 15, toX - 15, toY - 15);
					}
				}
				
				if (currentGeneratingTree != null) {
					g.setColor(Color.red);
					for (Edge edge: currentGeneratingTree.edgesList) {
						Node from = edge.from;
						Node to = edge.to;
						
						int fromX = getActualX(image.getWidth()/2, from.longitude);
						int fromY = getActualY(image.getHeight()/2, from.latitude);
						int toX = getActualX(image.getWidth()/2, to.longitude);
						int toY = getActualY(image.getHeight()/2, to.latitude);
						g.drawLine(fromX - 15, fromY - 15, toX - 15, toY - 15);
					}
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
	
	private static void generateCompleteGraph() {
		String[] nodes = {"AAA", "AAE", "BLQ", "BPS", "GOA", "IQQ", "POP", "MRU", "SJK", "VLN", "YBA", "WLG", "VNO", "SXM", "TRV"};
		try {
			File fout = new File("edges3.txt");
			FileOutputStream fos = new FileOutputStream(fout);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write("" + ((15 * 14)/2));
			writer.newLine();
			for (int i = 0; i < nodes.length; i++) {
				for (int j = i; j < nodes.length; j++) {
					writer.write(nodes[i] + " " + nodes[j] + " " + ((i+j)*100));
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
