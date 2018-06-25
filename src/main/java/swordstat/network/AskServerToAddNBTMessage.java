package swordstat.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.Main;
import swordstat.util.SwordStatResourceLocator;
import swordstat.util.swordutil.SwordNBTHelper;

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

	public static class AskServerToAddNBTMessageHandler 
		implements IMessageHandler<AskServerToAddNBTMessage, IMessage> {

		@Override
		public IMessage onMessage( AskServerToAddNBTMessage message, MessageContext ctx ) {

			if ( ctx.side != Side.SERVER ) {
		        Main.LOGGER.error("SendEntitySortMessage received on wrong side:" + ctx.side);
		        return null;
			}
			
		    final EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		    serverPlayer.getServerWorld().addScheduledTask(new Runnable() {
		    	
		    	public void run() {
		    		
		    		SwordNBTHelper swordNBTHelper = SwordStatResourceLocator.getSwordNBTHelper();
		    		ItemStack itemStack = serverPlayer.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		    		if ( itemStack != null && itemStack.getItem() instanceof ItemSword ) {
		    			try {
		    				swordNBTHelper.attachNBT(itemStack, false, serverPlayer.getServerWorld());
		    			}
		    			catch ( IllegalArgumentException e ){
		    			}
		    			swordNBTHelper.updateNBTData(itemStack, serverPlayer.getServerWorld());
		    		}
		    	}
		    });
		    
			return null;
		}
		
		
	}
}
