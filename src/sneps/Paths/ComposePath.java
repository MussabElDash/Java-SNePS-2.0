/**
 * @className ComposePath.java
 * 
 * @ClassDescription A composed path exists between two nodes A and B, 
 * 	if there is a series of paths going from A to B. This class 
 * 	extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.Iterator;
import java.util.LinkedList;

import SNeBR.Context;
import sneps.Node;
import sneps.PathTrace;

public class ComposePath extends Path{

	/**
	 * a LinkedList of paths included in this composed path. 
	 * The paths included in the composed path can be of any type.
	 */
	private LinkedList<Path> paths;
	
	/**
	 * The first constructor of this class.
	 */
	public ComposePath(LinkedList<Path> paths){
		this.paths = paths;
	}
	
	/**
	 * The second constructor of this class.
	 */
	public ComposePath(Path...paths){
		this.paths = new LinkedList<Path>(java.util.Arrays.asList(paths));
	}
	
	/**
	 * The third constructor of this class.
	 */
	public ComposePath(LinkedList<Path> list, Path...paths){
		this.paths = new LinkedList<Path>();
		this.paths.addAll(list);
		this.paths.addAll(java.util.Arrays.asList(paths));
	}
	
	/**
	 * 
	 * @return the list of paths included in the current 
	 * 	composed path.
	 */
	public LinkedList<Path> getPaths(){
		return this.paths;
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		if(!this.paths.isEmpty()){
			LinkedList<Path> pList = new LinkedList<Path>();
			pList.addAll(this.paths);
			Path p = pList.removeFirst();
			ComposePath cPath = new ComposePath(pList);
			LinkedList<Object[]> temp = p.follow(node, trace, context);
			return follow(temp, context, cPath);
		}
		return new LinkedList<Object[]>();	
	}
	
	/**
	 * A helper follow method that is invoked from the original
	 * follow method. IT follows the given path starting at all 
	 * nodes in the given list of pairs.
	 * 
	 * @param temp
	 * 			a LinkedList of Node-PathTrace pairs.
	 * @param context
	 * 			the context that the propositions along this path 
	 * 			are asserted in.
	 * @param path
	 * 			the path that will be followed starting at nodes in 
	 * 			the list of pairs.
	 * 
	 * @return a LinkedList of Node-PathTrace pairs resulted from following the given
     * 	path starting at all nodes in the given list of pairs.
	 */
	private LinkedList<Object[]> follow(LinkedList<Object[]> temp, Context context, ComposePath path){
		if(path.getPaths().isEmpty())
			return temp;
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		for(int i = 0; i < temp.size(); i++){
			Object[] o = temp.get(i);
			Node node = (Node) o[0];
			PathTrace pt = (PathTrace) o[1];
			LinkedList<Object[]> fList = path.follow(node, pt, context);
			result.addAll(fList);
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		if(!this.paths.isEmpty()){
			LinkedList<Path> pList = new LinkedList<Path>();
			pList.addAll(this.paths);
			Path p = pList.removeLast();
			ComposePath cPath = new ComposePath(pList);
			LinkedList<Object[]> temp = p.followConverse(node, trace, context);
			return followConverse(temp, context, cPath);
		}
		return new LinkedList<Object[]>();
	}
	
	/**
	 * A helper follow method that is invoked from the original
	 * follow method. It follows the converse of the given path 
	 * starting at all nodes in the given list of pairs
	 * 
	 * @param temp
	 * 			a LinkedList of Node-PathTrace pairs.
	 * @param context
	 * 			the context that the propositions along this path 
	 * 			are asserted in.
	 * @param path
	 * 			the path that its converse will be followed starting 
	 * 			at nodes in the list of pairs.
	 * 
	 * @return a LinkedList of Node-PathTrace pairs resulted from following the converse
     * 	of the given path starting at all nodes in the given list of pairs.
	 */
	private LinkedList<Object[]> followConverse(LinkedList<Object[]> temp, Context context, ComposePath path){
		if(path.getPaths().isEmpty())
			return temp;
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		for (int i = 0; i < temp.size(); i++){
			Object[] o = temp.get(i);
			Node node = (Node) o[0];
			PathTrace pt = (PathTrace) o[1];
			LinkedList<Object[]> fList = path.followConverse(node, pt, context);
			result.addAll(fList);
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public ComposePath clone() {
		LinkedList<Path> paths = new LinkedList<Path>();
		for (int i = 0; i < this.paths.size(); i++){
			paths.add(this.paths.get(i).clone());
		}
		return new ComposePath(paths);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ComposePath))
			return false;
		ComposePath cPath = (ComposePath) obj;
		if (cPath.getPaths().size() != this.paths.size())
			return false;
		for (int i = 0; i < this.paths.size(); i++){
			if (!this.paths.get(i).equals(cPath.getPaths().get(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from
	 * the Object class.
	 */
	@Override
	public String toString()
	{
		String s = "compose(";
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
		LinkedList<Path> temp = new LinkedList<Path>();
		Iterator<Path> i = this.paths.descendingIterator();
		while(i.hasNext())
			temp.add(i.next().converse());
		return new ComposePath(temp);
	}

}
