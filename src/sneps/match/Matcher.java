package sneps.match;

import java.util.LinkedList;

import sneps.DownCable;
import sneps.DownCableSet;
import sneps.MolecularNode;
import sneps.Node;
import sneps.NodeSet;
import sneps.VariableNode;

public class Matcher {

	public boolean hERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourceList,LinkedList<Substitutions> targetList,boolean UVBR, boolean rightOrder) {
		if (!compatible(sourceNode, targetNode))
			return false;
		if (sourceNode.getSyntacticType() == "Variable")
			if (!VARHERE(sourceNode, targetNode, sourceList,targetList,UVBR,rightOrder))
				return false;
			else if( targetNode.getSyntacticType() == "Variable") 
				if (!VARHERE(sourceNode, targetNode, sourceList,targetList,UVBR,rightOrder))
					return false;	
		 else if (sourceNode.getSyntacticType() == "Molecular"
				&& targetNode.getSyntacticType() == "Molecular")
		{
			MolecularNode n1 = (MolecularNode) sourceNode;
			MolecularNode n2 = (MolecularNode) targetNode;
			DownCableSet cs1 = n1.getDownCableSet();
			DownCableSet cs2 = n2.getDownCableSet();
			if (cs1.getCaseFrame() != cs2.getCaseFrame())
				return false;
			else {
				DownCable[] dcs1 = (DownCable[]) cs1.getDownCables().values()
						.toArray();
				DownCable[] dcs2 = (DownCable[]) cs2.getDownCables().values()
						.toArray();

				for (DownCable cable1 : dcs1)
					for (DownCable cable2 : dcs2)
						if (cable1.getRelation().equals(cable2.getRelation())) {
							int size1 = cable1.getNodeSet().size();
							int size2 = cable2.getNodeSet().size();
							String adjust = cable1.getRelation().getAdjust();

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
									cable2.getNodeSet(), sourceList,targetList,UVBR, rightOrder))
								return false;
						}

			}

		}

		return true;
	}

	public boolean VARHERE(Node sourceNode, Node targetNode,
			LinkedList<Substitutions> sourcelist,LinkedList<Substitutions> targetList,boolean UVBR,boolean rightOrder) {

		boolean unifiable = false;
		LinkedList<Substitutions> variableList;
		LinkedList<Substitutions> bindingList;
		VariableNode variableNode;
		Node bindingNode;
		if(rightOrder){
			variableList=sourcelist;
			bindingList=targetList;
			variableNode=(VariableNode) sourceNode;
			bindingNode=targetNode;
		}
		else{
			variableList=targetList;
			bindingList=variableList;
			variableNode=(VariableNode) targetNode;
			bindingNode=sourceNode;
		}
		
		for (int i = 0; i < variableList.size(); i++) {
			Substitutions currentVSub = variableList.removeFirst();
            Substitutions currentBSub = bindingList.removeFirst();
			if (!currentVSub.isBound(variableNode)) {
				if(bindingNode.getSyntacticType()=="Variable" && UVBR)
                        	if(currentBSub.isBound((VariableNode) bindingNode)||currentVSub.isValue(bindingNode))
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
			LinkedList<Substitutions> sList,LinkedList<Substitutions> tList,boolean UVBR, boolean rightOrder) {
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
					if (UVBR&&uvbrConflict(ns1, ns2, n1, n2))
						continue;
						if (hERE(n1, n2, newSList,newTList,UVBR, rightOrder))
							if (setUnify(others1, others2, newSList,newTList,UVBR, rightOrder)) {
								sList.addAll(newSList);
								tList.addAll(newTList);
								unifiable = true;
							}

				}
			}

		}

		return unifiable;
	}
	
	public Node vere(VariableNode n,Substitutions sourceR,Substitutions targetR,Substitutions sourceS,Substitutions targetS){
	Node bindingNode;
	if(sourceR.isBound(n)){
		Node RbindingNode=sourceR.term(n);
		if(RbindingNode.getSyntacticType()=="Base"||RbindingNode.getSyntacticType()=="Closed"){
	bindingNode=RbindingNode;
	sourceS.putIn(new Binding(n,RbindingNode));
	sourceR.update(sourceR.getBindingByVariable(n),n);

	//TODO:add appropriate getters and setters in subs
	
		}
		else if(RbindingNode.getSyntacticType()=="Pattern"){
/*done*/	sourceR.update(sourceR.getBindingByVariable(n),n);
/*loop*/    sourceS.update(sourceS.getBindingByVariable(n),n);
            bindingNode=termVere((MolecularNode) RbindingNode,sourceR,targetR,sourceS,targetS); 
/*loop*/    sourceS.update(sourceS.getBindingByVariable(n),bindingNode);
            
		}
		else if(sourceS.isBound(n)){
			if(sourceS.term((VariableNode) n).equals(n))
/*fail loop*/	return null;
			else bindingNode=sourceS.term((VariableNode) n);
			
		}
		else
			bindingNode=sourceR.term(n);
	}
	else{
		sourceR.insert(new Binding(n, n));
		bindingNode=n;
		sourceS.insert(new Binding(n,bindingNode));
	}
	
	return bindingNode;
	}
	
	public Node termVere(MolecularNode n,Substitutions sourceR,Substitutions targetR,Substitutions sourceS,Substitutions targetS){
	MolecularNode changedNode=null;
	
	
	return changedNode;
	}
	

//	public Node VERE(VariableNode n, Substitutions r, Substitutions s) {
//
//		Stack<VariableNode> path = source(n, r);
//		VariableNode v = path.pop();
//		Node bindingNode;
//		if (!r.isBound(v)) {
//			bindingNode = v;
//			// done
//			r.update(r.getBindingByVariable(v), v);
//		} else if (r.value(v).getSyntacticType() == "Base") {
//			bindingNode = r.value(v);
//			// done
//			r.update(r.getBindingByVariable(v), v);
//			if (s.isBound(v))
//				s.update(s.getBindingByVariable(v), bindingNode);
//			else
//				s.insert(new Binding(v, bindingNode));
//		} else if (r.value(v).getSyntacticSuperClass() == "Molecular") {
//			// done
//			r.update(r.getBindingByVariable(v), v);
//			MolecularNode y = (MolecularNode) bindingNode;
//
//			// TODO:stack loop start on x
//
//			while (!path.isEmpty()) {
//				/* TODO: s(x)==loop */
//			}
//			// stack loop end
//			bindingNode = termVERE(y, r, s);
//			if (s.isBound(v))
//				s.update(s.getBindingByVariable(v), bindingNode);
//			else
//				s.insert(new Binding(v, bindingNode));
//
//		} else if (!s.isBound(v)) {
//			bindingNode = v;
//
//		} else if (v.Sloop()) {
//			// fail
//		} else {
//			bindingNode = s.value(v);
//
//		}
//
//		// stack loop for x
//		while (!path.isEmpty()) {
//			VariableNode x = path.pop();
//			if (s.isBound(x))
//				s.update(s.getBindingByVariable(x), bindingNode);
//			else
//				s.insert(new Binding(x, bindingNode));
//		}
//		// stack loop end
//		return bindingNode;
//	}
//
//	public Node termVERE(MolecularNode n, Substitutions r, Substitutions s) {
//		DownCableSet dcset = n.getDownCableSet();
//		Hashtable<String, DownCable> dcs = dcset.getDownCables();
//		String[] dckeys = dcs.keySet().toArray(new String[dcs.size()]);
//
//		for (String dckey : dckeys) {
//			DownCable dc = dcs.get(dckey);
//			Relation relation = dc.getRelation();
//			NodeSet ns = dc.getNodeSet();
//
//			for (int i = 0; i < ns.size(); i++) {
//				Node currentNode = ns.getNode(i);
//
//				if (currentNode.getSyntacticType() == "Variable") {
//					if (currentNode.Rdone()) {
//						if (currentNode.Sloop()) {
//							// fail
//						} else {
//							// replace by s(currentNode)
//						}
//					} else if (r.isBound((VariableNode) currentNode)) {
//						// replace by VERE(currentNode)
//					}
//				} else if (currentNode.getSyntacticSuperClass() == "Molecular") {
//					// replace by termvere(currentNode)
//				}
//
//			}
//		}
//
//		return null;
//	}
//
//	public Stack<VariableNode> source(VariableNode n, Substitutions r) {
//		// TODO:UVBR Rloop
//		Stack<VariableNode> path = new Stack<VariableNode>();
//
//		VariableNode current = n;
//		while (r.value(current).getSyntacticType() == "Variable"
//				&& !r.value(current).equals(n)) {
//			path.push(current);
//			current = (VariableNode) r.value(current);
//
//		}
//
//		return path;
//	}

	
	public boolean uvbrConflict(NodeSet ns1, NodeSet ns2, Node n, Node m) {
		return (n.getSyntacticType() == "Variable" || m.getSyntacticType() == "Variable")
				&& (ns1.contains(m) || ns2.contains(n));
	}
	
	
	public static void main(String[] args){
		
	}
	
}
