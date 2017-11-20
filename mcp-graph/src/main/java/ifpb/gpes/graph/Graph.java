package ifpb.gpes.graph;

import ifpb.gpes.Call;

/**
 *
 * @author juan
 */
public interface Graph {
    Matrix toMatrix();
    void buildNode(Call call);
    //TODO: metodo para criar o graph
//    public Matrix applyToMatrix(List<Call> calls);
}
