package swordstat.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * There are so many of them they have their own class.
 */
public class SwordParentButtons {
	
	public static class TogglePageButton extends GuiButton {
		
		public static final int WIDTH = 23, HEIGHT = 13;
		public static final ResourceLocation RESOURCE_LOCATION = 
				new ResourceLocation("textures/gui/book.png");
		
		/** Coordinates of the bottom left corner of the button.*/
		private int x, y;
		private boolean isForward;
		
		public TogglePageButton( int buttonId, int x, int y, boolean isForward ) {
			
			super(buttonId, x, y, WIDTH, HEIGHT, "");
			this.x = x;
			this.y = y;
			this.isForward = isForward;
		}
		
		public boolean isFoward() {
			
			return isForward;
		}
		
		@Override
		public void drawButton( Minecraft mc, int mouseX, int mouseY, float partialTicks ) {
			
			mc.renderEngine.bindTexture(RESOURCE_LOCATION);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + WIDTH && mouseY < this.y + HEIGHT;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int textureX = 0;
            int textureY = 192;
            if ( flag ){
                textureX += 23;
            }
            if ( !this.isForward ){
                textureY += 13;
            }
	        drawTexturedModalRect(x, y, textureX, textureY, width, height);
		}
	}

	public static class TabButton extends GuiButton {
		
		protected static final ResourceLocation buttonLoc = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");
		
		protected int textureX = 0, textureY = 0;
		protected final ItemStack foregroundItemStack;
		protected final RenderItem itemRenderer;
		
		public TabButton( int buttonId, int x, int y, int widthIn, int heightIn, 
				ItemStack foregroundItemStack, RenderItem itemRenderer ) {
			
			super(buttonId, x, y, widthIn, heightIn, "");
			this.foregroundItemStack = foregroundItemStack;
			this.itemRenderer = itemRenderer;
		}
		
		@Override
		public void playPressSound(SoundHandler soundHandlerIn) {}
		
		@Override
		public void drawButton( Minecraft mc, int mouseX, int mouseY, float partialTicks ) {
			
			mc.renderEngine.bindTexture(buttonLoc);
	        drawTexturedModalRect(x, y, textureX, textureY, width, height);
	        itemRenderer.renderItemAndEffectIntoGUI(foregroundItemStack, x + 6, y + 8);
		}
	}
	
	public static class SelectedTabButton extends TabButton {
		
		private static final int firstTextureX = 0, firstTextureY = 32;
		private static final int middleTextureX = 28, middleTextureY = 32;
		private static final int endTextureX = 140, endTextureY = 32;
		public static final int width = 27, height = 32;		
		public enum ButtonType {FIRST, MIDDLE, END}
		
		public SelectedTabButton( int buttonId, int x, int y, ButtonType buttonType,
				ItemStack foregroundItemStack, RenderItem itemRenderer ) {
			
			super(buttonId, x, y, width, height, foregroundItemStack, itemRenderer);
			switch ( buttonType ){
				case FIRST: 
					textureX = firstTextureX; textureY = firstTextureY;
					break;
				case END:
					textureX = endTextureX; textureY = endTextureY;
					break;
				default:
					textureX = middleTextureX; textureY = middleTextureY;
					break;
			}	
		}
	}
	
	public static class UnselectedTabButton extends TabButton {
		
		public static final int width = 27, height = 28;
		private static final ResourceLocation buttonLoc =
				new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");
		
		private int textureX = 28, textureY = 0;
		
		public UnselectedTabButton( int buttonId, int x, int y, ItemStack foregroundItemStack,
				RenderItem itemRenderer ) {
			
			super(buttonId, x, y, width, height, foregroundItemStack, itemRenderer);
		}
	}
	
	public static class ForwardTabsButton extends GuiButton {
		
		private static final int U_TEXTURE_X = 10, U_TEXTURE_Y = 5;
		private static final int H_TEXTURE_X = 10, H_TEXTURE_Y = 37;
		public static final int WIDTH = 14, HEIGHT = 22;
		
		private final ResourceLocation buttonLoc =
				new ResourceLocation("minecraft:textures/gui/resource_packs.png");
		
		public ForwardTabsButton( int buttonId, int x, int y ) {
			
			super(buttonId, x, y, WIDTH, HEIGHT, "");	
		}
		
		@Override
		public void drawButton( Minecraft mc, int mouseX, int mouseY, float partialTicks ) {
			
			mc.renderEngine.bindTexture(buttonLoc);
			if ( mouseX > x && mouseX < x + WIDTH &&
					mouseY > y && mouseY < y + HEIGHT ){
				drawTexturedModalRect(x, y, H_TEXTURE_X, H_TEXTURE_Y, width, height);
			}
			else {
				drawTexturedModalRect(x, y, U_TEXTURE_X, U_TEXTURE_Y, width, height);
			}
		}
	}
	
	public static class BackwardTabsButton extends GuiButton {
		
		private static final int U_TEXTURE_X = 34, U_TEXTURE_Y = 5;
		private static final int H_TEXTURE_X = 34, H_TEXTURE_Y = 37;
		public static final int WIDTH = 14, HEIGHT = 22;
		
		private final ResourceLocation buttonLoc = new ResourceLocation("minecraft:textures/gui/resource_packs.png");
		
		public BackwardTabsButton( int buttonId, int x, int y ) {
			
			super(buttonId, x, y, WIDTH, HEIGHT, "");	
		}
		
		@Override
		public void drawButton( Minecraft mc, int mouseX, int mouseY, float partialTicks ) {
			
			mc.renderEngine.bindTexture(buttonLoc);
			if ( mouseX > x && mouseX < x + WIDTH &&
					mouseY > y && mouseY < y + HEIGHT ){
				drawTexturedModalRect(x, y, H_TEXTURE_X, H_TEXTURE_Y, WIDTH, HEIGHT);
			}
			else {
				drawTexturedModalRect(x, y, U_TEXTURE_X, U_TEXTURE_Y, WIDTH, HEIGHT);
			}
		}
	}
}
