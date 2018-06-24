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
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.SwordStatResourceLocator;
import swordstat.util.swordutil.SwordDataEnum;
import swordstat.util.swordutil.SwordNBTHelper;

public class LivingDeathEventHandler {
	
	SwordNBTHelper attachmentHandler = new SwordNBTHelper(SwordStatResourceLocator.getEntitySorting());

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
		EntitySorting entitySorting = SwordStatResourceLocator.getEntitySorting();
		Map<String, Class<? extends Entity>> bossMapping =
				entitySorting.getSorting(SwordStatResourceLocator.BOSS_STRING);
		Map<String, Class<? extends Entity>> monsterMapping =
				entitySorting.getSorting(SwordStatResourceLocator.MONSTER_STRING);
		Map<String, Class<? extends Entity>> passiveMapping =
				entitySorting.getSorting(SwordStatResourceLocator.PASSIVE_STRING);
		// Check if it has NBT here & give it NBT
		try {
			attachmentHandler.attachNBT(sword, false, player.world);
		}
		catch ( IllegalArgumentException e ) {
			// The sword already has relevant NBT attached.
		}
		attachmentHandler.incNBTData(sword, SwordDataEnum.TOTAL_KILLS);
		Class<? extends Entity> entityClass = event.getEntity().getClass();
		if ( event.getEntity() instanceof EntityPlayer ){
			attachmentHandler.incNBTData(sword, SwordDataEnum.PLAYER_KILLS);
		}
		else if ( bossMapping.containsValue(entityClass) ){
			attachmentHandler.incNBTData(
					sword, SwordDataEnum.BOSS_KILLS, entityClass
			);
		}
		else if ( monsterMapping.containsValue(entityClass) ){
			attachmentHandler.incNBTData(
					sword, SwordDataEnum.MONSTER_KILLS, entityClass
			);
		}
		else if ( passiveMapping.containsValue(entityClass) ){
			attachmentHandler.incNBTData(
					sword, SwordDataEnum.PASSIVE_KILLS, entityClass
			);
		}
		else {

		}
		
		
	}
}
