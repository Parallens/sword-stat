package swordstat.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import swordstat.SwordStat;
import swordstat.gui.GuiEnum;
import swordstat.init.SwordStatKeyBindings;
import swordstat.network.AskServerToAddNBTMessage;


public class KeyInputEventHandler {
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onKeyInput( InputEvent.KeyInputEvent event ) {

		if ( SwordStatKeyBindings.swordMenu.isPressed() ){
			EntityPlayer player = Minecraft.getMinecraft().player;
			// Check if the player is holding a sword in the mainhand.
			ItemStack itemStack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND); 
			if ( itemStack == null || ! (itemStack.getItem() instanceof ItemSword) ) return;
			
			// Ask the server to add NBT to the sword (server will decide if this is necessary, e.g. could already be added)
			SwordStat.INSTANCE.sendToServer(new AskServerToAddNBTMessage(true));
			
			/*
			// We want no container so we call it on the client side only.
			player.openGui(
					Main.instance, GuiEnum.SWORD_MENU.ordinal(), player.world, 0, 0, 0
			);
			*/

		}
	}
}
