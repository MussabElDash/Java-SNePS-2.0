/**
 * @className AndPath.java
 * 
 * @ClassDescription An and-path exists between two nodes A and B, 
 * 	if all the paths included in this and-path are paths from A to B. 
 * 	This class extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;
import java.util.ListIterator;

import SNeBR.Context;
import sneps.Node;
import sneps.PathTrace;

public class AndPath extends Path {
	
	/**
	 * A LinkedList of Paths included in this and-path. 
	 * The paths included in the and-path can be of any type.
	 */
	private LinkedList<Path> paths;	
	
	/**
	 * The first constructor of this class.
	 * 
	 * @param paths
	 * 			the list of paths included in the and-path.
	 */
	public AndPath(LinkedList<Path> paths){
		LinkedList<Path> pList = new LinkedList<Path>();
		pList.addAll(paths);
		for(int i = 0; i < pList.size()-1; i++){
			Path p = pList.get(i);
			for(int j = i+1; j < pList.size(); j++){
				if(pList.get(j).equals(p)){
					pList.remove(j);
					j--;
				}
			}
		}
		this.paths = pList;
	}
	
	/**
	 * The second constructor of this class.
	 */
	public AndPath(Path... paths){
		LinkedList<Path> pList = new LinkedList<Path>(java.util.Arrays.asList(paths));
		this.paths = pList;
	}
	
	/**
	 * 
	 * @return the list of paths included in the current and-path.
	 */
	public LinkedList<Path> getPaths(){
		return this.paths;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		if (this.paths.isEmpty())
			return new LinkedList<Object[]>();
		LinkedList<Path> pList = new LinkedList<Path>();
		pList.addAll(this.paths);
		Path p = pList.removeFirst();
		AndPath andPath = new AndPath(pList);
		if(pList.size() > 0)
			return intersectionAnd(p.follow(node, trace, context), andPath.follow(node, trace, context));
		else
			return p.follow(node, trace, context);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		if(this.paths.isEmpty())
			return new LinkedList<Object[]>();
		LinkedList<Path> pList = new LinkedList<Path>();
		pList.addAll(this.paths);
		Path p = pList.removeFirst();
		AndPath andPath = new AndPath(pList);
		if(pList.size() > 0)
			return intersectionAnd(p.followConverse(node, trace, context), andPath.followConverse(node, trace, context));
		else 
			return p.followConverse(node, trace, context);
	}
	
	 /**
     * This method returns the intersection between the two Lists of pairs.
     *
     * @param list1 
     * 			a LinkedList of Node-PathTrace pairs
     * @param list2 
     * 			a LinkedList of Node-PathTrace pairs
     * 
     * @return a LinkedList of Node-PathTrace pairs resulted from getting the intersection
     * between the two given lists
     */
	private LinkedList<Object[]> intersectionAnd(LinkedList<Object[]> l1, LinkedList<Object[]> l2){
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		for (int i = 0; i < l1.size(); i++){
			Object[] ob1 = l1.get(i);
			Node n1 = (Node) ob1[0];
			PathTrace pt1 = (PathTrace) ob1[1];
			for (int j = 0; j < l2.size(); j++){
				Object[] ob2 = l2.get(j);
				Node n2 = (Node) ob2[0];
				PathTrace pt2 = (PathTrace) ob2[1];
				if(n1.equals(n2)){
					PathTrace pt = pt1.clone();
					pt.and(pt2.getPath());
					pt.addAllSupports(pt2.getSupports());
					Object[] o = new Object[2];
					o[0] = n1;
					o[1] = pt;
					result.add(o);
				}
			}
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public AndPath clone() {
		LinkedList<Path> pList = new LinkedList<Path>();
		for (int i = 0; i < this.paths.size(); i++){
			pList.add(this.paths.get(i).clone());
		}
		return new AndPath(pList);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().getSimpleName().equals("AndPath"))
			return false;
		AndPath andPath = (AndPath) obj;
		if (this.paths.size() != andPath.getPaths().size())
			return false;
		for (int i = 0; i < this.paths.size(); i++){
			Path p = this.paths.get(i);
			boolean found = false;
			for (int j = 0; j < andPath.getPaths().size(); j++){
				Path p2 = andPath.getPaths().get(j);
				if (p.equals(p2))
					found = true;
			}
			if (! found)
				return false;
		}
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from the
	 * Object class.
	 */
	@Override
	public String toString(){
		String s = "";
        s += "and(";
        for(int i=0;i<this.paths.size();i++)
        {
                s += this.paths.get(i).toString();
                if(i<this.paths.size()-1)
                        s += " ";
        }
        s += ")";
        return s;

	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		LinkedList<Path> result = new LinkedList<Path>();
        ListIterator<Path> i = paths.listIterator(0);
        while(i.hasNext())
                result.add(i.next().converse());
        return new AndPath(result);
	}

}
