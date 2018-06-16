package swordstat.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import swordstat.util.swordutil.SwordDataHelper;
import swordstat.util.swordutil.SwordKillsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class GuiSwordParent extends GuiScreen {
	
	public final static int X_SIZE = 175, Y_SIZE = 165;
	/** Represents the x and y coordinates of the top left corner of the GUI. **/
	private int widthOffset, heightOffset;
	private final World world;
	/** Encapsulates data about the sword, except the specific mob types the sword has killed. **/
	private final SwordDataHelper swordDataHelper;
	/** Encapsulates information about the specific mob types the sword has killed. **/
	private final SwordKillsHelper swordKillsHelper;
	
	/** List of (both visible and invisible) pages. **/
	private List<IGuiSwordPage> pages = new ArrayList<IGuiSwordPage>();
	/** Maximum number of tabs that can be rendered on the GUI at any given moment. **/
	private final static int MAX_TABS = 5;
	/** Index of the page which is currently selected and being rendered. **/
	private int activePageIndex = 0;
	/** Index of the page whose tab button should be rendered as the leftmost GUI tab. **/
	private int startPageIndex = 0;
	/** How many tabs are currently being displayed. **/
	private int tabsPresent;
	
	public GuiSwordParent( World world, SwordDataHelper swordDataHelper,
			SwordKillsHelper swordKillsHelper ) {
		
		super();
		this.world = world;
		this.swordDataHelper = swordDataHelper;
		this.swordKillsHelper = swordKillsHelper;
		Set<String> modStrings = swordKillsHelper.getModStrings();
		// Initialise all of the pages which correspond to tabs
		pages.add(new PageSword(this, X_SIZE, Y_SIZE, swordDataHelper));
		// Add the vanilla tab
		pages.add(new PageEntity(swordKillsHelper.getEntityStringsFromMod(swordKillsHelper.getVanillaString()), swordKillsHelper.getModCreativeTab(swordKillsHelper.getVanillaString()), swordKillsHelper, width, height){
				
			@Override
			public ItemStack getIconItemStack() {
			
				return new ItemStack(Items.ENDER_PEARL);
			}
			
			@Override
			public String getTitleString() {
				
				return "Vanilla";
			}
		});
		for ( String modString : modStrings ){
			if ( modString.equals(swordKillsHelper.getVanillaString()) ) continue;
			CreativeTabs inferedCreativeTab = swordKillsHelper.getModCreativeTab(modString);
			pages.add(new PageEntity(swordKillsHelper.getEntityStringsFromMod(modString), inferedCreativeTab, swordKillsHelper, width, height));
		}
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		widthOffset = width / 2 - X_SIZE / 2;
        heightOffset = height / 2 - Y_SIZE / 2;
        int endIndex = Math.min(startPageIndex + MAX_TABS - 1, pages.size() - 1);
        tabsPresent = endIndex - startPageIndex + 1;
        // Create the buttons
        for ( int i = startPageIndex; i <= endIndex; i++ ){
        	if ( i == activePageIndex ){
        		SelectedTabButton.ButtonType buttonType = SelectedTabButton.ButtonType.MIDDLE;
        		//if ( (i - startPageIndex) == 0 ) buttonType = SelectedTabButton.ButtonType.FIRST;
        		//else if ( i == endIndex ) buttonType = SelectedTabButton.ButtonType.END;
        		buttonList.add(new SelectedTabButton(
        				i - startPageIndex,
        				15 + widthOffset + (i - startPageIndex) * (SelectedTabButton.width + 2),
        				heightOffset - SelectedTabButton.height + 4, buttonType,
        				pages.get(i).getIconItemStack(), itemRender
        		));
        	} 
        	else {
        		buttonList.add(new UnselectedTabButton(
        				i - startPageIndex,
        				15 + widthOffset + (i - startPageIndex) * (UnselectedTabButton.width + 2),
        				heightOffset - UnselectedTabButton.height,
        				pages.get(i).getIconItemStack(), itemRender
        		));
        	}
        }
        // Add the forward and backward buttons
        buttonList.add(new BackwardPageButton(tabsPresent,
        		widthOffset,
        		//widthOffset + MAX_TABS * (SelectedTabButton.width + 2),
        		heightOffset - BackwardPageButton.HEIGHT
        ));
        buttonList.add(new ForwardPageButton(tabsPresent + 1,
        		widthOffset + MAX_TABS * (SelectedTabButton.width + 2) + BackwardPageButton.WIDTH,
        		heightOffset - ForwardPageButton.HEIGHT
        ));
        pages.get(activePageIndex).onResize(width, height);
        // Add monster, bosses and misc button if its an entity page.
        if ( pages.get(activePageIndex) instanceof PageEntity ){
        	buttonList.addAll(pages.get(activePageIndex).getButtons(buttonList.size()));
        }
	}
	
	@Override
	protected void actionPerformed( GuiButton button ) {
		
		int index = buttonList.indexOf(button);
		if ( index < tabsPresent ){	// It's one of the tab buttons
			activePageIndex = startPageIndex + index;
		}
		else if ( index == tabsPresent && startPageIndex != 0 ){	
			// It's the backward button
			startPageIndex -= MAX_TABS;
		}
		else if ( index == tabsPresent + 1 && startPageIndex + MAX_TABS <= pages.size() - 1 ){
			// It's the forward button
			startPageIndex += MAX_TABS;
		}
		else if ( pages.get(activePageIndex) instanceof PageEntity && index == tabsPresent + 2 ){
			// It's the monster button on one of the entity pages
			((PageEntity)pages.get(activePageIndex)).setCurrentEntityType(SwordKillsHelper.EntityType.MONSTER);
		}
		else if ( pages.get(activePageIndex) instanceof PageEntity && index == tabsPresent + 3 ){
			// It's the boss button on one of the entity pages
			((PageEntity)pages.get(activePageIndex)).setCurrentEntityType(SwordKillsHelper.EntityType.BOSS);
		}
		else if ( pages.get(activePageIndex) instanceof PageEntity && index == tabsPresent + 4 ){
			// It's the passive button on one of the entity pages
			((PageEntity)pages.get(activePageIndex)).setCurrentEntityType(SwordKillsHelper.EntityType.PASSIVE);
		}
		else if ( pages.get(activePageIndex) instanceof PageEntity && index == tabsPresent + 5 ){
			// The show all / only kills button has been toggled
			PageEntity curPage = ((PageEntity)pages.get(activePageIndex));
			curPage.setShowAll(!curPage.getShowAll());
		}
		//DEBUG
		/*
		System.out.println("Start page index: " + startPageIndex);
		System.out.println("Active page index: " + activePageIndex);
		*/
		buttonList.clear();
		initGui();
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		
		drawDefaultBackground();
		mc.renderEngine.bindTexture(
				new ResourceLocation("paras_sword_stat:textures/gui/sword_page.png")
		);
        drawTexturedModalRect(widthOffset, heightOffset, 0, 0, X_SIZE, Y_SIZE);
		// Render the active child screen
        pages.get(activePageIndex).drawContents(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	private static class TabButton extends GuiButton {
		
		protected static final ResourceLocation buttonLoc = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");
		
		protected int textureX = 0, textureY = 0;
		protected final ItemStack foregroundItemStack;
		protected final RenderItem itemRenderer;
		
		public TabButton( int buttonId, int x, int y, int widthIn, int heightIn, 
				ItemStack foregroundItemStack, RenderItem itemRenderer ) {
			
			super(buttonId, x, y, widthIn, heightIn, "" );
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
	
	private static class SelectedTabButton extends TabButton {
		
		private static final int firstTextureX = 0, firstTextureY = 32;
		private static final int middleTextureX = 28, middleTextureY = 32;
		private static final int endTextureX = 140, endTextureY = 32;
		private static final int width = 27, height = 32;		
		private enum ButtonType {FIRST, MIDDLE, END}
		
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
	
	private static class UnselectedTabButton extends TabButton {
		
		private static final int width = 27, height = 28;
		private static final ResourceLocation buttonLoc = new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png");
		
		private int textureX = 28, textureY = 0;
		
		public UnselectedTabButton( int buttonId, int x, int y, ItemStack foregroundItemStack,
				RenderItem itemRenderer ) {
			
			super(buttonId, x, y, width, height, foregroundItemStack, itemRenderer);
		}
	}
	
	private static class ForwardPageButton extends GuiButton {
		
		private static final int U_TEXTURE_X = 10, U_TEXTURE_Y = 5;
		private static final int H_TEXTURE_X = 10, H_TEXTURE_Y = 37;
		protected static final int WIDTH = 14, HEIGHT = 22;
		
		private final ResourceLocation buttonLoc = new ResourceLocation("minecraft:textures/gui/resource_packs.png");
		
		public ForwardPageButton( int buttonId, int x, int y ) {
			
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
	
	private static class BackwardPageButton extends GuiButton {
		
		private static final int U_TEXTURE_X = 34, U_TEXTURE_Y = 5;
		private static final int H_TEXTURE_X = 34, H_TEXTURE_Y = 37;
		private static final int WIDTH = 14, HEIGHT = 22;
		
		private final ResourceLocation buttonLoc = new ResourceLocation("minecraft:textures/gui/resource_packs.png");
		
		public BackwardPageButton( int buttonId, int x, int y ) {
			
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