package ifpb.gpes.graph;

import java.util.Set;

/**
 *
 * @author juan
 */
public interface Graph<V,E> {

    Matrix toMatrix();
    public Set<V> vertex();
    public E edge(V source, V target);
    public boolean isConnected(V source, V target);
}