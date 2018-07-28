package swordstat.network;

import java.util.Arrays;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.SwordStat;
import swordstat.swordinfo.SwordDataEnum;
import swordstat.util.ServerResourceLocator;
import swordstat.util.swordutil.SwordNBTHelper;

/**
 *If the player has a sword in their hand, and it does not have NBT
 *ask the server to add NBT to it. 
 */
public class AskServerToAddNBTMessage implements IMessage {
	
	private boolean openGUIAfterProcessing = false;
	
	public AskServerToAddNBTMessage() {
		
	}
	
	public AskServerToAddNBTMessage( final boolean openGUIAfterProcessing ) {
		
		this.openGUIAfterProcessing = openGUIAfterProcessing;
	}

	@Override
	public void toBytes( ByteBuf buf ) {

		buf.writeBoolean(openGUIAfterProcessing);
	}

	@Override
	public void fromBytes( ByteBuf buf ) {

		openGUIAfterProcessing = buf.readBoolean();
	}

	public static class AskServerToAddNBTMessageHandler 
		implements IMessageHandler<AskServerToAddNBTMessage, IMessage> {

		@Override
		public IMessage onMessage( final AskServerToAddNBTMessage message, MessageContext ctx ) {

			if ( ctx.side != Side.SERVER ) {
		        SwordStat.LOGGER.error("SendEntitySortMessage received on wrong side:" + ctx.side);
		        return null;
			}
			
		    final EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		    serverPlayer.getServerWorld().addScheduledTask(new Runnable() {
		    	
		    	public void run() {
		    		
		    		SwordNBTHelper swordNBTHelper = SwordStat.SERVER_RESOURCE_LOCATOR.getSwordNBTHelper();
		    		ItemStack itemStack = serverPlayer.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

		    		try {
		    			swordNBTHelper.attachNBT(itemStack, false, serverPlayer.getServerWorld());
		    		}
		    		catch ( IllegalArgumentException e ){}
		    		
		    		swordNBTHelper.updateNBTData(itemStack, serverPlayer.getServerWorld());
		    		// We don't send it in the return below, we send it here to ensure processing above is done
		    		if ( message.openGUIAfterProcessing ){
		    			SwordStat.INSTANCE.sendTo(
		    					new OpenSwordStatGuiOnClientMessage(itemStack.getTagCompound()),
		    					serverPlayer
		    			);
		    		}
		    	}
		    });
		    
		    return null;
		}
		
		
	}
}
