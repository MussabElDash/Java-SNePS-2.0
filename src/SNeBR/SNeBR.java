package SNeBR;

import java.util.HashSet;
import java.util.Iterator;

import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Proposition;

public class SNeBR {
	private static ContextSet contextSet = new ContextSet();;
	private static Context currentContext = new Context();;
	private static HashSet<Support> newPropositionSupport = new HashSet<Support>();
	private static HashSet<Support> oldPropositionSupport = new HashSet<Support>();
	private static Proposition newContradictingProp = new Proposition();
	private static PropositionSet oldContradictingPropSet = new PropositionSet();

	private SNeBR() {
	}

	/**
         * 
         */
	public static Support createSupport(PropositionSet propSet) {
		Context c = contextSet.getContext(propSet);
		Support s = new Support(c);
		return s;

	}

	/**
	 * sets the current context to the selected context
	 * 
	 * @param context
	 *            the context i want to make the current context
	 */
	public static void setCurrentContext(Context context) {
		currentContext = context;
	}

	public static Context getCurrentContext() {
		return currentContext;
	}

	public static Context getContextByName(String name) {
		return contextSet.getContext(name);
	}

	public static Context getContextByID(int id) {
		return contextSet.getContext(id);
	}

	/**
	 * remove a proposition from the current context
	 * 
	 * @param proposition
	 *            the proposition i want to remove from the context
	 */
	public static Context removeProposition(PropositionNode proposition) {
		Context context = new Context(currentContext);
		if (context.hypothesisSet.propositions.contains(proposition)) {
			context.addNames(currentContext.getNames());
			currentContext.clearNames();
			System.out.println("erorrrrrrrrrrrrrrr");
			context.hypothesisSet.propositions.remove(proposition);
			context.removePropFromRestriction(proposition);
			context = afterRemoved(context);
			removeRestrictions(context);
			context.addContextToProp(context);
			currentContext = context;
			return context;
		}

		else {
			System.out.print("this proposition is not in this context");

			return null;
		}
	}

	/**
	 * remove a proposition from the context that has this name
	 * 
	 * @param proposition
	 *            the proposition i want to remove
	 * @param name
	 *            the name of the context i want to remove the proposition from
	 */
	public static Context removeProposition(PropositionNode proposition,
			String name) {
		Context context = contextSet.getContext(name);

		if (context == null) {
			System.out
					.print("there is no context with such name to remove the proposition from");
			return null;
		} else if (context.hypothesisSet.propositions.contains(proposition)) {
			context.RemoveName(name);
			Context newContext = new Context(context);
			if (((Proposition) proposition.getSemantic()).getContextSet()
					.contains(context)) {
				newContext.hypothesisSet.propositions.remove(proposition);
				newContext.removePropFromRestriction(proposition);
				newContext.addName(name);
				newContext = afterRemoved(newContext, name);
				newContext.addContextToProp(newContext);
				removeRestrictions(newContext);

				return newContext;
			}

		} else {
			System.out.print("this proposition is not in this context");
			return null;
		}
		return null;
	}

	/**
	 * remove a proposition from the selected context
	 * 
	 * @param proposition
	 *            the proposition i want to remove
	 * @param context
	 *            the context i want to remove the proposition from
	 */
	public static Context removeProposition(PropositionNode proposition,
			Context context) {
		if (context == currentContext) {
			removeProposition(proposition);
			return null;
		} else if (context.hypothesisSet.propositions.contains(proposition)) {
			Context newContext = new Context(context);
			newContext.addNames(context.getNames());
			context.clearNames();
			System.out.println("erorrrrrrrrrrrrrrr1");
			newContext.hypothesisSet.propositions.remove(proposition);
			newContext.removePropFromRestriction(proposition);
			newContext = afterRemoved(newContext);
			newContext.addContextToProp(newContext);
			removeRestrictions(newContext);
			return newContext;
		} else {
			System.out.print("this proposition is not in this context");
			return null;
		}
	}

	/**
	 * adding a proposition to a context with a specific name if found added to
	 * it if not creats a new one
	 * 
	 * @param proposition
	 *            proposition you want to add to the context
	 * @param name
	 *            the name of the context
	 */
	public static Context assertProposition(PropositionNode proposition,
			String name) {
		Context context = contextSet.getContext(name);
		if (context == null) {
			Context newContext = new Context(proposition, name);
			contextSet.addContext(newContext);
			((Proposition) proposition.getSemantic()).addContext(newContext);
			return newContext;
		} else {
			if (context.canBeAdded(proposition)) {
				Context newContext = new Context(context);
				newContext.addNames(context.getNames());
				newContext.addToContext(proposition);
				newContext.addPropToRestriction(proposition);
				newContext = afterAssert(newContext);
				context.addContextToProp(newContext);
				((Proposition) proposition.getSemantic())
						.addContext(newContext);
				context.RemoveName(name);
				newContext.addName(name);
				proposition.contradiction(newContext);
				return newContext;
			} else {
				System.out
						.print("you can not add this one or an old contradiction will occur again");
				return null;
			}
		}

	}

	public static ContextSet getContextSet() {
		return contextSet;
	}

	public static void setContextSet(ContextSet contextSet) {
		SNeBR.contextSet = contextSet;
	}

	public static HashSet<Support> getNewPropositionSupport() {
		return newPropositionSupport;
	}

	public static void setNewPropositionSupport(
			HashSet<Support> newPropositionSupport) {
		SNeBR.newPropositionSupport = newPropositionSupport;
	}

	public static HashSet<Support> getOldPropositionSupport() {
		return oldPropositionSupport;
	}

	public static void setOldPropositionSupport(
			HashSet<Support> oldPropositionSupport) {
		SNeBR.oldPropositionSupport = oldPropositionSupport;
	}

	public static Proposition getNewContradictingProp() {
		return newContradictingProp;
	}

	public static void setNewContradictingProp(Proposition newContradictingProp) {
		SNeBR.newContradictingProp = newContradictingProp;
	}

	public static PropositionSet getOldContradictingPropSet() {
		return oldContradictingPropSet;
	}

	public static void setOldContradictingPropSet(
			PropositionSet oldContradictingPropSet) {
		SNeBR.oldContradictingPropSet = oldContradictingPropSet;
	}

	/**
	 * adds a proposition to a certain context
	 * 
	 * @param proposition
	 *            is the proposition you want to assert to this context
	 * @param context
	 *            the context you want to add to it
	 */
	public static Context assertProposition(PropositionNode proposition,
			Context context) {
		if (context == currentContext) {
			return assertProposition(proposition);

		} else if (context.canBeAdded(proposition)) {
			Context newContext = new Context(context);
			newContext.addToContext(proposition);
			context.addContextToProp(newContext);
			newContext.addPropToRestriction(proposition);
			newContext = afterAssert(newContext);
			((Proposition) proposition.getSemantic()).addContext(newContext);
			newContext.addNames(context.getNames());
			context.clearNames();
			System.out.println("erorrrrrrrrrrrrrrr2");
			proposition.contradiction(newContext);
			return newContext;
		} else {
			System.out
					.print("you can not add this one or an old contradiction will occur again");
			return null;
		}

	}

	/**
	 * adds a proposition to a new context with no names
	 * 
	 * @param proposition
	 *            the proposition i want to add to the current context
	 */
	public static Context assertProposition(PropositionNode proposition) {

		Context context = new Context(currentContext);
		if (context.canBeAdded(proposition)) {
			context.addToContext(proposition);
			currentContext.addContextToProp(context);
			context.addPropToRestriction(proposition);
			context = afterAssert(context);
			((Proposition) proposition.getSemantic()).addContext(context);
			context.addNames(currentContext.getNames());
			currentContext = context;
			proposition.contradiction(context);
			return context;
		} else {
			System.out
					.print("you can not add this one or an old contradiction will occur again");
			return null;
		}

	}

	/**
	 * this method removes all unneeded restrictions an unneeded restriction is
	 * a restriction with none of the opposing proposition set exists
	 * 
	 * @param context
	 *            the context i want to remove all unneeded restriction from
	 */
	public static void removeRestrictions(Context context) {
		for (Iterator<Restriction> iterator = context.restrictionSet.iterator(); iterator
				.hasNext();) {
			Restriction r = iterator.next();
			for (Iterator<PropositionSet> iterator2 = r.restrictions.iterator(); iterator2
					.hasNext();) {
				PropositionSet p = iterator2.next();
				if (!p.assertedInContext(context))
					r.restrictions.remove(p);
			}
			if (r.restrictions.isEmpty())
				context.restrictionSet.remove(r);
		}

	}

	/**
	 * this method is called after assertion to check if the resultant context
	 * already existed or not
	 * 
	 * @param context
	 *            the context i want to check for
	 * @return the chosen context whether it turns out to be an old or new one
	 */
	public static Context afterAssert(Context context) {
		for (Iterator<Context> iterator = contextSet.iterator(); iterator
				.hasNext();) {
			Context temp = iterator.next();
			if (context.hypothesisSet.propositions
					.equals(temp.hypothesisSet.propositions)) {
				temp.unionRestriction(context);
				return temp;
			}
		}
		contextSet.addContext(context);
		return context;
	}

	/**
	 * this method checks if there is already a context with the same
	 * proposition set as this one so it selects it , if not then it creates a
	 * new context
	 * 
	 * @param context
	 *            the new context i want to add
	 * @param name
	 *            the name of the context to be transfered from one to another
	 * @return the context chosen whether old or new one
	 */
	// must change like the other after removed
	public static Context afterRemoved(Context context, String name) {
		for (Iterator<Context> iterator = contextSet.iterator(); iterator
				.hasNext();) {
			Context cont = iterator.next();
			if (cont.hypothesisSet.propositions
					.equals(context.hypothesisSet.propositions)) {
				cont.unionRestriction(context);
				cont.addName(name);
				context.RemoveName(name);
				return cont;
			}

		}
		contextSet.addContext(context);
		return context;
	}

	/**
	 * this method checks if there is already a context with the same
	 * proposition set as this one so it selects it , if not then it creates a
	 * new context
	 * 
	 * @param context
	 *            the new context i want to add
	 * @return returns the context chosen whether it's an old or a new one
	 */
	private static Context afterRemoved(Context context) {

		for (Iterator<Context> iterator = contextSet.iterator(); iterator
				.hasNext();) {
			Context cont = iterator.next();
			if (cont.hypothesisSet.propositions
					.equals(context.hypothesisSet.propositions)) {
				context.unionRestriction(cont);
				contextSet.remove(cont);
				contextSet.add(context);
				return context;
			}

		}
		contextSet.add(context);
		return context;
	}

	/**
	 * this methods is used once the user chooses a set of proposition to
	 * discard to solve the contradiction
	 * 
	 * @param propSet
	 *            the propositions the user wants to discard
	 * @param whichSupport
	 *            the name of the support either oldPropositionSupport or
	 *            newPropositionSupport
	 */
	public static void propositionsToBeDiscarded(PropositionSet propSet,
			String whichSupport, Context context, Contradiction contradiction) {
		newPropositionSupport = contradiction.newContradictingProp
				.getOriginSupport();
		for (Iterator<PropositionNode> iterator = contradiction.oldContradictingPropSet.propositions
				.iterator(); iterator.hasNext();) {
			oldPropositionSupport.addAll(((Proposition) (iterator.next()
					.getSemantic())).getOriginSupport());
		}
		if (whichSupport.equals("oldPropositionSupport")) {
			if (context.contradictions.contains(contradiction)) {
				System.out.println(oldPropositionSupport.size() + "sss");
				context.contradictions.remove(contradiction);
				restrictionOfContext(oldPropositionSupport, context,
						newPropositionSupport);
				multipleRemove(propSet, context);
				oldContradictingPropSet = null;
				newContradictingProp = null;
				oldPropositionSupport = null;
				newPropositionSupport = null;
			}
		} else {
			if (context.contradictions.contains(contradiction)) {
				context.contradictions.remove(contradiction);
				restrictionOfContext(newPropositionSupport, context,
						oldPropositionSupport);
				multipleRemove(propSet, context);
				oldContradictingPropSet = null;
				newContradictingProp = null;
				oldPropositionSupport = null;
				newPropositionSupport = null;
			}
		}
	}

	/**
	 * this method turns all supports chosen by the user as restrictions for the
	 * context after a contradiction
	 * 
	 * @param supportSet
	 *            the set of supports to be turned to restrictions
	 * @param context
	 *            the context i want to add the restrictions to
	 */
	private static void restrictionOfContext(HashSet<Support> supportSet,
			Context context, HashSet<Support> supportSet2) {
		for (Iterator<Support> iterator = supportSet.iterator(); iterator
				.hasNext();) {
			Support support = iterator.next();
			if (support.assertedInContext(context)) {
				context.addRestriction(support.originSet, null, supportSet2);
			} else {
				System.out.println("zz");
				PropositionSet propSet = new PropositionSet();
				for (Iterator<PropositionNode> iterator1 = support.originSet.hypothesisSet.propositions
						.iterator(); iterator1.hasNext();) {
					PropositionNode prop1 = iterator1.next();

					if (!((Proposition) prop1.getSemantic())
							.isAsserted(context))
						propSet.propositions.add(prop1);
				}
				context.addRestriction(support.originSet, propSet, supportSet2);
			}

		}

	}

	/**
	 * this method takes two support set and put each of the supports in one set
	 * as a restriction on all the other set
	 * 
	 * @param supportSet1
	 *            the first set of supports
	 * @param supportSet2
	 *            the second set of supports
	 */
	public static void restrictionsOfRestrictions(HashSet<Support> supportSet1,
			HashSet<Support> supportSet2) {
		for (Iterator<Support> iterator1 = supportSet1.iterator(); iterator1
				.hasNext();) {
			Support S1 = iterator1.next();
			for (Iterator<Support> iterator2 = supportSet2.iterator(); iterator2
					.hasNext();) {
				Support S2 = iterator2.next();
				S1.addRestriction(S2.originSet, S2.originSet.hypothesisSet,
						supportSet1);
				S2.addRestriction(S1.originSet, S1.originSet.hypothesisSet,
						supportSet2);
			}
		}
	}

	/**
	 * remove more than 1 proposition from a certain context
	 * 
	 * @param propSet
	 *            the proposition set i want to remove
	 * @param context
	 *            the context i want to remove from
	 */
	private static void multipleRemove(PropositionSet propSet, Context context) {
		Context newContext = new Context(context);
		newContext.addNames(context.getNames());
		for (Iterator<PropositionNode> iterator = propSet.propositions
				.iterator(); iterator.hasNext();) {
			PropositionNode prop = iterator.next();
			newContext.hypothesisSet.propositions.remove(prop);
			newContext.removePropFromRestriction(prop);

		}
		newContext = afterRemoved(newContext);
		System.out.println(contextSet.contains(newContext));
		if (newContext.contradictions.size() == 0) {
			newContext.setConflict(false);

		}

		context.clearNames();

	}

}
