package sneps.match;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

import sneps.CaseFrame;
import sneps.Network;
import sneps.PathTrace;
import sneps.RCFP;
import sneps.Relation;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.MolecularNode;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PatternNode;
import sneps.Nodes.VariableNode;
import sneps.Paths.Path;
import snip.Rules.RuleNodes.AndNode;
import snip.Rules.RuleNodes.AndOrNode;
import snip.Rules.RuleNodes.NumericalNode;
import snip.Rules.RuleNodes.OrNode;
import snip.Rules.RuleNodes.RuleNode;
import snip.Rules.RuleNodes.ThreshNode;
import SNeBR.SNeBR;

public class Matcher {

	private static boolean UVBR = true;

	public static LinkedList<Object[]> Match(MolecularNode sourceNode)
			throws Exception {
		LinkedList<Object[]> matches = new LinkedList<Object[]>();
		CaseFrame sourceCF = sourceNode.getDownCableSet().getCaseFrame();
		NodeSet candidateNodes = Network.getMolecularNodes().get(
				sourceCF.getId());

		for (int i = 0; i < candidateNodes.size(); i++) {
			MolecularNode candidateNode = (MolecularNode) candidateNodes
					.getNode(i);
			if (sourceNode.equals(candidateNode))
				continue;
			LinkedList<Substitutions> sourceList = new LinkedList<Substitutions>();
			LinkedList<Substitutions> targetList = new LinkedList<Substitutions>();
			sourceList.add(new LinearSubstitutions());
			targetList.add(new LinearSubstitutions());
			NodeSet sourceNodeVariables = getTerms(sourceNode, true);
			MatchingSet sourceNodeTerms = new MatchingSet();
			MatchingSet targetNodeTerms = new MatchingSet();
			sourceNodeTerms.add(getTerms(sourceNode, false));
			targetNodeTerms.add(getTerms(candidateNode, false));
			if (hERE(sourceNode, candidateNode, sourceList, targetList, true,
					sourceNodeTerms, sourceNodeTerms.size(), targetNodeTerms,
					targetNodeTerms.size())) {

				possibleMatches: for (int j = 0; j < sourceList.size(); j++) {
					Substitutions sourceR = sourceList.get(j);
					Substitutions sourceS = new LinearSubstitutions();
					Substitutions targetR = targetList.get(j);
					Substitutions targetS = new LinearSubstitutions();

					Substitutions sourceBindings = new LinearSubstitutions();
					Substitutions targetBindings = new LinearSubstitutions();

					NodeSet candidateNodeVariables = getTerms(candidateNode,
							true);

					for (int k = 0; k < sourceNodeVariables.size(); k++) {
						Node sbinding = vere(
								(VariableNode) sourceNodeVariables.getNode(k),
								sourceR, targetR, sourceS, targetS);
						if (sbinding == null)
							continue possibleMatches;
						else
							sourceBindings.insert(new Binding(
									(VariableNode) sourceNodeVariables
											.getNode(k), sbinding));

					}
					for (int k = 0; k < candidateNodeVariables.size(); k++) {
						Node cbinding = vere(
								(VariableNode) candidateNodeVariables
										.getNode(k),
								targetR, sourceR, targetS, sourceS);
						if (cbinding == null)
							continue possibleMatches;
						else
							targetBindings.insert(new Binding(
									(VariableNode) candidateNodeVariables
											.getNode(k), cbinding));

					}
					Object[] match = new Object[] { candidateNode,
							sourceBindings, targetBindings };
					matches.add(match);

				}
			}
		}

		return matches;
	}

	private static boolean violatesUTIRBrute(MolecularNode node,
			Substitutions bindings) {
		NodeSet terms = getTerms(node, false);

		return violatesUTIRBrute(terms, bindings, false);

	}

	private static boolean violatesUTIRBrute(NodeSet terms,
			Substitutions bindings, boolean helper) {
		System.out.println("called with " + terms);
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
		if (violatesUTIRBrute(newTerms, bindings, true))
			return true;
		if (helper)
			return false;
		System.out.println("after first call " + terms);
		terms.removeNode(term0);
		if (term0.getSyntacticSuperClass().equals("Molecular"))
			terms.addAll(getTerms((MolecularNode) term0, false));
		if (violatesUTIRBrute(terms, bindings, false)) {
			// System.out.println(terms.toString());
			return true;
		}

		return false;
	}

	private static boolean differentTermsEqual(Node term1, Node term2,
			Substitutions bindings) {

		if (term1.getSyntacticType().equals("Variable"))
			if (term2.getSyntacticType().equals("Variable"))
				if (term1.equals(term2))
					return false;

		if (term1.getSyntacticType().equals("Variable")
				&& bindings.isBound((VariableNode) term1))
			term1 = bindings.term((VariableNode) term1);
		if (term2.getSyntacticType().equals("Variable")
				&& bindings.isBound((VariableNode) term2))
			term2 = bindings.term((VariableNode) term2);

		if (!term1.getSyntacticType().equals(term2.getSyntacticType()))
			return false;

		if (term1.getSyntacticType().equals("Base")
				|| term1.getSyntacticType().equals("Variable"))
			return term1.equals(term2);// TODO: same constants aren't different
		// Molecular
		if (term1.equals(term2)
				&& term1.getSyntacticSuperClass().equals("Molecular")) {
			DownCable[] dc1 = new DownCable[((MolecularNode) term1)
					.getDownCableSet().size()];
			dc1 = ((MolecularNode) term1).getDownCableSet().getDownCables()
					.values().toArray(dc1);
			DownCable[] dc2 = new DownCable[((MolecularNode) term2)
					.getDownCableSet().size()];
			dc2 = ((MolecularNode) term2).getDownCableSet().getDownCables()
					.values().toArray(dc2);

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

						return ns1.equals(ns2);// TODO: recheck

					}
				}
				if (!found)
					return false;
			}

		}

		return true;
	}

	private static NodeSet getTerms(MolecularNode node, boolean var) {
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

	public static boolean violatesUTIRorOccursCheck(MatchingSet boundTerms,
			Substitutions sourceR, Substitutions targetR, int originalSize)
			throws Exception {
		Node[] tempTerms = boundTerms.toArray();

		for (int i = 0; i < tempTerms.length; i++) {
			Node term = tempTerms[i];
			Node newTerm;
			if (term.getSyntacticType().equals("Base"))
				continue;
			Substitutions tempS = new LinearSubstitutions();
			Substitutions tempT = new LinearSubstitutions();

			for (int j = 0; j < sourceR.cardinality(); j++) {
				tempS.putIn(sourceR.getBinding(j).clone());
			}
			for (int j = 0; j < targetR.cardinality(); j++) {
				tempT.putIn(targetR.getBinding(j).clone());
			}

			if (term.getSyntacticType().equals("Variable"))
				newTerm = vere((VariableNode) term, tempS, tempT,
						new LinearSubstitutions(), new LinearSubstitutions());
			else
				newTerm = termVere((MolecularNode) term, tempT, tempS,
						new LinearSubstitutions(), new LinearSubstitutions());

			if (newTerm == null)// occurs check
				return true;

			boundTerms.replace(term, newTerm);

			if (boundTerms.size() != originalSize)
				return true;
		}

		return boundTerms.size() != originalSize;
	}

	public static boolean hERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourceList,
			LinkedList<Substitutions> targetList, boolean rightOrder,
			MatchingSet sourceBoundTerms, int sourceOriginalSize,
			MatchingSet targetBoundTerms, int targetOriginalSize)
			throws Exception {
		// System.out.println("source "+sourceNode);
		// System.out.println("target "+targetNode);
		if (!compatible(sourceNode, targetNode))
			return false;
		if (sourceNode.getSyntacticType().equals("Variable")) {
			if (!VARHERE(sourceNode, targetNode, sourceList, targetList,
					rightOrder, sourceBoundTerms, sourceOriginalSize,
					targetBoundTerms, targetOriginalSize))
				return false;
		} else if (targetNode.getSyntacticType().equals("Variable")) {
			if (!VARHERE(sourceNode, targetNode, sourceList, targetList,
					!rightOrder, sourceBoundTerms, sourceOriginalSize,
					targetBoundTerms, targetOriginalSize))
				return false;
		} else if (sourceNode.getSyntacticSuperClass().equals("Molecular")
				&& targetNode.getSyntacticSuperClass().equals("Molecular")) {
			MolecularNode n1 = (MolecularNode) sourceNode;
			MolecularNode n2 = (MolecularNode) targetNode;
			DownCableSet cs1 = n1.getDownCableSet();
			DownCableSet cs2 = n2.getDownCableSet();
			if (cs1.getCaseFrame() != cs2.getCaseFrame()) {
				System.out.println("here");
				return false;
			} else {
				DownCable[] dcs1 = new DownCable[cs1.getDownCables().size()];

				dcs1 = cs1.getDownCables().values().toArray(dcs1);
				DownCable[] dcs2 = new DownCable[cs2.getDownCables().size()];

				dcs2 = cs2.getDownCables().values().toArray(dcs2);

				for (DownCable cable1 : dcs1)
					for (DownCable cable2 : dcs2)
						if (cable1.getRelation().getName()
								.equals(cable2.getRelation().getName())) {
							int size1 = cable1.getNodeSet().size();
							int size2 = cable2.getNodeSet().size();
							String adjust = cable1.getRelation().getAdjust();

							switch (adjust) {
							case "none":
								if (size1 != size2)
									return false;
								break;
							case "reduce":
								if ((rightOrder && size1 > size2)
										|| (!rightOrder && size2 > size1))
									return false;
								break;
							case "expand":
								if ((rightOrder && size1 < size2)
										|| (!rightOrder && size2 < size1))
									return false;
								break;
							}
							// System.out.println(cable1.getNodeSet());
							// System.out.println(cable2.getNodeSet());
							if (!setUnify(cable1.getNodeSet(),
									cable2.getNodeSet(), sourceList,
									targetList, rightOrder, sourceBoundTerms,
									sourceOriginalSize, targetBoundTerms,
									targetOriginalSize))
								return false;
						}

			}

		}

		return true;
	}

	public static boolean VARHERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourcelist,
			LinkedList<Substitutions> targetList, boolean rightOrder,
			MatchingSet sourceBoundTerms, int sourceOriginalSize,
			MatchingSet targetBoundTerms, int targetOriginalSize)
			throws Exception {

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
			bindingList = sourcelist;
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
				if (UVBR
						&& (violatesUTIRorOccursCheck(sourceBoundTerms,
								currentVSub.insert(new Binding(variableNode,
										bindingNode)), currentBSub,
								sourceOriginalSize) || violatesUTIRorOccursCheck(
								targetBoundTerms, currentBSub,
								currentVSub.insert(new Binding(variableNode,
										bindingNode)), targetOriginalSize)))
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

	private static boolean compatible(Node sourceNode, Node targetNode) {
		String sourceType = sourceNode.getSyntacticSuperClass().equals(
				"Molecular") ? "Molecular" : sourceNode.getSyntacticType();
		String targetType = targetNode.getSyntacticSuperClass().equals(
				"Molecular") ? "Molecular" : targetNode.getSyntacticType();
		if (sourceType.equals("Variable") || targetType.equals("Variable"))
			return true;
		if (sourceType.equals(targetType)) {
			if (sourceType.equals("Molecular"))
				return true;
			else
				return (sourceNode.getIdentifier().equals(targetNode
						.getIdentifier()));
		} else
			return false;

	}

	private static boolean setUnify(NodeSet ns1, NodeSet ns2,
			LinkedList<Substitutions> sList, LinkedList<Substitutions> tList,
			boolean rightOrder, MatchingSet sourceBoundTerms,
			int sourceOriginalSize, MatchingSet targetBoundTerms,
			int targetOriginalSize) throws Exception {
		if (ns1.size() == 0 || ns2.size() == 0)
			return true;
		boolean unifiable = false;
		int sListSize = sList.size();
		for (int i = 0; i < sListSize; i++) {
			Substitutions cSSub = sList.removeFirst();
			Substitutions cTSub = tList.removeFirst();
			Substitutions currentSourceSub = cSSub
					.union(new LinearSubstitutions());
			Substitutions currentTargetSub = cTSub
					.union(new LinearSubstitutions());
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
					System.out.println(currentSourceSub);
					if (UVBR && uvbrConflict(ns1, ns2, n1, n2))
						continue;
					if ((hERE(n1, n2, newSList, newTList, rightOrder,
							sourceBoundTerms, sourceOriginalSize,
							targetBoundTerms, targetOriginalSize))
							&& (setUnify(others1, others2, newSList, newTList,
									rightOrder, sourceBoundTerms,
									sourceOriginalSize, targetBoundTerms,
									targetOriginalSize))) {
						sList.addAll(newSList);
						tList.addAll(newTList);
						unifiable = true;
					} else {
						currentSourceSub = cSSub
								.union(new LinearSubstitutions());
						currentTargetSub = cTSub
								.union(new LinearSubstitutions());
					}

				}
			}

		}

		return unifiable;
	}

	public static Node vere(VariableNode n, Substitutions sourceR,
			Substitutions targetR, Substitutions sourceS, Substitutions targetS) {

		return UVBR ? vereUVBR(n, sourceR, targetR, sourceS, targetS)
				: vereNUVBR(n, sourceR, sourceS, targetR, targetS);
	}

	public static Node vereUVBR(VariableNode n, Substitutions sourceR,
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

			} else if (sourceS.isBound(n)
					|| targetS.isBound((VariableNode) RbindingNode)) {

				// if (sourceS.term((VariableNode) n).equals(n))
				// /* fail loop */return null;
				if (targetS.term((VariableNode) RbindingNode).equals(
						RbindingNode))
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

	private static MolecularNode termVere(MolecularNode oldNode,
			Substitutions sourceR, Substitutions targetR,
			Substitutions sourceS, Substitutions targetS) {

		return UVBR ? termVereUVBR(oldNode, sourceR, targetR, sourceS, targetS)
				: termVERENUVBR(oldNode, sourceR, targetR, sourceS, targetS);
	}

	private static MolecularNode termVereUVBR(MolecularNode oldNode,
			Substitutions sourceR, Substitutions targetR,
			Substitutions sourceS, Substitutions targetS) {
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
						if (currentNewNode == null)
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

	protected static boolean sameFunction(MolecularNode f1, MolecularNode f2) {
		DownCableSet DCS1 = f1.getDownCableSet();
		DownCableSet DCS2 = f2.getDownCableSet();
		CaseFrame cs1 = DCS1.getCaseFrame();
		CaseFrame cs2 = DCS2.getCaseFrame();
		if (!cs1.equals(cs2))
			return false;
		else {

			Enumeration<String> relations = DCS1.getDownCables().keys();
			Hashtable<String, DownCable> DCables1 = DCS1.getDownCables();
			Hashtable<String, DownCable> DCables2 = DCS2.getDownCables();

			while (relations.hasMoreElements()) {
				String relation = relations.nextElement();
				DownCable downcable1 = DCables1.get(relation);
				DownCable downcable2 = DCables2.get(relation);
				NodeSet ns1 = downcable1.getNodeSet().Union(new NodeSet());
				NodeSet ns2 = downcable2.getNodeSet().Union(new NodeSet());

				for (int i = 0; i < ns1.size(); i++) {
					Node n1 = ns1.getNode(i);
					boolean molecular = n1.getSyntacticSuperClass().equals(
							"Molecular");
					for (int j = 0; j < ns2.size(); j++) {
						Node n2 = ns2.getNode(j);
						if (molecular) {
							if (n2.getSyntacticSuperClass().equals("Molecular")
									&& sameFunction((MolecularNode) n1,
											(MolecularNode) n2)) {
								ns1.removeNode(n1);
								ns2.removeNode(n2);
							}
						} else if (n1.equals(n2)) {
							ns1.removeNode(n1);
							ns2.removeNode(n2);

						}

					}
				}

				if (ns1.size() != 0 || ns2.size() != 0)
					return false;

			}

		}
		return true;
	}

	public static Node applySubstitution(Node node, Substitutions sub) {
		Node boundNode;
		if (node.getSyntacticType().equals("Variable"))
			boundNode = vere((VariableNode) node, sub,
					new LinearSubstitutions(), new LinearSubstitutions(),
					new LinearSubstitutions());
		else if (node.getSyntacticType().equals("Base"))
			boundNode = node;
		else
			boundNode = termVere((MolecularNode) node,
					new LinearSubstitutions(), sub, new LinearSubstitutions(),
					new LinearSubstitutions());

		return boundNode;
	}

	//
	// private static boolean patHERE(MolecularNode sourceNode,MolecularNode
	// targetNode,LinkedList<Substitutions> sList,LinkedList<Substitutions>
	// tList,boolean UVBR,boolean rightOrder,MatchingSet sourceBoundTerms,int
	// sourceOriginalSize,MatchingSet targetBoundTerms,int targetOriginalSize){
	// if(!checkRuleCompatibility(sourceNode,targetNode))
	// return false;
	//
	// boolean flag=false;
	// CaseFrame sourceCF=sourceNode.getDownCableSet().getCaseFrame();
	// CaseFrame targetCF= targetNode.getDownCableSet().getCaseFrame();
	// Enumeration<RCFP> RCFPs=targetCF.getRelations().elements();
	// while(RCFPs.hasMoreElements()){
	// RCFP rcfp=RCFPs.nextElement();
	// Relation r=rcfp.getRelation();
	// Path path=r.getPath();
	// if(path==null)
	// continue;
	// if(!patHerePossible(sourceNode,targetNode,rcfp))
	// continue;
	// PathTrace pathTrace=new PathTrace();
	// //TODO: pathtrace and context
	// LinkedList<Object[]> pathNs=path.follow(sourceNode,pathTrace
	// ,SNeBR.getCurrentContext());
	//
	// for (int i = 0; i < pathNs.size(); i++) {
	// Node currentNode=(Node) pathNs.get(i)[0];
	// PathTrace currentPathTrace=(PathTrace) pathNs.get(i)[1];
	// LinkedList <Relation> currentRelations= currentPathTrace.getFirst();
	// NodeSet currentNodeSet=new NodeSet();//place?
	// //case 1 or 2
	// if((currentRelations.size()==1&&currentRelations.get(0).equals(r))||(sourceCF.equals(targetCF)))
	// { //TODO: clone?
	// currentNodeSet=sourceNode.getDownCableSet().getDownCable(r.getName()).getNodeSet().Union(new
	// NodeSet());
	// //case 2
	// if(!(sourceCF.equals(targetCF)))
	// {
	// if(!caseFramesCompatibleThroughAdjustability(sourceCF,targetCF)){
	// flag=false;
	// break;
	// }
	// }
	// if(!currentNodeSet.contains(currentNode)){
	// flag=true;
	// currentNodeSet.addNode(currentNode);
	// }
	//
	// }
	// else{
	// //case 3
	// boolean relationAdded=false;
	// if(sourceCF.getRelation(r)==null){
	// sourceCF.getRelations().put(r.getName(), rcfp);
	// relationAdded=true;
	// }
	// else
	// currentNodeSet=sourceNode.getDownCableSet().getDownCable(r.getName()).getNodeSet();
	// //TODO: clone? ^
	// //sourceCF!=targetCF
	// int unusedSize=currentRelations.size();
	// //TODO:
	// int usedSize=0;
	// if(!sourceCF.getRelations().equals(targetCF.getRelations())){
	// LinkedList<Relation> relations= relationDifference(sourceCF,targetCF);
	//
	//
	// for(Relation relation:relations){
	// NodeSet
	// sourceRelationNS=sourceNode.getDownCableSet().getDownCable(relation.getName()).getNodeSet();
	//
	// if(unusedSize!=0&&relation.equals(currentRelations.getFirst())){
	// sourceRelationNS.removeNode(sourceRelationNS.getNode(i));
	// unusedSize--;
	// if(sourceRelationNS.isEmpty()){
	// sourceNode.getDownCableSet().getDownCables().remove(relation.getName());
	// //TODO: clone?
	// sourceCF.getRelations().remove(relation.getName());
	// }
	// }
	// else
	// usedSize+=sourceRelationNS.size();
	//
	// }
	// }
	// if(usedSize-unusedSize>0)
	// if(!caseFramesCompatibleThroughAdjustability(sourceCF, targetCF)){
	// flag=false;
	// break;
	// }
	// if(!currentNodeSet.contains(currentNode)){
	// currentNodeSet.addNode(currentNode);
	// flag=true;
	// }
	// if(relationAdded&&sourceCF.getRelations().containsKey(r.getName()))
	// sourceCF.getRelations().remove(r.getName());
	//
	//
	// }
	//
	// }
	//
	// if(flag&&setUnify(currentNodeSet, ns2, sList, tList, UVBR, rightOrder,
	// sourceBoundTerms, sourceOriginalSize, targetBoundTerms,
	// targetOriginalSize))
	// continue;
	//
	// }
	//
	// return flag;
	// }

	private static boolean caseFramesCompatibleThroughAdjustability(
			CaseFrame adjusted, CaseFrame goal) {
		Enumeration<String> adjustedKeys = adjusted.getRelations().keys();
		Hashtable<String, RCFP> adjustedRelations = adjusted.getRelations();
		Hashtable<String, RCFP> goalRelations = goal.getRelations();
		while (adjustedKeys.hasMoreElements()) {
			String adjustedKey = adjustedKeys.nextElement();
			if (!goalRelations.containsKey(adjustedKey))
				if (adjustedRelations.get(adjustedKey).getAdjust()
						.equals("reduce")) {
					if (adjustedRelations.get(adjustedKey).getLimit() > 0)
						return false;
				} else
					return false;

		}
		return true;
	}

	private static boolean checkRuleCompatibility(MolecularNode sourceNode,
			MolecularNode targetNode) {
		Class sClass = sourceNode.getClass();
		Class tClass = targetNode.getClass();
		if (!RuleNode.class.isAssignableFrom(sClass)
				&& !RuleNode.class.isAssignableFrom(tClass))
			return true;
		if (!sClass.equals(tClass))
			return false;
		if (sClass.equals(AndOrNode.class)) {
			int ti = ((AndOrNode) targetNode).getMin();
			int tj = ((AndOrNode) targetNode).getMax();
			int tn = ((AndOrNode) targetNode).getArg();
			int si = ((AndOrNode) sourceNode).getMin();
			int sj = ((AndOrNode) sourceNode).getMax();
			int sn = ((AndOrNode) sourceNode).getArg();
			if (tn > sn)
				return false;
			if (ti != (Math.max(si - (sn - tn), 0)))
				return false;
			if (tj != sj)
				return false;
			return true;
		} else if (sClass.equals(AndNode.class)) {
			int sourceAndant = ((AndNode) sourceNode).getant();
			int targetAndant = ((AndNode) targetNode).getant();
			int sourceCq = ((AndNode) sourceNode).getCq();
			int targetCq = ((AndNode) targetNode).getCq();
			// TODO: dcq
			if (sourceAndant != targetAndant)
				return false;
			if (targetCq > sourceCq)
				return false;

			return true;
		} else if (sClass.equals(OrNode.class)) {
			int sourceAnt = ((OrNode) sourceNode).getAnt();
			int targetAnt = ((OrNode) targetNode).getAnt();
			int sourceCq = ((OrNode) sourceNode).getCq();
			int targetCq = ((OrNode) targetNode).getCq();
			// TODO: dcq
			if (sourceAnt != targetAnt)
				return false;
			if (targetCq > sourceCq)
				return false;
			return true;
		} else if (sClass.equals(ThreshNode.class)) {
			int ti = ((ThreshNode) targetNode).getThresh();
			int tj = ((ThreshNode) targetNode).getThreshMax();
			// TODO:correct tj
			int tn = ((ThreshNode) targetNode).getArg();
			int si = ((ThreshNode) sourceNode).getThresh();
			int sj = ((ThreshNode) sourceNode).getThreshMax();
			// TODO:correct sj
			int sn = ((ThreshNode) sourceNode).getArg();
			if (tn > sn)
				return false;
			if (ti != (Math.max(si - (sn - tn), 0)))
				return false;
			if (tj != sj)
				return false;
			return true;

		}

		else if (sClass.equals(NumericalNode.class)) {
			// TODO
		}
		return true;
	}

	// returns relationName,size pairs
	private static LinkedList<Object[]> downCableDifference(MolecularNode s,
			MolecularNode t) {
		LinkedList<Object[]> relationNodePairs = new LinkedList<Object[]>();
		RCFP[] sRelations = new RCFP[s.getDownCableSet().getCaseFrame()
				.getRelations().size()];
		sRelations = s.getDownCableSet().getCaseFrame().getRelations().values()
				.toArray(sRelations);
		RCFP[] tRelations = new RCFP[t.getDownCableSet().getCaseFrame()
				.getRelations().size()];
		tRelations = t.getDownCableSet().getCaseFrame().getRelations().values()
				.toArray(tRelations);
		Hashtable<String, DownCable> sDCs = s.getDownCableSet().getDownCables();
		Hashtable<String, DownCable> tDCs = t.getDownCableSet().getDownCables();
		for (int i = 0; i < sRelations.length; i++) {
			boolean found = false;
			for (int j = 0; j < tRelations.length; j++) {
				if (sRelations[i].equals(tRelations[j])) {
					found = true;
					int sns = sDCs.get(sRelations[i]).getNodeSet().size();
					int tns = tDCs.get(tRelations[j]).getNodeSet().size();
					if (sns > tns)
						relationNodePairs.add(new Object[] {
								sRelations[i].getRelation().getName(),
								sns - tns });
					break;
				}
			}
			if (!found)
				relationNodePairs.add(new Object[] {
						sRelations[i].getRelation().getName(),
						sDCs.get(sRelations[i]).getNodeSet().size() });
		}

		return relationNodePairs;
	}

	private static boolean patHerePossible(MolecularNode sourceNode,
			MolecularNode targetNode, RCFP pathDfRelation) {
		Hashtable<String, RCFP> sourceRelations = sourceNode.getDownCableSet()
				.getCaseFrame().getRelations();
		LinkedList<Relation> frels = pathDfRelation.getRelation().getPath()
				.firstRelations();

		return false;
	}

	public static Node vereNUVBR(VariableNode n, Substitutions sourceR,
			Substitutions sourceS, Substitutions targetR, Substitutions targetS) {

		Stack<VariableNode> path = source(n, sourceR, targetR);
		VariableNode v = path.pop();
		Node bindingNode = null;
		if (!sourceR.isBound(v)) {
			bindingNode = v;
			// done
			sourceR.update(sourceR.getBindingByVariable(v), v);
		} else if (sourceR.value(v).getSyntacticType().equals("Base")
				|| sourceR.value(v).getSyntacticType().equals("Closed")) {
			bindingNode = sourceR.value(v);
			// done
			sourceR.update(sourceR.getBindingByVariable(v), v);
			if (sourceS.isBound(v))
				sourceS.update(sourceS.getBindingByVariable(v), bindingNode);
			else
				sourceS.insert(new Binding(v, bindingNode));
		} else if (sourceR.value(v).getSyntacticSuperClass().equals("Pattern")) {
			// done
			sourceR.update(sourceR.getBindingByVariable(v), v);
			MolecularNode y = (MolecularNode) bindingNode;

			// TODO:stack loop start on x

			while (!path.isEmpty()) {
				VariableNode x = path.pop();
				if (sourceR.isBound(x))
					sourceS.insertOrUpdate(new Binding(x, x));
			}
			// stack loop end
			bindingNode = termVERENUVBR(y, sourceR, targetR, sourceS, targetS);
			if (sourceS.isBound(v))
				sourceS.update(sourceS.getBindingByVariable(v), bindingNode);
			else
				sourceS.insert(new Binding(v, bindingNode));

		} else if (!sourceS.isBound(v)) {
			bindingNode = v;

		} else if (sourceS.value(v).equals(v)) {
			return null;
		} else {
			bindingNode = sourceS.value(v);

		}

		// stack loop for x
		while (!path.isEmpty()) {
			VariableNode x = path.pop();
			if (sourceS.isBound(x))
				sourceS.update(sourceS.getBindingByVariable(x), bindingNode);
			else
				sourceS.insert(new Binding(x, bindingNode));
		}
		// stack loop end
		return bindingNode;
	}

	public static MolecularNode termVERENUVBR(MolecularNode n,
			Substitutions sourceR, Substitutions targetR,
			Substitutions sourceS, Substitutions targetS) {
		DownCableSet dcset = n.getDownCableSet();
		Hashtable<String, DownCable> dcs = dcset.getDownCables();
		String[] dckeys = dcs.keySet().toArray(new String[dcs.size()]);

		for (String dckey : dckeys) {
			DownCable dc = dcs.get(dckey);
			Relation relation = dc.getRelation();
			NodeSet ns = dc.getNodeSet();

			for (int i = 0; i < ns.size(); i++) {
				Node currentNode = ns.getNode(i);

				if (currentNode.getSyntacticType().equals("Variable")) {
					if (sourceR.value((VariableNode) currentNode).equals(
							currentNode)) {
						if (sourceS.value((VariableNode) currentNode).equals(
								currentNode)) {
							return null;
						} else {
							// replace by s(currentNode)
						}
					} else if (sourceR.isBound((VariableNode) currentNode)) {
						// replace by VERE(currentNode)
					}
				} else if (currentNode.getSyntacticSuperClass().equals(
						"Molecular")) {
					// replace by termvere(currentNode)
				}

			}
		}

		return null;
	}

	public static Stack<VariableNode> source(VariableNode n,
			Substitutions sourceR, Substitutions targetR) {
		// TODO:UVBR Rloop
		Stack<VariableNode> path = new Stack<VariableNode>();
		Substitutions r = sourceR;
		VariableNode current = n;
		while (r.value(current).getSyntacticType().equals("Variable")
				&& !r.value(current).equals(n)) {
			path.push(current);
			current = (VariableNode) r.value(current);
			r = r.equals(sourceR) ? targetR : sourceR;
		}

		return path;
	}

	private static boolean uvbrConflict(NodeSet ns1, NodeSet ns2, Node n, Node m) {
		return (n.getSyntacticType().equals("Variable") || m.getSyntacticType()
				.equals("Variable")) && (ns1.contains(m) || ns2.contains(n));
	}

	public static boolean UVBR() {
		return UVBR;
	}

	public static void setUVBR(boolean value) {
		UVBR = value;
	}

	public static void main(String[] args) {

	}

}
