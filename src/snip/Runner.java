package snip;

import java.util.ArrayDeque;
import java.util.Queue;

import sneps.Nodes.Node;

public class Runner {
	
	Queue<Node> highQueue;
	Queue<Node> lowQueue;

	public Runner() {
		highQueue = new ArrayDeque<Node>();
		lowQueue = new ArrayDeque<Node>();
	}
	
	public void run() {
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

}
