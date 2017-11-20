package ifpb.gpes.graph;

import ifpb.gpes.Call;
import java.util.Stack;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author juan
 */
public class SmartDirectGraph implements Graph {

    private final DefaultDirectedWeightedGraph<Node, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
//    private Matrix matrix = new Matrix();
    private final Stack<Node> nodes = new Stack<>();

    @Override
    public Matrix toMatrix() {
        return new AdapterMatrix().apply(graph);
    }

    @Override
    public void buildNode(Call call) {
        Node firstnode = new Node();
        firstnode.setClassName(call.getClassType());
        firstnode.setMethodName(call.getMethodName());
        firstnode.setReturnType(call.getReturnType());
        addNodeAsVertix(firstnode);

        if (isNotNullCallMethod(call)) {
            Node get = nodes.pop();
            addNodeAsVertix(get);
            updateNodesToGraph(firstnode, get);

            if (isInvokedByMethod(call)) {
                nodes.push(firstnode);
            } else {
                Node second = nodes.pop();
                addNodeAsVertix(second);
                updateNodesToGraph(second, firstnode);
            }
        } else {
            Node secondnode = new Node();
            secondnode.setClassName(call.getCalledInClass());
            secondnode.setMethodName(call.getCalledInMethod());
            secondnode.setReturnType(call.getCalledInMethodReturnType());
            nodes.push(secondnode);
            nodes.push(firstnode);
        }
    }

    public DefaultDirectedWeightedGraph<Node, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    private boolean isInvokedByMethod(Call call) {
        String invokedBy = call.getInvokedBy();
        if (invokedBy == null) {
            return false;
        }
        return invokedBy.endsWith(")") && !invokedBy.contains("new");
    }

    private void addNodeAsVertix(Node node) {
        if (!graph.containsVertex(node)) {
            graph.addVertex(node);
        }
    }

    private void updateNodesToGraph(Node first, Node second) {
        if (!graph.containsEdge(first, second)) {
            DefaultWeightedEdge addEdge = graph.addEdge(first, second);
            graph.setEdgeWeight(addEdge, 1);
        } else {
            DefaultWeightedEdge edge = graph.getEdge(first, second);
            graph.setEdgeWeight(edge, graph.getEdgeWeight(edge) + 1);
        }
    }

    // OR isNullCallMethod
    private boolean isNotNullCallMethod(Call call) {
        return call.getCallMethod() != null && !"null".equals(call.getCallMethod().trim());
    }

//    private DefaultWeightedEdge edge(Node[] vertices, int i, int j) {
//        Node firstNode = vertices[i];
//        Node secondNode = vertices[j];
//        DefaultWeightedEdge edge = graph.getEdge(firstNode, secondNode);
//        return edge;
//    }
//    private int weight(DefaultWeightedEdge edge) {
//        if (edge != null) {
//            return (int) graph.getEdgeWeight(edge);
//        } else {
//            return 0;
//        }
//    }
}

//class AdapterMatrix implements Function<AbstractBaseGraph<Node, DefaultWeightedEdge>, Matrix> {
//
//    private Matrix matrix = new Matrix();
//    private AbstractBaseGraph<Node, DefaultWeightedEdge> graph;
//
//    @Override
//    public Matrix apply(AbstractBaseGraph<Node, DefaultWeightedEdge> graph) {
//        this.graph = graph;
//        Node[] vertices =  graph.vertexSet().toArray(new Node[]{});
//        int numeroDeVertices = vertices.length;
//
//        this.matrix = new Matrix(numeroDeVertices);
//
//        for (int i = 0; i < numeroDeVertices; i++) {
//            for (int j = 0; j < numeroDeVertices; j++) {
//                Matrix.Cell cell = matrix.cell(i, j);
//                DefaultWeightedEdge edge = edge(vertices, i, j);
//                cell.set(weight(edge));
//            }
//            matrix.updateNameColumn(i, vertices[i].getMethodName());
//        }
//        return this.matrix;
//    }
//
//    private DefaultWeightedEdge edge(Node[] vertices, int i, int j) {
//        Node firstNode = vertices[i];
//        Node secondNode = vertices[j];
//        DefaultWeightedEdge edge = graph.getEdge(firstNode, secondNode);
//        return edge;
//    }
//
//    private int weight(DefaultWeightedEdge edge) {
//        if (edge != null) {
//            return (int) graph.getEdgeWeight(edge);
//        } else {
//            return 0;
//        }
//    }
//}
