package MultiGraph;

public interface Edge {

    String getLabel();

    Node origin();

    Node destination();
}