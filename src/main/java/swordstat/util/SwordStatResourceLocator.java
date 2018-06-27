package swordstat.util;

import java.util.HashMap;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.init.EntitySorter;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySorter.IEntityGroupSorter;

public class SwordStatResourceLocator {
	
	public static final String BOSS_STRING = "bosses";
	public static final String MONSTER_STRING = "monsters";
	public static final String PASSIVE_STRING = "passives";

	private final EntitySorter entitySorter = 
			new EntitySorter(ForgeRegistries.ENTITIES.getValuesCollection());
	private EntitySorting entitySorting = null;
	
	public EntitySorter getEntitySorter() {
		
		return entitySorter;
	}
	
	public EntitySorting getEntitySorting() {
		
		if ( entitySorting == null ){
			// if null create empty sorting
			entitySorting = entitySorter.sort(new HashMap<String, IEntityGroupSorter>());
		}
		return entitySorting;
	}
	
	public void setEntitySorting( EntitySorting entitySorting ) {
		
		this.entitySorting = entitySorting;
	}	
}
