import java.util.Scanner;

public class TP01{
	public static void main (String [] args) {
		/*Scanner in = new Scanner (System.in);
		String line = "";
		String [] lineParsed = null;
		int directed, size, vertice;
		Graph graph = null, complementary = null;
		//end variables
		
		line = in.nextLine();
		directed = Integer.parseInt(line);
		line = in.nextLine();
		size = Integer.parseInt(line);
		graph = new Graph(directed, size);
		
		//instantiate nodes
		for (int i = 0; i < size; i++) {
			graph.addNode(new Node (i));
		}
		
		do {
			line = in.nextLine();
			if(!line.equals("FIM")) {
				lineParsed = line.split(",");
				graph.addEdge(Integer.parseInt(lineParsed[0]), Integer.parseInt(lineParsed[1]), Integer.parseInt(lineParsed[2]));
			}
		}while (!line.equals("FIM"));
		
		line = in.nextLine();
		vertice = Integer.parseInt(line);
		line = in.nextLine();
		lineParsed = line.split(",");
		
		System.out.println(graph.nodes.get(vertice).degree);
		
		if ( graph.isEdge(Integer.parseInt(lineParsed[0]), Integer.parseInt(lineParsed[1])) )
			 System.out.println("SIM");
		else
			System.out.println("NAO");
		
		System.out.println(graph.totalEdges());
		
		
		if (graph.isComplete())
			 System.out.println("SIM");
		else
			System.out.println("NAO");
		complementary = graph.complementary();
		complementary.showGraph();
		
		in.close();//close scanner*/
	}//end main()
}//end TP01
