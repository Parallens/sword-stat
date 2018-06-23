package swordstat.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.Main;
import swordstat.init.EntitySorter;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySorter.IEntityGroupSorter;

public final class SwordStatResourceLocator {
	
	private static final EntitySorter ENTITY_SORTER = 
			new EntitySorter(ForgeRegistries.ENTITIES.getValuesCollection());
	private static EntitySorting ENTITY_SORTING = null;
	
	public static final String BOSS_STRING = "bosses";
	public static final String MONSTER_STRING = "monsters";
	public static final String PASSIVE_STRING = "passives";
	
	private static EntityHelper ENTITY_HELPER = null;
	
	public static EntityHelper getEntityHelper( World world ) {
	
		if ( ENTITY_HELPER == null ){
			ENTITY_HELPER = new EntityHelper(world);
		}
		return ENTITY_HELPER;
	}
	
	public static EntitySorting getEntitySorting() {
		
		if ( ENTITY_SORTING == null ){
			Map<String, IEntityGroupSorter> sorters = new HashMap<>();
			sorters.put(BOSS_STRING, bossSorter);
			sorters.put(MONSTER_STRING, monsterSorter);
			sorters.put(PASSIVE_STRING, passiveSorter);
			ENTITY_SORTING = ENTITY_SORTER.sort(sorters);
		}
		return ENTITY_SORTING;
	}

	private static IEntityGroupSorter bossSorter = new IEntityGroupSorter() {
		
		@Override
		public boolean isInGroup( Class<? extends Entity> entityClass ) {
		
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				return false;
			}
			Entity entity;
			try {
				entity = entityClass.getConstructor(World.class)
						.newInstance(Minecraft.getMinecraft().world);
			} catch ( Exception e ){
				Main.LOGGER.error("Could not initialise entity of " + entityClass + ", skipping...");
				return false;
			}
			return EntityMob.class.isAssignableFrom(entityClass);
		}
	};
	
	private static IEntityGroupSorter monsterSorter = new IEntityGroupSorter() {
		
		@Override
		public boolean isInGroup( Class<? extends Entity> entityClass ) {
		
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				return false;
			}
			return EntityMob.class.isAssignableFrom(entityClass) && 
					!bossSorter.isInGroup(entityClass);
		}
	};
	
	private static IEntityGroupSorter passiveSorter = new IEntityGroupSorter() {
		
		@Override
		public boolean isInGroup( Class<? extends Entity> entityClass ) {
		
			if ( Modifier.isAbstract(entityClass.getModifiers()) ){
				return false;
			}
			return !( bossSorter.isInGroup(entityClass) || monsterSorter.isInGroup(entityClass) );
		}
	};	
}
