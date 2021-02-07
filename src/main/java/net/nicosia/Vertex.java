package net.nicosia;

import java.util.HashMap;
import java.util.Map;

public class Vertex {
    public final String id;
    public Map<String, GraphEdge> edges = new HashMap<String, GraphEdge>();
    public boolean visited = false;
    private Integer capacity = Integer.MIN_VALUE;
    public Vertex previous;

    public Vertex(String id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

	public void setCapacity(int capacity) {
        this.capacity = capacity;
	}
}
