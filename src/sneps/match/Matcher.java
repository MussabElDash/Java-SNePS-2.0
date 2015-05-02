package sneps.match;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;

public class Matcher {

//	public LinkedList<Object[]> Match(MolecularNode sourceNode,
//			Network network, boolean UVBR) {
//		LinkedList<Object[]> matches = new LinkedList<Object[]>();
//		CaseFrame sourceCF = sourceNode.getDownCableSet().getCaseFrame();
//		NodeSet candidateNodes = network.getMolecularNodes().get(
//				sourceCF.getId());
//
//		for (int i = 0; i < candidateNodes.size(); i++) {
//			MolecularNode candidateNode = (MolecularNode) candidateNodes
//					.getNode(i);
//			if (sourceNode.equals(candidateNode))
//				continue;
//			LinkedList<Substitutions> sourceList = new LinkedList<Substitutions>();
//			LinkedList<Substitutions> targetList = new LinkedList<Substitutions>();
//			sourceList.add(new LinearSubstitutions());
//			targetList.add(new LinearSubstitutions());
//			NodeSet sourceNodeVariables = getTerms(sourceNode, true);
//			if (hERE(sourceNode, candidateNode, sourceList, targetList, UVBR,
//					true)) {
//
//				possibleMatches: for (int j = 0; j < sourceList.size(); j++) {
//					Substitutions sourceR = sourceList.get(j);
//					Substitutions sourceS = new LinearSubstitutions();
//					Substitutions targetR = targetList.get(j);
//					Substitutions targetS = new LinearSubstitutions();
//
//					Substitutions sourceBindings = new LinearSubstitutions();
//					Substitutions targetBindings = new LinearSubstitutions();
//
//					NodeSet candidateNodeVariables = getTerms(candidateNode,
//							true);
//
//					for (int k = 0; k < sourceNodeVariables.size(); k++) {
//						Node sbinding = vere(
//								(VariableNode) sourceNodeVariables.getNode(k),
//								sourceR, targetR, sourceS, targetS);
//						if (sbinding == null)
//							continue possibleMatches;
//						else
//							sourceBindings.insert(new Binding(
//									(VariableNode) sourceNodeVariables
//											.getNode(k), sbinding));
//
//					}
//					for (int k = 0; k < candidateNodeVariables.size(); k++) {
//						Node cbinding = vere(
//								(VariableNode) candidateNodeVariables
//										.getNode(k),
//								targetR, sourceR, targetS, sourceS);
//						if (cbinding == null)
//							continue possibleMatches;
//						else
//							targetBindings.insert(new Binding(
//									(VariableNode) candidateNodeVariables
//											.getNode(k), cbinding));
//
//					}
//
//					if (!(violatesUTIRBrute(sourceNode, sourceBindings) || violatesUTIRBrute(
//							candidateNode, targetBindings))) {
//						Object[] match = new Object[] { candidateNode,
//								sourceBindings, targetBindings };
//						matches.add(match);
//					}
//
//				}
//			}
//		}
//
//		return matches;
//	}

	public boolean violatesUTIRBrute(MolecularNode node, Substitutions bindings) {
		NodeSet terms = getTerms(node, false);

		return violatesUTIRBrute(terms, bindings,false);

	}

	public boolean violatesUTIRBrute(NodeSet terms, Substitutions bindings,boolean helper) {
        System.out.println("called with "+terms);
		if (terms.size() < 2)
			return false;
		Node term0 = terms.getNode(0);
		Node term1 = terms.getNode(1);

		if (differentTermsEqual(term0, term1, bindings))
			return true;

		NodeSet newTerms = new NodeSet();
		newTerms.addAll(terms);
		newTerms.removeNode(term1);
		if (term1.getSyntacticSuperClass().equals("Molecular"))
			newTerms.addAll(getTerms((MolecularNode) term1, false));
		if (violatesUTIRBrute(newTerms, bindings,true))
			return true;
		if(helper)
			return false;
		System.out.println("after first call "+terms);
		terms.removeNode(term0);
		if (term0.getSyntacticSuperClass().equals("Molecular"))
			terms.addAll(getTerms((MolecularNode) term0, false));
		if (violatesUTIRBrute(terms, bindings,false)){
			//System.out.println(terms.toString());
			return true;}

		return false;
	}

	public boolean differentTermsEqual(Node term1, Node term2,
			Substitutions bindings) {

		if (term1.getSyntacticType().equals("Variable"))
			if (term2.getSyntacticType().equals("Variable"))
				if (term1.equals(term2))
					return false;

		if (term1.getSyntacticType().equals("Variable")&&bindings.isBound((VariableNode) term1))
			term1 = bindings.term((VariableNode) term1);
		if (term2.getSyntacticType().equals("Variable")&&bindings.isBound((VariableNode) term2))
			term2 = bindings.term((VariableNode) term2);
        
		if (!term1.getSyntacticType().equals(term2.getSyntacticType()))
			return false;

		
		if (term1.getSyntacticType().equals("Base")||term1.getSyntacticType().equals("Variable"))
			return term1.equals(term2);//TODO: same constants aren't different
		// Molecular
		if (term1.equals(term2)&&term1.getSyntacticSuperClass().equals("Molecular")) {
			DownCable[] dc1 = new DownCable[((MolecularNode) term1).getDownCableSet().size()];
					dc1= ((MolecularNode) term1)
					.getDownCableSet().getDownCables().values().toArray(dc1);
			DownCable[] dc2 = new DownCable[((MolecularNode) term2).getDownCableSet().size()];
					dc2= ((MolecularNode) term2)
					.getDownCableSet().getDownCables().values().toArray(dc2);

			for (int i = 0; i < dc1.length; i++) {
				boolean found = false;
				for (int j = 0; j < dc2.length; j++) {
					if (dc1[i].getRelation() == dc2[j].getRelation()) {
						found = true;
						NodeSet ns1 = dc1[i].getNodeSet();
						NodeSet ns2 = dc2[j].getNodeSet();

						for (int k = 0; k < ns1.size(); k++) {
							Node n = ns1.getNode(k);
							if (n.getSyntacticType().equals("Variable"))
								if (bindings.isBound((VariableNode) n))
									if (!bindings.value((VariableNode) n)
											.equals(n))

									{
										ns1.removeNode(ns1.getNode(k));
										ns1.addNode(bindings
												.value((VariableNode) n));
									}
						}
						for (int k = 0; k < ns2.size(); k++) {
							Node n = ns2.getNode(k);
							if (n.getSyntacticType().equals("Variable"))
								if (bindings.isBound((VariableNode) n))
									if (!bindings.value((VariableNode) n)
											.equals(n))

									{
										ns2.removeNode(ns2.getNode(k));
										ns2.addNode(bindings
												.value((VariableNode) n));
									}

						}
                       
						 return ns1.equals(ns2);//TODO: recheck
						
					}
				}
				if (!found)
					return false;
			}

		}

		return true;
	}

	public NodeSet getTerms(MolecularNode node, boolean var) {
		NodeSet ns = new NodeSet();
		Hashtable<String, DownCable> dcs = node.getDownCableSet()
				.getDownCables();
		Enumeration<DownCable> elements = dcs.elements();

		while (elements.hasMoreElements()) {
			NodeSet nodes = elements.nextElement().getNodeSet();

			for (int i = 0; i < nodes.size(); i++) {
				Node n = nodes.getNode(i);

				if (var && n.getSyntacticType().equals("Variable"))
					ns.addNode(n);
				else if (!var)
					ns.addNode(n);
			}

		}
		return ns;
	}
	
	
	public boolean violatesUTIRorOccursCheck(MatchingSet boundTerms,Substitutions sourceR,Substitutions targetR,int originalSize)
	throws Exception{
		
		
		 Node[] tempTerms=boundTerms.toArray();
	
	for (int i = 0; i < tempTerms.length; i++) 
		{
			Node term=tempTerms[i];
			Node newTerm;
			if(term.getSyntacticType().equals("Base"))
				continue;
			Substitutions tempS=new LinearSubstitutions();
			Substitutions tempT=new LinearSubstitutions();
		
			for (int j = 0; j < sourceR.cardinality(); j++) {
				tempS.putIn(sourceR.getBinding(j).clone());
			}
			for (int j = 0; j < targetR.cardinality(); j++) {
				tempT.putIn(targetR.getBinding(j).clone());
			}
//			 System.out.println(tempS);
			if(term.getSyntacticType().equals("Variable"))
			newTerm=vere((VariableNode) term, tempS, tempT, new LinearSubstitutions(), new LinearSubstitutions());
			else
			newTerm=termVere((MolecularNode) term, tempT, tempS,new LinearSubstitutions(),new LinearSubstitutions());
//            System.out.println(newTerm);
	           
			if(newTerm==null)//occurs check
				return true;
			
			boundTerms.replace(term,newTerm);
			//System.out.println(tempTerms);
			if (boundTerms.size()!=originalSize)
				return true;
		}
		
		
		return boundTerms.size()!=originalSize;
	}
	
	

	public boolean hERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourceList,
			LinkedList<Substitutions> targetList, boolean UVBR,
			boolean rightOrder,MatchingSet boundTerms,int originalSize) throws Exception {
		if (!compatible(sourceNode, targetNode))
			return false;
		if (sourceNode.getSyntacticType().equals("Variable"))
			if (!VARHERE(sourceNode, targetNode, sourceList, targetList, UVBR,
					rightOrder,boundTerms,originalSize))
				return false;
			else if (targetNode.getSyntacticType().equals("Variable"))
				if (!VARHERE(sourceNode, targetNode, sourceList, targetList,
						UVBR, !rightOrder,boundTerms,originalSize))
					return false;
				else if (sourceNode.getSyntacticType().equals("Molecular")
						&& targetNode.getSyntacticType().equals("Molecular")) {
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
											targetList, UVBR, rightOrder,boundTerms,originalSize))
										return false;
								}

					}

				}

		return true;
	}

	public boolean VARHERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourcelist,
			LinkedList<Substitutions> targetList, boolean UVBR,
			boolean rightOrder,MatchingSet boundTerms,int originalSize) throws Exception {

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
				if (bindingNode.getSyntacticType().equals("Variable") && UVBR)
					if (currentBSub.isBound((VariableNode) bindingNode)
							|| currentVSub.isValue(bindingNode))
						continue;
                if(UVBR&&violatesUTIRorOccursCheck(boundTerms, currentVSub.insert(new Binding(variableNode, bindingNode)), currentBSub, originalSize))
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
		String sourceType = sourceNode.getSyntacticSuperClass().equals(
				"Molecular") ? "Molecular" : sourceNode.getSyntacticType();
		String targetType = targetNode.getSyntacticSuperClass().equals(
				"Molecular") ? "Molecular" : targetNode.getSyntacticType();
		if (sourceType.equals("Variable") || targetType.equals("Variable"))
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
			boolean UVBR, boolean rightOrder,MatchingSet boundTerms,int originalSize) throws Exception {
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
					if (hERE(n1, n2, newSList, newTList, UVBR, rightOrder,boundTerms,originalSize))
						if (setUnify(others1, others2, newSList, newTList,
								UVBR, rightOrder,boundTerms,originalSize)) {
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
			if (RbindingNode.getSyntacticType().equals("Base")
					|| RbindingNode.getSyntacticType().equals("Closed")) {
				bindingNode = RbindingNode;
				sourceS.putIn(new Binding(n, RbindingNode));
				sourceR.update(sourceR.getBindingByVariable(n), n);

				

			} else if (RbindingNode.getSyntacticType().equals("Pattern")) {
				/* done */sourceR.update(sourceR.getBindingByVariable(n), n);
				/* loop */sourceS.putIn(new Binding(n, n));
				bindingNode = termVere((MolecularNode) RbindingNode, sourceR,
						targetR, sourceS, targetS);
				if (bindingNode == null)
					return null;
				/* loop */sourceS.update(sourceS.getBindingByVariable(n),
						bindingNode);

			} else if (sourceS.isBound(n)||targetS.isBound((VariableNode) RbindingNode)) {
				
//				if (sourceS.term((VariableNode) n).equals(n))
//					/* fail loop */return null;
				 if (targetS.term((VariableNode) RbindingNode).equals(RbindingNode))
					return null;
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
                
				if (currentOldNode.getSyntacticType().equals("Variable")) {
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
                               if(currentNewNode==null)
                            	   return null;
					}

					else
						currentNewNode = currentOldNode;

				} else if (currentOldNode.getSyntacticType().equals("Pattern"))
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
			
					oldNode.getSemanticType(), oldNode.getIdentifier(), newDCS);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return newNode;
	}
	
	public static boolean sameFunction(MolecularNode f1,MolecularNode f2){
		DownCableSet DCS1=f1.getDownCableSet();
		DownCableSet DCS2=f1.getDownCableSet();
		CaseFrame cs1=DCS1.getCaseFrame();
		CaseFrame cs2=DCS2.getCaseFrame();
		if(!cs1.equals(cs2))
		return false;
		else{
			
			Enumeration<String> relations=DCS1.getDownCables().keys();
			Hashtable<String,DownCable> DCables1=DCS1.getDownCables();
			Hashtable<String,DownCable> DCables2=DCS2.getDownCables();
			
			while(relations.hasMoreElements()){
				String relation=relations.nextElement();
				DownCable downcable1=DCables1.get(relation);
                DownCable downcable2=DCables2.get(relation);
                NodeSet ns1=downcable1.getNodeSet();
                NodeSet ns2=downcable2.getNodeSet();
                
                for (int i = 0; i < ns1.size(); i++) {
                	Node n1=ns1.getNode(i);
                	boolean molecular=n1.getSyntacticSuperClass().equals("Molecular");
					for (int j = 0; j < ns2.size(); j++) {
						Node n2=ns2.getNode(j);
						if(molecular)
							{if(n2.getSyntacticSuperClass().equals("Molecular")&&sameFunction((MolecularNode) n1, (MolecularNode) n2))
							{
								ns1.removeNode(n1);
								ns2.removeNode(n2);
							}}
						else if(n1.equals(n2)){
							ns1.removeNode(n1);
							ns2.removeNode(n2);
						
						}
							
							
							
							
					}
				}
                
                if(ns1.size()!=0||ns2.size()!=0)
                	return false;
                
			}
			
					
					
					
		}
		return true;
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
		return (n.getSyntacticType().equals("Variable") || m.getSyntacticType()
				.equals("Variable")) && (ns1.contains(m) || ns2.contains(n));
	}

	public static void main(String[] args) {

	}

}
