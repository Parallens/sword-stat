package swordstat.util.swordutil;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import swordstat.Main;
import swordstat.util.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public final class SwordNBTHelper {
	
	public EntityHelper entityHandler;
	
	public SwordNBTHelper() {
		
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
		
		if ( ! (sword.getItem() instanceof ItemSword) ){
			throw new IllegalArgumentException(
					"Itemstack is not a sword!"
			);
		}
		if ( sword.hasTagCompound() && sword.getTagCompound().hasKey(Main.MODID) ){
			throw new IllegalArgumentException(
					"Sword already has an appropriate NBT sub-compound attached!"
			);
		}
		/*
		 * We attach: 
		 * total kills
		 * date(irl and in game) sword created if applicable
		 * mobs mapped to kills
		 * 
		 * We don't attach:
		 * durability/charge
		 * current master
		 * sword type/name
		 * times repaired?
		 * cooldown?
		 */
		entityHandler = new EntityHelper(worldObj);
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
				SwordDataEnum.BOSS_KILLS.toString(),
				new int[entityHandler.getBossMap().size()]
		);
		compound.setIntArray(
				SwordDataEnum.MONSTER_KILLS.toString(),
				new int[entityHandler.getMonsterMap().size()]
		);
		compound.setIntArray(
				SwordDataEnum.PASSIVE_KILLS.toString(),
				new int[entityHandler.getPassiveMap().size()]
		);
		
		/* Now for the names of the mobs: 
		 * These are necessary as well as the integer arrays in order for the
		 * mod to update the kills correctly if new mod that adds entities is
		 * added.
		 */
		
		compound.setTag(
				SwordDataEnum.BOSS_NAMES.toString(),
				getNBTList(entityHandler.getBossMap())
		);
		compound.setTag(
				SwordDataEnum.MONSTER_NAMES.toString(),
				getNBTList(entityHandler.getMonsterMap())
		);
		compound.setTag(
				SwordDataEnum.PASSIVE_NAMES.toString(),
				getNBTList(entityHandler.getPassiveMap())
		);
		
		NBTTagCompound baseCompound = 
				(sword.hasTagCompound())? sword.getTagCompound().copy(): new NBTTagCompound();
		baseCompound.setTag(Main.MODID, compound);
		sword.setTagCompound(baseCompound);
	}
	
	public void incNBTData( ItemStack sword, SwordDataEnum data,
		Class<? extends Entity> entityClass )  
				throws IllegalArgumentException {
		
		if ( ! itemStackValidCheck(sword) ){
			throw new IllegalArgumentException("Invalid itemstack!");
		}
		SwordDataEnum listEnum;
		if ( data.equals(SwordDataEnum.BOSS_KILLS) ){
			listEnum = SwordDataEnum.BOSS_NAMES;
		}
		else if ( data.equals(SwordDataEnum.MONSTER_KILLS) ){
			listEnum = SwordDataEnum.MONSTER_NAMES;
		}
		else if ( data.equals(SwordDataEnum.PASSIVE_KILLS) ){
			listEnum = SwordDataEnum.PASSIVE_NAMES;
		}
		else {
			throw new IllegalArgumentException(
					"Data not editable with called method."
			);			
		}
		// Get NBT and edit it.
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(Main.MODID).copy();
		int[] kills = tag.getIntArray(data.toString());
		// DEBUG
		//System.out.println("length: " + kills.length);
		NBTTagList tagList = tag.getTagList(listEnum.toString(), Constants.NBT.TAG_COMPOUND);
		// DEBUG
		//System.out.println("tagList: " + tagList);
		for ( int i = 0; i < tagList.tagCount(); i++ ){
			NBTTagCompound monoTag = (NBTTagCompound) tagList.get(i);
			// The tag only has one element that's an int so this is ok.
			String tagClass = monoTag.getKeySet().iterator().next();
			if ( tagClass.equals(entityClass.toString()) ){
				int index = monoTag.getInteger(tagClass);
				kills[index]++;
				// DEBUG
				//System.out.println("Class: " + tagClass + " , kills: " + kills[index]);
				break; // We've found the correct entity.
			}
		}
		tag.setIntArray(data.toString(), kills);
		sword.getTagCompound().setTag(Main.MODID, tag);
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
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(Main.MODID).copy();
		int kills = tag.getInteger(data.toString());
		kills++;
		tag.setInteger(data.toString(), kills);
		sword.getTagCompound().setTag(Main.MODID, tag);
	}
	
	public void updateNBTData( ItemStack sword, World world )
			throws IllegalArgumentException {
		
		// mappings have the current updated list of mobs.

		if ( ! itemStackValidCheck(sword) ){
			throw new IllegalArgumentException("Invalid itemstack!");
		}
		
		entityHandler = new EntityHelper(world);
		NBTTagCompound tag = sword.getTagCompound().getCompoundTag(Main.MODID).copy();
		
		Map<String, Class<? extends Entity>> bossMapping = 
				entityHandler.getBossMap();
		NBTTagList bossTagList = tag.getTagList(
				SwordDataEnum.BOSS_NAMES.toString(), Constants.NBT.TAG_COMPOUND
		).copy();
		int[] bossKills = tag.getIntArray(SwordDataEnum.BOSS_KILLS.toString());
		Map<String, Class<? extends Entity>> monsterMapping = 
				entityHandler.getMonsterMap();
		NBTTagList monsterTagList = tag.getTagList(
				SwordDataEnum.MONSTER_NAMES.toString(), Constants.NBT.TAG_COMPOUND
		).copy();
		int[] monsterKills = tag.getIntArray(SwordDataEnum.MONSTER_KILLS.toString());
		
		Map<String, Class<? extends Entity>> passiveMapping = 
				entityHandler.getPassiveMap();
		NBTTagList passiveTagList = tag.getTagList(
				SwordDataEnum.PASSIVE_NAMES.toString(), Constants.NBT.TAG_COMPOUND
		).copy();
		
		int[] passiveKills = tag.getIntArray(SwordDataEnum.PASSIVE_KILLS.toString());
		
		tag.setTag(
				SwordDataEnum.BOSS_NAMES.toString(),
				getNBTList(entityHandler.getBossMap())
		);
		tag.setTag(
				SwordDataEnum.MONSTER_NAMES.toString(),
				getNBTList(entityHandler.getMonsterMap())
		);
		tag.setTag(
				SwordDataEnum.PASSIVE_NAMES.toString(),
				getNBTList(entityHandler.getPassiveMap())
		);
		
		tag.setIntArray(
				SwordDataEnum.BOSS_KILLS.toString(),
				updatedArray(bossMapping, bossTagList, bossKills)
		);
		tag.setIntArray(
				SwordDataEnum.MONSTER_KILLS.toString(),
				updatedArray(monsterMapping, monsterTagList, monsterKills)
		);
		tag.setIntArray(
				SwordDataEnum.PASSIVE_KILLS.toString(),
				updatedArray(passiveMapping, passiveTagList, passiveKills)
		);
		
		sword.getTagCompound().setTag(Main.MODID, tag);
	}
	
	private boolean itemStackValidCheck( ItemStack itemStack ) {
		
		if ( ! (itemStack.getItem() instanceof ItemSword) ){
			return false;
		}
		// Mindf@ck
		if ( 
				! (itemStack.hasTagCompound() &&
				itemStack.getTagCompound().hasKey(Main.MODID)) 
		){
			return false;
		}
		return true;
		
	}
	
	private static NBTTagList getNBTList(
			Map<String, Class<? extends Entity>> mapping ) {
		
		NBTTagList NBTList = new NBTTagList();
		int i = 0;
		for ( String entityName : mapping.keySet() ){
			NBTTagCompound shortTag = new NBTTagCompound();
			String entityClassString = mapping.get(entityName).toString();
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
				if ( entityClass.toString().equals(tagClass) ){
					newKillArr[i] = arr[j];
				}
			}
			i++;
		}
		return newKillArr;
	}
}
