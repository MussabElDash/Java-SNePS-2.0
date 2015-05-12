package SNeBR;

import java.util.HashSet;
import java.util.Iterator;

public class ContextSet {
	HashSet<Context> contextSet;

	public ContextSet() {
		this.contextSet = new HashSet<Context>();
	}

	public Context getContext(PropositionSet propSet) {
		for (Iterator<Context> iterator = this.contextSet.iterator(); iterator
				.hasNext();) {
			Context c = iterator.next();
			if (c.hypothesisSet.propositions.equals(propSet.propositions))
				return c;
		}
		Context c1 = new Context(propSet);
		return c1;
	}

	public Context getContext(String name) {
		for (Iterator<Context> iterator = this.contextSet.iterator(); iterator
				.hasNext();) {
			Context c = iterator.next();
			if (c.names.contains(name))
				return c;
		}
		return null;
	}

	public void addContext(Context newContext) {
		for (Iterator<Context> iterator = this.contextSet.iterator(); iterator
				.hasNext();) {
			Context context = iterator.next();
			if (context.hypothesisSet == newContext.hypothesisSet) {
				context.unionRestriction(context);
				return;
			}
		}
		this.contextSet.add(newContext);
	}

	public HashSet<Context> getContextSet() {
		return contextSet;
	}

	public void setContextSet(HashSet<Context> contextSet) {
		this.contextSet = contextSet;
	}

	public Iterator<Context> iterator() {
		return contextSet.iterator();
	}

	public boolean contains(Context context) {
		return contextSet.contains(context);
	}
}
