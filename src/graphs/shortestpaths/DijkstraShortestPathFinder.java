package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see ShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    implements ShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
        You'll also need to change the part of the class declaration that says
        `ArrayHeapMinPQ<T extends Comparable<T>>` to `ArrayHeapMinPQ<T>`.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public ShortestPath<V, E> findShortestPath(G graph, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        HashMap<V, E> edgeToV = new HashMap<>();
        HashMap<V, Double> distToV = new HashMap<>();
        distToV.put(start, 0.0);
        DoubleMapMinPQ<V> orderedPerimeter = new DoubleMapMinPQ<>();
        orderedPerimeter.add(start, 0);
        List<E> edges = new ArrayList<>();
        //Initialize distance to infinity
        //Mark source as having a distance of 0.
        //while there are unprocessed vertices
        V temp = orderedPerimeter.peekMin();
        if (!orderedPerimeter.isEmpty()) {
            for (E edge : graph.outgoingEdgesFrom(temp)) {
                V to = edge.to();
                distToV.put(to, Double.POSITIVE_INFINITY);
                edgeToV.put(to, edge);
            }
        }
        while (!orderedPerimeter.isEmpty()) {
            //Let u be the closest unprocessed vertex
            V from = orderedPerimeter.removeMin();
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();
                if (!edgeToV.containsKey(to)) {
                    edgeToV.put(to, edge);
                }
                //distToV.put(to,Double.POSITIVE_INFINITY);
                double oldDist = Double.POSITIVE_INFINITY;
                if (distToV.containsKey(to)) {
                    oldDist = distToV.get(to);
                }
                double newDist = distToV.get(from) + edge.weight();
                if (newDist < oldDist) {
                    edgeToV.put(to, edge);
                    distToV.put(to, newDist);
                    //edges.add(edge);
                    if (orderedPerimeter.contains(to)) {
                        orderedPerimeter.changePriority(to, newDist);
                    } else {
                        orderedPerimeter.add(to, newDist);
                    }
                }
            }
        }
        if (edgeToV.containsKey(end)) {

            V node = end;
            List<E> path = new ArrayList<>(); // In reverse order
            while (!node.equals(start)) {
                E prevEdge = edgeToV.get(node);
                V prevNode = prevEdge.from();
                path.add(prevEdge);
                node = prevNode;
            }
            Collections.reverse(path);
            return new ShortestPath.Success<>(path);
        }
        return new ShortestPath.Failure<>();

    }

}
