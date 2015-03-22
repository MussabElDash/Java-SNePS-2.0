package sneps.Tests;

import java.util.Arrays;
import java.util.LinkedList;

import sneps.CaseFrame;
import sneps.RCFP;
import sneps.Relation;
import sneps.Cables.DownCable;
import sneps.Cables.DownCableSet;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.VariableNode;

public class MolBuildTests {
	
	private static Object[][] turnIntoRelNodeSet(Object[][] array){
		Object[][] temp = new Object[array.length][];
		for (int i = 0; i < array.length; i++) {
			  temp[i] = Arrays.copyOf(array[i], array[i].length);
		}
		Object[][] temp2 = new Object[array.length][];
		for (int i = 0; i < array.length; i++) {
			  temp2[i] = Arrays.copyOf(array[i], array[i].length);
		}
		int relcounter = 0;
		for(int i = 0; i < temp.length; i++){
			if(temp[i][0] != null){
				Relation r = (Relation) temp[i][0];
				relcounter++;
				if (i+1 < temp.length){
					for(int j = i+1; j < temp.length; j++){
						if(temp[j][0] != null){
							if(((Relation)temp[j][0]).equals(r)){
								temp[j][0]= null;
							}
						}
					}
				}
			}
		}
		int addcounter = 0;
		Object[][] result = new Object[relcounter][2];
		for(int i = 0; i < temp2.length; i++){
			if(temp2[i][0] != null){
				Relation r = (Relation) temp2[i][0];
				NodeSet ns = new NodeSet();
				if(!temp2[i][1].getClass().getSimpleName().equals("NodeSet")){
					ns.addNode((Node)temp2[i][1]);
				}
				if (i+1 < temp2.length){
					for(int j = i+1; j < temp2.length; j++){
						if(temp2[j][0] != null){
							if(((Relation)temp2[j][0]).equals(r)){
								if(!temp2[j][1].getClass().getSimpleName().equals("NodeSet")){
									ns.addNode((Node)temp2[j][1]);
								}
								temp2[j][0]= null;
							}
						}
					}
				}
				result[addcounter][0] = r;
				result[addcounter][1] = ns;
				addcounter++;
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		Relation member = new Relation("member", "Entity", "reduce", 1);
		Relation cl = new Relation("class", "Entity", "none", 1);
		Relation reducible = new Relation("r", "Entity", "none", 1);
		VariableNode node1 = new VariableNode("Entity", "x");
		VariableNode node2 = new VariableNode("Entity", "y");
		Node node3 = new Node("Base", "Entity", "Dog");
		NodeSet ns = new NodeSet();
		ns.addNode(node1);
		ns.addNode(node2);
		NodeSet ns1 = new NodeSet();
		ns1.addNode(node3);
		DownCable dc = new DownCable(member, ns);
		DownCable dc1 = new DownCable(cl, ns1);
		LinkedList<DownCable> dList = new LinkedList<DownCable>();
		dList.add(dc);
		dList.add(dc1);
		RCFP prop = new RCFP(member, "none", 2);
		RCFP prop2 = new RCFP(cl, "none", 1);
		LinkedList<RCFP> propList = new LinkedList<RCFP>();
		propList.add(prop);
		propList.add(prop2);
		CaseFrame cf = new CaseFrame("Entity", propList);
		DownCableSet dcs = new DownCableSet(dList, cf);
		Object[][] relNode = new Object[4][2];
		NodeSet empty = new NodeSet();
		relNode[0][0] = member;
		relNode[0][1] = node1;
		relNode[2][0] = member;
		relNode[2][1] = node2;
		relNode[3][0] = cl;
		relNode[3][1] = node3;
		relNode[1][0] = reducible;
		relNode[1][1] = empty;
//		System.out.println("checking" + dcs.size());
//		Pattern p = new Pattern("M1", dcs);
//		Entity e = new Entity();
//		PatternNode pNode = new PatternNode(p, e);
//		nodes.put(pNode.getIdentifier(), pNode);
//		caseFrames.put(cf.getId(), cf);
//		molecularNodes.put(cf.getId(), new NodeSet());
//		molecularNodes.get(cf.getId()).addNode(pNode);
//		System.out.println("start");
//		System.out.println(downCableSetExists(relNode));
		Object[][] relNodeSet = turnIntoRelNodeSet(relNode);
		System.out.println(relNodeSet.length);
		System.out.println(relNodeSet[0][0]);
		System.out.println(relNodeSet[0][1]);
		System.out.println(relNodeSet[1][0]);
		System.out.println(relNodeSet[1][1]);
		System.out.println(relNodeSet[2][0]);
		System.out.println(relNodeSet[2][1]);
	}
	
///////// The old TurnIntoRelNodeSet method before adding the handling for empty NodeSets representing red to 0 //////
//	
//	private static Object[][] turnIntoRelNodeSet(Object[][] array){
//		Object[][] temp = new Object[array.length][];
//		for (int i = 0; i < array.length; i++) {
//			  temp[i] = Arrays.copyOf(array[i], array[i].length);
//		}
//		Object[][] temp2 = new Object[array.length][];
//		for (int i = 0; i < array.length; i++) {
//			  temp2[i] = Arrays.copyOf(array[i], array[i].length);
//		}
//		int relcounter = 0;
//		for(int i = 0; i < temp.length; i++){
//			if(temp[i][0] != null){
//				Relation r = (Relation) temp[i][0];
//				relcounter++;
//				if (i+1 < temp.length){
//					for(int j = i+1; j < temp.length; j++){
//						if(temp[j][0] != null){
//							if(((Relation)temp[j][0]).equals(r)){
//								temp[j][0]= null;
//							}
//						}
//					}
//				}
//			}
//		}
//		int addcounter = 0;
//		Object[][] result = new Object[relcounter][2];
//		for(int i = 0; i < temp2.length; i++){
//			if(temp2[i][0] != null){
//				Relation r = (Relation) temp2[i][0];
//				NodeSet ns = new NodeSet();
//				ns.addNode((Node)temp2[i][1]);
//				if (i+1 < temp2.length){
//					for(int j = i+1; j < temp2.length; j++){
//						if(temp2[j][0] != null){
//							if(((Relation)temp2[j][0]).equals(r)){
//								ns.addNode((Node)temp2[j][1]);
//								temp2[j][0]= null;
//							}
//						}
//					}
//				}
//				result[addcounter][0] = r;
//				result[addcounter][1] = ns;
//				addcounter++;
//			}
//		}
//		return result;
//	}

}
