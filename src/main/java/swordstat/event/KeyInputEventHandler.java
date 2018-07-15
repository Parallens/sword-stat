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
import swordstat.proxy.ClientProxy;


public class KeyInputEventHandler {
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onKeyInput( InputEvent.KeyInputEvent event ) {

		if ( SwordStatKeyBindings.swordMenu.isPressed() ){
			EntityPlayer player = Minecraft.getMinecraft().player;
			// Check if the player is holding a sword in the mainhand.
			ItemStack itemStack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND); 
			if ( ClientProxy.OPEN_GUI_CONTROLLER.tryItem(itemStack) ){
				// Ask the server to add NBT to the sword (server will decide if this is necessary,
				// e.g. could already be added), true indicates open GUI in event of success
				SwordStat.INSTANCE.sendToServer(new AskServerToAddNBTMessage(true));
			}
		}
	}
}
