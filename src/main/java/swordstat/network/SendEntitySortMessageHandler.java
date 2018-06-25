package swordstat.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.Main;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.SwordStatResourceLocator;

public class SendEntitySortMessageHandler
	implements IMessageHandler<SendEntitySortMessage, IMessage> {

	@Override
	public IMessage onMessage( SendEntitySortMessage message, MessageContext ctx ) {

		if ( ctx.side != Side.CLIENT ) {
	        Main.LOGGER.error("SendEntitySortMessage received on wrong side:" + ctx.side);
	        return null;
		}
		EntitySorting entitySorting = message.getEntitySorting();
		if ( entitySorting == null ){
			Main.LOGGER.error("Received null entity sorting on client, this is bad, no entities will be displayed on sword menu");
			entitySorting = new EntitySorting(new HashMap<String, Map<String, Class<? extends Entity>>>());
		}
		else {
			Main.LOGGER.info("Entity Sorting seems to have been received from server successfully.");
			Main.LOGGER.debug("Entity sorting: " + entitySorting);
		}
		
		final EntitySorting entitySortingCopy = entitySorting;
	    Minecraft minecraft = Minecraft.getMinecraft();
	    minecraft.addScheduledTask(new Runnable() {
	    	
	    	public void run() {
	    		SwordStatResourceLocator.setEntitySorting(entitySortingCopy);
	    	}
	    });
	    
	    return null;
	}
	
}