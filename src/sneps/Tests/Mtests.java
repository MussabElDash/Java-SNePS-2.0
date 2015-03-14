package sneps.Tests;
//package sneps;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Hashtable;
//import java.util.LinkedList;
//
//import sneps.Paths.FUnitPath;
//import sneps.Paths.Path;
//import sneps.SemanticClasses.Entity;
//import SNeBR.Context;
//
//public class Mtests {
//
//	private static Hashtable<String, Node> nodes = new Hashtable<String, Node>();
//	
//	private static ArrayList<Node> nodesIndex = new ArrayList<Node>();
//	
//	private static Hashtable<String, NodeSet> molecularNodes = new Hashtable<String, NodeSet>();
//	
//	private static Hashtable<String, CaseFrame> caseFrames = new Hashtable<String, CaseFrame>();
//	
////	// array of rel-node pairs
////	public static boolean downCableSetExists(Object[][] array){
////		Object[][] relNodeSet = turnIntoRelNodeSet(array);
////		Object[][] temp = new Object[relNodeSet.length][relNodeSet[0].length];
////		for(int i = 0; i < relNodeSet.length; i++){
////			temp[i][0] = new FUnitPath((Relation) relNodeSet[i][0]);
////			temp[i][1] = (NodeSet) relNodeSet[i][1];
////		}
////		System.out.println(temp.length);
////		LinkedList<Object[]> ns = find(temp, new Context());
////		System.out.println(ns.size());
////		for(int j = 0; j < ns.size(); j++){
////			Object[] x = ns.get(j);
////			MolecularNode n = (MolecularNode) x[0];
////			// I don't understand what does this for loop do?????????
////			for (int i = 0; i < array.length; i++) {
////				if (array[i][1].getClass().getSimpleName().equals("NodeSet")) {
////					if (n.getDownCableSet().contains(
////					((Relation) array[i][0]).getName()) && n.getDownCableSet().getDownCable(((Relation) array[i][0])
////							.getName()).getNodeSet().isEmpty()){
////						continue;
////					} else {
////						ns.remove(j);
////						j--;
////						break;
////					}
////				}
////			}	
////		}
////		for (int i = 0; i < ns.size(); i++) {
////			Object[] x = ns.get(i);
////			MolecularNode n = (MolecularNode) x[0];
////			int c = 0;
////			for (int j = 0; j < n.getDownCableSet().size(); j++) {
////				Cable cb = n.getDownCableSet().getDownCable(j);
////				if (cb.getNodeSet().isEmpty())
////					c++;
////				else
////					c += cb.getNodeSet().size();
////			}
////			if (c != array.length) {
////				ns.remove(i);
////				i--;
////			}
////		}
////
////		System.out.println(((Node)ns.get(0)[0]).getIdentifier());
////		System.out.println(((Node)ns.get(1)[0]).getIdentifier());
////		return ns.size() == 1;
////	}
//	
////	private static boolean downCableSetExists(Object[][] array) {
////		int size = 0;
////		for (int i = 0; i < array.length; i++) {
////			if (!array[i][1].getClass().getSimpleName().equals("NodeSet"))
////				size++;
////		}
////		Object[][] temp = new Object[size][2];
////		int counter = 0;
////		for (int i = 0; i < array.length; i++) {
////			if (array[i][1].getClass().getSimpleName().equals("NodeSet"))
////				continue;
////			temp[counter][0] = new FUnitPath((Relation) array[i][0]);
////			NodeSet ns1 = new NodeSet();
////			ns1.addNode((Node) array[i][1]);
////			temp[counter][1] = ns1;
////			counter++;
////		}
////		LinkedList<Object[]> ns = find(temp, new Context());
////
////		for (int j = 0; j < ns.size(); j++) {
////			Object[] x = ns.get(j);
////			MolecularNode n = (MolecularNode) x[0];
////			for (int i = 0; i < array.length; i++) {
////				if (array[i][1].getClass().getSimpleName().equals("NodeSet")) {
////					if (n.getDownCableSet().contains(
////							((Relation) array[i][0]).getName())
////							&& n.getDownCableSet()
////							.getDownCable(
////									((Relation) array[i][0]).getName())
////									.getNodeSet().isEmpty())
////						continue;
////					else {
////						ns.remove(j);
////						j--;
////						break;
////					}
////				}
////			}
////		}
////		for (int i = 0; i < ns.size(); i++) {
////			Object[] x = ns.get(i);
////			MolecularNode n = (MolecularNode) x[0];
////			int c = 0;
////			for (int j = 0; j < n.getDownCableSet().size(); j++) {
////				Cable cb = n.getDownCableSet().getDownCable(j);
////				if (cb.getNodeSet().isEmpty())
////					c++;
////				else
////					c += cb.getNodeSet().size();
////			}
////			if (c != array.length) {
////				ns.remove(i);
////				i--;
////			}
////		}
////
////		return ns.size() == 1;
////	}
//	
//	/**
//	* @param array
//	* the array that contains pairs of paths and node sets
//	* @return the node set of nodes that we can start following those paths in
//	* the array from, in order to reach at least one node at each node
//	* set in all entries of the array
//	*/
//	public static LinkedList<Object[]> find(Object[][] array, Context context) {
//		return findIntersection(array, context, 0);
//	}
//	
//	
//	/**
//	* @param array
//	* the array that contains pairs of paths and node sets
//	* @param index
//	* the index of the array at which we should start traversing it
//	* @return the node set of nodes that we can start following those paths in
//	* the array from, in order to reach at least one node of node sets
//	* at each path-nodeset pair.
//	*/
//	private static LinkedList<Object[]> findIntersection(Object[][] array,
//	Context context, int index) {
//	LinkedList<Object[]> result = new LinkedList<Object[]>();
//	if (index == array.length){
//		System.out.println("Yes");
//		return result;
//	}
//	Path path = (Path) array[index][0];
//	NodeSet nodeSet = (NodeSet) array[index][1];
//
//	if (index < array.length - 1) {
//		LinkedList<Object[]> list1 = findUnion(path, nodeSet, context);
//		System.out.println(list1.size());
//		LinkedList<Object[]> list2 = findIntersection(array, context,
//				++index);
//		System.out.println(list2.size());
//		for (int i = 0; i < list1.size(); i++) {
//			Object[] ob1 = list1.get(i);
//			Node n1 = (Node) ob1[0];
//			PathTrace pt1 = (PathTrace) ob1[1];
//			for (int j = 0; j < list2.size(); j++) {
//				Object[] ob2 = list2.get(j);
//				Node n2 = (Node) ob2[0];
//				PathTrace pt2 = (PathTrace) ob2[1];
//				if (n1.equals(n2)) {
//					PathTrace pt = pt1.clone();
//					pt.and(pt2.getPath());
//					pt.addAllSupports(pt2.getSupports());
//					Object[] o = { n1, pt };
//					result.add(o);
//				}
//			}
//		}
//	} else {
//		result.addAll(findUnion(path, nodeSet, context));
//	}
//
//	return result;
//	}
//	
//	/**
//	* @param path
//	* the path that can be followed to get to one of the nodes
//	* specified
//	* @param nodeSet
//	* the nodes that can be reached by following the path
//	* @return a node set of nodes that we can start following the path from in
//	* order to get to one of the nodes in the specified node set
//	*/
//	private static LinkedList<Object[]> findUnion(Path path, NodeSet nodeSet, Context context) {
//		LinkedList<Object[]> result = new LinkedList<Object[]>();
//		System.out.println(path.toString());
//		for (int i = 0; i < nodeSet.size(); i++) {
//			System.out.println("Node name: " + ((Node)nodeSet.getNode(i)).getIdentifier());
//			LinkedList<Object[]> temp = path.followConverse(nodeSet.getNode(i),
//					new PathTrace(), context);
//			System.out.println("findUnion " + temp.size());
//			result.addAll(temp);
//		}
//	
//		return result;
//	}
//	
//	// turnIntoRelNodeSet should not change the array itself but creates copies of it .. IMPORTANT
//	private static Object[][] turnIntoRelNodeSet(Object[][] array){
//		Object[][] temp = new Object[array.length][];
//		for (int i = 0; i < array.length; i++) {
//			  temp[i] = Arrays.copyOf(array[i], array[i].length);
//		}
//		Object[][] temp2 = new Object[array.length][];
//		for (int i = 0; i < array.length; i++) {
//			  temp2[i] = Arrays.copyOf(array[i], array[i].length);
//		}
//		int relcounter = 0;
//		for(int i = 0; i < temp.length; i++){
//			if(temp[i][0] != null){
//				Relation r = (Relation) temp[i][0];
//				relcounter++;
//				if (i+1 < temp.length){
//					for(int j = i+1; j < temp.length; j++){
//						if(temp[j][0] != null){
//							if(((Relation)temp[j][0]).equals(r)){
//								temp[j][0]= null;
//							}
//						}
//					}
//				}
//			}
//		}
//		int addcounter = 0;
//		Object[][] result = new Object[relcounter][2];
//		for(int i = 0; i < temp2.length; i++){
//			if(temp2[i][0] != null){
//				Relation r = (Relation) temp2[i][0];
//				NodeSet ns = new NodeSet();
//				ns.addNode((Node)temp2[i][1]);
//				if (i+1 < temp2.length){
//					for(int j = i+1; j < temp2.length; j++){
//						if(temp2[j][0] != null){
//							if(((Relation)temp2[j][0]).equals(r)){
//								ns.addNode((Node)temp2[j][1]);
//								temp2[j][0]= null;
//							}
//						}
//					}
//				}
//				result[addcounter][0] = r;
//				result[addcounter][1] = ns;
//				addcounter++;
//			}
//		}
//		return result;
//	}
//	
//	public static MolecularNode getMolecularNode(Object[][] array) {
//		if (!downCableSetExists(array))
//			return (null);
//		else {
//			int counter = 0;
//			NodeSet intersection = new NodeSet();
//			while (true) {
//				if (array[counter][1].getClass().getSimpleName()
//						.equals("NodeSet"))
//					counter++;
//				else {
//					String r = ((Relation) array[counter][0]).getName();
//					NodeSet ns = ((Node) array[counter][1]).getUpCableSet()
//							.getUpCable(r).getNodeSet();
//					intersection.addAll(ns);
//					break;
//				}
//			}	
//			for (int i = counter; i < array.length; i++) {
//				if (array[i][1].getClass().getSimpleName().equals("NodeSet"))
//					continue;
//				else {
//					Relation r1 = (Relation) array[i][0];
//					NodeSet ns1 = ((Node) array[i][1]).getUpCableSet()
//							.getUpCable(r1.getName()).getNodeSet();
//					intersection = intersection.Intersection(ns1);
//				}
//			}
//			return (MolecularNode) intersection.getNode(0);
//		}
//	}
//	
//	public static void main(String[] args) throws Exception{
//		Relation member = new Relation("member", "Entity", "reduce", 1);
//		Relation cl = new Relation("class", "Entity", "none", 1);
//		Relation r = new Relation("reducible", "Entity", "none", 3);
//		VariableNode node1 = new VariableNode("Entity", "x");
//		VariableNode node2 = new VariableNode("Entity", "y");
//		Node node3 = new Node("Base", "Entity", "Dog");
//		NodeSet ns = new NodeSet();
//		ns.addNode(node1);
//		ns.addNode(node2);
//		NodeSet ns1 = new NodeSet();
//		ns1.addNode(node3);
//		DownCable dc = new DownCable(member, ns);
//		DownCable dc1 = new DownCable(cl, ns1);
//		LinkedList<DownCable> dList = new LinkedList<DownCable>();
//		dList.add(dc);
//		dList.add(dc1);
//		RCFP prop = new RCFP(member, "none", 2);
//		RCFP prop2 = new RCFP(cl, "none", 1);
//		LinkedList<RCFP> propList = new LinkedList<RCFP>();
//		propList.add(prop);
//		propList.add(prop2);
//		CaseFrame cf = new CaseFrame("Entity", propList);
//		DownCableSet dcs = new DownCableSet(dList, cf);
//		Object[][] relNode = new Object[3][2];
//		relNode[0][0] = member;
//		relNode[0][1] = node1;
//		relNode[1][0] = member;
//		relNode[1][1] = node2;
//		relNode[2][0] = cl;
//		relNode[2][1] = node3;
////		relNode[3][0] = r;
////		relNode[3][1] = new NodeSet();
//		System.out.println("checking" + dcs.size());
//		Pattern p = new Pattern("M1", dcs);
//		Entity e = new Entity();
//		PatternNode pNode = new PatternNode(p, e);
//		nodes.put(pNode.getIdentifier(), pNode);
//		caseFrames.put(cf.getId(), cf);
//		molecularNodes.put(cf.getId(), new NodeSet());
//		molecularNodes.get(cf.getId()).addNode(pNode);
//		System.out.println("start");
//		System.out.println(downCableSetExists(relNode));
//		System.out.println(getMolecularNode(relNode).getIdentifier());
//	}
//}
