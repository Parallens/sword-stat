package swordstat.util.swordutil;

import java.util.HashMap;

import swordstat.SwordStat;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class SwordDataHelper {
	
	private HashMap<String, Object> dataMapping = new HashMap<String, Object>();
	private final ItemStack sword;
	private final EntityPlayer playerIn;
	private final NBTTagCompound tagCompound;
	
	public SwordDataHelper( ItemStack sword, NBTTagCompound tagCompound, EntityPlayer playerIn ) {
		
		this.sword = sword;
		this.playerIn = playerIn;
		this.tagCompound = tagCompound;
		
		dataMapping.put(
				SwordDataEnum.NAME.toString(),
				sword.getDisplayName()
		);
		dataMapping.put(
				SwordDataEnum.TYPE.toString(),
				new ItemStack(sword.getItem()).getDisplayName()
		);
		dataMapping.put(
				SwordDataEnum.CURRENT_MASTER.toString(),
				playerIn.getName()
		);
		//Need to check id this isnt an electrical item
		dataMapping.put(
				SwordDataEnum.DURABILITY.toString(),
				sword.getMaxDamage() - sword.getItemDamage()
		);
		dataMapping.put(
				SwordDataEnum.MAX_DURABILITY.toString(),
				sword.getMaxDamage()
		);
		dataMapping.put(
				SwordDataEnum.CHARGE.toString(),
				null//Method to get charge of item. 
		);
		dataMapping.put(
				SwordDataEnum.REPAIR_COST.toString(),
				sword.getRepairCost()
		);
		
		// Now for the data obtained from NBT
		
		dataMapping.put(
				SwordDataEnum.IRL_DATE_CRAFTED.toString(),
				(tagCompound.hasKey(SwordDataEnum.IRL_DATE_CRAFTED.toString()))?
						tagCompound.getString(SwordDataEnum.IRL_DATE_CRAFTED.toString()):
						null
		);
		dataMapping.put(
				SwordDataEnum.INGAME_AGE.toString(),
				Minecraft.getMinecraft().world.getTotalWorldTime() - 
				Integer.parseInt(tagCompound.getString(SwordDataEnum.INGAME_AGE.toString()))
		);
		dataMapping.put(
				SwordDataEnum.IRL_DATE_FOUND.toString(),
				(tagCompound.hasKey(SwordDataEnum.IRL_DATE_FOUND.toString()))?
						tagCompound.getString(SwordDataEnum.IRL_DATE_FOUND.toString()):
						null
		);
		dataMapping.put(
				SwordDataEnum.TOTAL_KILLS.toString(),
				tagCompound.getInteger(SwordDataEnum.TOTAL_KILLS.toString())
		);
		dataMapping.put(
				SwordDataEnum.PLAYER_KILLS.toString(),
				tagCompound.getInteger(SwordDataEnum.PLAYER_KILLS.toString())
		);
		dataMapping.put(
				SwordDataEnum.BOSS_KILLS.toString(),
				tagCompound.getIntArray(SwordDataEnum.BOSS_KILLS.toString())
		);
		dataMapping.put(
				SwordDataEnum.MONSTER_KILLS.toString(),
				tagCompound.getIntArray(SwordDataEnum.MONSTER_KILLS.toString())
		);
		dataMapping.put(
				SwordDataEnum.PASSIVE_KILLS.toString(),
				tagCompound.getIntArray(SwordDataEnum.PASSIVE_KILLS.toString())
		);
	}
	
	public ItemStack getSword() {
		
		return sword;
	}
	
	public EntityPlayer getPlayer() {
		
		return playerIn;
	}
	
	public int[] getMonsterKills() {
		
		return (int[]) dataMapping.get(SwordDataEnum.MONSTER_KILLS.toString());
	}
	
	public int[] getBossKills() {
		
		return (int[]) dataMapping.get(SwordDataEnum.BOSS_KILLS.toString());
	}
	
	public int[] getPassiveKills() {
		
		return (int[]) dataMapping.get(SwordDataEnum.PASSIVE_KILLS.toString());
	}
	
	public int getTotalKills() {
		
		return (Integer) dataMapping.get(SwordDataEnum.TOTAL_KILLS.toString());
	}
	
	public String getCurrentMaster() {
		
		return (String) dataMapping.get(SwordDataEnum.CURRENT_MASTER.toString());
	}
	
	public Long getIngameAge() {
		
		return (Long) dataMapping.get(SwordDataEnum.INGAME_AGE.toString());
	}
	
	public String getIrlDateCrafted() {
		
		return (String) dataMapping.get(SwordDataEnum.IRL_DATE_CRAFTED.toString());
	}
	
	public String getIrlDateFound() {
		
		return (String) dataMapping.get(SwordDataEnum.IRL_DATE_FOUND.toString());
	}
	
	public String getName() {
		
		return (String) dataMapping.get(SwordDataEnum.NAME.toString());
	}
	
	public String getType() {
		
		return (String) dataMapping.get(SwordDataEnum.TYPE.toString());
		
	}
	
	public int getRepairCost() {
		
		return (Integer) dataMapping.get(SwordDataEnum.REPAIR_COST.toString());
	}
	
	public int getDurability() {
		
		return (Integer) dataMapping.get(SwordDataEnum.DURABILITY.toString());
	}
	
	public int getMaxDurability() {
		
		return (Integer) dataMapping.get(SwordDataEnum.MAX_DURABILITY.toString());
	}
	
	public int getChargeCost() {
		
		return (Integer) dataMapping.get(SwordDataEnum.CHARGE.toString());
	}
	
	public int getPlayerKills() {
		
		return (Integer) dataMapping.get(SwordDataEnum.PLAYER_KILLS.toString());
	}
	
	public String getSwordType() {
		
		return (String) dataMapping.get(SwordDataEnum.TYPE.toString());
	}
	
}
