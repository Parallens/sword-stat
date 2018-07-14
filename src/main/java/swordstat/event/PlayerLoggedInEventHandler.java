package swordstat.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySortingInit;
import swordstat.network.TellClientToSortEntitiesMessage;

public class PlayerLoggedInEventHandler {

	private boolean isFirstPlayer = true;
	
	// Only called on server
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onEvent( final PlayerLoggedInEvent event ) {

		// only want to do this once
		if ( isFirstPlayer ){
			SwordStat.LOGGER.info("Sorting entities into bosses, monsters and passives");
			EntitySorting entitySorting = EntitySortingInit.createEntitySorting(
					SwordStat.SERVER_RESOURCE_LOCATOR.getEntitySorter(),
					event.player.world
			);
			SwordStat.SERVER_RESOURCE_LOCATOR.setEntitySorting(entitySorting);
			 if ( entitySorting.getInternalMapping().size() > 0 ){
			        SwordStat.LOGGER.info("Entity sorting appears to have been processed on the server correctly");
		        }
		     else {
		    	 SwordStat.LOGGER.info("Entity sorting does not appear to have been processed correctly on server");
		     }
		}
		
		isFirstPlayer = false;
		
		// Tell client to sort entities as well
		SwordStat.LOGGER.info("Asking client to sort entities");
		SwordStat.INSTANCE.sendTo(new TellClientToSortEntitiesMessage(), (EntityPlayerMP) event.player);
	}

}
