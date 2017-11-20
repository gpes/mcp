package ifpb.gpes.graph;

import ifpb.gpes.Call;

/**
 *
 * @author juan
 */
public interface Graph {
    Matrix toMatrix();
    void buildNode(Call call);
    //TODO?
    // public Set<Node> vertex 
    // public int edge 
}
