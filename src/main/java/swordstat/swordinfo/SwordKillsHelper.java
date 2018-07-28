package swordstat.swordinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import scala.actors.threadpool.Arrays;
import swordstat.SwordStat;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

/**
 * Encapsulates sword information pertaining to how many 'kills' a sword has.
 */
public final class SwordKillsHelper {
	
	/** The NBTTagCompound stored on the sword. **/
	private final NBTTagCompound swordTag;
	private final int[] entityKills;
	private Map<String, Integer> entityStringToKillsMapping =  new HashMap<>();
	private ImmutableBiMap<String, Class<? extends Entity>> entityStringToClassBiMap;
	
	public SwordKillsHelper( final NBTTagCompound swordTag, 
			ImmutableMap<String, Class<? extends Entity>> entityClassNameToEntityClassMapping ) {
		
		this.swordTag = swordTag;
		this.entityKills = swordTag.getIntArray(SwordDataEnum.ENTITY_KILLS_ARRAY.toString());

		this.entityStringToClassBiMap = ImmutableBiMap
				.<String, Class<? extends Entity>>builder().putAll(entityClassNameToEntityClassMapping)
				.build();
		
		NBTTagList entityNameTagList= swordTag.getTagList(SwordDataEnum.ENTITY_NAMES_ARRAY.toString(),
				Constants.NBT.TAG_COMPOUND);
		for ( int i = 0; i < entityNameTagList.tagCount(); i++ ){
			NBTTagCompound monoTag;
			try {
				monoTag = (NBTTagCompound) entityNameTagList.get(i);
			}
			catch ( ClassCastException e ){
				continue;
			}
			String tagClass;
			try {
				tagClass = monoTag.getKeySet().iterator().next();				
			}
			catch ( NoSuchElementException e ){
				continue;
			}
			entityStringToKillsMapping.put(tagClass, (i >= entityKills.length)? 0 : entityKills[i]);
		}
	}
	
	/**
	 * Attempt to infer the creative tab a mod belongs to. If a creative cannot be found/guessed, 
	 * null is returned.
	 * 
	 * @param modClassString
	 * @return
	 */
	public CreativeTabs getModCreativeTab( String modClassString ) {
		
		String[] modSplit = modClassString.split("\\.");
		for ( CreativeTabs creativeTab : CreativeTabs.CREATIVE_TAB_ARRAY ){
			String creativeTabClassString = creativeTab.getClass().toString();
			if ( creativeTabClassString.contains(modClassString) ){
				return creativeTab;
			}
			String[] tabSplit = creativeTabClassString.split("\\.");
			try {
				if ( modSplit[0].equals(tabSplit[0]) && modSplit[1].equals(tabSplit[1]) ){
					return creativeTab;
				}
			}
			catch ( Exception e ){}
		}
		return null;
	}
	
	public int getNumberOfModMappings() {
		
		return SwordStat.CLIENT_RESOURCE_LOCATOR.getModIDToEntityClassMapping().keys().size();
	}
	
	/**
	 * Uses Class String of the entity.
	 * 
	 * @param entityString
	 * @return
	 */
	public int getEntityKillsFromString( String entityString ) {
		
		if ( entityStringToKillsMapping.containsKey(entityString) ){
			return entityStringToKillsMapping.get(entityString);
		}
		else {
			return 0;
		}
	}
	
	public String getEntityStringFromClass( Class<? extends Entity> entityClass ) {
		
		String entityString = entityStringToClassBiMap.inverse().get(entityClass);
		return (entityString == null)? "unknown" : entityString;
	}
	
	public int getEntityKillsFromClass( Class<? extends Entity> entityClass ) {
		
		String entityString = entityStringToClassBiMap.inverse().get(entityClass);
		if ( entityString == null ){
			return 0;
		}
		else {
			return getEntityKillsFromString(entityString);
		}
	}
	
	/**
	 * Get the class object of an entity from its class object's string(the class object's toString
	 * method.
	 * 
	 * @param entityClassString
	 * @return
	 */
	public Class<? extends Entity> getEntityClassFromString( String entityClassString ) {
		
		return entityStringToClassBiMap.get(entityClassString);
	}
	
	public int getTotalKills() {
		
		int total = 0;
		for ( int kill : entityStringToKillsMapping.values() ){
			total += kill;
		}
		return total;
	}
}