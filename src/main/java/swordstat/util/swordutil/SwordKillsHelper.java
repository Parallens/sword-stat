package swordstat.util.swordutil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.SwordStatResourceLocator;


public final class SwordKillsHelper {
	
	private static final Pattern entitySplit = Pattern.compile("\\.(entity)|(entities)\\.");
	
	/** The NBTTagCompound stored on the sword. **/
	private final NBTTagCompound swordTag;
	private final int[] bossKills, monsterKills, passiveKills;
	
	private Map<String, Integer> bossKillsMapping,  monsterKillsMapping, passiveKillsMapping;
	// Old TreeMaps
	private Map<String, Class<? extends Entity>> bossClassMapping,  monsterClassMapping, passiveClassMapping;
	private Map<String, Integer> entityStringToKillsMapping;
	private Map<String, Class<? extends Entity>> entityStringToClassMapping;
	private TreeMap<String, Set<String>> modToEntityMapping;
	
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
		entityStringToClassMapping = new HashMap<String, Class<? extends Entity>>();
		bossKillsMapping = new HashMap<String, Integer>();
		monsterKillsMapping =  new HashMap<String, Integer>();
		passiveKillsMapping = new HashMap<String, Integer>();
		int i = 0;
		for ( Entry<String, Class<? extends Entity>> entry : bossClassMapping.entrySet() ){
			bossKillsMapping.put(entry.getValue().toString(), bossKills[i]);
			entityStringToClassMapping.put(entry.getValue().toString(), entry.getValue());i++;
		}
		i = 0;
		for ( Entry<String, Class<? extends Entity>> entry : monsterClassMapping.entrySet() ){
			monsterKillsMapping.put(entry.getValue().toString(), monsterKills[i]);
			entityStringToClassMapping.put(entry.getValue().toString(), entry.getValue());i++;
		}
		i = 0;
		for ( Entry<String, Class<? extends Entity>> entry : passiveClassMapping.entrySet() ){
			passiveKillsMapping.put(entry.getValue().toString(), passiveKills[i]);
			entityStringToClassMapping.put(entry.getValue().toString(), entry.getValue());i++;
		}
		entityStringToKillsMapping = new HashMap<String, Integer>();
		entityStringToKillsMapping.putAll(bossKillsMapping);
		entityStringToKillsMapping.putAll(monsterKillsMapping);
		entityStringToKillsMapping.putAll(passiveKillsMapping);
		// Now create the different
		modToEntityMapping = new TreeMap<String, Set<String>>();
		for ( String className : entityStringToKillsMapping.keySet() ){
			String[] split = entitySplit.split(className);
			if ( split.length == 2 ){
				if ( modToEntityMapping.containsKey(split[0]) ){
					modToEntityMapping.get(split[0]).add(className);
				}
				else {
					modToEntityMapping.put(split[0], new HashSet<String>(Arrays.asList(new String[] {className})));
				}
			}
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
	
	/**
	 * Get the "type" of an entity, aka which button it should appear in. If the entity is not
	 * deemed to be in any of the registered types then null is returned.
	 * 
	 * @param entityClassString
	 * @return
	 */
	public EntityType getEntityType( String entityClassString ) {
		
		if ( bossKillsMapping.containsKey(entityClassString) ){
			return EntityType.BOSS;
		}
		else if ( monsterKillsMapping.containsKey(entityClassString) ){
			return EntityType.MONSTER;
		}
		else if ( passiveKillsMapping.containsKey(entityClassString) ){
			return EntityType.PASSIVE;
		}
		else {
			return null;
		}
	}
	
	public int getNumberOfModMappings() {
		
		return modToEntityMapping.keySet().size();
	}
	
	public Set<String> getNonEmptypModStrings() {
		
		Set<String> nonEmptyMapping = new TreeSet<String>();
		for ( Entry<String, Set<String>> entry : modToEntityMapping.entrySet() ){
			if ( entry.getValue().size() != 0 ){
				nonEmptyMapping.add(entry.getKey());
			}
		}
		return nonEmptyMapping;
	}
	
	/**
	 * Get a list of mod strings, including the vanilla string!
	 * @return
	 */
	public Set<String> getModStrings() {
		
		return modToEntityMapping.keySet();
	}
	
	public String getVanillaString() {
		
		for ( String mod : modToEntityMapping.keySet() ){
			if ( mod.contains("net.minecraft") ){
				return mod;
			}
		}
		return null;
	}
	
	public Set<String> getEntityStringsFromMod( String modString ) {
		
		return modToEntityMapping.get(modString);
	}
	
	public int getEntityKillsFromString( String entityClassString ) {
		
		return entityStringToKillsMapping.get(entityClassString);
	}
	
	/**
	 * Get the class object of an entity from its class object's string(the class object's toString
	 * method.
	 * 
	 * @param entityClassString
	 * @return
	 */
	public Class<? extends Entity> getEntityClassFromString( String entityClassString ) {
		
		return entityStringToClassMapping.get(entityClassString);
	}
	
	public int getTotalKills() {
		
		int total = 0;
		for ( Integer kill : entityStringToKillsMapping.values() ){
			total += kill;
		}
		return total;
	}
}