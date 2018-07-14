package swordstat.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import swordstat.init.SwordStatEventHandlers;

public class ServerProxy extends CommonProxy {

	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
	}
	
	public void fmlLifeCycleEvent( FMLInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
	}
	
	public void fmlLifeCycleEvent( FMLPostInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
	}	
}
