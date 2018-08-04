package swordstat.event;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.proxy.CommonProxy;
import swordstat.swordinfo.SwordDataEnum;
import swordstat.swordinfo.SwordNBTAttacher;
import swordstat.util.ServerResourceLocator;

public class LivingDeathEventHandler {
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onEvent( LivingDeathEvent event ) {
		
		// Not interested if its not a player kill or the player is not using a sword
		if ( !(event.getSource().getTrueSource() instanceof EntityPlayer) ){
			return;
		}
		ItemStack heldItemStack = ((EntityPlayer) event.getSource().getTrueSource())
			.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		
		boolean shouldDeathBeProcessed = CommonProxy.OPEN_GUI_CONTROLLER.tryItem(heldItemStack) &&
				!event.getEntity().world.isRemote;
						
		if ( shouldDeathBeProcessed ){
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			ItemStack sword = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
			EntitySorting entitySorting = SwordStat.SERVER_RESOURCE_LOCATOR.getEntitySorting();
			Map<String, Class<? extends Entity>> passiveMapping =
					entitySorting.getSorting(ServerResourceLocator.PASSIVE_STRING);

			// Check if it has NBT here & give it NBT
			SwordNBTAttacher swordNBTAttacher = SwordStat.SERVER_RESOURCE_LOCATOR.getSwordNBTHelper();
			swordNBTAttacher.attachNBT(sword, false, player.world);
			swordNBTAttacher.incTotalKills(sword);
			Class<? extends Entity> entityClass = event.getEntity().getClass();
			if ( event.getEntity() instanceof EntityPlayer ){
				swordNBTAttacher.incPlayerKills(sword);
			}
			else {
				swordNBTAttacher.incKillsForEntity(sword, entityClass);
			}
		}
	}
}
