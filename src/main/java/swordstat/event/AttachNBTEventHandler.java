package swordstat.event;

import swordstat.util.SwordStatResourceLocator;
import swordstat.util.swordutil.SwordNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AttachNBTEventHandler {
	
	/*
	 * There are three conditions in which we may want to attach NBT data the 
	 * mod uses to a sword:
	 * > When the item is crafted so we can document the time it was crafted
	 * > When the item is picked up(e.g. rare drop from zombie)
	 * > When the gui is called as the above conditions may not have been met
	 *   if the mod had not been installed at the time.
	 *   
	 *   Also possibly when the player logs in
	 */
	
	public void attachNBT( ItemStack itemStack, World worldObj ) {

		SwordNBTHelper swordNBTHelper = SwordStatResourceLocator.getSwordNBTHelper();
		if ( itemStack.getItem() instanceof ItemSword ){
			swordNBTHelper.attachNBT(itemStack, true, worldObj);
		}
		else {
			// Raise some error
		}
	}
	
	@SubscribeEvent
	public void onEvent( PlayerEvent.ItemCraftedEvent event ) {
		
		if ( event.crafting.getItem() instanceof ItemSword && !event.player.getEntityWorld().isRemote ){
			attachNBT(event.crafting, event.player.getEntityWorld());
		}
		
	}
}
