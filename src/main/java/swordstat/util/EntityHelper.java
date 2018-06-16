package swordstat.util;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.Main;

public final class EntityHelper {
	
	private Collection<EntityEntry> entities = ForgeRegistries.ENTITIES.getValuesCollection();
	private Map<String, Class<? extends Entity>> bossMap;
	private Map<String, Class<? extends Entity>> monsterMap;
	private Map<String, Class<? extends Entity>> passiveMap;
	
	private static EntityHelper INSTANCE;
	
	public EntityHelper( World worldObj ) {
		
		// Should be EntityLiving
		bossMap = new TreeMap<String, Class<? extends Entity>>();
		monsterMap = new TreeMap<String, Class<? extends Entity>>();
		passiveMap = new TreeMap<String, Class<? extends Entity>>();
		//We need to sort the mappings as well!
		for ( EntityEntry entry : entities ){
			Class<? extends Entity> entityClass = entry.getEntityClass();
			/*
			 * If the entity is a boss, add it to the bossMap
			 * If the entity is a monster, add it to the monsterMap
			 * If the entity is a subclass of entityLiving add it to the passiveMap
			 */
			Entity entity = null;
			String entityName = entry.getName();
			// We want to ignore the abstract classes
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				continue;
			}
			try {
				// This is needed otherwise the entity will not be rendered by the GUI
				entity = entityClass.getConstructor(World.class).newInstance(worldObj);
			} catch ( Exception e ){
				Main.logger.error("Could not initialise entity of " + entityClass + ", skipping...");
			}
			// I think this is only for 1.10, for 1.7.10 I think bosses
			// implement a special interface for their health bar.
			if ( entity != null && ! entity.isNonBoss() ){
				bossMap.put(entityName, entityClass);
			}
			else if ( EntityMob.class.isAssignableFrom(entityClass) ){
				monsterMap.put(entityName, entityClass);
			}
			else if ( EntityLiving.class.isAssignableFrom(entityClass) ){
				passiveMap.put(entityName, entityClass);
			}
		}
	}
	
	public static EntityHelper getInstance( World world ) {
		
		if ( INSTANCE == null ){
			INSTANCE = new EntityHelper(world);
		}
		return INSTANCE;
	}
	
	//TODO make these unmodifiable
	public Map<String, Class<? extends Entity>> getBossMap() {
		
		return bossMap;
	}
	
	public Map<String, Class<? extends Entity>> getMonsterMap() {
		
		return monsterMap;
	}
	
	public Map<String, Class<? extends Entity>> getPassiveMap() {
		
		return passiveMap;
	}
}
