package swordstat.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import swordstat.gui.page.ISwordPages;

public class SwordStatGuiCalledEvent extends Event {
	
	private final ISwordPages swordPages;
	private final World clientWorld;
	
	public SwordStatGuiCalledEvent( final ISwordPages swordPages, final World clientWorld ) {
		
		this.swordPages = swordPages;
		this.clientWorld = clientWorld;
	}

	public ISwordPages getSwordPages() {
		
		return swordPages;
	}

	public World getClientWorld() {
		
		return clientWorld;
	}
}
