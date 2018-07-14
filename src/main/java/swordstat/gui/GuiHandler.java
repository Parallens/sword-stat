package swordstat.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import swordstat.SwordStat;
import swordstat.gui.page.ArrayListSwordPages;
import swordstat.gui.page.ISwordPages;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.util.swordutil.SwordDataHelper;
import swordstat.util.swordutil.SwordKillsHelper;

public class GuiHandler implements IGuiHandler {
	
	private NBTTagCompound tagCompoundInUse = null;
	
	public void setTagCompoundToUse( final NBTTagCompound tagCompoundInUse ) {
		
		this.tagCompoundInUse = tagCompoundInUse;
	}
	
	public NBTTagCompound getTagCompoundInUse() {
		
		return tagCompoundInUse;
	}
	
	@Nullable
    public Object getServerGuiElement(
          int ID, 
          EntityPlayer player, 
          World world, 
          int x, int y, int z) {
		
        return null;
    }

	@Nullable
	public Object getClientGuiElement(
			int ID, EntityPlayer player, World world,
			int x, int y, int z ) {
		
		// all client side, world == Minecraft.getMinecraft().world
		EntitySorting entitySorting = SwordStat.CLIENT_RESOURCE_LOCATOR.getEntitySorting();
		
		ItemStack sword = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		if ( tagCompoundInUse == null || !tagCompoundInUse.hasKey(SwordStat.MODID) ){
			SwordStat.LOGGER.error("NBT used to store info by this mod could not be found on the sword, this should not happen!");
			return null;
		}
		
		SwordDataHelper swordDataHelper = new SwordDataHelper(
				sword, tagCompoundInUse.getCompoundTag(SwordStat.MODID), player
		);
		SwordKillsHelper swordKillsHelper = new SwordKillsHelper(
				tagCompoundInUse.getCompoundTag(SwordStat.MODID), entitySorting
		);
		//MinecraftForge.EVENT_BUS.post(event);
		ISwordPages swordPages = new ArrayListSwordPages();
		if ( ID == GuiEnum.SWORD_MENU.ordinal() ){
			return new GuiSwordParent(swordPages, world, swordDataHelper, swordKillsHelper);
		}
		return null;
	}
}
