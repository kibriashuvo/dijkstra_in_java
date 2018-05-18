import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

//-----------Heap for vertices-----------------
class HeapDijkstra {

	int heapSize;
	Vertex[] dump;
	HashMap<Integer, Integer> map = new HashMap<>();

	public void buildMinHeap(Vertex[] A) {
		dump = A;
		this.heapSize = dump.length - 1;
		for (int i = heapSize / 2; i > 0; i--) {
			minHeapify(dump, i);
		}
		for (int i = 0; i < this.heapSize; i++) {
			map.put(dump[i].id, i);
		}

	}

	public void minHeapify(Vertex[] A, int i) {

		int min;
		int left = 2 * i;
		int right = (2 * i) + 1;

		if (left <= this.heapSize && A[left].distance <= A[i].distance) {
			min = left;
		} else {
			min = i;
		}
		if (right <= this.heapSize && A[right].distance <= A[min].distance) {
			min = right;
		}
		if (min != i) {
			map.put(A[min].id, i);
			map.put(A[i].id, min);
			Vertex temp = A[min];
			A[min] = A[i];
			A[i] = temp;
			minHeapify(A, min);
		}

	}

	public Vertex extractMin() {

		if (this.heapSize < 1)
			return null;
		else {
			Vertex min = dump[1];
			dump[1] = dump[this.heapSize];
			// dump[this.heapSize] = null;
			this.heapSize--;
			minHeapify(dump, 1);
			return min;
		}

	}

	public void decreaseKey(int key, int i) {
		if (key > dump[i].distance)
			return;
		else {
			dump[i].distance = key;
			while (i > 1 && dump[i / 2].distance > dump[i].distance) {
				map.replace(dump[i].id, i / 2);
				map.replace(dump[i / 2].id, i);
				Vertex temp = dump[i];
				dump[i] = dump[i / 2];
				dump[i / 2] = temp;

			}
		}

	}

}




class Vertex {
	public Vertex(int d) {
		this.distance = d;
	}

	public Vertex() {
	}

	int id;
	int distance;

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	Vertex predc;
	LinkedList<Neighbour> adj;

}

class Neighbour extends Vertex {

	int edgeWeight;

	public Neighbour(int label, int w) {

		super.id = label;
		this.edgeWeight = w;

	}
}




class Edge {
	int source;
	int destination;
	int weight;

	public Edge(int s, int d, int w) {
		this.source = s;
		this.destination = d;
		this.weight = w;
	}

}
class Graph {

	Vertex[] vertices;
	Edge[] edges;

	Graph() {
	}

	public void initializeSingleSource(int v, Edge[] e, int source) {

		vertices = new Vertex[v + 1];
		vertices[0] = new Vertex(-1);
		vertices[0].id = -1;
		vertices[0].adj = new LinkedList<>();

		for (int i = 1; i <= v; i++) {

			vertices[i] = new Vertex(Integer.MAX_VALUE);
			vertices[i].id = i - 1;
			vertices[i].predc = null;
			vertices[i].adj = new LinkedList<Neighbour>();
		}

		vertices[source + 1].distance = 0;

		for (Edge i : e) {
			vertices[i.source + 1].adj.add(new Neighbour(i.destination, i.weight));
		}

		dijkstrasShortestPath(this, source);

	}

	public void dijkstrasShortestPath(Graph G, int source) {

		HeapDijkstra h = new HeapDijkstra();
		HashMap<Integer, Integer> mp = new HashMap<>();
		HashMap<Integer, Integer> trace = new HashMap<>();

		for (Vertex v : G.vertices) {
			if (v.id == -1 || v.id == source)
				continue;
			trace.put(v.id, Integer.MAX_VALUE);
		}

		trace.put(source, 0);

		Vertex[] store = G.vertices;
		Vertex[] dis = G.vertices;

		for (Vertex v : dis) {
			mp.put(v.id, v.distance);
		}

		h.buildMinHeap(store);

		LinkedList<Vertex> completed = new LinkedList<Vertex>();

		while (h.heapSize != 0) {

			Vertex u = h.extractMin();

			if (completed.contains(u))
				continue;

			completed.add(u);

			for (Neighbour v : u.adj) {

				if (mp.get(v.id) > mp.get(u.id) + v.edgeWeight) {
					int newDistance = mp.get(u.id) + v.edgeWeight;
					mp.replace(v.id, newDistance);
					trace.replace(v.id, u.id);
					h.decreaseKey(newDistance, h.map.get(v.id));

				}
			}
		}

		int vno = dis.length - 1;

		for (int j = 0; j < vno; j++) {
			if (j == source)
				continue;
			int dest = j;
			LinkedList<Integer> list = new LinkedList<>();
			list.add(dest);
			while (true) {
				list.add(trace.get(dest));
				if (trace.get(dest) == source)
					break;
				dest = trace.get(dest);
			}
			int listSize = list.size();
			System.out.print(source + "->" + j + ": ");
			for (int i = 0; i < listSize; i++) {
				System.out.print(list.removeLast() + " ");
			}
			System.out.println("= " + mp.get(j));

			System.out.println();
		}

	}

}



class Dijkstra {

	
	


	public static void main(String[] args) {

		int v, e, source;
		Scanner in = new Scanner(System.in);
		v = in.nextInt();
		e = in.nextInt();
		// int[][] weight = new int [v+1][v+1];

		Edge[] edges = new Edge[e];
		int s, d, w;

		for (int i = 0; i < e; i++) {
			s = in.nextInt();
			d = in.nextInt();
			w = in.nextInt();

			// weight[s][d]=w;
			Edge p = new Edge(s, d, w);
			edges[i] = p;

		}

		source = in.nextInt();

		in.close();

		Graph g = new Graph();
		g.initializeSingleSource(v, edges, source);

	}

}
