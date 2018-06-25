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
import swordstat.network.SendEntitySortMessage;
import swordstat.util.SwordStatResourceLocator;

public class PlayerLoggedInEventHandler {

	private boolean isFirstPlayer = true;
	
	// Only called on server
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onEvent( final PlayerLoggedInEvent event ) {

		// only want to do this once
		if ( isFirstPlayer ){
			Main.LOGGER.info("Sorting entities into bosses, monsters and passives");
			final IEntityGroupSorter bossSorter = new BossSorter(event.player.world);
			final IEntityGroupSorter monsterSorter = new MonsterSorter(bossSorter);
			final IEntityGroupSorter passiveSorter = new PassiveSorter(bossSorter, monsterSorter);
			
			Map<String, IEntityGroupSorter> sorters = new HashMap<>();
			sorters.put(SwordStatResourceLocator.BOSS_STRING, bossSorter);
			sorters.put(SwordStatResourceLocator.MONSTER_STRING, monsterSorter);
			sorters.put(SwordStatResourceLocator.PASSIVE_STRING, passiveSorter);
			EntitySorter entitySorter = SwordStatResourceLocator.getEntitySorter();
			SwordStatResourceLocator.setEntitySorting(entitySorter.sort(sorters));
		}
		
		isFirstPlayer = false;
		
		// Send entity sorting to newly logged in client
		EntitySorting entitySorting = SwordStatResourceLocator.getEntitySorting();
		Main.INSTANCE.sendTo(new SendEntitySortMessage(entitySorting), (EntityPlayerMP) event.player);
	}
	
	private static class BossSorter implements IEntityGroupSorter {
		
		private World world;
		
		public BossSorter( World world ) {
			
			this.world = world;
		}
		
		@Override
		public boolean isInGroup( Class<? extends Entity> entityClass ) {
		
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				return false;
			}
			Entity entity;
			try {
				entity = entityClass.getConstructor(World.class)
						.newInstance(world);
			} catch ( Exception e ){
				Main.LOGGER.error("Could not initialise entity of " + entityClass + ", skipping...");
				return false;
			}
			if ( entity != null && !entity.isNonBoss() ){
				// remove entity
				entity.setDead();
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	private static class MonsterSorter implements IEntityGroupSorter {
		
		IEntityGroupSorter bossSorter;
		
		public MonsterSorter( final IEntityGroupSorter bossSorter ) {
			
			this.bossSorter = bossSorter;
		}
		
		@Override
		public boolean isInGroup( Class<? extends Entity> entityClass ) {
		
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				return false;
			}
			return EntityMob.class.isAssignableFrom(entityClass) && 
					!bossSorter.isInGroup(entityClass);
		}
	}
	
	private static class PassiveSorter implements IEntityGroupSorter {
		
		private final IEntityGroupSorter bossSorter, monsterSorter;
		
		public PassiveSorter( final IEntityGroupSorter bossSorter, final IEntityGroupSorter monsterSorter ) {
			
			this.bossSorter = bossSorter;
			this.monsterSorter = monsterSorter;
		}
		
		@Override
		public boolean isInGroup( Class<? extends Entity> entityClass ) {
		
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				return false;
			}
			// Check if living and not in the other groups
			else if ( EntityLiving.class.isAssignableFrom(entityClass) ){
				return !( bossSorter.isInGroup(entityClass) || monsterSorter.isInGroup(entityClass) );
			}
			else {
				return false;
			}
		}
	};
}
