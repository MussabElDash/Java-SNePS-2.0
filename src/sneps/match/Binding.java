/**
 * @(#)Binding.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/3/14
 */
package sneps.match;

import sneps.Nodes.Node;
import sneps.Nodes.VariableNode;



public class Binding
{
        private Node node;
        private VariableNode variable;
        /**
         *Creates new binding from variable and node
         *@param node
         *@param variablear
         */
    public Binding(VariableNode variable,Node node)
    {
        this.node=node;
        this.variable=variable;
    }
   
    /**
         *returns the variable of the binding
         *@return variable
         */
    public VariableNode getVariable()
    {
        return variable;
    }
    /**
         *returns the node of the binding
         *@return node
         */
    public Node getNode()
    {
        return node;
    }
   
    /**
         *Check if binding is equal to this binding
         *@param binding Binding
         *@return true if equal false otherwise
         */
    public boolean isEqual(Binding binding)
    {
        if(this.getNode() == binding.getNode() &&
                        this.getVariable() == binding.getVariable())
                return true;
                        return false;
    }
   
    /**
         *Set the value of the variable of the binding with m
         *@param m the new variable
         */
    public void setVariable(VariableNode m)
    {
        variable=m;
    }
   
    /**
         *Set the value of the node of the binding with m
         *@param m the new node
         */
    public void setNode(Node m)
    {
        node=m;
    }
   
    /**
     * Create a copy of this binding
     */
    public Binding clone()
    {
        return new Binding(variable,node);
    }
}
