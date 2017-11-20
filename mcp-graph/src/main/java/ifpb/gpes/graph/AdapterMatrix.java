package ifpb.gpes.graph;

import java.util.function.Function;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 20/11/2017, 16:51:04
 */
public class AdapterMatrix implements Function<AbstractBaseGraph<Node, DefaultWeightedEdge>, Matrix> {

    private Matrix matrix = new Matrix();
    private AbstractBaseGraph<Node, DefaultWeightedEdge> graph;

    @Override
    public Matrix apply(AbstractBaseGraph<Node, DefaultWeightedEdge> graph) {
        this.graph = graph;
        Node[] vertices =  graph.vertexSet().toArray(new Node[]{});
        int numeroDeVertices = vertices.length;

        this.matrix = new Matrix(numeroDeVertices);

        for (int i = 0; i < numeroDeVertices; i++) {
            for (int j = 0; j < numeroDeVertices; j++) {
                Matrix.Cell cell = matrix.cell(i, j);
                DefaultWeightedEdge edge = edge(vertices, i, j);
                cell.set(weight(edge));
            }
            matrix.updateNameColumn(i, vertices[i].getMethodName());
        }
        return this.matrix;
    }

    private DefaultWeightedEdge edge(Node[] vertices, int i, int j) {
        Node firstNode = vertices[i];
        Node secondNode = vertices[j];
        DefaultWeightedEdge edge = graph.getEdge(firstNode, secondNode);
        return edge;
    }

    private int weight(DefaultWeightedEdge edge) {
        if (edge != null) {
            return (int) graph.getEdgeWeight(edge);
        } else {
            return 0;
        }
    }
}
