package swordstat.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 *If the player has a sword in their hand, and it does not have NBT
 *ask the server to add NBT to it. 
 */
public class AskServerToAddNBTMessage implements IMessage {

	@Override
	public void fromBytes( ByteBuf buf ) {

	}

	@Override
	public void toBytes( ByteBuf buf ) {

	}

	private static class AskServerToAddNBTMessageHandler {
		
		
	}
}
