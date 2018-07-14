package swordstat.gui.page;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
	void onResize( int screenWidth, int screenHeight, int parentWidth, int parentHeight );
	
	// Analogous to drawScreen()
	void drawContents( GuiScreen parent, int mouseX, int mouseY, float partialTicks );
	
	/**
	 * Get the list of buttons currently held with within this page.
	 * 
	 * @return list of button held by this page
	 */
	List<GuiButton> getButtons();
	
	/**
	 * Recreate buttons held by this page. Should ideally be called after onResize()
	 * to ensure buttons are in the correct position. 
	 * 
	 * @param buttonStartIndex starting index to assign to the first button
	 * @return list of button held by this page
	 */	
	List<GuiButton> recreateButtons( int buttonsStartIndex );
	
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
