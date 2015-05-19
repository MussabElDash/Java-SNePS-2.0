package sneps.match;

import sneps.Nodes.Node;
import sneps.Nodes.VariableNode;

public interface Substitutions {

	public boolean isNew();
	public void putIn(Binding mb);
	public boolean isCompatible(Binding mb);
	public void update(Binding mb , Node mn);
    public boolean isBound(VariableNode mv);
    public boolean isValue(Node mn);
    public VariableNode srcNode(Node mn);
    public Binding getBindingByVariable(VariableNode mv);
    public Binding getBindingByNode(Node mn);
    public boolean isMember(Binding mb);
    public boolean isSubSet(Substitutions s);
    public boolean isEqual(Substitutions s);
    public Substitutions union (Substitutions s);
    public void unionIn (Substitutions s);
    public Substitutions restrict(VariableNode [] ns);
    public Node term(VariableNode mv);
    public int cardinality();
    public Binding choose();
    public Substitutions others();
    public Node value(VariableNode n);
    public Substitutions insert(Binding m);
    public boolean isCompatible(Substitutions s);
    public Binding getBinding(int x);
    public Substitutions[] split();
    public void clear();
    public void insert(Substitutions s);
    public boolean sub(String x, String y);
    public String toString();
    public int termID(int variableID);
    public void insertOrUpdate(Binding mb);

	
	
	
}
