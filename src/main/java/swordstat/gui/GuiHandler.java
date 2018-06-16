package swordstat.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import swordstat.Main;
import swordstat.gui2.GuiSwordParent;
import swordstat.util.EntityHelper;
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
		
		//System.out.println("called server");
		
		/*
        if ( ID == GuiEnum.SWORD_MENU.ordinal() ){
            return new ContainerSwordMenu(player.inventory, world, x, y, z);
        }
		*/
        return null;
    }

	@Nullable
	public Object getClientGuiElement(
			int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		EntityHelper entityHandler = EntityHelper.getInstance(world);
		SwordNBTHelper swordNBTHelper = new SwordNBTHelper();
		ItemStack sword = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		// Attach data to the sword if applicable.
		try {
			swordNBTHelper.attachNBT(sword, false, world);
		}
		catch ( IllegalArgumentException e ){// Sword already has relevant NBT
		}
		swordNBTHelper.updateNBTData(sword, world);
		SwordDataHelper swordDataHelper = new SwordDataHelper(sword, player);
		SwordKillsHelper swordKillsHelper = new SwordKillsHelper(sword.getTagCompound().getCompoundTag(Main.MODID), entityHandler);
		if ( ID == GuiEnum.SWORD_MENU.ordinal() ){
            return new GuiSwordMenu(player.inventory, world, swordDataHelper, swordKillsHelper);
        }
		else if ( ID == GuiEnum.SWORD_MENU2.ordinal() ){
			return new GuiSwordParent(world, swordDataHelper, swordKillsHelper);
		}
		return null;
	}
}
