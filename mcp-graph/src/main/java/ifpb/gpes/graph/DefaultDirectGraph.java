package ifpb.gpes.graph;

import ifpb.gpes.Call;
import java.util.Set;
import java.util.Stack;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author juan
 */
public class DefaultDirectGraph implements Graph<Node,Double> {

    private final DefaultDirectedWeightedGraph<Node, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private final Stack<Node> nodes = new Stack<>();

    @Override
    public Matrix toMatrix() {
        return new AdapterMatrix(graph)
                .get();
    }

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

    @Override
    public Set<Node> vertex() {
        return graph.vertexSet();
    }

    @Override
    public Double edge(Node source, Node target) {
        if(isConnected(source, target)){
            DefaultWeightedEdge edge = graph.getEdge(source, target);
            return graph.getEdgeWeight(edge);
        }
        return 0d;
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

    private boolean isNotNullCallMethod(Call call) {
        return call.getCallMethod() != null && !"null".equals(call.getCallMethod().trim());
    }
    
    @Override
    public boolean isConnected(Node source, Node target) {
        return graph.getEdge(source, target) != null;      
    }
}