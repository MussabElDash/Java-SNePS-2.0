package SNePSLOG;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.Nodes.Node;
import sneps.Nodes.VariableNode;
import sneps.SyntaticClasses.Pattern;
import java_cup.runtime.Symbol;

public class AP {

private static Hashtable<Integer,String> variables = new Hashtable<Integer, String>();
private static int  count = 0 ;
private static ArrayList<String> symbols = new ArrayList<String>();
private static ArrayList<String> termSets = new ArrayList<String>();
private static String [] list = new String [10000];
private static int mode = 1;
	public static void h() {
		System.out.println("hi");
	}

	public static boolean find(String x){
		for(int i=0;i<list.length;i++){
			if(list[i] == x){
				return true;
			}
		}
		return false;


	}
	/*
	********

	
	********
	*/
	public static void addSymbolToSymbolSequence(String s){
		symbols.add(s);

	}

	public static void addTerm(String s){
		termSets.add(s);
	}


	public static ArrayList<String> getSymbols(){
		return symbols;

	}

	public static ArrayList<String> getTerms(){
		return termSets;
	}
	
	public static int getMode(){
		return mode;
	}

	public static void setMode(int newMode){

		if(newMode >=1 && newMode <=3){
		mode = newMode;	
		}
		else {
			System.out.println("Invalid MODE set!");
		}
		
	}

	public static String[] getList(){
		return list;
	}

	public static void add(String x){
		if(!find(x)){
		list[count++]=x;	
		}
		else {
			System.out.print("already there!");
		}
		
	}
	public static void addToVariables(int i,String s){
		variables.put(new Integer(i), s);
	}

	public static String build(String x){

		add(x);
		return x;



	}
	
	public static void printBuildingNodeResults(Node n, String name){
		System.out.println("");
		if(n != null){
			System.out.println("Node " + name + " was successfully built ... ");
			System.out.println("");
			System.out.println("Current count of the nodes in the system: " + Node.getCount());
			System.out.println("");
			System.out.println("Checking its paramters: ");
			System.out.println("-----------------------");
			System.out.println("ID: " + n.getId());
			System.out.println("Syntactic type: " + n.getSyntacticType());
			System.out.println("Semantic type: " + n.getSemanticType());
			System.out.println("Identifier: " + n.getIdentifier());
			if(n.getSyntacticType().equals("Pattern")){
				System.out.println("");
				System.out.println("The free variables dominated by this node: ");
				System.out.println("------------------------------------------");
				LinkedList<VariableNode> freeVars =  ((Pattern)n.getSyntactic()).getFreeVariables();
				for(int i = 0; i < freeVars.size(); i++){
					System.out.println((i + 1) + ". " + freeVars.get(i).getIdentifier());
				}
			}
		}
		else{
			System.out.println("The Node named, " + name + ", was not successfully built ... ");
		}
		System.out.println("");
		System.out.println("...................................................................................." +
				"....................................");
		System.out.println("...................................................................................." +
				"....................................");
		
	}
	
	public static ArrayList<String> reverse(ArrayList<String> list) {
	    for(int i = 0, j = list.size() - 1; i < j; i++) {
	        list.add(i, list.remove(j));
	    }
	    return list;
	}

	public static String convertArrayToString(ArrayList<String> list){
		String strResult ="";
		for (int i = 0; i < list.size(); i++) {
			strResult+=list.get(i)+" ";
			}
		return strResult;
	}
	public static void main(String[] args) {

		String inFile = "Sample1.in";

		if (args.length > 1) {
			inFile = args[0];
		}

		try {
			FileInputStream fis = new FileInputStream(inFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"Sample1.out"));

			parser parser = new parser(new Lexer(dis));
			Symbol res = parser.parse();

			String value = (String) res.value;
			writer.write(value);

			System.out.println("Done");
			

			fis.close();
			bis.close();
			dis.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
