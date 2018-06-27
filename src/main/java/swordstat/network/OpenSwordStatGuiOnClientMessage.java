package swordstat.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.Main;
import swordstat.gui.GuiEnum;
import swordstat.proxy.ClientProxy;

public class OpenSwordStatGuiOnClientMessage implements IMessage {

	private NBTTagCompound tagCompound;
	
	public OpenSwordStatGuiOnClientMessage() {
		
	}
	
	public OpenSwordStatGuiOnClientMessage( final NBTTagCompound tagCompound ) {
		
		this.tagCompound = tagCompound;
	}
	
	@Override
	public void toBytes( ByteBuf buf ) {

		ByteBufUtils.writeTag(buf, tagCompound);
	}

	@Override
	public void fromBytes( ByteBuf buf ) {

		tagCompound = ByteBufUtils.readTag(buf);
	}

	public static class OpenSwordStatGuiOnClientMessageHandler 
		implements IMessageHandler<OpenSwordStatGuiOnClientMessage, IMessage> {

		@Override
		public IMessage onMessage( final OpenSwordStatGuiOnClientMessage message, MessageContext ctx ) {

			if ( ctx.side != Side.CLIENT ) {
		        Main.LOGGER.error("OpenSwordStatGuiOnClientMessage received on wrong side:" + ctx.side);
		        return null;
			}
			
		    Minecraft minecraft = Minecraft.getMinecraft();
		    final EntityPlayerSP player = minecraft.player;
		    minecraft.addScheduledTask(new Runnable() {
		    	
		    	public void run() {
		    		
		    		ClientProxy.GUI_HANDLER.setTagCompoundToUse(message.tagCompound);
		    		player.openGui(
							Main.instance, GuiEnum.SWORD_MENU.ordinal(), player.world, 0, 0, 0
		    		);
		    	}
		    });
			return null;
		}
	}
}
