/**
 * @(#)RuleUseInfoSet.java
 *
 *
 * @author Mohamed Karam Gabr
 * @version 1.00 2010/4/6
 */
package snip.Rules.DataStructures;

import java.util.Vector;

public class RuleUseInfoSet
{
	private Vector<RuleUseInfo> rs;
	
	/**
	 * Create a new empty rule use info set
	 */
	public RuleUseInfoSet()
	{
		rs = new Vector<RuleUseInfo> ();
	}
	
	/**
	 * Check is the rule use info set is new or not 
	 * @return true if new otherwise false
	 */
	public boolean isNew()
	{
		return rs.isEmpty();
	}
	
	/**
	 * Add r to the rule use info set
	 * @param r rule use info
	 */
	public void putIn(RuleUseInfo r)
	{
		rs.add(r);
	}
	
	/**
	 * Return the index of r in the rule use info set, if r is not in the rule use 
	 * info set return -1 (used in Sindexing only)
	 * @param r RuleUseInfo
	 * @return int
	 */
	public int getIndex(RuleUseInfo r)
	{
		for(int i=0;i<rs.size();i++)
		{
			if(r.getSub().isEqual(rs.get(i).getSub()))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Return the rule use info number x in the rule use info set
	 * @param x int
	 * @return RuleUseInfo
	 */
	public RuleUseInfo getRuleUseInfo(int x)
	{
		return rs.get(x);
	}
	
	/**
	 * Remove the rule use info number x from the rule use info set
	 * @param x int
	 */
	public void remove(int x)
	{
		rs.remove(x);
	}
	
	/**
	 * Combine rui with the rule use info set
	 * @param rui RuleUseInfo
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet combine(RuleUseInfo rui)
	{
		RuleUseInfoSet res=new RuleUseInfoSet();
		for(int i=0;i<rs.size();i++)
		{
			RuleUseInfo tmp=rui.combine(rs.get(i));
			if(tmp!=null)
				res.putIn(tmp);
		}
		return res;
	}
	
	/**
	 * Combine ruis with the rule use info set
	 * @param ruis RuleUseInfoSet
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet combine(RuleUseInfoSet ruis)
	{
		RuleUseInfoSet res=new RuleUseInfoSet();
		for(int i=0;i<this.rs.size();i++)
		{
			RuleUseInfoSet temp=ruis.combine(this.rs.get(i));
			for(int j=0;j<temp.rs.size();j++)
			{
				res.putIn(temp.rs.get(j));
			}
		}
		return res;
	}
	
	/**
	 * insert rui in the rule use info set when Sindexing and Ptree are not used and
	 * return the set of new rule use info set
	 * @param rui RuleUseInfo
	 * @return RuleUseInfoSet
	 */
	public RuleUseInfoSet insert(RuleUseInfo rui)
	{
		RuleUseInfoSet temp=this.combine(rui);
		temp.putIn(rui);
		for(int i=0;i<temp.rs.size();i++)
		{
			this.putIn(temp.rs.get(i));
		}
		return temp;
	}
	
	/**
	 * Return the number of rule use infos in this set
	 * @return int
	 */
	public int cardinality()
	{
		return rs.size();
	}
}