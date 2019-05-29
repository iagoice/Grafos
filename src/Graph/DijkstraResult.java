package Graph;

public class DijkstraResult {
	public double[] distances;
	public String[] paths;
	
	public DijkstraResult(double[] distancesIn, String[] pathsIn) {
		distances = distancesIn;
		paths = pathsIn;
	}
}
