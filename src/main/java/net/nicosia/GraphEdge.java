package net.nicosia;

import java.util.HashMap;
import java.util.Map;

public class GraphEdge {

	public Integer cost;
    public Map<String, Boolean> visited = new HashMap<String, Boolean>();;
    public Vertex v1;
    public Vertex v2;

    public GraphEdge(Vertex v1, Vertex v2, int cost) {
        this.cost = cost;
        this.v1 = v1;
        this.v2 = v2;
        this.visited.put(v1.id, Boolean.FALSE);
        this.visited.put(v2.id, Boolean.FALSE);
    }

    @Override
    public String toString() {
        return "GraphEdge [cost=" + cost + ", v1=" + v1.id + ", v2=" + v2.id + "]";
    }

}
