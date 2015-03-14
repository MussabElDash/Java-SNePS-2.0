/**
 * @className CFResFUnitPath.java
 * 
 * @ClassDescription A case frame restricted forward unit path is 
 * 	the same as the forward unit path except that the path is 
 * 	restricted to a certain case frame. Thus, this path exists between 
 * 	two nodes A and B, if there an arc going from node A, whose down 
 * 	cable set implements the restricting case frame, to node B and the 
 * 	arc is labeled with the relation specified in the case frame restricted 
 * 	forward unit path. This class extends Path.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps.Paths;

import java.util.LinkedList;

import sneps.CaseFrame;
import SNeBR.Context;
import sneps.DownCable;
import sneps.DownCableSet;
import sneps.MolecularNode;
import sneps.Node;
import sneps.NodeSet;
import sneps.PathTrace;
import sneps.Relation;

public class CFResFUnitPath extends Path{
	
	/**
	 * The relation that labels the arc of this case frame restricted
	 * forward unit path.
	 */
	private Relation relation;
	
	/**
	 * The case frame that this case frame restricted forward unit 
	 * path is restricted to.
	 */
	private CaseFrame caseFrame;
	
	/**
	 * The constructor of this class.
	 * 
	 * @param relation
	 * 			the relation that labels the arc of this path.
	 * @param cf
	 * 			the restricting case frame.
	 */
	public CFResFUnitPath(Relation relation, CaseFrame cf){
		this.relation = relation;
		this.caseFrame = cf;
	}
	
	/**
	 * 
	 * @return the relation of the current path.
	 */
	public Relation getRelation(){
		return this.relation;
	}
	
	/**
	 * 
	 * @return the case frame that the current path is restricted
	 * 	to.
	 */
	public CaseFrame getCaseFrame(){
		return this.caseFrame;
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#follow(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> follow(Node node, PathTrace trace, Context context) 
	{	
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		if (node.getSyntacticSuperClass().equals("Molecular")){
			 MolecularNode mNode = (MolecularNode) node;
			 DownCableSet dSet = mNode.getDownCableSet();
			 // Check the restriction of the CaseFrame
			 if (dSet.getCaseFrame().getId().equals(this.caseFrame.getId())){
				 DownCable dCable = dSet.getDownCable(this.relation.getName());
				 if (dCable != null){
					 NodeSet ns = dCable.getNodeSet();
					 for (int i = 0; i < ns.size(); i++){
						 Node n = ns.getNode(i);
						 PathTrace t = trace.clone();
						 t.compose(new CFResFUnitPath(this.relation, this.caseFrame));
						 Object[] o = new Object[2];
	                     o[0] = n;
	                     o[1] = t;
	                     result.add(o);
					 }
				 }			
			 }
		}
		return result;	
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#followConverse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public LinkedList<Object[]> followConverse(Node node, PathTrace trace, Context context) {
		return new CFResBUnitPath(this.relation, this.caseFrame).follow(node, trace, context);
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#clone(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public CFResFUnitPath clone() {
		return new CFResFUnitPath(this.relation, this.caseFrame);
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#equals(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().getSimpleName().equals("CFResFUnitPath"))
			return false;
		if(!((CFResFUnitPath)obj).getRelation().equals(this.relation))
			return false;
		if(!((CFResFUnitPath)obj).getCaseFrame().getId().equals(this.caseFrame.getId()))
			return false;
		return true;
	}
	
	/**
	 * This method overrides the toString method inherited from the
	 * Object class.
	 */
	@Override
	public String toString(){
		return "CaseFrame-restricted (" + this.relation.toString() + ")";
	}
	
	/** (non-Javadoc)
     * @see sneps.Paths.Path#converse(sneps.Node, sneps.PathTrace, SNeBR.Context)
     */
	@Override
	public Path converse() {
		return new CFResBUnitPath(this.relation, this.caseFrame);
	}

}
