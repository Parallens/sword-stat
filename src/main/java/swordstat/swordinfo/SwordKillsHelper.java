package swordstat.swordinfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.SwordStatResourceLocator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class SwordKillsHelper {
	
	private static final Pattern entitySplit = Pattern.compile("\\.(entity)|(entities)\\.");
	
	/** The NBTTagCompound stored on the sword. **/
	private final NBTTagCompound swordTag;
	private final int[] bossKills, monsterKills, passiveKills;
	
	private Map<String, Integer> bossKillsMapping,  monsterKillsMapping, passiveKillsMapping;
	// Old TreeMaps
	private Map<String, Class<? extends Entity>> bossClassMapping,  monsterClassMapping, passiveClassMapping;
	private Map<String, Integer> entityStringToKillsMapping;
	private BiMap<String, Class<? extends Entity>> entityStringToClassBiMap;
	
	public enum EntityType {
		BOSS, MONSTER, PASSIVE
	};
	
	public SwordKillsHelper( final NBTTagCompound swordTag, final EntitySorting entitySorting ) {
		
		this.swordTag = swordTag;
		bossKills = swordTag.getIntArray(SwordDataEnum.BOSS_KILLS.toString());
		monsterKills = swordTag.getIntArray(SwordDataEnum.MONSTER_KILLS.toString());
		passiveKills = swordTag.getIntArray(SwordDataEnum.PASSIVE_KILLS.toString());
		// We want:
		// A mapping of string classes to class objects
		// A mapping of string classes to kills
		// A mapping of mods to sets containing string classes
		bossClassMapping = entitySorting.getSorting(SwordStatResourceLocator.BOSS_STRING);
		monsterClassMapping = entitySorting.getSorting(SwordStatResourceLocator.MONSTER_STRING);
		passiveClassMapping = entitySorting.getSorting(SwordStatResourceLocator.PASSIVE_STRING);
		entityStringToClassBiMap = HashBiMap.create();
		bossKillsMapping = new HashMap<String, Integer>();
		monsterKillsMapping =  new HashMap<String, Integer>();
		passiveKillsMapping = new HashMap<String, Integer>();
		int i = 0;
		for ( Entry<String, Class<? extends Entity>> entry : bossClassMapping.entrySet() ){
			bossKillsMapping.put(entry.getValue().toString(), bossKills[i]);
			entityStringToClassBiMap.put(entry.getValue().toString(), entry.getValue());i++;
		}
		i = 0;
		for ( Entry<String, Class<? extends Entity>> entry : monsterClassMapping.entrySet() ){
			monsterKillsMapping.put(entry.getValue().toString(), monsterKills[i]);
			entityStringToClassBiMap.put(entry.getValue().toString(), entry.getValue());i++;
		}
		i = 0;
		for ( Entry<String, Class<? extends Entity>> entry : passiveClassMapping.entrySet() ){
			passiveKillsMapping.put(entry.getValue().toString(), passiveKills[i]);
			entityStringToClassBiMap.put(entry.getValue().toString(), entry.getValue());i++;
		}
		entityStringToKillsMapping = new HashMap<>();
		entityStringToKillsMapping.putAll(bossKillsMapping);
		entityStringToKillsMapping.putAll(monsterKillsMapping);
		entityStringToKillsMapping.putAll(passiveKillsMapping);
		// Now create the different
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

	
	public Set<Class<? extends Entity>> getEntityStringsFromMod( String modID ) {

		if ( modID == null ){
			return new HashSet<Class<? extends Entity>>();
		}
		else {
			return new HashSet<>(SwordStat.CLIENT_RESOURCE_LOCATOR.getModIDToEntityClassMapping().get(modID));
		}
	}
	
	public int getEntityKillsFromString( String entityString ) {
		
		return entityStringToKillsMapping.get(entityString);
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
		for ( Integer kill : entityStringToKillsMapping.values() ){
			total += kill;
		}
		return total;
	}
}