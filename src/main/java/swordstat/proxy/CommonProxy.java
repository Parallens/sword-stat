package swordstat.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.SwordStat;
import swordstat.condition.ICouldOpenGUICondition;
import swordstat.condition.OpenGUIController;
import swordstat.init.SwordStatEventHandlers;
import swordstat.integration.TinkerIntegration;
import swordstat.network.AskServerToAddNBTMessage;
import swordstat.network.AskServerToAddNBTMessage.AskServerToAddNBTMessageHandler;
import swordstat.network.OpenSwordStatGuiOnClientMessage;
import swordstat.network.OpenSwordStatGuiOnClientMessage.OpenSwordStatGuiOnClientMessageHandler;
import swordstat.network.TellClientToSortEntitiesMessage;
import swordstat.network.TellClientToSortEntitiesMessageHandler;

public class CommonProxy {
	
	//public final static SimpleNetworkWrapper network;
	public final static OpenGUIController OPEN_GUI_CONTROLLER = new OpenGUIController();

	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
		OPEN_GUI_CONTROLLER.add(new ICouldOpenGUICondition() {

			@Override
			public boolean openForItemStack( ItemStack itemStack ) {

				if ( itemStack != null && itemStack.getItem() instanceof ItemSword ) {
					return true;
				}
				else {
					return false;
				}
			}
		});
	}
	
	public void fmlLifeCycleEvent( FMLInitializationEvent event ) {
		
		SwordStatEventHandlers.registerServer();
		SwordStat.INSTANCE.registerMessage(
				TellClientToSortEntitiesMessageHandler.class, TellClientToSortEntitiesMessage.class, 0, Side.CLIENT
		);
		SwordStat.INSTANCE.registerMessage(
				AskServerToAddNBTMessageHandler.class, AskServerToAddNBTMessage.class, 1, Side.SERVER
		);
		SwordStat.INSTANCE.registerMessage(
				OpenSwordStatGuiOnClientMessageHandler.class, OpenSwordStatGuiOnClientMessage.class, 2, Side.CLIENT
		);
	}
	
	public void fmlLifeCycleEvent( FMLPostInitializationEvent event ) {

		if ( Loader.isModLoaded("tconstruct") ){
			TinkerIntegration.runServerIntegration();
		}		
	}
}
