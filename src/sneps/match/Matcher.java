package sneps.match;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.MolecularNode;
import sneps.Network;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;

public class Matcher {
	
	
	public LinkedList<Object[]> Match(MolecularNode sourceNode,Network network,boolean UVBR){
		LinkedList<Object[]> matches=new LinkedList<Object[]>();
		CaseFrame sourceCF=sourceNode.getDownCableSet().getCaseFrame();
		NodeSet candidateNodes = network.getMolecularNodes().get(sourceCF.getId());
		
		for (int i = 0; i < candidateNodes.size(); i++) {
			MolecularNode candidateNode=(MolecularNode) candidateNodes.getNode(i);
			if(sourceNode.equals(candidateNode))
				continue;
			LinkedList<Substitutions> sourceList = new LinkedList<Substitutions>();
			LinkedList<Substitutions> targetList = new LinkedList<Substitutions>();
			sourceList.add(new LinearSubstitutions());
			targetList.add(new LinearSubstitutions());
			NodeSet sourceNodeVariables=getTerms(sourceNode,true);
			if(hERE(sourceNode,candidateNode,sourceList,targetList,UVBR,true)){
				
possibleMatches:	for (int j = 0; j < sourceList.size(); j++) {
					Substitutions sourceR=sourceList.get(j);
					Substitutions sourceS=new LinearSubstitutions();
					Substitutions targetR=targetList.get(j);
					Substitutions targetS=new LinearSubstitutions();
					
					Substitutions sourceBindings=new LinearSubstitutions();
					Substitutions targetBindings=new LinearSubstitutions();
					
					NodeSet candidateNodeVariables=getTerms(candidateNode,true);
					
					for (int k = 0; k < sourceNodeVariables.size(); k++) {
						Node sbinding=vere((VariableNode) sourceNodeVariables.getNode(k), sourceR, targetR, sourceS, targetS); 
						if(sbinding==null)
							continue possibleMatches;
						else
					      sourceBindings.insert(new Binding((VariableNode) sourceNodeVariables.getNode(k),sbinding));		
							
					}
					for (int k = 0; k < candidateNodeVariables.size(); k++) {
						Node cbinding=vere((VariableNode) candidateNodeVariables.getNode(k), targetR, sourceR, targetS, sourceS); 
						if(cbinding==null)
							continue possibleMatches;
						else
						      targetBindings.insert(new Binding((VariableNode) candidateNodeVariables.getNode(k),cbinding));
							
					}
					
					
					if(!(violatesUTIR(sourceNode,sourceBindings)||violatesUTIR(candidateNode,targetBindings))){
						Object[] match=new Object []{candidateNode,sourceBindings,targetBindings};
						matches.add(match);
					}
					
				}
			}
		}
		
		return matches;
	}
	public boolean violatesUTIR(MolecularNode node,Substitutions bindings){
		NodeSet terms=getTerms(node,false);
		
		return violatesUTIR(terms,bindings);
		
		
	}
	
	public boolean violatesUTIR(NodeSet terms,Substitutions bindings){
		
		if(terms.size()<2)
			return false;
		Node term0=terms.getNode(0);
		Node term1=terms.getNode(1);
		
		if(termsEqual(term0,term1,bindings))
			return true;
		
		NodeSet newTerms=new NodeSet();
		newTerms.addAll(terms);
		newTerms.removeNode(term1);
		if(term1.getSyntacticSuperClass()=="Molecular")
		newTerms.addAll(getTerms((MolecularNode) term1,false));
		if(violatesUTIR(newTerms,bindings))
			return true;
		terms.removeNode(term0);
		if(term0.getSyntacticSuperClass()=="Molecular")
			terms.addAll(getTerms((MolecularNode) term0,false));
		if(violatesUTIR(terms,bindings))
			return true;
		
		return false;
	}
	
	public boolean termsEqual(Node term1,Node term2,Substitutions bindings){
		if(term1.getSyntacticType()=="Variable")
			term1=bindings.term((VariableNode) term1);
		if(term2.getSyntacticType()=="Variable")
			term2=bindings.term((VariableNode) term2);
		
		if(!term1.getSyntacticType().equals(term2.getSyntacticType()))
		return false;
		
		if(term1.getSemanticType()=="Base")
			return term1.equals(term2);
		//Molecular
		if(term1.equals(term2)){
		DownCable[] dc1=(DownCable[]) ((MolecularNode) term1).getDownCableSet().getDownCables().values().toArray();
		DownCable[] dc2=(DownCable[]) ((MolecularNode) term2).getDownCableSet().getDownCables().values().toArray();
		
		for (int i = 0; i < dc1.length; i++) {
			boolean found=false;
			for (int j = 0; j < dc2.length; j++) {
			if(dc1[i].getRelation()==dc2[j].getRelation()){
				found=true;
				NodeSet ns1=dc1[i].getNodeSet();
				NodeSet ns2=dc2[j].getNodeSet();
				if(!ns1.equals(ns2))
					return false;
			}
			}
			if(!found)
			return false;
		}
		
		}
		
		return true;
	}
	
	public NodeSet getTerms(MolecularNode node,boolean var){
		NodeSet ns=new NodeSet();
		Hashtable <String,DownCable> dcs=node.getDownCableSet().getDownCables();
		Enumeration <DownCable> elements= dcs.elements();
		
		while(elements.hasMoreElements()){
			NodeSet nodes=elements.nextElement().getNodeSet();
			
			for (int i = 0; i < nodes.size(); i++) {
				Node n=nodes.getNode(i);
				
				if(var&&n.getSyntacticType()=="Variable")
					ns.addNode(n);
				else if(!var)
					ns.addNode(n);
			}
			
		}
		return ns;
	}

	public boolean hERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourceList,
			LinkedList<Substitutions> targetList, boolean UVBR,
			boolean rightOrder) {
		if (!compatible(sourceNode, targetNode))
			return false;
		if (sourceNode.getSyntacticType() == "Variable")
			if (!VARHERE(sourceNode, targetNode, sourceList, targetList, UVBR,
					rightOrder))
				return false;
			else if (targetNode.getSyntacticType() == "Variable")
				if (!VARHERE(sourceNode, targetNode, sourceList, targetList,
						UVBR, rightOrder))
					return false;
				else if (sourceNode.getSyntacticType() == "Molecular"
						&& targetNode.getSyntacticType() == "Molecular") {
					MolecularNode n1 = (MolecularNode) sourceNode;
					MolecularNode n2 = (MolecularNode) targetNode;
					DownCableSet cs1 = n1.getDownCableSet();
					DownCableSet cs2 = n2.getDownCableSet();
					if (cs1.getCaseFrame() != cs2.getCaseFrame())
						return false;
					else {
						DownCable[] dcs1 = (DownCable[]) cs1.getDownCables()
								.values().toArray();
						DownCable[] dcs2 = (DownCable[]) cs2.getDownCables()
								.values().toArray();

						for (DownCable cable1 : dcs1)
							for (DownCable cable2 : dcs2)
								if (cable1.getRelation().equals(
										cable2.getRelation())) {
									int size1 = cable1.getNodeSet().size();
									int size2 = cable2.getNodeSet().size();
									String adjust = cable1.getRelation()
											.getAdjust();

									switch (adjust) {
									case "none":
										if (size1 != size2)
											return false;
									case "reduce":
										if ((rightOrder && size1 > size2)
												|| (!rightOrder && size2 > size1))
											return false;
									case "expand":
										if ((rightOrder && size1 < size2)
												|| (!rightOrder && size2 < size1))
											return false;
									}
									if (!setUnify(cable1.getNodeSet(),
											cable2.getNodeSet(), sourceList,
											targetList, UVBR, rightOrder))
										return false;
								}

					}

				}

		return true;
	}

	public boolean VARHERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourcelist,
			LinkedList<Substitutions> targetList, boolean UVBR,
			boolean rightOrder) {

		boolean unifiable = false;
		LinkedList<Substitutions> variableList;
		LinkedList<Substitutions> bindingList;
		VariableNode variableNode;
		Node bindingNode;
		if (rightOrder) {
			variableList = sourcelist;
			bindingList = targetList;
			variableNode = (VariableNode) sourceNode;
			bindingNode = targetNode;
		} else {
			variableList = targetList;
			bindingList = variableList;
			variableNode = (VariableNode) targetNode;
			bindingNode = sourceNode;
		}

		for (int i = 0; i < variableList.size(); i++) {
			Substitutions currentVSub = variableList.removeFirst();
			Substitutions currentBSub = bindingList.removeFirst();
			if (!currentVSub.isBound(variableNode)) {
				if (bindingNode.getSyntacticType() == "Variable" && UVBR)
					if (currentBSub.isBound((VariableNode) bindingNode)
							|| currentVSub.isValue(bindingNode))
						continue;

				currentVSub.putIn(new Binding(variableNode, bindingNode));
				variableList.add(currentVSub);
				bindingList.add(currentBSub);
				unifiable = true;

			} else {
				if (currentVSub.getBindingByVariable(variableNode).getNode()
						.equals(bindingNode)) {
					variableList.add(currentVSub);
					bindingList.add(currentBSub);
					unifiable = true;
				}
			}
		}

		return unifiable;
	}

	public boolean compatible(Node sourceNode, Node targetNode) {
		String sourceType = sourceNode.getSyntacticSuperClass() == "Molecular" ? "Molecular"
				: sourceNode.getSyntacticType();
		String targetType = targetNode.getSyntacticSuperClass() == "Molecular" ? "Molecular"
				: targetNode.getSyntacticType();
		if (sourceType == "Variable" || targetType == "Variable")
			return true;
		if (sourceType == targetType)
			// TODO: check identifying nodes
			return (sourceNode.getIdentifier().equals(targetNode
					.getIdentifier()));
		else
			return true;

	}

	public boolean setUnify(NodeSet ns1, NodeSet ns2,
			LinkedList<Substitutions> sList, LinkedList<Substitutions> tList,
			boolean UVBR, boolean rightOrder) {
		if (ns1.size() == 0 || ns2.size() == 0)
			return true;

		boolean unifiable = false;

		for (int i = 0; i < sList.size(); i++) {
			Substitutions currentSourceSub = sList.removeFirst();
			Substitutions currentTargetSub = tList.removeFirst();
			for (int j = 0; j < ns1.size(); j++) {
				Node n1 = ns1.getNode(j);
				NodeSet others1 = new NodeSet();
				others1.addAll(ns1);
				others1.removeNode(n1);
				for (int j2 = 0; j2 < ns2.size(); j2++) {
					Node n2 = ns2.getNode(j2);
					NodeSet others2 = new NodeSet();
					others2.addAll(ns2);
					others2.removeNode(n2);
					LinkedList<Substitutions> newSList = new LinkedList<Substitutions>();
					LinkedList<Substitutions> newTList = new LinkedList<Substitutions>();
					newSList.add(currentSourceSub);
					newTList.add(currentTargetSub);
					if (UVBR && uvbrConflict(ns1, ns2, n1, n2))
						continue;
					if (hERE(n1, n2, newSList, newTList, UVBR, rightOrder))
						if (setUnify(others1, others2, newSList, newTList,
								UVBR, rightOrder)) {
							sList.addAll(newSList);
							tList.addAll(newTList);
							unifiable = true;
						}

				}
			}

		}

		return unifiable;
	}

	public Node vere(VariableNode n, Substitutions sourceR,
			Substitutions targetR, Substitutions sourceS, Substitutions targetS) {
		Node bindingNode;
		if (sourceR.isBound(n)) {
			Node RbindingNode = sourceR.term(n);
			if (RbindingNode.getSyntacticType() == "Base"
					|| RbindingNode.getSyntacticType() == "Closed") {
				bindingNode = RbindingNode;
				sourceS.putIn(new Binding(n, RbindingNode));
				sourceR.update(sourceR.getBindingByVariable(n), n);

				// TODO:add appropriate getters and setters in subs

			} else if (RbindingNode.getSyntacticType() == "Pattern") {
				/* done */sourceR.update(sourceR.getBindingByVariable(n), n);
				/* loop */sourceS.update(sourceS.getBindingByVariable(n), n);
				bindingNode = termVere((MolecularNode) RbindingNode, sourceR,
						targetR, sourceS, targetS);
				if (bindingNode == null)
					return null;
				/* loop */sourceS.update(sourceS.getBindingByVariable(n),
						bindingNode);

			} else if (sourceS.isBound(n)) {
				if (sourceS.term((VariableNode) n).equals(n))
					/* fail loop */return null;
				else
					bindingNode = sourceS.term((VariableNode) n);

			} else
				bindingNode = sourceR.term(n);
		} else {
			sourceR.insert(new Binding(n, n));
			bindingNode = n;
			sourceS.insert(new Binding(n, bindingNode));
		}

		return bindingNode;
	}

	public MolecularNode termVere(MolecularNode oldNode, Substitutions sourceR,
			Substitutions targetR, Substitutions sourceS, Substitutions targetS) {
		MolecularNode newNode = null;
		DownCableSet newDCS = null;
		DownCableSet oldDCS = oldNode.getDownCableSet();
		Enumeration<DownCable> oldDCs = oldDCS.getDownCables().elements();
		LinkedList<DownCable> newDClist = new LinkedList<DownCable>();
		while (oldDCs.hasMoreElements()) {
			DownCable currentOldCable = oldDCs.nextElement();
			NodeSet currentOldNS = currentOldCable.getNodeSet();
			NodeSet currentNewNS = new NodeSet();
			for (int i = 0; i < currentOldNS.size(); i++) {
				Node currentOldNode = currentOldNS.getNode(i);
				Node currentNewNode = null;

				if (currentOldNode.getSyntacticType() == "Variable") {
					if (targetR.isBound((VariableNode) currentOldNode)) {
						if (targetR.term((VariableNode) currentOldNode) == currentOldNode) {
							if (targetS.term((VariableNode) currentOldNode) == currentOldNode)
								return null;
							else
								currentNewNode = targetS
										.term((VariableNode) currentOldNode);
						} else
							currentNewNode = vere(
									(VariableNode) currentOldNode, targetR,
									sourceR, targetS, sourceS);

					}

					else
						currentNewNode = currentOldNode;

				} else if (currentOldNode.getSyntacticType() == "Pattern")
					currentNewNode = termVere((PatternNode) currentOldNode,
							targetR, sourceR, targetS, sourceS);

				else
					currentNewNode = currentOldNode;

				currentNewNS.addNode(currentNewNode);
			}
			newDClist.add(new DownCable(currentOldCable.getRelation(),
					currentNewNS));
		}

		newDCS = new DownCableSet(newDClist, oldDCS.getCaseFrame());
		try {
			newNode = new MolecularNode(oldNode.getSyntacticType(),
			// TODO:name
					oldNode.getSemanticType(), "tempName", newDCS);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return newNode;
	}

	// public Node VERE(VariableNode n, Substitutions r, Substitutions s) {
	//
	// Stack<VariableNode> path = source(n, r);
	// VariableNode v = path.pop();
	// Node bindingNode;
	// if (!r.isBound(v)) {
	// bindingNode = v;
	// // done
	// r.update(r.getBindingByVariable(v), v);
	// } else if (r.value(v).getSyntacticType() == "Base") {
	// bindingNode = r.value(v);
	// // done
	// r.update(r.getBindingByVariable(v), v);
	// if (s.isBound(v))
	// s.update(s.getBindingByVariable(v), bindingNode);
	// else
	// s.insert(new Binding(v, bindingNode));
	// } else if (r.value(v).getSyntacticSuperClass() == "Molecular") {
	// // done
	// r.update(r.getBindingByVariable(v), v);
	// MolecularNode y = (MolecularNode) bindingNode;
	//
	// // TODO:stack loop start on x
	//
	// while (!path.isEmpty()) {
	// /* TODO: s(x)==loop */
	// }
	// // stack loop end
	// bindingNode = termVERE(y, r, s);
	// if (s.isBound(v))
	// s.update(s.getBindingByVariable(v), bindingNode);
	// else
	// s.insert(new Binding(v, bindingNode));
	//
	// } else if (!s.isBound(v)) {
	// bindingNode = v;
	//
	// } else if (v.Sloop()) {
	// // fail
	// } else {
	// bindingNode = s.value(v);
	//
	// }
	//
	// // stack loop for x
	// while (!path.isEmpty()) {
	// VariableNode x = path.pop();
	// if (s.isBound(x))
	// s.update(s.getBindingByVariable(x), bindingNode);
	// else
	// s.insert(new Binding(x, bindingNode));
	// }
	// // stack loop end
	// return bindingNode;
	// }
	//
	// public Node termVERE(MolecularNode n, Substitutions r, Substitutions s) {
	// DownCableSet dcset = n.getDownCableSet();
	// Hashtable<String, DownCable> dcs = dcset.getDownCables();
	// String[] dckeys = dcs.keySet().toArray(new String[dcs.size()]);
	//
	// for (String dckey : dckeys) {
	// DownCable dc = dcs.get(dckey);
	// Relation relation = dc.getRelation();
	// NodeSet ns = dc.getNodeSet();
	//
	// for (int i = 0; i < ns.size(); i++) {
	// Node currentNode = ns.getNode(i);
	//
	// if (currentNode.getSyntacticType() == "Variable") {
	// if (currentNode.Rdone()) {
	// if (currentNode.Sloop()) {
	// // fail
	// } else {
	// // replace by s(currentNode)
	// }
	// } else if (r.isBound((VariableNode) currentNode)) {
	// // replace by VERE(currentNode)
	// }
	// } else if (currentNode.getSyntacticSuperClass() == "Molecular") {
	// // replace by termvere(currentNode)
	// }
	//
	// }
	// }
	//
	// return null;
	// }
	//
	// public Stack<VariableNode> source(VariableNode n, Substitutions r) {
	// // TODO:UVBR Rloop
	// Stack<VariableNode> path = new Stack<VariableNode>();
	//
	// VariableNode current = n;
	// while (r.value(current).getSyntacticType() == "Variable"
	// && !r.value(current).equals(n)) {
	// path.push(current);
	// current = (VariableNode) r.value(current);
	//
	// }
	//
	// return path;
	// }

	public boolean uvbrConflict(NodeSet ns1, NodeSet ns2, Node n, Node m) {
		return (n.getSyntacticType() == "Variable" || m.getSyntacticType() == "Variable")
				&& (ns1.contains(m) || ns2.contains(n));
	}

	public static void main(String[] args) {

	}

}
