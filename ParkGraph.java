import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ParkGraph {
    private final List<ParkNode> nodes = new ArrayList<>();
    private final Map<Integer, List<Edge>> adjacencyList = new HashMap<>();

    public void loadNodesFromCSV(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            int id = 0;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",");
                String name = tokens[0].trim();
                String borough = tokens[1].trim();
                double acres = Double.parseDouble(tokens[2].trim());
                double latitude = Double.parseDouble(tokens[3].trim());
                double longitude = Double.parseDouble(tokens[4].trim());

                ParkNode node = new ParkNode(id, name, borough, acres, latitude, longitude);
                nodes.add(node);
                adjacencyList.put(id, new ArrayList<>());
                id++;
            }
        }
    }

    public void addUndirectedEdge(ParkNode a, ParkNode b, double weight) {
        adjacencyList.get(a.getId()).add(new Edge(a, b, weight));
        adjacencyList.get(b.getId()).add(new Edge(b, a, weight));
    }

    public List<ParkNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Edge> getNeighbors(int nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    //Gets distance between parks by miles 
    public double getDistance(ParkNode a, ParkNode b) {
        return Math.hypot((a.getLatitude() - b.getLatitude()) * 69.0,
                          (a.getLongitude() - b.getLongitude()) * 52.3);
    }

    //Connect every park to its 4 closest neighbors
    public void buildEdges() {
        int n = nodes.size();
        boolean[][] connected = new boolean[n][n];

        for (ParkNode current : nodes) {
            List<ParkNode> sorted = new ArrayList<>(nodes);
            //Sort all parks by their distance from the current park using Comparator
            sorted.sort(Comparator.comparingDouble(other -> getDistance(current, other)));

            //gets the 4 closest neighbors of the current park
            for (int i = 1; i <= 4; i++) {
                ParkNode neighbor = sorted.get(i);
                int u = current.getId();
                int v = neighbor.getId();

                if (!connected[u][v]) {
                    double roadWeight = getDistance(current, neighbor) * 1.3; // 1.3x urban road cost
                    addUndirectedEdge(current, neighbor, roadWeight);
                    connected[u][v] = connected[v][u] = true;
                }
            }
        }
    }

    //Prints the neighbors of a specific park by its ID and their weights
    public void printNeighbors(int nodeId) {
        //invalid ID check
        if (nodeId < 0 || nodeId >= nodes.size()) {
            System.out.println("Invalid ID");
            return;
        }

        ParkNode node = nodes.get(nodeId);
        System.out.println("Neighbors for " + node.getName());
        
        List<Edge> neighbors = getNeighbors(nodeId);
        for (Edge edge : neighbors) {
            System.out.printf("  -> [%02d] %-35s | Weight: %.2f miles\n", 
                    edge.getTarget().getId(), 
                    edge.getTarget().getName(), 
                    edge.getWeight());
        }
    }

    public static void main(String[] args) {
        ParkGraph graph = new ParkGraph();
        try {
            graph.loadNodesFromCSV("CSCI_331_Project\\Parks.csv");
            graph.buildEdges();
            for (ParkNode node : graph.getNodes()) {
                //System.out.println(node);
            }
            System.out.println(graph.getNeighbors(02).get(0).getSource());
            graph.printNeighbors(02);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}