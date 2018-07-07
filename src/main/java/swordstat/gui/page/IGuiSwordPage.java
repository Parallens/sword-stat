package swordstat.gui.page;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

public interface IGuiSwordPage {

	/**
	 * Get ItemStack to render on this pages tab.
	 * 
	 * @return item stack to render on this page's tab
	 */
	ItemStack getIconItemStack();
	
	/**
	 * Called when a change occurs to the specified button.
	 * 
	 * @param button the button to which a change has occurred
	 */
	void actionPerformed( GuiButton button );
	
	// Analogous to initGui()
	void onResize( int screenWidth, int screenHeight );
	
	// Analogous to drawScreen()
	void drawContents( int mouseX, int mouseY, float partialTicks );
	
	List<GuiButton> getButtons( int buttonStartIndex );
	
	/**
	 * Flag of whether or not to render a forward button on this page.
	 * 
	 * @return True if forward button should be rendered, false otherwise
	 */
	boolean isPageForwardButtonVisible();
	
	/**
	 * Flag of whether or not to render a backward button on this page.
	 * 
	 * @return True if backward button should be rendered, false otherwise
	 */
	boolean isPageBackwardButtonVisible(); 
}
