package swordstat.proxy;

import swordstat.Main;
import swordstat.gui.GuiHandler;
import swordstat.init.SwordStatEventHandlers;
import swordstat.init.SwordStatKeyBindings;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);	
	}
	
	@Override
	public void fmlLifeCycleEvent( FMLInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
		SwordStatKeyBindings.init();
		SwordStatEventHandlers.registerClient();
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
	}
	
	@Override
	public void fmlLifeCycleEvent( FMLPostInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
	}
}
