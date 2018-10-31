package MultiGraph;

public class Line implements Edge {

    private String label;
    private Station origin, destination;

    Line (String label, Station origin, Station destination) {
        this.label = label;
        this.origin = origin;
        this.destination = destination;
    }

    // effects: returns lineLabel
    @Override
    public String getLabel() {
        return label;
    }

    // effects: returns lineFirstStation
    @Override
    public Station origin() {
        return origin;
    }

    // effects: returns lineSecondStation
    @Override
    public Station destination () {
        return destination;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Line)
            return (((Line)o).label.equals(label) && ((Line)o).origin.equals(origin)
                    && ((Line)o).destination.equals(destination));
        return false;
    }

    @Override
    public int hashCode () {
        return label.hashCode()+origin.hashCode()+destination.hashCode();
    }

    @Override
    public String toString() {
        return origin.toString()+" "+label+" "+destination.toString();
    }
}