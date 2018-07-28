package swordstat.init;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import org.apache.commons.lang3.text.WordUtils;

import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySorter.IEntityGroupSorter;
import swordstat.util.ServerResourceLocator;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public final class EntitySortingInit {
	
	

	/**
	 * Return a Multimap of mod ids to entities added by those mods. Thanks to <a href="https://github.com/McJtyMods/TheOneProbe/blob/9c1e1d94c30d795dc22ad35f752dbb8182250220/src/main/java/mcjty/theoneprobe/Tools.java">the one probe</a>
	 * for this one.
	 * 	
	 * @param entities Collection of entities to be sorted by mod id
	 * @return Multimap of mod ids to entities added by those mods
	 */
    public static Multimap<String, Class<? extends Entity>> 
    	createModIDToEntityClassMapping( Collection<EntityEntry> entities ) {
    	
    	Multimap<String, Class<? extends Entity>> modIDToEntityClassMapping = HashMultimap.create(128, 32);
    	for ( EntityEntry entry : entities ){
    		EntityRegistry.EntityRegistration modSpawn = EntityRegistry.instance().lookupModSpawn(entry.getEntityClass(), true);
    		String modID;
    		if ( modSpawn == null ){
    			modID = "Minecraft";
    		}
    		else {
    			ModContainer container = modSpawn.getContainer();
    			if ( container == null ){
    				modID = "Minecraft";
    			}
    			else {
    	    		modID = container.getModId();    				
    			}
    		}
    		modIDToEntityClassMapping.put(modID, entry.getEntityClass());
    	}
        return modIDToEntityClassMapping;
}	
	
	/**
	 * Create an EntitySorting with (currently hardcoded) sorters
	 * 
	 * @param entitySorter
	 * @param world
	 * @return
	 */
	public static EntitySorting createEntitySorting( final EntitySorter entitySorter,
			final World world ) {
		
		final IEntityGroupSorter bossSorter = new BossSorter(world);
		final IEntityGroupSorter monsterSorter = new MonsterSorter(bossSorter);
		final IEntityGroupSorter passiveSorter = new PassiveSorter(bossSorter, monsterSorter);
		
		Map<String, IEntityGroupSorter> sorters = new HashMap<>();
		sorters.put(ServerResourceLocator.BOSS_STRING, bossSorter);
		sorters.put(ServerResourceLocator.MONSTER_STRING, monsterSorter);
		sorters.put(ServerResourceLocator.PASSIVE_STRING, passiveSorter);
		
		return entitySorter.sort(sorters);
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
				SwordStat.LOGGER.error("Could not initialise entity of " + entityClass + ", skipping...");
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
