package net.nicosia;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


/*


    Proof:

    At any point in the algorithm, there will be 2 sets of vertices A and B. 
    The vertices in A will be the vertices to which the correct maximum minimum 
    capacity path has been found. And set B has vertices to which we haven't 
    found the answer.

    Inductive Hypothesis: At any step, all vertices in set A have the correct 
    values of maximum minimum capacity path to them. ie., all previous iterations 
    are correct.

    Correctness of base case: When the set A has the vertex S only. Then the 
    value to S is infinity, which is correct.

    In current iteration, we set

    val[W] = max(val[W], min(val[V], width_between(V-W)))
    Inductive step: Suppose, W is the vertex in set B with the largest val[W]. 
    And W is dequeued from the queue and W has been set the answer val[W].

    Now, we need to show that every other S-W path has a width <= val[W]. 
    This will be always true because all other ways of reaching W will go 
    through some other vertex (call it X) in the set B.

    And for all other vertices X in set B, val[X] <= val[W]

    Thus any other path to W will be constrained by val[X], which is never 
    greater than val[W].

    Thus the current estimate of val[W] is optimum and hence algorithm computes 
    the correct values for all the vertices.

*/
public class FindMinMax {

    static Map<String, Vertex> vertices = new HashMap<String, Vertex>();
    static Map<String, Integer> paths = new HashMap<String, Integer>();

    // insert n vertices RC = O(nlog n)
    // add(Object)       RC = O(logn)
    // remove(Object)    RC = O(n)
    static Queue<Vertex> queue = new PriorityQueue<>(new Comparator<Vertex>(){
        @Override
        public int compare(Vertex o1, Vertex o2) {
            int c = o1.getCapacity().compareTo(o2.getCapacity());
            return c * -1;
        }
    });

    // array prev will store, for each node u, the node that gets 
    // us to reach node u with the maximum path capacity.
    static Map<String, String> prev = new HashMap<String, String>();    

    public static void main(String[] args) {

        // setup
        populate(vertices);
        // populateSmall(vertices);
        
        Vertex source = vertices.get("S");
        // Vertex goal = vertices.get("T");
        Vertex goal = vertices.get("G");

        StringBuffer sb = new StringBuffer();
        Set<GraphEdge> edges = new HashSet<>();
        for (Vertex v : vertices.values()) {
            edges.addAll(v.edges.values());
        }
        for (GraphEdge ge : edges) {
            sb.append("\t" + ge + "\n");
        }
        System.out.println("Weighted graph G: \n" + sb.toString());

        System.out.println("\n\nInitialize capacity of all vertices to -infinity except initialize source vertex to +infinity");
        System.out.println("\nCreate Max Heap with all vertices based on capacity of vertex");
        System.out.println("\nRuntime Complexity = O(V) + O(V), this is because we traverse each vertex to set capacity and than heapify");
        System.out.println("\nFor each vertex in Max Heap");
        System.out.println("\nRemove max capacity vertex from the Max Heap");
        System.out.println("\nRuntime Complexity = O(VlogV), this is because each vertex is removed one at a time for processing");
        System.out.println("\nFor each vertex visit all neighbors and update the capacity of the neighbor to the maximum of 1) the edge cost and 2) the neighbors parent vertex capacity");
        System.out.println("\nFor the neighbor vertex, set the parent vertex");
        System.out.println("\nAfter updating capacity of the neighbor vertex, swap the vertex with the root of the Max Heap and percolate up");
        System.out.println("\nRuntime Complexity = O(log V), this is because the number of children a vertex can have in a Max Heap is at most 2, therefore a constant and the percolate up takes O(logE).  The index in the heap (heap defined using array) can be kept in a hashtable allowing constant time retrieval from the heap.");
        System.out.println("\nTraverse from destinatioon vertex to previous until previous is null");
        System.out.println("\nRuntime Complexity = O(E), potentially all edges will be traversed in the worst case");

        System.out.println("\nMaxMin: " + findMinMax(source, goal));

        System.out.print("\nExample Path:" );
        Vertex v = goal;
        while (v.previous != null) {
            System.out.print(v.id + " <- ");
            v = v.previous;
        }
        System.out.println(v.id);
    }

    public static Integer findMinMax(Vertex source, Vertex goal) {

        source.setCapacity(Integer.MAX_VALUE);

        for (Vertex v : vertices.values()) {
            v.previous = null;
            queue.add(v);
        }

        /**
         * Initialize capacity of all vertices to -infinty, except set source 
         * vertex capacity to +infinity.
         * 
         * Add all vertices to a PriorityQueue ordered by higest capacity.
         * 
         * While more vertices are in the PriorityQueue [RC = O(V)]
         *  1) poll PriorityQueue to get vertex with max capacity [RC = O(logV)]
         *  2) check all neighbors of the vertex. Assume the edges don't all have [RC = O(E)]
         *      a) get max value between the neighbors' capacity and the cost of the edge with neighbor
         *      b) set neighbors capacity to max value
         *      c) reorder PriorityQueue [RC = O(V) + O(logV)]
         */

         // RC = O(V)
         while (!queue.isEmpty()) {
            // get vertex with max capacity
            // RC = O(logV)
            Vertex u = queue.poll();
            if (u.getCapacity() == Integer.MIN_VALUE) {
                break;
            }
            // visit neighbor vertices
            // RC = O(E)
            for (GraphEdge e : u.edges.values()) {
                // get neighboring vertex from the edge
                Vertex v = e.v1.id.equals(u.id) ? e.v2 : e.v1;

                // get max value between the neighbors' capacity and the cost of the edge
                int alt = Math.max(v.getCapacity(), Math.min(u.getCapacity(), e.cost));
                if (alt > v.getCapacity()) {   
                    // remove vertex v, re-add after updating capacity    
                    // RC = O(V)
                    // decrease-key
                    queue.remove(v);
                    v.setCapacity(alt);
                    v.previous = u;
                    // order queue based on new capacity
                    // RC = O(logV)
                    queue.add(v);
                }
            }
        }
        // RC = O(V) * O(logV) + O(V) * O(E) * O(V) + O(V) * O(E) * O(logV) 
        return goal.getCapacity();
    }

    private static void populate(Map<String, Vertex> vertices) {
        Vertex S = new Vertex("S");
        Vertex A = new Vertex("A");
        Vertex B = new Vertex("B");
        Vertex C = new Vertex("C");
        Vertex D = new Vertex("D");
        Vertex E = new Vertex("E");
        Vertex G = new Vertex("G");
        S.setCapacity(Integer.MIN_VALUE);
        A.setCapacity(Integer.MIN_VALUE);
        B.setCapacity(Integer.MIN_VALUE);
        C.setCapacity(Integer.MIN_VALUE);
        D.setCapacity(Integer.MIN_VALUE);
        E.setCapacity(Integer.MIN_VALUE);
        G.setCapacity(Integer.MIN_VALUE);

        GraphEdge sa = new GraphEdge(A, S, 2);
        A.edges.put("S", sa);
        S.edges.put("A", sa);

        GraphEdge sb = new GraphEdge(S, B, 7);
        B.edges.put("S", sb);
        S.edges.put("B", sb);

        GraphEdge ad = new GraphEdge(A, D, 9);
        A.edges.put("D", ad);
        D.edges.put("A", ad);

        GraphEdge sc = new GraphEdge(S, C, 8);
        S.edges.put("C", sc);
        C.edges.put("S", sc);

        GraphEdge be = new GraphEdge(B, E, 4);
        B.edges.put("E", be);
        E.edges.put("B", be);

        GraphEdge ce = new GraphEdge(C, E, 1);
        E.edges.put("C", ce);
        C.edges.put("E", ce);

        GraphEdge de = new GraphEdge(D, E, 5);
        D.edges.put("E", de);
        E.edges.put("D", de);

        GraphEdge dg = new GraphEdge(D, G, 7);
        D.edges.put("G", dg);
        G.edges.put("D", dg);

        GraphEdge eg = new GraphEdge(G, E, 3);
        G.edges.put("E", eg);
        E.edges.put("G", eg);

        vertices.put("S", S);
        vertices.put("A", A);
        vertices.put("B", B);
        vertices.put("C", C);
        vertices.put("D", D);
        vertices.put("E", E);
        vertices.put("G", G);
    }

    private static void populateSmall(Map<String, Vertex> vertices) {
        Vertex S = new Vertex("S");
        Vertex A = new Vertex("A");
        Vertex B = new Vertex("B");
        Vertex T = new Vertex("T");
        S.setCapacity(Integer.MIN_VALUE);
        A.setCapacity(Integer.MIN_VALUE);
        B.setCapacity(Integer.MIN_VALUE);
        T.setCapacity(Integer.MIN_VALUE);

        GraphEdge sa = new GraphEdge(A, S, 350);
        A.edges.put("S", sa);
        S.edges.put("A", sa);

        GraphEdge sb = new GraphEdge(S, B, 350);
        B.edges.put("S", sb);
        S.edges.put("B", sb);

        GraphEdge at = new GraphEdge(A, T, 200);
        A.edges.put("T", at);
        T.edges.put("A", at);

        GraphEdge bt = new GraphEdge(B, T, 100);
        B.edges.put("T", bt);
        T.edges.put("B", bt);

        vertices.put("S", S);
        vertices.put("A", A);
        vertices.put("B", B);
        vertices.put("T", T);
    }
}