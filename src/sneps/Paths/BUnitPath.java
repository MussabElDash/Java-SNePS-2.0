/**
 * @className BUnitPath.java
 * 
 * @ClassDescription A backward unit path exists between two nodes A 
 * 	and B, if there is a single arc going from B to A labeled with 
 * 	the relation specified in the backward unit path. This class extends
 * 	Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import SNeBR.Context;
import sneps.PathTrace;
import sneps.Relation;
import sneps.Cables.UpCable;
import sneps.Cables.UpCableSet;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;

public class BUnitPath extends Path{

	/**
	 * The relation that labels the arc of this backward unit path
	 */
	private Relation relation;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param relation
	 * 			the relation that labels the arc of the
	 * 			backward unit path.
	 */
	public BUnitPath(Relation relation){
		this.relation = relation;
	}
	
	/**
	 * 
	 * @return the relation of the current backward unit path
	 */
	public Relation getRelation(){
		return this.relation;
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) {
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		UpCableSet upSet = node.getUpCableSet();
		UpCable upCable = upSet.getUpCable(this.relation.getName());
		if(upCable != null){
			NodeSet ns = upCable.getNodeSet();
			for(int i = 0; i < ns.size(); i++){
				Node n = ns.getNode(i);
				PathTrace t = trace.clone();
				t.compose(new BUnitPath(this.relation));
				Object[] o = new Object[2];
                o[0] = n;
                o[1] = t;
                result.add(o);		
			}
		}
		return result;
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		return new FUnitPath(this.relation).follow(node, trace, context);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public BUnitPath clone() {
		return new BUnitPath(this.relation);
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if(! obj.getClass().getSimpleName().equals("BUnitPath"))
			return false;
		if(! ((BUnitPath)obj).getRelation().equals(this.relation))
			return false;
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from
	 * the Object class.
	 */
	@Override
	public String toString(){
		return this.relation.toString() + "-";
	}

	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Nodes.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return new FUnitPath(this.relation);
	}

	@Override
	public LinkedList<Relation> firstRelations() {
	LinkedList<Relation> r= new LinkedList<Relation>();
	r.add(relation);
	return r;
		
	}

}
