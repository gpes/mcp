package ifpb.gpes.graph;

import ifpb.gpes.Call;
import java.util.HashMap;
import java.util.Map;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 *
 * @author juan
 */
public class SmartDirectGraph implements Graph {

    private final SimpleDirectedWeightedGraph<Node, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private Matrix matrix = new Matrix();
    private Map<String, Node> mapa = new HashMap<>();

    public SimpleDirectedWeightedGraph<Node, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    @Override
    public void adicionarNos(Call call) {
        Node firstnode = new Node();
        firstnode.setClassName(call.getClassType());
        firstnode.setMethodName(call.getMethodName());
        firstnode.setReturnType(call.getReturnType());
        putNode(call, firstnode);
        addNodeAsVertix(firstnode);

        if (isNotNullCallMethod(call)) {
            Node secondnode = new Node();
            secondnode.setClassName(call.getCalledInClass());
            secondnode.setMethodName(call.getCalledInMethod());
            secondnode.setReturnType("...");
            addNodeAsVertix(secondnode);
            updateNodesToGraph(secondnode, firstnode);
            String chave = call.getCalledInClass() + call.getCalledInMethod() + call.getCallMethod();
            Node get = mapa.get(chave);
            if (get != null) {
                updateNodesToGraph(firstnode, get);
            }
        }
    }

    private void putNode(Call call, Node firstnode) {
        String chave = call.getCalledInClass() + call.getCalledInMethod() + call.getMethodName();
        if (!mapa.containsKey(chave)) {
            mapa.put(chave, firstnode);
        }
    }

    private void addNodeAsVertix(Node secondnode) {
        if (!graph.containsVertex(secondnode)) {
            graph.addVertex(secondnode);
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
//                System.out.println(cell);
            }
        }

        return this.matrix;
//         Integer[][] array = IntStream.range(0, 2)
//                .mapToObj(linha -> {
//                    return IntStream.range(0, 2)
//                            .mapToObj(coluna -> {
//                                int a = matrix[linha][coluna];
//                                if (a > 0) {
//                                    return matrix[linha][coluna] + 1;
//                                }
//                                return matrix[linha][coluna];
//                            })
//                            .toArray(Integer[]::new);
//                })
//                .toArray(Integer[][]::new);
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
