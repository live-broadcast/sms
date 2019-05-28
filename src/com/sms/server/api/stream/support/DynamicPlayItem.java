package com.sms.server.api.stream.support;

import com.sms.server.api.stream.IPlayItem;
import com.sms.server.messaging.IMessageInput;

/**
 * Dynamic playlist item implementation
 */
public class DynamicPlayItem implements IPlayItem {

	/**
	 * Playlist item name
	 */
	protected final String name;

	/**
	 * Start mark
	 */
	protected final long start;

	/**
	 * Length - amount to play
	 */
	protected final long length;

	/**
	 * Size - for VOD items this will be the file size
	 */
	protected long size = -1;

	/**
	 * Offset
	 */
	protected double offset;

	/**
	 * Message source
	 */
	protected IMessageInput msgInput;

	private DynamicPlayItem(String name, long start, long length) {
		this.name = name;
		this.start = start;
		this.length = length;
	}

	private DynamicPlayItem(String name, long start, long length, double offset) {
		this.name = name;
		this.start = start;
		this.length = length;
		this.offset = offset;
	}

	/**
	 * Returns play item length in milliseconds
	 * 
	 * @return	Play item length in milliseconds
	 */
	public long getLength() {
		return length;
	}

	/**
	 * Returns IMessageInput object. IMessageInput is an endpoint for a consumer
	 * to connect.
	 * 
	 * @return	IMessageInput object
	 */
	public IMessageInput getMessageInput() {
		return msgInput;
	}

	/**
	 * Returns item name
	 * 
	 * @return	item name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns boolean value that specifies whether item can be played
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Alias for getMessageInput
	 * 
	 * @return      Message input source
	 */
	public IMessageInput getMsgInput() {
		return msgInput;
	}

	/**
	 * Setter for message input
	 *
	 * @param msgInput Message input
	 */
	public void setMsgInput(IMessageInput msgInput) {
		this.msgInput = msgInput;
	}

	/**
	 * Returns size in bytes
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Set the size in bytes
	 * 
	 * @param size size in bytes
	 */
	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + (int) (start ^ (start >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynamicPlayItem other = (DynamicPlayItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size != other.size)
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	/**
	 * Builder for DynamicPlayItem
	 * 
	 * @param name
	 * @param start
	 * @param length
	 * @return play item instance
	 */
	public static DynamicPlayItem build(String name, long start, long length) {
		DynamicPlayItem playItem = new DynamicPlayItem(name, start, length);
		return playItem;
	}

	/**
	 * Builder for DynamicPlayItem
	 * 
	 * @param name
	 * @param start
	 * @param length
	 * @param offset
	 * @return play item instance
	 */
	public static DynamicPlayItem build(String name, long start, long length, double offset) {
		DynamicPlayItem playItem = new DynamicPlayItem(name, start, length, offset);
		return playItem;
	}
}
