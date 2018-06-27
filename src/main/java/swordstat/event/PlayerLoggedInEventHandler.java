package swordstat.event;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import swordstat.Main;
import swordstat.init.EntitySorter;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySorter.IEntityGroupSorter;
import swordstat.init.EntitySortingInit;
import swordstat.network.TellClientToSortEntitiesMessage;
import swordstat.util.ServerResourceLocator;

public class PlayerLoggedInEventHandler {

	private boolean isFirstPlayer = true;
	
	// Only called on server
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onEvent( final PlayerLoggedInEvent event ) {

		// only want to do this once
		if ( isFirstPlayer ){
			Main.LOGGER.info("Sorting entities into bosses, monsters and passives");
			EntitySorting entitySorting = EntitySortingInit.createEntitySorting(
					Main.SERVER_RESOURCE_LOCATOR.getEntitySorter(),
					event.player.world
			);
			Main.SERVER_RESOURCE_LOCATOR.setEntitySorting(entitySorting);
		}
		
		isFirstPlayer = false;
		
		// Tell client to sort entities as well
		Main.INSTANCE.sendTo(new TellClientToSortEntitiesMessage(), (EntityPlayerMP) event.player);
	}
	

}
