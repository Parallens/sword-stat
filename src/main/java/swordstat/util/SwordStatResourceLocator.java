package swordstat.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.init.EntitySorter;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.init.EntitySorter.IEntityGroupSorter;
import swordstat.init.EntitySortingInit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

public class SwordStatResourceLocator {
	
	public static final String BOSS_STRING = "bosses";
	public static final String MONSTER_STRING = "monsters";
	public static final String PASSIVE_STRING = "passives";

	private final EntitySorter entitySorter = 
			new EntitySorter(ForgeRegistries.ENTITIES.getValuesCollection());
	private EntitySorting entitySorting = null;
	private ImmutableMap<String, Class<? extends Entity>> entityClassNameToEntityClassMapping;
	private ImmutableMultimap<String, Class<? extends Entity>> modIDToEntityClassMapping;
	private Map<String, ItemStack> modIDToItemStackIconMapping = new HashMap<>();
	
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
	
	public ImmutableMultimap<String, Class<? extends Entity>> getModIDToEntityClassMapping() {
		
		if ( modIDToEntityClassMapping == null ){
			initialiseModIDToEntityClassMapping();
		}
		return modIDToEntityClassMapping;
	}
	
	private void initialiseModIDToEntityClassMapping() {

		modIDToEntityClassMapping = ImmutableMultimap.<String, Class<? extends Entity>>builder()
				.putAll(EntitySortingInit.
						createModIDToEntityClassMapping(ForgeRegistries.ENTITIES.getValuesCollection()))
				.build();
	}

	public ItemStack getModItemStackIconFromModID( String modID ) {
		
		if ( !modIDToItemStackIconMapping.containsKey(modID) ){
			String modName = getModNameFromID(modID);
			// try creative tabs
			for ( CreativeTabs creativeTab : CreativeTabs.CREATIVE_TAB_ARRAY ){
				if ( creativeTab.getTabLabel().equals(modName) || creativeTab.toString().contains(modID) ){
					modIDToItemStackIconMapping.put(modID, creativeTab.getIconItemStack());
					return modIDToItemStackIconMapping.get(modID);
				}
			}
			// try to find an item belonging to the mod and use that, is there a faster way than iterating through all
			// existing items?
			for ( Item item : ForgeRegistries.ITEMS.getValuesCollection() ){
				if ( item.getRegistryName().getResourceDomain().equals(modID) ){
					modIDToItemStackIconMapping.put(modID, new ItemStack(item));
					return modIDToItemStackIconMapping.get(modID);	
				}
			}
			// Everything failed: return a random vanilla item
			modIDToItemStackIconMapping.put(modID, new ItemStack(Items.APPLE));
			return modIDToItemStackIconMapping.get(modID);
		}
		return modIDToItemStackIconMapping.get(modID);
	}
	
	public ImmutableMap<String, Class<? extends Entity>> getEntityClassNameToEntityClassMapping() {
		
		if ( entityClassNameToEntityClassMapping == null ){
			initialiseEntityClassNameToEntityClassMapping();
		}
		return entityClassNameToEntityClassMapping;
	}
	
	private void initialiseEntityClassNameToEntityClassMapping() {

		HashMap<String, Class<? extends Entity>> map = new HashMap<>();
		for ( EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection() ){
			map.put(entry.getEntityClass().getName(), entry.getEntityClass());
		}
		entityClassNameToEntityClassMapping =
				ImmutableMap.<String, Class<? extends Entity>>builder().putAll(map).build();
	}
	
	
	public String getModNameFromID( final String modID ) {
		
        Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
        for ( Map.Entry<String, ModContainer> modEntry : modMap.entrySet() ){
        	if ( modEntry.getValue().getModId().equals(modID) ){
        		return modEntry.getValue().getName();
        	}
        }
        return "unknown";
	}
}
