package swordstat.event;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.ServerResourceLocator;
import swordstat.util.swordutil.SwordDataEnum;
import swordstat.util.swordutil.SwordNBTHelper;

public class LivingDeathEventHandler {
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onEvent( LivingDeathEvent event ) {
		
		// Not interested if its not a player kill or the player is not using a sword
		if ( 
				! (event.getSource().getTrueSource() instanceof EntityPlayer) ||
				! (((EntityPlayer) event.getSource().getTrueSource())
				.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)
				.getItem() instanceof ItemSword) || event.getEntity().world.isRemote
			){
			return;
		}

		EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
		ItemStack sword = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		EntitySorting entitySorting = SwordStat.SERVER_RESOURCE_LOCATOR.getEntitySorting();
		Map<String, Class<? extends Entity>> bossMapping =
				entitySorting.getSorting(ServerResourceLocator.BOSS_STRING);
		Map<String, Class<? extends Entity>> monsterMapping =
				entitySorting.getSorting(ServerResourceLocator.MONSTER_STRING);
		Map<String, Class<? extends Entity>> passiveMapping =
				entitySorting.getSorting(ServerResourceLocator.PASSIVE_STRING);
		// Check if it has NBT here & give it NBT

		SwordNBTHelper swordNBTHelper = SwordStat.SERVER_RESOURCE_LOCATOR.getSwordNBTHelper();
		try {
			swordNBTHelper.attachNBT(sword, false, player.world);
		}
		catch ( IllegalArgumentException e ) {
			// The sword already has relevant NBT attached.
		}
		swordNBTHelper.incNBTData(sword, SwordDataEnum.TOTAL_KILLS);
		Class<? extends Entity> entityClass = event.getEntity().getClass();
		if ( event.getEntity() instanceof EntityPlayer ){
			swordNBTHelper.incNBTData(sword, SwordDataEnum.PLAYER_KILLS);
		}
		else if ( bossMapping.containsValue(entityClass) ){
			swordNBTHelper.incNBTData(
					sword, SwordDataEnum.BOSS_KILLS, entityClass
			);
		}
		else if ( monsterMapping.containsValue(entityClass) ){
			swordNBTHelper.incNBTData(
					sword, SwordDataEnum.MONSTER_KILLS, entityClass
			);
		}
		else if ( passiveMapping.containsValue(entityClass) ){
			swordNBTHelper.incNBTData(
					sword, SwordDataEnum.PASSIVE_KILLS, entityClass
			);
		}
		else {

		}
		
		
	}
}
