package ifpb.gpes.graph;

import ifpb.gpes.Call;
import java.util.List;

/**
 *
 * @author juan
 */
public interface Graph {
    Matrix generateMatrix();
    void buildNode(Call call);
    //TODO: metodo para criar o graph
    public Matrix applyToMatrix(List<Call> calls);
}
