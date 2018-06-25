package swordstat.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import swordstat.Main;
import swordstat.init.EntitySorter.EntitySorting;
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
			int x, int y, int z) {
		
		// all client side world == Minecraft.getMinecraft().world
		EntitySorting entitySorting = SwordStatResourceLocator.getEntitySorting();
		SwordNBTHelper swordNBTHelper = SwordStatResourceLocator.getSwordNBTHelper();
		ItemStack sword = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		// TODO send packet to server to add NBT if applicable
		// Attach data to the sword if applicable.
		try {
			swordNBTHelper.attachNBT(sword, false, world);
		}
		catch ( IllegalArgumentException e ){// Sword already has relevant NBT
		}
		swordNBTHelper.updateNBTData(sword, world);
		SwordDataHelper swordDataHelper = new SwordDataHelper(sword, player);
		SwordKillsHelper swordKillsHelper = new SwordKillsHelper(sword.getTagCompound().getCompoundTag(Main.MODID), entitySorting);
		if ( ID == GuiEnum.SWORD_MENU.ordinal() ){
			return new GuiSwordParent(world, swordDataHelper, swordKillsHelper);
		}
		return null;
	}
}
