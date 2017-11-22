package ifpb.gpes.graph;

import java.util.Set;

/**
 *
 * @author juan
 */
public interface Graph {

    Matrix toMatrix();

    public Set<Node> vertex();
    public double edge(Node source, Node target);
    public boolean isConnected(Node source, Node target);
}
