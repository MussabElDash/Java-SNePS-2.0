/**
 * @className PathTrace.java
 * 
 * @ClassDescription This class represents a pair of a path followed along with its supports.
 * 
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps;

import java.util.LinkedList;

import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Paths.AndPath;
import sneps.Paths.ComposePath;
import sneps.Paths.EmptyPath;
import sneps.Paths.FUnitPath;
import sneps.Paths.Path;

public class PathTrace {
	
	/**
	 *  The path followed to reach the node that have this
	 *  path trace.
	 */
	private Path path;
	
	/**
	 * The node set that contains the nodes that represent the 
	 * supports collected along the path.
	 */
	private NodeSet supports;
	
	/**
	 * The constructor of the class. It initializes an empty 
	 * path and an empty node set.
	 */
	public PathTrace(){
		this.path = new EmptyPath();
		this.supports = new NodeSet();
	}
	
	/**
	 * This method overrides the clone method inherited
	 * from the Object class.
	 * 
	 * @return a new path trace that is exactly
	 * 	the same as the current path trace. (same 
	 * 	path and same supports).
	 */
	@Override
	public PathTrace clone(){
		PathTrace pt = new PathTrace();
		pt.path = this.path.clone();
		for (int i = 0; i < this.supports.size(); i++){
			pt.addSupport(this.supports.getNode(i));
		}
		return pt;
	}
	
	/**
	 * 
	 * @return the path of the current path trace.
	 */
	public Path getPath(){
		return this.path;
	}
	
	/**
	 * 
	 * @return the node set that contains the supports
	 * 	of the current path trace.
	 */
	public NodeSet getSupports(){
		return this.supports;
	}
	
	/**
	 * This method adds a new support (node) to
	 * the node set of supports of the current 
	 * path trace.
	 * 
	 * @param node
	 * 			the new support that will be added.
	 */
	public void addSupport(Node node){
		this.supports.addNode(node);
	}
	
	/**
	 * This method adds multiple supports (nodes)
	 * to the node set of supports of the current 
	 * path trace
	 * 
	 * @param supports
	 * 			the node set that contains the new 
	 * 			supports that will be added.
	 */
	public void addAllSupports(NodeSet supports){
		this.supports.addAll(supports);
	}
	
	/**
	 * This method adds (appends) a given path to the path of
	 * the current path trace.
	 * 
	 * @param path
	 * 			the given path that will be added to the path
	 * 			of the current path trace.
	 */	
	public void compose(Path path){
		if(path.getClass().getSimpleName().equals("EmptyPath"))
			return;
		if(this.path.getClass().getSimpleName().equals("EmptyPath")){
			this.path = path;
			return;
		}
		LinkedList<Path> pList = new LinkedList<Path>();
		if(this.path.getClass().getSimpleName().equals("ComposePath"))
			pList.addAll(((ComposePath)this.path).getPaths());
		else
			pList.add(this.path);
		if(path.getClass().getSimpleName().equals("ComposePath"))
			pList.addAll(((ComposePath)path).getPaths());
		else
			pList.add(path);
		this.path = new ComposePath(pList);	
	}
	
	/**
	 * This method sets the path of the current path trace to 
	 * be equal to the and path of the current path and the
	 * given path
	 * 
	 * @param path
	 * 			the given path that will be anded with the current
	 * 			path to form an and path.
	 */
	public void and(Path path){
		if(path.getClass().getSimpleName().equals("EmptyPath"))
			return;
		if(this.path.getClass().getSimpleName().equals("EmptyPath")){
			this.path = path;
			return;
		}
		LinkedList<Path> pList = new LinkedList<Path>();
		if(this.path.getClass().getSimpleName().equals("AndPath"))
			pList.addAll(((AndPath)this.path).getPaths());
		else
			pList.add(this.path);
		if(path.getClass().getSimpleName().equals("AndPath"))
			pList.addAll(((AndPath)path).getPaths());
		else
			pList.add(path);
		this.path = new AndPath(pList);
	}
	
	/**
	 * This method overrides the equals method inherited
	 * from the Object class. 
	 * 
	 * @return true if the given object is a path trace
	 *  and has the same path and supports as the current
	 *  path trace, and false otherwise. 
	 */
	@Override
	public boolean equals(Object obj){
		// TODO why not use instanceOF for checking instead of this?????
		if (! obj.getClass().getSimpleName().equals("PathTrace"))
			return false;
		PathTrace pt = (PathTrace) obj;
		if(! this.supports.equals(pt.getSupports()))
			return false;
		if(! this.path.equals(pt.getPath()))
			return false;
		return true;
	}
	
	 
    /**
     * @return a LinkedList of relations which are the first 
     * 	relations on the path trace
     */
    public LinkedList<Relation> getFirst()
    {
            LinkedList<Path> l = getFirst(this.path);
            LinkedList<Relation> list = new LinkedList<Relation>();
            for(int i=0;i<l.size();i++)
            {
                    if(l.get(i).getClass().getSimpleName().equals("FUnitPath"))
                            list.add(((FUnitPath)l.get(i)).getRelation());
            }
            for(int i=0;i<list.size()-1;i++)
            {
                    Relation r = list.get(i);
                    for(int j=i+1;j<list.size();j++)
                    {
                            if(r.equals(list.get(j)))
                            {
                                    list.remove(j);
                                    j--;
                            }
                    }
            }
            return list;
    }
   
    /**
     * @param path a path to get its first relation
     * 
     * @return a LinkedList of paths
     */
    private LinkedList<Path> getFirst(Path path)
    {
            if(path.getClass().getSimpleName().equals("FUnitPath"))
            {
                    LinkedList<Path> l = new LinkedList<Path>();
                    l.add(path);
                    return l;
            }
            if(path.getClass().getSimpleName().equals("BUnitPath"))
            {
                    LinkedList<Path> l = new LinkedList<Path>();
                    l.add(path);
                    return l;
            }
            if(path.getClass().getSimpleName().equals("EmptyPath"))
            {
                    return new LinkedList<Path>();
            }
            if(path.getClass().getSimpleName().equals("ComposePath"))
            {
                    for(int i=0;i<((ComposePath) path).getPaths().size();i++)
                    {
                            LinkedList<Path> l = getFirst(((ComposePath) path).getPaths().get(i));
                            if(! l.isEmpty())
                                    return l;
                    }
            }
            if(path.getClass().getSimpleName().equals("AndPath"))
            {
                    LinkedList<Path> list = new LinkedList<Path>();
                    for(int i=0;i<((AndPath) path).getPaths().size();i++)
                    {
                            list.addAll(getFirst(((AndPath) path).getPaths().get(i)));
                    }
                    return list;
            }
           
            return null;
    }
	
}
