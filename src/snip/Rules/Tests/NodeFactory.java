package snip.Rules.Tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import sneps.Network;
import sneps.Nodes.Node;
import sneps.SemanticClasses.Individual;

public class NodeFactory {

	private ArrayList<ArrayList<String>> names = new ArrayList<ArrayList<String>>();
	private Hashtable<Set<String>, Node> nodes = new Hashtable<Set<String>, Node>();
	private ArrayList<String> types = new ArrayList<String>();

	public NodeFactory() {
		String[] males = new String[] { "Mussab", "Bob", "Mary", "John",
				"Fred", "Bob", "Steve" };
		String[] females = new String[] { "Jane", "Deb", "Ada", "Sue" };
		String[] birds = new String[] { "Tweety", "Road Runner", "Zazu",
				"Woody", "Donald" };
		String[] types = new String[] { "Male", "Female", "Bird" };
		fillArrayList(males, females, birds);
		fillTypeList(types);
	}

	private void fillTypeList(String[] types) {
		for (String s : types)
			this.types.add(s);
	}

	private void fillArrayList(String[]... arrays) {
		for (String[] arr : arrays) {
			ArrayList<String> list = new ArrayList<String>();
			for (String s : arr)
				list.add(s);
			names.add(list);
		}
	}

	public Node getNameBaseNode() {
		int randType = (int) Math.random() * this.types.size();
		ArrayList<String> names = this.names.get(randType);
		int randName = (int) Math.random() * names.size();
		String name = names.get(randName);
		Set<String> set = new HashSet<String>();
		set.add(name);
		return getBaseNode(name);
	}

	public Node getBaseNode(String name) {
		Set<String> set = new HashSet<String>();
		set.add(name);
		Node n = nodes.get(set);
		if (n == null) {
			n = Network.buildBaseNode(name, new Individual());
			nodes.put(set, n);
		}
		return n;
	}

}
