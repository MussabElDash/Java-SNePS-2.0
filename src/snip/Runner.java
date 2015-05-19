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

	public static String run() {
		String sequence = "";
		while(!highQueue.isEmpty() || !lowQueue.isEmpty()) {
			while(!highQueue.isEmpty()) {
				System.out.println("\n\n");
				System.out.println(" AT HIGH QUEUE ");
				Node toRunNext = highQueue.poll();
				System.out.println(toRunNext);
				System.out.println("\n\n");
				toRunNext.processReports();
				sequence += 'H';
			}
			while(!lowQueue.isEmpty()) {
				System.out.println("in");
				Node toRunNext = lowQueue.poll();
				toRunNext.processRequests();
				sequence += 'L';
				if(!highQueue.isEmpty())
					break;
			}
		}
		return sequence;
	}
	
	public static void addToHighQueue(Node node) {
		highQueue.add(node);
	}
	
	public static void addToLowQueue(Node node) {
		lowQueue.add(node);
	}

}
