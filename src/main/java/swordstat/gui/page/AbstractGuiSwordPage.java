package swordstat.gui.page;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;

public abstract class AbstractGuiSwordPage implements IGuiSwordPage {

	private final List<GuiButton> buttons = new ArrayList<>();
	/** Width of the parent.*/
	private int parentWidth;
	/** Height of the parent.*/
	private int parentHeight;
	/** Width of the screen in pixels.*/
	private int screenWidth;
	/** Height of the screen in pixels.*/
	private int screenHeight;
	
	public AbstractGuiSwordPage( int parentWidth, int parentHeight ) {

		this.parentWidth = parentWidth;
		this.parentHeight = parentHeight;
	}

	@Override
	public void onResize( int screenWidth, int screenHeight ) {

		buttons.clear();
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public List<GuiButton> getButtons( int buttonStartIndex ) {

		return buttons;
	}	

	public int getParentWidth() {
		
		return parentWidth;
	}

	public int getParentHeight() {
		
		return parentHeight;
	}
	
	public int getScreenWidth() {
		
		return screenWidth;
	}

	public int getScreenHeight() {
		
		return screenHeight;
	}
	
}
