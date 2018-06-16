/*package swordstat.network;

import io.netty.buffer.ByteBuf;
import swordstat.Main;
import swordstat.gui.GuiEnum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Deprecated
public class OpenSwordMenuMessage implements IMessage {
	
	private String text;
	
	public OpenSwordMenuMessage() {
		
	}
	
	public OpenSwordMenuMessage( String text ) {
		
		this.text = text;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {

		ByteBufUtils.writeUTF8String(buf, text);
	}
	
	public static class OpenMenuHandler
		implements IMessageHandler<OpenSwordMenuMessage, IMessage> {
		
		@Override
		public IMessage onMessage( OpenSwordMenuMessage message, final MessageContext ctx ) {
			
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler()
					.playerEntity.worldObj;
			mainThread.addScheduledTask(new Runnable() {
				
				@Override
				public void run() {
					
					EntityPlayer player = ctx.getServerHandler().playerEntity;
					ItemStack heldItemStack = 
							player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
					// Check if the player has item in his/her hand and if it is
					// a sword
					if ( 
							heldItemStack != null && 
							heldItemStack.getItem() instanceof ItemSword
						){
						// DEBUG
						System.out.println("Attempting to open gui");
						BlockPos pos = player.getPosition();
						player.openGui(
								Main.instance, 
								GuiEnum.SWORD_MENU.ordinal(),
								player.worldObj,
								pos.getX(), pos.getY(), pos.getZ()
						);
					}
				}
			});
			return null;
		}
	}
}
*/