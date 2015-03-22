/**
 * @className OrPath.java
 * 
 * @ClassDescription An or-path exists between two nodes A and B, 
 * 	if any of the paths included in this or-path is a path from A 
 * 	to B. This class extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import SNeBR.Context;
import sneps.PathTrace;
import sneps.Nodes.Node;

public class OrPath extends Path {
	
	/**
	 * The list of paths included in the or-path.
	 */
	private LinkedList<Path> paths;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param paths
	 * 			the list of paths included in the or-path.
	 */
	public OrPath(LinkedList<Path> paths){
		LinkedList<Path> pList = new LinkedList<Path>();
		pList.addAll(paths);
		for (int i = 0; i < pList.size()-1; i++){
			Path p = pList.get(i);
			for (int j = i+1; j < pList.size(); j++){
				if(pList.get(j).equals(p)){
					pList.remove(j);
					j--;
				}	
			}
		}
		this.paths = pList;	
	}
	
	/**
	 * 
	 * @return the list of paths included in the current or-path
	 */
	public LinkedList<Path> getPaths(){
		return this.paths;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		if(this.paths.isEmpty())
			return new LinkedList<Object[]>();
		LinkedList<Path> pList = new LinkedList<Path>();
		pList.addAll(this.paths);
		Path p = pList.removeFirst();
		OrPath orPath = new OrPath(pList);
		return UnionOr(p.follow(node, trace, context), orPath.follow(node, trace, context));
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		if(this.paths.isEmpty())
			return new LinkedList<Object[]>();
		LinkedList<Path> pList = new LinkedList<Path>();
		pList.addAll(this.paths);
		Path p = pList.removeFirst();
		OrPath orPath = new OrPath(pList);
		return UnionOr(p.followConverse(node, trace, context), orPath.followConverse(node, trace, context));
	}
	
	/**
     * This method gets the union of two lists of Node-PathTrace pairs.
     *
     * @param list1 
     * 			a LinkedList of Node-PathTrace pairs
     * @param list2 
     * 			a LinkedList of Node-PathTrace pairs
     * 
     * @return a LinkedList of Node-PathTrace pairs resulted from getting the 
     * 	union of the two given lists.
     */
	private LinkedList<Object[]> UnionOr(LinkedList<Object[]> l1, LinkedList<Object[]> l2){
		LinkedList<Object[]> unionList = new LinkedList<Object[]>();
		unionList.addAll(l1);
		unionList.addAll(l2);
		return unionList;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public OrPath clone() {
		LinkedList<Path> pList = new LinkedList<Path>();
		for (int i = 0; i < this.paths.size(); i++){
			pList.add(this.paths.get(i).clone());
		}
		return new OrPath(pList);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().getSimpleName().equals("OrPath"))
			return false;
		OrPath orPath = (OrPath) obj;
		if(this.paths.size() != orPath.getPaths().size())
			return false;
		for(int i = 0; i < this.paths.size(); i++){
			Path p = this.paths.get(i);
			boolean found = false;
			for(int j = 0; j < orPath.getPaths().size(); j++){
				Path p2 = orPath.getPaths().get(j);
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
		String s = "or(";
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
     * @see sneps.Paths.Path#converse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		LinkedList<Path> result = new LinkedList<Path>();
		for (Path p : this.paths)
			result.add(p.converse());
		return new OrPath(result);
	}

}
