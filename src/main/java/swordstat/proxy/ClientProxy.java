package swordstat.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import swordstat.SwordStat;
import swordstat.gui.GuiHandler;
import swordstat.init.SwordStatEventHandlers;
import swordstat.init.SwordStatKeyBindings;

public class ClientProxy extends CommonProxy {
	
	public static GuiHandler GUI_HANDLER = new GuiHandler();

	@Override
	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
	}
	
	@Override
	public void fmlLifeCycleEvent( FMLInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
		SwordStatKeyBindings.init();
		SwordStatEventHandlers.registerClient();
		NetworkRegistry.INSTANCE.registerGuiHandler(SwordStat.instance, GUI_HANDLER);
	}
	
	@Override
	public void fmlLifeCycleEvent( FMLPostInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
	}
}
