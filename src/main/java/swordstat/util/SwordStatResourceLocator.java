package swordstat.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
	
	/**
	 * Equivalent to the most recent call of createEntitySortingWith()
	 * 
	 * @return
	 */
	public static EntitySorting getEntitySorting() {
		
		if ( ENTITY_SORTING == null ){
			// if null create empty sorting
			ENTITY_SORTING = ENTITY_SORTER.sort(new HashMap<String, IEntityGroupSorter>());
		}
		return ENTITY_SORTING;
	}

	public static EntitySorting createEntitySortingWith( Map<String, IEntityGroupSorter> sorters ) {
		
		ENTITY_SORTING = ENTITY_SORTER.sort(sorters);
		return ENTITY_SORTING;
	}
}
