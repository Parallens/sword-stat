package swordstat.gui2;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

public interface IGuiSwordPage {
	
	public ItemStack getIconItemStack();
	
	// Analogous to initGui()
	public void onResize( int screenWidth, int screenHeight );
	
	// Analogous to drawScreen()
	public void drawContents( int mouseX, int mouseY, float partialTicks );
	
	public List<GuiButton> getButtons( int buttonStartIndex );
		
}
