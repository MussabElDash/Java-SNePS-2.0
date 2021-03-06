package sneps.Nodes;

import java.util.Iterator;

import SNeBR.Context;
import SNeBR.Contradiction;
import SNeBR.Support;
import sneps.Cables.DownCable;
import sneps.Cables.UpCable;
import sneps.Cables.UpCableSet;
import sneps.SemanticClasses.Entity;
import SNeBR.PropositionSet;
import SNeBR.SNeBR;
import sneps.SemanticClasses.*;
import sneps.SyntaticClasses.Molecular;
import sneps.SyntaticClasses.Term;

public class PropositionNode extends MolecularNode {

	public PropositionNode(Molecular syn, Proposition sem) {
		super(syn, sem);
		Context c = new Context(this);
		Support s = new Support(c);
		((Proposition) this.getSemantic()).getOrigin().add(s);
		// TODO Auto-generated constructor stub
	}

	// public PropositionNode() {
	// super();
	// }

	public void contradiction(Context context) {
		NodeSet nodeSetNegated = checkIfNegated(this);
		NodeSet nodeSetNegating = checkIfNegating(this);
		PropositionSet propSet = new PropositionSet();
		for (int i = 0; i < nodeSetNegated.size(); i++) {
			Node n = nodeSetNegated.getNode(i);
			Entity e = n.getSemantic();
			if (e.getClass().getSimpleName().equals("Proposition")) {
				// Proposition prop = (Proposition) e;
				PropositionNode p = (PropositionNode) n;
				if (((Proposition) p.getSemantic()).isAsserted(context)) {
					propSet.propositions.add((PropositionNode) n);
				}
			}
		}
		if (nodeSetNegating != null) {
			for (int i = 0; i < nodeSetNegating.size(); i++) {
				Node n = nodeSetNegating.getNode(i);
				Entity e = n.getSemantic();
				if (e.getClass().getSimpleName().equals("Proposition")) {
					PropositionNode p = (PropositionNode) n;
					if (((Proposition) p.getSemantic()).isAsserted(context)) {
						propSet.propositions.add((PropositionNode) n);
					}
				}
			}
		}
		System.out.println(nodeSetNegated.size());
		if (!propSet.propositions.isEmpty()) {
			context.setConflicting(true);
			System.out.println("done!!!!");
			SNeBR.oldPropositionSupport.clear();
			SNeBR.newPropositionSupport = ((Proposition) this.getSemantic())
					.getOrigin();
			for (Iterator<PropositionNode> iterator = propSet.propositions
					.iterator(); iterator.hasNext();) {
				PropositionNode p = iterator.next();
				SNeBR.oldPropositionSupport.addAll(((Proposition) p
						.getSemantic()).getOrigin());
			}
			SNeBR.restrictionsOfRestrictions(SNeBR.oldPropositionSupport,
					SNeBR.newPropositionSupport);
			Contradiction cont = new Contradiction(
					(Proposition) this.getSemantic(), propSet);
			context.addCont(cont);
		}
	}

	/**
	 * this methods returns all the nodes that this proposition is negated by
	 * 
	 * @param Prop
	 *            the proposition i want to check
	 * @return the nodes that negates this proposition
	 */
	private static NodeSet checkIfNegated(PropositionNode prop) {
		Term term = prop.getSyntactic();
		Node temp;
		NodeSet probableNodeSet = new NodeSet();
		NodeSet nodeSetFinal = new NodeSet();
		UpCableSet upcableset = term.getUpCableSet();
		// for (int i = 0; i < upcableset.size(); i++) {
		// UpCable upcable = upcableset.getUpCable(i);
		// NodeSet newNodeSet = upcable.getNodeSet();
		// String newRel = upcable.getRelation().getName();
		// if (newRel.equals("arg")) {
		// probableNodeSet.addAll(newNodeSet);
		// break;
		// }
		// }

		// TODO
		UpCable upcable = upcableset.getUpCable("arg");
		System.out.println(upcable == null);
		if (upcable != null) {
			probableNodeSet.addAll(upcable.getNodeSet());
		}
		for (int i = 0; i < probableNodeSet.size(); i++) {
			temp = probableNodeSet.getNode(i);
			Term t1 = temp.getSyntactic();
			if (!t1.getClass().getSuperclass().getSimpleName()
					.equals("Molecular")) {
				probableNodeSet.removeNode(temp);
			}
			MolecularNode molNode = (MolecularNode) temp;
			if (containsMinMaxZero(molNode)) {
				nodeSetFinal.addNode(probableNodeSet.getNode(i));
			}
		}
		return nodeSetFinal;
	}

	/**
	 * this methods returns all the nodes that this proposition is negating
	 * 
	 * @param Prop
	 *            the proposition i want to check
	 * @return the nodes that are negated by this proposition
	 */
	private static NodeSet checkIfNegating(PropositionNode prop) {
		// Node node = Prop.getNode();
		Term term = prop.getSyntactic();
		if (!(term.getClass().getSuperclass().getSimpleName()
				.equals("Molecular"))) {
			return null;
		} else {
			Molecular molNode = (Molecular) term;
			return containsMinMaxZeroArg(molNode);
		}
	}

	/**
	 * checks if this molecular node has a min-max zero to some nodes , and an
	 * arg relation to other nodes
	 * 
	 * @param mol
	 *            the node i want to check for its children nodes
	 * @return all the nodes that has arg upcable to the molecular node , if the
	 *         molecular node has a min-max zero
	 */
	private static NodeSet containsMinMaxZeroArg(Molecular mol) {
		boolean min = false;
		boolean max = false;
		boolean arg = false;
		NodeSet nodeSet;
		NodeSet nodeSetFinal = new NodeSet();
		DownCable dCable = mol.getDownCableSet().getDownCable("min");
		if (dCable != null) {
			nodeSet = dCable.getNodeSet();
			for (int j = 0; j < dCable.getNodeSet().size(); j++) {
				if (nodeSet.getNode(j).getIdentifier().equals("0")) {
					min = true;
					break;
				}
			}
		}
		DownCable dCable1 = mol.getDownCableSet().getDownCable("max");
		if (dCable1 != null) {
			nodeSet = dCable1.getNodeSet();
			for (int j = 0; j < dCable.getNodeSet().size(); j++) {
				if (nodeSet.getNode(j).getIdentifier().equals("0")) {
					max = true;
					break;
				}

			}
		}
		DownCable dCable2 = mol.getDownCableSet().getDownCable("arg");
		if (dCable1 != null) {
			nodeSet = dCable2.getNodeSet();
			nodeSetFinal = nodeSet;
			arg = true;
		}

		if (min && max && arg)
			return nodeSetFinal;
		return null;
	}

	/**
	 * checks if any of the nodes in the arg upcable of a certain molecular
	 * nodes contains a min-max with zero , which means negating this molecular
	 * node
	 * 
	 * @param molNode
	 *            the node i want to check for its parent nodes
	 * @return all the nodes that has arg to the molecular node , and min-max
	 *         zero
	 */
	private static boolean containsMinMaxZero(MolecularNode molNode) {
		boolean min = false;
		boolean max = false;
		NodeSet nodeSet;
		DownCable dCable = molNode.getDownCableSet().getDownCable("min");
		if (dCable != null) {
			nodeSet = dCable.getNodeSet();
			for (int j = 0; j < dCable.getNodeSet().size(); j++) {
				if (nodeSet.getNode(j).getIdentifier().equals("0")) {
					min = true;
					break;
				}

			}
		}
		DownCable dCable1 = molNode.getDownCableSet().getDownCable("max");
		if (dCable1 != null) {
			nodeSet = dCable1.getNodeSet();
			for (int j = 0; j < dCable.getNodeSet().size(); j++) {
				if (nodeSet.getNode(j).getIdentifier().equals("0")) {
					max = true;
					break;
				}

			}
		}
		if (min && max)
			return true;
		return false;
	}
}
