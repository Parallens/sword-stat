package swordstat.init;

import swordstat.event.AttachNBTEventHandler;
import swordstat.event.KeyInputEventHandler;
import swordstat.event.LivingDeathEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class SwordStatEventHandlers {
	
	public static void registerServer() {
		
		// register event handlers here.
		MinecraftForge.EVENT_BUS.register(new AttachNBTEventHandler());
		MinecraftForge.EVENT_BUS.register(new LivingDeathEventHandler());
	}
	
	public static void registerClient() {
		
		// register event handlers here.
		MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());
	}
}
