package snip;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChannelSet implements Iterable<Channel> {
	private Set<Channel> channels;

	public ChannelSet() {
		channels = new HashSet<Channel>();
	}

	public void addChannel(Channel channel) {
		channels.add(channel);
	}

	@Override
	public Iterator<Channel> iterator() {
		return channels.iterator();
	}
}
