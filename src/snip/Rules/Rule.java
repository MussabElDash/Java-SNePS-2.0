package snip.Rules;

import java.util.HashSet;
import java.util.Set;

import sneps.Cables.DownCable;
import sneps.Nodes.Node;
import sneps.Nodes.NodeSet;
import sneps.Nodes.PropositionNode;
import sneps.SemanticClasses.Proposition;
import sneps.SyntaticClasses.Molecular;
import snip.AntecedentToRuleChannel;
import snip.Channel;
import snip.ChannelTypes;
import snip.MatchChannel;
import snip.RuleToConsequentChannel;

public abstract class Rule extends PropositionNode {
	public Rule(Molecular syn, Proposition sym) {
		super(syn,sym);
	}
	
	abstract public void applyRuleHandler();
	
	@SuppressWarnings("unused")
	@Override
	public void processRequests() {
		for(Channel currentChannel : incomingChannels) {
			if(currentChannel instanceof RuleToConsequentChannel) {
				//TODO Akram: no free variable
				if(true) {
					Proposition semanticType = (Proposition) this.getSemantic();
					if(semanticType.isAsserted(currentChannel.getContext())) {
						//TODO Akram: if rule is usable
						if(true) {
							//TODO Akram: relation name "Antecedent"
							DownCable antecedntCable = this.getDownCableSet().getDownCable("Antecedent");
							NodeSet antecedentNodeSet = antecedntCable.getNodeSet();
							Set<Node> antecedentNodes = new HashSet<Node>();
							for(int i = 0; i < antecedentNodeSet.size(); ++i) {
								Node currentNode = antecedentNodeSet.getNode(i);
								//TODO Akram: if not yet been requested for this instance
								if(true) {
									antecedentNodes.add(currentNode);
								}
							}
							sendRequests(antecedentNodes, currentChannel.getContext(), ChannelTypes.RuleAnt);
						}else {
							//TODO Akram: establish the rule
						}
					}
				}else if(true) {
					
				}else if(true) {
					
				}
			}else {
				super.processSingleRequest(currentChannel);
			}
		}
	}
	
	@Override
	public void processReports() {
		for (Channel currentChannel : outgoingChannels) {
			processSingleReport(currentChannel);
			if(currentChannel instanceof AntecedentToRuleChannel) {
				this.applyRuleHandler();
			}
		}
	}
}
