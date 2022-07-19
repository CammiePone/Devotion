package dev.cammiescorner.devotion.api;

import java.util.*;

/**
 * @author UpcraftLP
 */
public class Graph<T> {
	public List<Node<T>> nodes = new ArrayList<>();
	public Map<Node<T>, List<Edge<T>>> adjacent = new HashMap<>();

	public void addNode(Node<T> node) {
		nodes.add(node);
		node.graph = this;
	}

	public void addEdge(Node<T> from, Node<T> to) {
		if (adjacent.containsKey(from) && adjacent.get(from).stream().anyMatch(e -> e.nodes.contains(to))) {
			return;
		}
		var edge = new Edge<T>(from, to);
		adjacent.computeIfAbsent(from, k -> new ArrayList<>()).add(edge);
		adjacent.computeIfAbsent(to, k -> new ArrayList<>()).add(edge);
	}

	public static class Node<T> {
		public final T obj;
		private Graph<T> graph;

		public Node(T obj) {
			this.obj = obj;
		}

		public List<Edge<T>> getConnections() {
			return graph != null ? graph.adjacent.get(this) : List.of();
		}
	}

	public static class Edge<T> {
		public final Set<Node<T>> nodes;

		public Edge(Node<T> from, Node<T> to) {
			this.nodes = Set.of(from, to);
		}
	}
}
