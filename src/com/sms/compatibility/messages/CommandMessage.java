package com.sms.compatibility.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sms.io.amf3.ByteArray;
import com.sms.io.amf3.IDataInput;
import com.sms.io.utils.RandomGUID;
import com.sms.server.util.SystemTimer;

/**
 * Command message as sent by the <code>mx:RemoteObject</code> tag.
 * 
 * @see <a href="http://osflash.org/documentation/amf3">osflash documentation (external)</a>
 * @see <a href="http://livedocs.adobe.com/flex/2/langref/mx/rpc/remoting/mxml/RemoteObject.html">Adobe Livedocs (external)</a>
 *
 */
public class CommandMessage extends AsyncMessage {

	private static final long serialVersionUID = 8805045741686625945L;

	protected static byte OPERATION_FLAG = 1;
	
	public String messageRefType;

	/** Command id to execute. */
	public int operation = Constants.UNKNOWN_OPERATION;

	public CommandMessage() {
		this.messageId = new RandomGUID().toString();
		this.timestamp = SystemTimer.currentTimeMillis();
	}
	
	/** {@inheritDoc} */
	protected void addParameters(StringBuilder result) {
		super.addParameters(result);
		result.append(",messageRefType=");
		result.append(messageRefType);
		result.append(",operation=");
		result.append(operation);
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	static Logger log = LoggerFactory.getLogger(CommandMessage.class);	
	
	@Override
	public void readExternal(IDataInput in) {
		log.debug("CommandMessage - Read external");
		super.readExternal(in);
		short[] flagsArray = readFlags(in);
		for (int i = 0; i < flagsArray.length; ++i) {
			short flags = flagsArray[i];
			log.debug("Unsigned byte: {}", flags);
			short reservedPosition = 0;
			if (i == 0) {
				if ((flags & OPERATION_FLAG) != 0) {
					Integer obj = in.readInt();
					log.debug("Operation object: {} name: {}", obj, obj.getClass().getName());
					this.operation = obj.intValue();
				}
				reservedPosition = 1;
			}
			if (flags >> reservedPosition == 0) {
				continue;
			}
			for (short j = reservedPosition; j < 6; j = (short) (j + 1)) {
				if ((flags >> j & 0x1) == 0) {
					continue;
				}
				Object obj = in.readObject();
				log.debug("Object2: {} name: {}", obj, obj.getClass().getName());
				if (obj instanceof ByteArray) {
					ByteArray ba = (ByteArray) obj;
					byte[] arr = new byte[ba.length()];
					ba.readBytes(arr);
					log.debug("Array length: {} Data: {}", arr.length, RandomGUID.fromByteArray(arr));
				}
			}
		}
		log.debug("Operation: {}", operation);
	}

}
