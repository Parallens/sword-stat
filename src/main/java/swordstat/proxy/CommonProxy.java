package swordstat.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import swordstat.Main;
import swordstat.init.SwordStatEventHandlers;
import swordstat.network.SendEntitySortMessage;
import swordstat.network.SendEntitySortMessageHandler;

public class CommonProxy {
	
	public static SimpleNetworkWrapper network;
	
	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
	}
	
	public void fmlLifeCycleEvent( FMLInitializationEvent event ) {
		
		SwordStatEventHandlers.registerServer();
		Main.INSTANCE.registerMessage(
				SendEntitySortMessageHandler.class, SendEntitySortMessage.class, 0, Side.CLIENT
		);
	}
	
	public void fmlLifeCycleEvent( FMLPostInitializationEvent event ) {
		
	}
}
