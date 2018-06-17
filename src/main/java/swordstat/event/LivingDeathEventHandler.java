package swordstat.event;

import swordstat.util.EntityHelper;
import swordstat.util.swordutil.SwordDataEnum;
import swordstat.util.swordutil.SwordNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingDeathEventHandler {
	
	SwordNBTHelper attachmentHandler = new SwordNBTHelper();
	EntityHelper entityHandler;

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
		entityHandler = new EntityHelper(player.world);
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
		else if ( entityHandler.getBossMap().containsValue(entityClass) ){
			attachmentHandler.incNBTData(
					sword, SwordDataEnum.BOSS_KILLS, entityClass
			);
		}
		else if ( entityHandler.getMonsterMap().containsValue(entityClass) ){
			attachmentHandler.incNBTData(
					sword, SwordDataEnum.MONSTER_KILLS, entityClass
			);
		}
		else if ( entityHandler.getPassiveMap().containsValue(entityClass) ){
			attachmentHandler.incNBTData(
					sword, SwordDataEnum.PASSIVE_KILLS, entityClass
			);
		}
		else {

		}
		
		
	}
}
