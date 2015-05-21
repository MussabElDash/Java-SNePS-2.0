package SNeBR;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

public class ContextSet {
	// HashSet<Context> contextSet;
	private Hashtable<Integer, Context> contextsMappedByID;
	private Hashtable<String, Context> contextsMappedByName;

	public ContextSet() {
		// this.contextSet = new HashSet<Context>();
		contextsMappedByID = new Hashtable<Integer, Context>();
		contextsMappedByName = new Hashtable<String, Context>();
	}

	public Context getContext(PropositionSet propSet) {
		for (Iterator<Context> iterator = this.iterator(); iterator.hasNext();) {
			Context c = iterator.next();
			if (c.hypothesisSet.propositions.equals(propSet.propositions))
				return c;
		}
		Context c1 = new Context(propSet);
		return c1;
	}

	public Context getContext(String name) {
		// for (Iterator<Context> iterator = this.iterator(); iterator
		// .hasNext();) {
		// Context c = iterator.next();
		// if (c.names.contains(name))
		// return c;
		// }
		return contextsMappedByName.get(name);
	}

	public Context getContext(int id) {
		return contextsMappedByID.get(id);
	}

	public void addContext(Context newContext) {
		for (Iterator<Context> iterator = this.iterator(); iterator.hasNext();) {
			Context context = iterator.next();
			if (context.hypothesisSet == newContext.hypothesisSet) {
				context.unionRestriction(context);
				return;
			}
		}
		this.add(newContext);
	}

	public void add(Context context) {
		contextsMappedByID.put(context.getId(), context);
		for (String s : context.names) {
			contextsMappedByName.put(s, context);
		}
	}

	public boolean remove(Context context) {
		boolean bool = false;
		bool |= contextsMappedByID.remove(context.getId()) == context;
		for (String s : context.names) {
			bool |= contextsMappedByName.remove(s) == context;
		}
		return bool;
	}

	public void addAll(Collection<Context> contexts) {
		for (Context context : contexts) {
			add(context);
		}
	}

	// public HashSet<Context> getContextSet() {
	// return contextSet;
	// }

	// public void setContextSet(HashSet<Context> contextSet) {
	// this.contextSet = contextSet;
	// }

	public Iterator<Context> iterator() {
		return contextsMappedByID.values().iterator();
	}

	public boolean contains(Context context) {
		return contextsMappedByID.containsKey(context.getId());
	}
}
