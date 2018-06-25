package swordstat.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import swordstat.Main;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.network.AskServerToAddNBTMessage;
import swordstat.util.SwordStatResourceLocator;
import swordstat.util.swordutil.SwordDataHelper;
import swordstat.util.swordutil.SwordKillsHelper;
import swordstat.util.swordutil.SwordNBTHelper;

public class GuiHandler implements IGuiHandler {
	
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
		EntitySorting entitySorting = SwordStatResourceLocator.getEntitySorting();
		SwordNBTHelper swordNBTHelper = SwordStatResourceLocator.getSwordNBTHelper();
		
		// wait for server to add NBT 
		ItemStack sword = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		if ( !sword.hasTagCompound() || !sword.getTagCompound().hasKey(Main.MODID) ){
			Main.LOGGER.error("NBT used to store info by this mod could not be found on the sword, this should not happen!");
			return null;
		}
		
		SwordDataHelper swordDataHelper = new SwordDataHelper(sword, player);
		SwordKillsHelper swordKillsHelper = new SwordKillsHelper(sword.getTagCompound().getCompoundTag(Main.MODID), entitySorting);
		if ( ID == GuiEnum.SWORD_MENU.ordinal() ){
			return new GuiSwordParent(world, swordDataHelper, swordKillsHelper);
		}
		return null;
	}
}
