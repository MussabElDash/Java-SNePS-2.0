package snip;

import java.util.ArrayDeque;
import java.util.Queue;

import sneps.Nodes.Node;

public class Runner {
	
	private static Queue<Node> highQueue;
	private static Queue<Node> lowQueue;
	
	public static void initiate() {
		highQueue = new ArrayDeque<Node>();
		lowQueue = new ArrayDeque<Node>();
	}

	public static void run() {
		while(!highQueue.isEmpty() || !lowQueue.isEmpty()) {
			while(!highQueue.isEmpty()) {
				Node toRunNext = highQueue.poll();
				toRunNext.processReports();
			}
			
			while(!lowQueue.isEmpty()) {
				Node toRunNext = lowQueue.poll();
				toRunNext.processRequests();
			}
		}
	}
	
	public static void addToHighQueue(Node node) {
		highQueue.add(node);
	}
	
	public static void addToLowQueue(Node node) {
		lowQueue.add(node);
	}

}
