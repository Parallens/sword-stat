package swordstat.integration;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.SwordCore;
import swordstat.condition.ICouldOpenGUICondition;
import swordstat.proxy.CommonProxy;

public class TinkerIntegration {

	public static void runClientIntegration() {
			
	}
	
	public static void runServerIntegration() {
		
		CommonProxy.OPEN_GUI_CONTROLLER.add(new ICouldOpenGUICondition() {

			@Override
			public boolean openForItemStack( ItemStack itemStack ) {

				if ( itemStack != null && itemStack.getItem() instanceof SwordCore ) {
					return true;
				}
				else {
					return false;
				}
			}
		});
	}
}
