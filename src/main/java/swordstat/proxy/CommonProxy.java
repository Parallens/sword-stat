package swordstat.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.SwordStat;
import swordstat.init.SwordStatEventHandlers;
import swordstat.network.AskServerToAddNBTMessage;
import swordstat.network.AskServerToAddNBTMessage.AskServerToAddNBTMessageHandler;
import swordstat.network.OpenSwordStatGuiOnClientMessage;
import swordstat.network.OpenSwordStatGuiOnClientMessage.OpenSwordStatGuiOnClientMessageHandler;
import swordstat.network.TellClientToSortEntitiesMessage;
import swordstat.network.TellClientToSortEntitiesMessageHandler;

public class CommonProxy {
	
	public static SimpleNetworkWrapper network;
	
	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
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
		
	}
}
