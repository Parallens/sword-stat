package swordstat.proxy;

import swordstat.init.SwordStatEventHandlers;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	
	public static SimpleNetworkWrapper network;
	
	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
	}
	
	public void fmlLifeCycleEvent( FMLInitializationEvent event ) {
		
		SwordStatEventHandlers.registerServer();
	}
	
	public void fmlLifeCycleEvent( FMLPostInitializationEvent event ) {
		
	}
}
