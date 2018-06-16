package swordstat.event;

import swordstat.Main;
import swordstat.gui.GuiEnum;
import swordstat.init.SwordStatKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;


public class KeyInputEventHandler {
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onKeyInput( InputEvent.KeyInputEvent event ) {
		
		if ( SwordStatKeyBindings.swordMenu.isPressed() ){
			EntityPlayer player = Minecraft.getMinecraft().player;
			// Check if the player is holding a sword in the mainhand.
			ItemStack itemStack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND); 
			if ( itemStack == null || ! (itemStack.getItem() instanceof ItemSword) ) return;
			// We want no container so we call it on the client side only.
			if ( player.world.isRemote ){
				player.openGui(
						Main.instance, GuiEnum.SWORD_MENU2.ordinal(), player.world, 0, 0, 0
				);
			}

		}
	}
}
