package swordstat.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.init.EntitySorter;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySorter.IEntityGroupSorter;
import swordstat.util.swordutil.SwordNBTHelper;

public final class SwordStatResourceLocator {
	
	private static final EntitySorter ENTITY_SORTER = 
			new EntitySorter(ForgeRegistries.ENTITIES.getValuesCollection());
	private static EntitySorting ENTITY_SORTING = null;
	private static SwordNBTHelper SWORD_NBT_HELPER = null;
	
	public static final String BOSS_STRING = "bosses";
	public static final String MONSTER_STRING = "monsters";
	public static final String PASSIVE_STRING = "passives";
	
	public static SwordNBTHelper getSwordNBTHelper() {
		
		if ( SWORD_NBT_HELPER == null ){
			SWORD_NBT_HELPER = new SwordNBTHelper(getEntitySorting());
		}
		return SWORD_NBT_HELPER;
	}
	
	public static final EntitySorter getEntitySorter() {
		
		return ENTITY_SORTER;
	}
	
	public static EntitySorting getEntitySorting() {
		
		if ( ENTITY_SORTING == null ){
			// if null create empty sorting
			ENTITY_SORTING = ENTITY_SORTER.sort(new HashMap<String, IEntityGroupSorter>());
		}
		return ENTITY_SORTING;
	}
	
	public static void setEntitySorting( EntitySorting entitySorting ) {
		
		ENTITY_SORTING = entitySorting;
	}
}
