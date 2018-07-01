package swordstat.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

public interface IGuiSwordPage {
	
	public ItemStack getIconItemStack();
	
	// Analogous to initGui()
	void onResize( int screenWidth, int screenHeight );
	
	// Analogous to drawScreen()
	void drawContents( int mouseX, int mouseY, float partialTicks );
	
	List<GuiButton> getButtons( int buttonStartIndex );
	
	boolean isPageForwardButtonVisible();
	
	boolean isPageBackwardButtonVisible(); 
}
