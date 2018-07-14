package swordstat.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySortingInit;

public class TellClientToSortEntitiesMessageHandler
	implements IMessageHandler<TellClientToSortEntitiesMessage, IMessage> {

	@Override
	public IMessage onMessage( TellClientToSortEntitiesMessage message, MessageContext ctx ) {

		if ( ctx.side != Side.CLIENT ) {
	        SwordStat.LOGGER.error("SendEntitySortMessage received on wrong side:" + ctx.side);
	        return null;
		}
		
	    final Minecraft minecraft = Minecraft.getMinecraft();
	    minecraft.addScheduledTask(new Runnable() {
	    	
	    	public void run() {
	    		
	    		EntitySorting entitySorting = EntitySortingInit.createEntitySorting(
	    				SwordStat.CLIENT_RESOURCE_LOCATOR.getEntitySorter(), minecraft.world
	    		);
	    		SwordStat.CLIENT_RESOURCE_LOCATOR.setEntitySorting(entitySorting);
		        if ( entitySorting.getInternalMapping().size() > 0 ){
			        SwordStat.LOGGER.info("Entity sorting appears to have been processed on client correctly");
		        }
		        else {
		        	SwordStat.LOGGER.info("Entity sorting does not appear to have been processed correctly on client");
		        }
		        SwordStat.LOGGER.debug(entitySorting);
	    	}
	    });
	    
	    return null;
	}
	
}