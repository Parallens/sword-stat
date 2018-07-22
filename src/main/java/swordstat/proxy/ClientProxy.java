package swordstat.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import swordstat.SwordStat;
import swordstat.condition.ICouldOpenGUICondition;
import swordstat.condition.OpenGUIController;
import swordstat.gui.GuiHandler;
import swordstat.init.SwordStatEventHandlers;
import swordstat.init.SwordStatKeyBindings;

public class ClientProxy extends CommonProxy {
	
	public final static OpenGUIController OPEN_GUI_CONTROLLER = new OpenGUIController();
	public static GuiHandler GUI_HANDLER = new GuiHandler();

	@Override
	public void fmlLifeCycleEvent( FMLPreInitializationEvent event ) {
		
		super.fmlLifeCycleEvent(event);
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
