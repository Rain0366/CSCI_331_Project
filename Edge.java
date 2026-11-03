public class Edge {
    private final ParkNode source;
    private final ParkNode target;
    private final double weight;

    public Edge(ParkNode source, ParkNode target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public ParkNode getSource() { return source; }
    public ParkNode getTarget() { return target; }
    public double getWeight() { return weight; }
}