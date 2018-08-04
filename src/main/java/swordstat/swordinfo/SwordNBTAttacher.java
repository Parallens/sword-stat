package swordstat.swordinfo;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.SwordStat;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.ServerResourceLocator;

/**
 * Class responsible for attaching and manipulating NBT used by Sword Stat. 
 */
public final class SwordNBTAttacher {
	
	private final Map<String, Class<? extends Entity>> entityStringToClassMapping;
	
	public SwordNBTAttacher( final EntitySorting entitySorting ) {
		
		this.entityStringToClassMapping = SwordStat.CLIENT_RESOURCE_LOCATOR
				.getEntityClassNameToEntityClassMapping();
	}
	
	/**
	 * Attach NBT to the given sword. Returns true if this was successful, and false
	 * if it was not (sword already has a tag sub-compound belonging to this mod attached).
	 * 
	 * @param sword ItemStack to be attached with an NBT sub-compound
	 * @param wasCrafted Whether this method has been called following the 
	 * 		  crafting of the passed item
	 * @return true if NBT was attached, false otherwise
	 */
	public boolean attachNBT( ItemStack sword, boolean wasCrafted, World worldObj ) {
		
		if ( isSwordStatNBTAttached(sword) ){
			return false;  // No need to attach NBT to an item that already has it
		}

		NBTTagCompound compound = new NBTTagCompound();
		// Add as sub-compound to minimize conflicts.
		Date dNow = new Date();
		String key1;
		if ( wasCrafted ){
			key1 = SwordDataEnum.IRL_DATE_CRAFTED.toString();
		}
		else {
			key1 = SwordDataEnum.IRL_DATE_FOUND.toString();
		}
		compound.setString(
				key1,
				dNow.toString()
		);

		compound.setString(
				SwordDataEnum.INGAME_AGE.toString(),
				Long.toString(worldObj.getTotalWorldTime())
				//Long.toString(Minecraft.getMinecraft().theWorld.getTotalWorldTime())
		);
		compound.setInteger(
				SwordDataEnum.TOTAL_KILLS.toString(),
				0
		);
		compound.setInteger(
				SwordDataEnum.PLAYER_KILLS.toString(),
				0
		);
		
		compound.setIntArray(
				SwordDataEnum.ENTITY_KILLS_ARRAY.toString(),
				new int[entityStringToClassMapping.size()]
		);
		
		compound.setTag(
				SwordDataEnum.ENTITY_NAMES_ARRAY.toString(),
				getNBTList(entityStringToClassMapping)
		);
		
		NBTTagCompound baseCompound = 
				(sword.hasTagCompound())? sword.getTagCompound().copy(): new NBTTagCompound();
		baseCompound.setTag(SwordStat.MODID, compound);
		sword.setTagCompound(baseCompound);
		return true;
	}
	
	/**
	 * Increment the number of kills for the specified entity for the specified sword.
	 * 
	 * @param sword the sword to increment kills for 
	 * @param entityClass the class of the entity that was killed
	 * @return true if incrementation was successful, false otherwise
	 */
	public boolean incKillsForEntity( ItemStack sword, Class<? extends Entity> entityClass ) {
		
		if ( !isSwordStatNBTAttached(sword) ){
			return false;
		}
		
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(SwordStat.MODID);
		int[] kills = tag.getIntArray(SwordDataEnum.ENTITY_KILLS_ARRAY.toString());
		NBTTagList tagList = tag.getTagList(SwordDataEnum.ENTITY_NAMES_ARRAY.toString(),
				Constants.NBT.TAG_COMPOUND);
		
		for ( int i = 0; i < tagList.tagCount(); i++ ){
			NBTTagCompound monoTag;
			String tagClass;
			try {
				monoTag = (NBTTagCompound) tagList.get(i);
				// The tag only has one element that's an int so this is ok.
				tagClass = monoTag.getKeySet().iterator().next();
			}
			catch ( Exception e ){
				continue;
			}
			if ( tagClass.equals(entityClass.getName()) ){
				int index = monoTag.getInteger(tagClass);
				kills[index]++;
				break; // We've found the correct entity.
			}
		}
		tag.setIntArray(SwordDataEnum.ENTITY_KILLS_ARRAY.toString(), kills);
		return true;
	}
	
	/**
	 * Increment total kills of a sword.
	 * 
	 * @param sword the sword whose total kills are to be incremented
	 * @return true if kills were incremented correctly false otherwise
	 */
	public boolean incTotalKills( ItemStack sword ) {
		
		if ( !isSwordStatNBTAttached(sword) ){
			return false;
		}
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(SwordStat.MODID);
		int kills = tag.getInteger(SwordDataEnum.TOTAL_KILLS.toString());
		tag.setInteger(SwordDataEnum.TOTAL_KILLS.toString(), kills + 1);
		return true;
	}
	
	/**
	 * Increment total player kills of a sword.
	 * 
	 * @param sword the sword whose total player kills are to be incremented
	 * @return true if kills were incremented correctly false otherwise
	 */
	public boolean incPlayerKills( ItemStack sword ) {
		
		if ( !isSwordStatNBTAttached(sword) ){
			return false;
		}
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(SwordStat.MODID);
		int kills = tag.getInteger(SwordDataEnum.PLAYER_KILLS.toString());
		tag.setInteger(SwordDataEnum.PLAYER_KILLS.toString(), kills + 1);
		return true;
	}	
	
	/**
	 * Update NBT data attached to a sword so that newly added mobs (added by new mods)
	 * do not mix up NBT data currently stored on swords.
	 * 
	 * @param sword sword to update
	 * @param world world object required
	 * @return true if NBT data was attached successfully, false otherwise
	 */
	public boolean updateNBTData( ItemStack sword, World world ) {
		
		// mappings have the current updated list of mobs.
		if ( !isSwordStatNBTAttached(sword) ){
			return false;
		}
		
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(SwordStat.MODID);
		
		NBTTagList entityNamesTagList = tag.getTagList(
				SwordDataEnum.ENTITY_NAMES_ARRAY.toString(), Constants.NBT.TAG_COMPOUND
		);
		int[] entityKills = tag.getIntArray(SwordDataEnum.ENTITY_KILLS_ARRAY.toString());
		tag.setTag(
				SwordDataEnum.ENTITY_NAMES_ARRAY.toString(),
				getNBTList(entityStringToClassMapping)
		);
		tag.setIntArray(
				SwordDataEnum.ENTITY_KILLS_ARRAY.toString(),
				updatedArray(entityStringToClassMapping, entityNamesTagList, entityKills)
		);
		return true;
	}
	
	/**
	 * Return true if sword has the Sword Stat NBT sub-compound attached to it.
	 * 
	 * @param itemStack the item stack to check
	 * @return true if the sword has the sub-compound attached, false otherwise
	 */
	public boolean isSwordStatNBTAttached( ItemStack itemStack ) {
		
		if ( itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(SwordStat.MODID) ){ 
			return true;
		}
		return false;
		
	}
	
	private static NBTTagList getNBTList(
			Map<String, Class<? extends Entity>> mapping ) {
		
		NBTTagList NBTList = new NBTTagList();
		int i = 0;
		for ( Class<? extends Entity> entityClass : mapping.values() ){
			NBTTagCompound shortTag = new NBTTagCompound();
			String entityClassString = entityClass.getName();
			shortTag.setInteger(entityClassString, i);
			NBTList.appendTag(shortTag);
			i++;
		}
		//System.out.println("NBT: " + NBTList);
		return NBTList;
	}
	
	private static int[] updatedArray(
			Map<String, Class<? extends Entity>> mapping, NBTTagList old,
			int[] arr ){
		
		int[] newKillArr = new int[mapping.keySet().size()];
		int i = 0;
		for ( Entry<String, Class<? extends Entity>> entry: mapping.entrySet() ){
			Class<? extends Entity> entityClass = entry.getValue();
			for ( int j = 0; j < old.tagCount(); j++ ){
				NBTTagCompound monoTag = (NBTTagCompound) old.get(j);
				// The tag only has one element that's an int so this is ok.
				String tagClass = monoTag.getKeySet().iterator().next();
				if ( entityClass.getName().equals(tagClass) ){
					newKillArr[i] = arr[j];
				}
			}
			i++;
		}
		return newKillArr;
	}
}
