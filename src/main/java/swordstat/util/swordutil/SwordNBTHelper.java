package swordstat.util.swordutil;

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
import swordstat.swordinfo.SwordDataEnum;
import swordstat.util.ServerResourceLocator;

public final class SwordNBTHelper {
	
	private final Map<String, Class<? extends Entity>> entityStringToClassMapping;
	
	public SwordNBTHelper( final EntitySorting entitySorting ) {
		
		this.entityStringToClassMapping = SwordStat.CLIENT_RESOURCE_LOCATOR
				.getEntityClassNameToEntityClassMapping();
	}
	
	/**
	 * 
	 * @param sword ItemStack to be attached with an NBT sub-compound
	 * @param wasCrafted Whether this method has been called following the 
	 * 		  crafting of the passed item
	 * @throws IllegalArgumentException
	 */
	public void attachNBT( ItemStack sword, boolean wasCrafted, World worldObj )
		throws IllegalArgumentException {
		
		if ( sword.hasTagCompound() && sword.getTagCompound().hasKey(SwordStat.MODID) ){
			throw new IllegalArgumentException(
					"Sword already has an appropriate NBT sub-compound attached!"
			);
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
	}
	
	public void incNBTData( ItemStack sword, Class<? extends Entity> entityClass )  
				throws IllegalArgumentException {
		
		if ( ! itemStackValidCheck(sword) ){
			throw new IllegalArgumentException("Invalid itemstack!");
		}
		
		// Get NBT and edit it.
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
	}
	
	public void incNBTData( ItemStack sword, SwordDataEnum data ) 
		throws IllegalArgumentException {
		
		if ( ! itemStackValidCheck(sword) ){
			throw new IllegalArgumentException("Invalid itemstack!");
		}
		SwordDataEnum[] validData = {
				SwordDataEnum.TOTAL_KILLS,
				SwordDataEnum.PLAYER_KILLS
		}; boolean valid = false;
		for ( SwordDataEnum swordData : validData ){
			if ( swordData.equals(data) ){
				valid = true; break;
			}
		}
		// Get NBT and edit it.
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(SwordStat.MODID);
		int kills = tag.getInteger(data.toString());
		kills++;
		tag.setInteger(data.toString(), kills);
	}
	
	public void updateNBTData( ItemStack sword, World world )
			throws IllegalArgumentException {
		
		// mappings have the current updated list of mobs.

		if ( ! itemStackValidCheck(sword) ){
			throw new IllegalArgumentException("Invalid itemstack!");
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
	}
	
	private boolean itemStackValidCheck( ItemStack itemStack ) {
		
		// Mindf@ck
		if ( 
				! (itemStack.hasTagCompound() &&
				itemStack.getTagCompound().hasKey(SwordStat.MODID)) 
		){
			return false;
		}
		return true;
		
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
