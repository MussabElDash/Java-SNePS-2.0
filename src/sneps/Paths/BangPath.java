/**
 * @className BangPath.java
 * 
 * @ClassDescription This class is used to create a special instance of 
 * 	Path that requires the proposition to be asserted in the given context, 
 * 	i.e. if a segment of a path requires all propositions in it to be asserted
 *	then this segment is added as a BangPath. This class extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import SNeBR.Context;
import sneps.Node;
import sneps.PathTrace;

public class BangPath extends Path{

	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		// check it's proposition and it's asserted
		if ((node.getSemantic().getSuperClassesNames().contains("Proposition") ||
				node.getSemantic().getClass().getSimpleName().equals("Proposition")) &&
					context.getHypothesisSet().propositions.contains(node.getSemantic())
		)
		{
			PathTrace pt = trace.clone();
			pt.addSupport(node);
			// add the pair to the result
			Object[] o = new Object[2];
			o[0] = node;
			o[1] = pt;
			result.add(o);
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		if ((node.getSemantic().getSuperClassesNames().contains("Proposition") ||
				node.getSemantic().getClass().getSimpleName().equals("Proposition")) &&
					context.getHypothesisSet().propositions.contains(node.getSemantic())
		)
		{
			PathTrace pt = trace.clone();
			pt.addSupport(node);
			// add the pair to the result
			Object[] o = new Object[2];
			o[0] = node;
			o[1] = pt;
			result.add(o);
		}
		return result;
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public BangPath clone() {
		return new BangPath();
	}
	
	/** 
     * This method overrides the toString method inherited from the 
     * Object class.
     */
	@Override
	public String toString(){
		return "!";
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		return obj.getClass().getSimpleName().equals("BangPath");
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return this;
	}

}
