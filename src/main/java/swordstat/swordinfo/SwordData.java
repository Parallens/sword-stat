package swordstat.swordinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Encapsulates data for the front page of the sword stat GUI. 
 */
public final class SwordData {
	
	private final ItemStack sword;
	private final EntityPlayer playerIn;
	private final NBTTagCompound tagCompound;

	// Fields in order they are shown in GUI, previously stored in a mapping.
	private final String name;            // Used as the title of the GUI.
	private final String masterName;      // Name of player holding the sword
	private final int totalKills;
	private final int repairCost;
	private final int playerKills;
	private final int currentDurability;
	private final int maxDurability;
	private final String swordType;
	private final boolean isSwordCrafted;
	private final String IRLAge;
	private final long inGameAge;
	private final int[] entityKills;
	
	
	public SwordData( ItemStack sword, NBTTagCompound tagCompound, EntityPlayer playerIn ) {
		
		this.sword = sword;
		this.playerIn = playerIn;
		this.tagCompound = tagCompound;
		
		this.name = sword.getDisplayName();
		this.masterName = playerIn.getName();
		this.repairCost = sword.getRepairCost();
		this.currentDurability = sword.getMaxDamage() - sword.getItemDamage();		
		this.maxDurability = sword.getMaxDamage();
		this.swordType = new ItemStack(sword.getItem()).getDisplayName();

		// Now for the data obtained from NBT		
		String totalKillsKey = SwordDataEnum.TOTAL_KILLS.toString();
		this.totalKills = ( tagCompound.hasKey(totalKillsKey) )?
				tagCompound.getInteger(totalKillsKey) : 1337;
				
		String playerKillsKey = SwordDataEnum.PLAYER_KILLS.toString();
		this.playerKills = ( tagCompound.hasKey(playerKillsKey) )?
				tagCompound.getInteger(playerKillsKey) : 1337;
		
		String entityKillsKey = SwordDataEnum.ENTITY_KILLS_ARRAY.toString();
		this.entityKills = ( tagCompound.hasKey(entityKillsKey) )?
				tagCompound.getIntArray(entityKillsKey) : new int[] {};

		String inGameAgeKey = SwordDataEnum.IRL_DATE_CRAFTED.toString();
		this.inGameAge = ( tagCompound.hasKey(inGameAgeKey) )?
				Minecraft.getMinecraft().world.getTotalWorldTime() - 
						Long.parseLong(tagCompound.getString(inGameAgeKey)) :
				0L;

		if ( tagCompound.hasKey(SwordDataEnum.IRL_DATE_CRAFTED.toString()) ){
			this.isSwordCrafted = true;
			String IRLAgeKey = SwordDataEnum.IRL_DATE_CRAFTED.toString();
			this.IRLAge = tagCompound.getString(IRLAgeKey);
		}
		else {
			this.isSwordCrafted = false;
			String IRLAgeKey = SwordDataEnum.IRL_DATE_FOUND.toString();
			this.IRLAge = tagCompound.getString(IRLAgeKey);			
		}
	}

	public ItemStack getSword() {
		return sword;
	}


	public EntityPlayer getPlayerIn() {
		return playerIn;
	}


	public NBTTagCompound getTagCompound() {
		return tagCompound;
	}


	public String getName() {
		return name;
	}


	public String getMasterName() {
		return masterName;
	}


	public int getTotalKills() {
		return totalKills;
	}


	public int getRepairCost() {
		return repairCost;
	}


	public int getPlayerKills() {
		return playerKills;
	}


	public int getCurrentDurability() {
		return currentDurability;
	}


	public int getMaxDurability() {
		return maxDurability;
	}


	public String getSwordType() {
		return swordType;
	}


	public boolean isSwordCrafted() {
		return isSwordCrafted;
	}


	public String getIRLAge() {
		return IRLAge;
	}


	public long getInGameAge() {
		return inGameAge;
	}

	public int[] getEntityKills() {
		return entityKills;
	}


}
