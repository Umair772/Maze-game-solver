package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        if (graph == null) {
            throw new IllegalArgumentException();
        }
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));
        Set<V> vertices = new HashSet<>();
        DisjointSets<V> disjointSets = createDisjointSets();

        List<E> mst = new ArrayList<>();

        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
            vertices.add(vertex);
        }
        for (E edge : edges) {
            V first = edge.from();
            V second = edge.to();

            int firstV = disjointSets.findSet(first);
            int secondV = disjointSets.findSet(second);


            if (firstV != secondV) {
                mst.add(edge);

                //union the set
                disjointSets.union(first, second);
            }
        }
        Set<Integer> roots = new HashSet<>();
        for (V vert : vertices) {
            int root = disjointSets.findSet(vert);
            roots.add(root);
        }
        return (roots.size() <= 1) ? new MinimumSpanningTree.Success<>(mst) : new MinimumSpanningTree.Failure<>();
    }
}
