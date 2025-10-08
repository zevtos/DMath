package io.github.zevtos.dmathcore.domain.graph;

import java.util.*;

/**
 * Basic graph algorithms working with {@link Graph}.
 */
public final class GraphAlgorithms {
    private GraphAlgorithms() {
    }

    public static <V> boolean isConnected(Graph<V> graph) {
        if (graph.getVertexCount() == 0) return true;
        if (graph.isDirected()) {
            // For directed graphs, consider weak connectivity by ignoring edge direction
            return isWeaklyConnected(graph);
        }
        V start = graph.getVertices().iterator().next();
        Set<V> visited = new HashSet<>();
        dfs(graph, start, visited);
        return visited.size() == graph.getVertexCount();
    }

    public static <V> List<Set<V>> connectedComponents(Graph<V> graph) {
        List<Set<V>> components = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        for (V v : graph.getVertices()) {
            if (!visited.contains(v)) {
                Set<V> component = new HashSet<>();
                dfs(graph, v, component);
                visited.addAll(component);
                components.add(component);
            }
        }
        return components;
    }

    public static <V> List<V> shortestPathBfs(Graph<V> graph, V start, V end) {
        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("Unknown start or end vertex");
        }
        Map<V, V> previous = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Deque<V> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            V u = queue.poll();
            if (Objects.equals(u, end)) break;
            for (V n : graph.getNeighbors(u)) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    previous.put(n, u);
                    queue.add(n);
                }
            }
        }
        if (!visited.contains(end)) return Collections.emptyList();
        List<V> path = new ArrayList<>();
        for (V at = end; at != null; at = previous.get(at)) {
            path.add(at);
            if (Objects.equals(at, start)) break;
        }
        if (!Objects.equals(path.get(path.size() - 1), start)) return Collections.emptyList();
        Collections.reverse(path);
        return path;
    }

    public static <V> boolean hasBridgesUndirected(Graph<V> graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Bridge detection implemented for undirected graphs only");
        }
        Map<V, Integer> disc = new HashMap<>();
        Map<V, Integer> low = new HashMap<>();
        Map<V, V> parent = new HashMap<>();
        int[] time = {0};
        boolean[] found = {false};
        for (V v : graph.getVertices()) {
            if (!disc.containsKey(v)) {
                bridgeDfs(graph, v, disc, low, parent, time, found);
                if (found[0]) return true;
            }
        }
        return false;
    }

    private static <V> void dfs(Graph<V> graph, V start, Set<V> visited) {
        Deque<V> stack = new ArrayDeque<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            V u = stack.pop();
            if (!visited.add(u)) continue;
            for (V n : graph.getNeighbors(u)) {
                if (!visited.contains(n)) stack.push(n);
            }
        }
    }

    private static <V> boolean isWeaklyConnected(Graph<V> graph) {
        if (graph.getVertexCount() == 0) return true;
        // Build undirected view via queue traversal treating edges bidirectionally
        V start = graph.getVertices().iterator().next();
        Set<V> visited = new HashSet<>();
        Deque<V> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            V u = queue.poll();
            for (V n : graph.getNeighbors(u)) {
                if (visited.add(n)) queue.add(n);
            }
            // also traverse incoming by scanning all vertices (costly for large graphs)
            for (V v : graph.getVertices()) {
                if (graph.hasEdge(v, u) && visited.add(v)) queue.add(v);
            }
        }
        return visited.size() == graph.getVertexCount();
    }

    private static <V> void bridgeDfs(
            Graph<V> graph,
            V u,
            Map<V, Integer> disc,
            Map<V, Integer> low,
            Map<V, V> parent,
            int[] time,
            boolean[] found
    ) {
        disc.put(u, ++time[0]);
        low.put(u, disc.get(u));
        for (V v : graph.getNeighbors(u)) {
            if (!disc.containsKey(v)) {
                parent.put(v, u);
                bridgeDfs(graph, v, disc, low, parent, time, found);
                low.put(u, Math.min(low.get(u), low.get(v)));
                if (low.get(v) > disc.get(u)) {
                    found[0] = true;
                }
            } else if (!Objects.equals(v, parent.get(u))) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }
    }
}
