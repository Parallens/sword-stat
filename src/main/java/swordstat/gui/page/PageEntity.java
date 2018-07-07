package swordstat.gui.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import swordstat.gui.GuiEntityScrollingList;
import swordstat.gui.GuiSwordParent;
import swordstat.util.swordutil.SwordKillsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PageEntity implements IGuiSwordPage {
	
	private final Set<String> entityStrings;
	private final Set<String> bossStrings = new TreeSet<>();
	private final Set<String> monsterStrings = new TreeSet<>();
	private final Set<String> passiveStrings = new TreeSet<>();
	private final SwordKillsHelper swordKillsHelper;
	private final CreativeTabs inferedCreativeTab;
	private GuiEntityScrollingList entityScrollingList;
	// static for convenience
	private static SwordKillsHelper.EntityType activeEntityType = SwordKillsHelper.EntityType.MONSTER;
	private int screenWidth, screenHeight;
	//private final ItemStack iconItemStack;
	
	public PageEntity( final Set<String> entityStrings, CreativeTabs inferedCreativeTab,
			final SwordKillsHelper swordKillsHelper, int screenWidth, int screenHeight ) {
		
		this.entityStrings = entityStrings;
		this.swordKillsHelper = swordKillsHelper;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.inferedCreativeTab = inferedCreativeTab;
		// Sort the entity strings into monsters, bosses and passives/misc
		for ( String entityString : entityStrings ){
			if ( SwordKillsHelper.EntityType.BOSS.equals(swordKillsHelper.getEntityType(entityString)) ){
				bossStrings.add(entityString);
			}
			else if ( SwordKillsHelper.EntityType.MONSTER.equals(swordKillsHelper.getEntityType(entityString)) ){
				monsterStrings.add(entityString);
			}
			else if ( SwordKillsHelper.EntityType.PASSIVE.equals(swordKillsHelper.getEntityType(entityString)) ){
				passiveStrings.add(entityString);
			}
		}
		entityScrollingList = new GuiEntityScrollingList(
				GuiSwordParent.X_SIZE, GuiSwordParent.Y_SIZE, screenWidth, screenHeight,
				swordKillsHelper, entityStrings
		);
		
	}
	
	@Override
	public void onResize( int screenWidth, int screenHeight ) {
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		Set<String> chosenEntityStringSet = entityStrings;
		if ( activeEntityType.equals(SwordKillsHelper.EntityType.MONSTER) ){
			chosenEntityStringSet = monsterStrings;
		}
		else if ( activeEntityType.equals(SwordKillsHelper.EntityType.BOSS) ){
			chosenEntityStringSet = bossStrings;
		}
		else if ( activeEntityType.equals(SwordKillsHelper.EntityType.PASSIVE) ){
			chosenEntityStringSet = passiveStrings;
		}
		entityScrollingList = new GuiEntityScrollingList(
				GuiSwordParent.X_SIZE, GuiSwordParent.Y_SIZE, screenWidth, screenHeight,
				swordKillsHelper, chosenEntityStringSet
		);
	}
	
	@Override
	public void drawContents( int mouseX, int mouseY, float partialTicks ) {
		
		//System.out.println(Arrays.toString(entityStrings.toArray()));
		//TODO remove references to parent.
		entityScrollingList.drawScreen(mouseX, mouseY, partialTicks);
		/*
		GL11.glPushMatrix();
		GL11.glScalef(1.5F, 1.5F, 1);
		*/
		Minecraft.getMinecraft().fontRenderer.drawString(
				this.getTitleString(), (screenWidth / 2 - GuiSwordParent.X_SIZE / 2 + 10),
				(screenHeight / 2 + GuiSwordParent.Y_SIZE / 2 - 17), 0x404040
		);
		//GL11.glPopMatrix();
		Minecraft.getMinecraft().fontRenderer.drawString(
				"", 0, 0, 0xFFFFFF
		);
	}
	
	public String getTitleString() {
		
		try{
			return I18n.format(inferedCreativeTab.getTranslatedTabLabel(), new Object[0]);
		}
		catch ( NullPointerException e ){
			return "";
		}
	}

	@Override
	public ItemStack getIconItemStack() {

		try {
			return inferedCreativeTab.getIconItemStack();
		}
		catch ( NullPointerException e ){
			return new ItemStack(Items.APPLE);
		}
	}
	
	@Override
	public List<GuiButton> getButtons( int buttonStartIndex ) {
		
		final int buttonHeight = 20;
		final int monsterWidth = 60, bossWidth = 50, passiveWidth = 35, showAllWidth = 60;
		final int xOffset = 9, yOffset = 7;//GuiSwordParent.Y_SIZE - buttonHeight - 5;
		final int spacing = 3;
		List<GuiButton> buttons = new ArrayList<GuiButton>();
		// TODO draw shape around currently selected menu
		buttons.add(new GuiButton(
				buttonStartIndex,
				screenWidth / 2 - GuiSwordParent.X_SIZE / 2 + xOffset,
				screenHeight / 2 - GuiSwordParent.Y_SIZE / 2 + yOffset,
				monsterWidth, buttonHeight, "Monsters"
		));
		buttons.add(new GuiButton(
				buttonStartIndex + 1,
				screenWidth / 2 - GuiSwordParent.X_SIZE / 2 + monsterWidth + xOffset + spacing,
				screenHeight / 2 - GuiSwordParent.Y_SIZE / 2 + yOffset,
				bossWidth, buttonHeight, "Bosses"
		));
		buttons.add(new GuiButton(
				buttonStartIndex + 2,
				screenWidth / 2 - GuiSwordParent.X_SIZE / 2 + monsterWidth + bossWidth + xOffset + spacing * 2,
				screenHeight / 2 - GuiSwordParent.Y_SIZE / 2 + yOffset,
				passiveWidth, buttonHeight, "Misc"
		));
		buttons.add(new GuiButton(
				buttonStartIndex + 3,
				screenWidth / 2 - GuiSwordParent.X_SIZE / 2 + monsterWidth + bossWidth + xOffset + spacing * 2 - (showAllWidth - passiveWidth),
				(screenHeight / 2 + GuiSwordParent.Y_SIZE / 2 - 25),
				showAllWidth, buttonHeight, "Show: " + (( GuiEntityScrollingList.getUseFiltered() )? "kills" : "all") 
		));
		return buttons;
	}
	
	public boolean getShowAll() {
		
		return !GuiEntityScrollingList.getUseFiltered();
	}
	
	public void setShowAll( boolean showAll ) {
		
		GuiEntityScrollingList.setUseFiltered(!showAll);
	}
	
	public void setCurrentEntityType( SwordKillsHelper.EntityType entityType ) {
		
		this.activeEntityType = entityType;
		onResize(screenWidth, screenHeight);
	}

	@Override
	public boolean isPageForwardButtonVisible() {

		return false;
	}

	@Override
	public boolean isPageBackwardButtonVisible() {

		return false;
	}

	@Override
	public void actionPerformed(GuiButton button) {
		// TODO Auto-generated method stub
		
	}
}