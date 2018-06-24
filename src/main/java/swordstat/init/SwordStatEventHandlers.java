package swordstat.init;

import net.minecraftforge.common.MinecraftForge;
import swordstat.event.AttachNBTEventHandler;
import swordstat.event.KeyInputEventHandler;
import swordstat.event.LivingDeathEventHandler;
import swordstat.event.PlayerLoggedInEventHandler;

public class SwordStatEventHandlers {
	
	public static void registerServer() {
		
		// register event handlers here.
		MinecraftForge.EVENT_BUS.register(new AttachNBTEventHandler());
		MinecraftForge.EVENT_BUS.register(new LivingDeathEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerLoggedInEventHandler());
	}
	
	public static void registerClient() {
		
		// register event handlers here.
		MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());
	}
}
