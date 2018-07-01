package swordstat.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import swordstat.gui.SwordParentButtons.BackwardTabsButton;
import swordstat.gui.SwordParentButtons.ForwardTabsButton;
import swordstat.gui.SwordParentButtons.SelectedTabButton;
import swordstat.gui.SwordParentButtons.TogglePageButton;
import swordstat.gui.SwordParentButtons.UnselectedTabButton;
import swordstat.util.swordutil.SwordDataHelper;
import swordstat.util.swordutil.SwordKillsHelper;

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
	private static int activePageIndex = 0;
	/** Index of the page whose tab button should be rendered as the leftmost GUI tab. **/
	private static int startPageIndex = 0;
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
        final IGuiSwordPage activePage = pages.get(activePageIndex);
        activePage.onResize(width, height);
		System.out.println("Called initGui()");
		widthOffset = width / 2 - X_SIZE / 2;
        heightOffset = height / 2 - Y_SIZE / 2;
        int endIndex = Math.min(startPageIndex + MAX_TABS - 1, pages.size() - 1);
        tabsPresent = endIndex - startPageIndex + 1;
        // Create the buttons
        for ( int i = startPageIndex; i <= endIndex; i++ ){
        	if ( i == activePageIndex ){
        		SelectedTabButton.ButtonType buttonType = SelectedTabButton.ButtonType.MIDDLE;
        		// double check does no harm
        		boolean startPageIndexInRange = startPageIndex >= 0 && startPageIndex < pages.size();
        		boolean activePageIndexInRange = activePageIndex >= 0 && activePageIndex < pages.size();
        		if ( !startPageIndexInRange || !activePageIndexInRange ){
        			startPageIndex = 0; activePageIndex = 0;
        		}
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
        // Add the forward and backward tab buttons
        buttonList.add(new BackwardTabsButton(tabsPresent,
        		widthOffset,
        		//widthOffset + MAX_TABS * (SelectedTabButton.width + 2),
        		heightOffset - BackwardTabsButton.HEIGHT
        ));
        buttonList.add(new ForwardTabsButton(tabsPresent + 1,
        		widthOffset + MAX_TABS * (SelectedTabButton.width + 2) + BackwardTabsButton.WIDTH,
        		heightOffset - ForwardTabsButton.HEIGHT
        ));
        
        System.out.println(GuiSwordParent.Y_SIZE);
        buttonList.addAll(activePage.getButtons(buttonList.size()));
        
        // Add the forward and backward page buttons if applicable
        if ( activePage.isPageBackwardButtonVisible() ){
        	int x = widthOffset + 3;
        	int y =  height - heightOffset - TogglePageButton.HEIGHT - 3;        	
        	buttonList.add(new TogglePageButton(buttonList.size(), x, y, false));
        }
        if ( activePage.isPageForwardButtonVisible() ){
        	int x = widthOffset + X_SIZE - TogglePageButton.WIDTH - 3;
        	int y =  height - heightOffset - TogglePageButton.HEIGHT - 3;
        	buttonList.add(new TogglePageButton(buttonList.size(), x, y, true));        	
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
		else if ( button instanceof TogglePageButton && ((TogglePageButton) button).isFoward() ){
			activePageIndex++;
			if ( activePageIndex >= tabsPresent ){
				activePageIndex = startPageIndex = 0;
			}
		}
		else if ( button instanceof TogglePageButton && !((TogglePageButton) button).isFoward() ){
			activePageIndex--;
			if ( activePageIndex < 0 ){
				activePageIndex = tabsPresent - 1;
				startPageIndex = tabsPresent - MAX_TABS;
				startPageIndex = ( startPageIndex < 0 )? 0 : startPageIndex;
			}
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

}