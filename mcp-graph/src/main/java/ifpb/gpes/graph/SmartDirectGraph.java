package ifpb.gpes.graph;

import ifpb.gpes.Call;
import java.util.List;
import java.util.Stack;
import java.util.function.BiConsumer;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author juan
 */
public class SmartDirectGraph implements Graph {

    private final DefaultDirectedWeightedGraph<Node, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private Matrix matrix = new Matrix();
    private final Stack<Node> nodes = new Stack<>();

    public DefaultDirectedWeightedGraph<Node, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    @Override
    public Matrix generateMatrix() {

        Node[] vertices = graph.vertexSet().toArray(new Node[]{});
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
    //TODO: Can we use this Function? 
//    public Matrix apply(List<Call> calls) {
//        calls.forEach(this::buildNode);
//        Matrix reduce = calls.stream().reduce(new Matrix(), (Matrix t, Call u) -> {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        }, (Matrix t, Matrix u) -> {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        });
//        
//        Matrix collect = calls.stream().collect(() -> {
//            return new Matrix();
//        }, new BiConsumer<Matrix, Call>() {
//            @Override
//            public void accept(Matrix t, Call u) {
//                buildNode(u);
//                
//            }
//        }, new BiConsumer<Matrix, Matrix>() {
//            @Override
//            public void accept(Matrix t, Matrix u) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        return matrix;
//    }

    @Override
    public Matrix applyToMatrix(List<Call> calls) {
        calls.forEach(this::buildNode);
        Matrix reduce = calls.stream().reduce(new Matrix(), (Matrix t, Call u) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }, (Matrix t, Matrix u) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });

        Matrix collect = calls.stream().collect(() -> {
            return new Matrix();
        }, new BiConsumer<Matrix, Call>() {
            @Override
            public void accept(Matrix t, Call u) {
                buildNode(u);

            }
        }, new BiConsumer<Matrix, Matrix>() {
            @Override
            public void accept(Matrix t, Matrix u) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        return matrix;
//        return apply(calls);
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

    private static boolean isInvokedByMethod(Call call) {
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
