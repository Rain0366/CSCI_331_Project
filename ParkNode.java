public class ParkNode {
    private final int id;
    private final String name;
    private final String borough;
    private final double acres;
    private final double latitude;
    private final double longitude;

    public ParkNode(int id, String name, String borough, double acres, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.borough = borough;
        this.acres = acres;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBorough() { return borough; }
    public double getAcres() { return acres; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return String.format("[%02d] %-42s (%s) | %.2f acres | (%.5f, %.5f)", id, name, borough, acres, latitude, longitude);
    }
}