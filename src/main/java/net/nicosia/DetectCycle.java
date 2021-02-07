package net.nicosia;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class DetectCycle {

	static boolean[] visited;
	static int[] depth;
	static int[] parent;

	// Node to store vertex and its parent info in BFS
	static class Node {
		int v;
		Node parent;
	
		Node(int v, Node parent) {
			this.v = v;
			this.parent = parent;
		}
		
		public String toString() {
			String parentPath = this.parent == null ? "<null>" : this.parent.toString();
			return this.v + " <-- " + parentPath;
		}
	}

	public static void main(String[] args) {
		List<List<Integer>> adjListG = new ArrayList<>();
		adjListG.add(new ArrayList<Integer>());
		
		System.out.println("Given: An Adjacency List defining Graph G, find a cycle\n");
		
		System.out.println("\tAdjacency Lists are defined using array indexed with the number of the vertex\n");
		adjListG.add(1, Collections.unmodifiableList(Arrays.asList(new Integer[] {2, 3})));
		adjListG.add(2, Collections.unmodifiableList(Arrays.asList(new Integer[] {5})));
		adjListG.add(3, Collections.unmodifiableList(Arrays.asList(new Integer[] {4})));
		adjListG.add(4, Collections.unmodifiableList(Arrays.asList(new Integer[] {5})));
		adjListG.add(5, Collections.unmodifiableList(Arrays.asList(new Integer[] {})));

//		adjListG.add(1, Collections.unmodifiableList(Arrays.asList(new Integer[] {2, 3, 4})));
//		adjListG.add(2, Collections.unmodifiableList(Arrays.asList(new Integer[] {5, 9})));
//		adjListG.add(3, Collections.unmodifiableList(Arrays.asList(new Integer[] {7, 6})));
//		adjListG.add(4, Collections.unmodifiableList(Arrays.asList(new Integer[] {8, 10, 11})));
//		adjListG.add(5, Collections.unmodifiableList(Arrays.asList(new Integer[] {9})));
//		adjListG.add(6, Collections.unmodifiableList(Arrays.asList(new Integer[] {})));
//		adjListG.add(7, Collections.unmodifiableList(Arrays.asList(new Integer[] {6})));
//		adjListG.add(8, Collections.unmodifiableList(Arrays.asList(new Integer[] {})));
//		adjListG.add(9, Collections.unmodifiableList(Arrays.asList(new Integer[] {})));
//		adjListG.add(10, Collections.unmodifiableList(Arrays.asList(new Integer[] {11})));
//		adjListG.add(11, Collections.unmodifiableList(Arrays.asList(new Integer[] {12})));
//		adjListG.add(12, Collections.unmodifiableList(Arrays.asList(new Integer[] {})));

		int nbrVertices = adjListG.size();
		visited = new boolean[nbrVertices + 1];
		depth = new int[nbrVertices + 1];
		parent = new int[nbrVertices + 1];

		System.out.println("\tGraph G: " + adjListG + "\n");
		
		// find Spanning Tree using BFS given Adjacency List defining Graph G 
		System.out.println("\t1) Find a Spanning Tree T using BFS algorithm, given Graph G Adjacency List and an arbitrary source vertex");
		List<List<Integer>> adjListT = BFS(adjListG, 1, nbrVertices);
		System.out.println("\t\tGraph T, created running BFS(G, src): " + adjListT + "\n");
		System.out.println("\t\tRuntime Complexity = O(V), this is due to the fact that each vertex is only visited once.\n");
		System.out.println("\t2) If no missing edges, there are no cycles");
		System.out.println("\t\tRuntime Complexity = O(1), this is due to the fact only a single comparison is done between the number of edges on G and T\n");
		// Need to find edge missing from adjListT
		System.out.println("\t3) Find a missing edge included in G but missing from T");
		System.out.println("\t\tRuntime Complexity = O(E), this is due to the fact the search traverses the edges in the Adjacency List\n");
		int[] edge = findMissingEdge(adjListG, adjListT);
		// if none run DFS from given vertex
		System.out.println("\t4) Find the Least Common Ancestor (LCA) of the vertices that make up the missing edge");
		System.out.println("\t\tRuntime Complexity = O(E), this is due to the fact that the edges are traversed to find the LCA\n");
		// System.out.println("\t\tRuntime Complexity = O(V + V) = O(V), this is due to the fact data structure is created once and than the search is performed once\n");
		// System.out.println("\t\ta) Generate data structure to maintain depth and parent of each vertex used by LCA");
		// System.out.println("\t\t\tRuntime Complexity = O(V), this is due to the fact DFS is run visiting each vertex once starting at the source vertex and identifying the depth and parent for each vertex\n");
		
		DFS(adjListT, 1);
		// get LCA for each vertex in the missing edge to identify cycle
		// System.out.println("\t\tb) Get path from vertices of missing edge to the LCA");
		// System.out.println("\t\t\tRuntime Complexity = O(h) where h is height of the tree, however can be O(V) worst case, this is due to the fact height may be an almost linear tree on all the vertices\n");
		System.out.println("\t\t\tPath that forms a cycle: " + Arrays.toString(findLCA(edge[0], edge[1])) + "\n");
		
		System.out.println("Total Runtime Complexity = O(V + E + E) = O(V + 2E) = O(V + E)");
		
	}
	
	private static int[] findMissingEdge(List<List<Integer>> adjListG, List<List<Integer>> adjListT) {
		int[] missing = new int[2];
		int missingIndex1 = -1;
		for (int i = 1; i <= adjListG.size() + 1; i++) {
			if (adjListT.get(i).size() != adjListG.get(i).size()) {
				// found missing edge
				missingIndex1 = i;
				break;
			}
		}
		List<Integer> t = adjListT.get(missingIndex1);
		List<Integer> g = adjListG.get(missingIndex1);
		
		for (int gv : g) {
			boolean foundGv = false;
			for (int tv : t) {
				if (tv == gv) {
					foundGv = true;
					break;
				}
			}
			if (!foundGv) {
				missing[0] = missingIndex1;
				missing[1] = gv;
			}
		}
		return missing;
	}

	/**
	 * 
	 * O(V) where V is number of vertices
	 * @param adjListT 
	 * 
	 * @param v
	 */
	private static void DFS(List<List<Integer>> adjListT, int v) {
		visited[v] = true;
		for (int u : adjListT.get(v)) {
			if (!visited[u]) {
				depth[u] = depth[v] + 1;
				parent[u] = v;
				DFS(adjListT, u);
			}
		}
	}
	
	/**
	 * 
	 * O(h) where h is height of the tree, however can be O(V) worst case where V is number of vertices
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	private static Integer[] findLCA(int u, int v) {
		int[] cyclePath = new int[depth[u] + depth[v] + 2];
		int path1index = 0;
		int path2Index = depth[u] + depth[v] +1;
		cyclePath[path1index] = v;
		cyclePath[++path1index] = u;
		cyclePath[path2Index] = v;
		while (depth[u] != depth[v]) {
			if (depth[u] > depth[v]) {
				u = parent[u];
				cyclePath[++path1index] = u;
			} else {
				v = parent[v];
				cyclePath[--path2Index] = v;
			}
		}
		while (u != v) {
			u = parent[u];
			v = parent[v];
			// is it odd
			if (cyclePath.length % 2 == 0) {
				cyclePath[++path1index] = u;
			} else {
				cyclePath[++path1index] = u;
				cyclePath[--path2Index] = v;
			}
		}
		List<Integer> path = new ArrayList<>();
		for (int i : cyclePath) {
			if (i != 0) {
				path.add(i);
			}
		}
		
		return path.toArray(new Integer[path.size()]);
	}
	
    public static List<List<Integer>> BFS(List<List<Integer>> adjListG, int src, int nbrVertices) {
    	
    	List<List<Integer>> adjListT = new ArrayList<>();
    	for (int i = 1; i <= nbrVertices + 1; i++) {
    		adjListT.add(new ArrayList<>());
    	}
    	
    	boolean foundCycle = false;
        // stores vertex is discovered or not
        Node[] discoveredNodes = new Node[nbrVertices + 1];
 
        // create a queue used to do BFS and
        // push source vertex into the queue
        Queue<Node> q = new ArrayDeque<>();
        Node n = new Node(src, null);
        q.add(n);
        // mark source vertex as discovered
        discoveredNodes[src] = n;
 
    	Node discoveredNode = null;
    	Node cycleNode = null;

        // loop till queue is empty
        while (!q.isEmpty() && !foundCycle) {
            // pop front node from queue and print it
            Node node = q.poll();
 
            // do for every edge (v -> u)
            for (int u : adjListG.get(node.v)) {
                if (discoveredNodes[u] == null) {
                	if (adjListT.size() < node.v || adjListT.get(node.v) == null) {
                		adjListT.add(node.v, new ArrayList<>());
                	}
                	adjListT.get(node.v).add(u);
                	Node newNode = new Node(u, node);
                    // mark it discovered
                	discoveredNodes[u] = newNode;
 
                    // construct the queue node containing info
                    // about vertex and push it into the queue
                    q.add(newNode);
                }
 
                // u is discovered and u is not a parent
//                else if (u != node.parent.v) {
//                	discoveredNode = discoveredNodes[u];
//                	cycleNode = new Node(u, node);
//                	foundCycle = true;
//                	break;
//                }
            }
        }
        
        if (foundCycle) {
            // we found a cross-edge ie. cycle is found
        	Node nextNode = discoveredNode.parent;
        	boolean[] path = new boolean[nbrVertices];
        	while (nextNode != null) {
        		int i = nextNode.v;
        		path[i] = true;
        		nextNode = nextNode.parent;
        	}
        	int commonParent = -1;
        	nextNode = cycleNode.parent;
        	while (nextNode != null) {
        		int i = nextNode.v;
        		if (path[i] == true) {
        			// found common parent
        			commonParent = i;
        			break;
        		}
        		nextNode = nextNode.parent;
        	}
        	List<Integer> path1 = new ArrayList<Integer>();
        	List<Integer> path2 = new ArrayList<Integer>();

        	nextNode = discoveredNode;
        	while (nextNode != null) {
    			path1.add(nextNode.v);
        		if (nextNode.v == commonParent) {
        			break;
        		}
        		nextNode = nextNode.parent;
        	}
        	
        	nextNode = cycleNode;
        	while (nextNode != null) {
    			path2.add(nextNode.v);
        		if (nextNode.v == commonParent) {
        			break;
        		}
        		nextNode = nextNode.parent;
        	}

//        	System.out.println(path1);
//        	System.out.println(path2);
        	return adjListT;
        }

        // No cross-edges found in the graph
        return adjListT;
    }
}
