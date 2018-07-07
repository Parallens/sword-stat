package swordstat.gui.page;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import swordstat.gui.GuiEntityScrollingList;
import swordstat.util.swordutil.SwordKillsHelper;

public class PageEntity extends AbstractGuiSwordPage {
	
	private final Set<String> entityStrings;
	private final Set<String> bossStrings = new TreeSet<>();
	private final Set<String> monsterStrings = new TreeSet<>();
	private final Set<String> passiveStrings = new TreeSet<>();
	private final SwordKillsHelper swordKillsHelper;
	private final CreativeTabs inferedCreativeTab;
	private GuiEntityScrollingList entityScrollingList;
	// static for convenience
	private static SwordKillsHelper.EntityType activeEntityType = SwordKillsHelper.EntityType.MONSTER;
	
	public PageEntity( final Set<String> entityStrings, CreativeTabs inferedCreativeTab,
			final SwordKillsHelper swordKillsHelper, int parentWidth, int parentHeight ) {
		
		super(parentWidth, parentHeight);
		this.entityStrings = entityStrings;
		this.swordKillsHelper = swordKillsHelper;
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
		
	}
	
	@Override
	public void onResize( int screenWidth, int screenHeight ) {
		
		super.onResize(screenWidth, screenHeight);
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
				getParentWidth(), getParentHeight(), screenWidth, screenHeight,
				swordKillsHelper, chosenEntityStringSet
		);
	}
	
	@Override
	public void drawContents( int mouseX, int mouseY, float partialTicks ) {

		if ( entityScrollingList == null ){
			entityScrollingList = new GuiEntityScrollingList(
					getParentWidth(), getParentHeight(), getScreenWidth(), getScreenHeight(),
					swordKillsHelper, entityStrings
			);
		}
		entityScrollingList.drawScreen(mouseX, mouseY, partialTicks);
		Minecraft.getMinecraft().fontRenderer.drawString(
				this.getTitleString(), (getScreenWidth() / 2 - getParentWidth() / 2 + 10),
				(getScreenHeight() / 2 + getParentHeight() / 2 - 17), 0x404040
		);
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

		// TODO draw shape around currently selected menu
		super.getButtons(buttonStartIndex).add(new GuiButton(
				buttonStartIndex,
				getScreenWidth() / 2 - getParentWidth() / 2 + xOffset,
				getScreenHeight() / 2 - getParentHeight() / 2 + yOffset,
				monsterWidth, buttonHeight, "Monsters"
		));
		super.getButtons(buttonStartIndex).add(new GuiButton(
				buttonStartIndex + 1,
				getScreenWidth() / 2 - getParentWidth() / 2 + monsterWidth + xOffset + spacing,
				getScreenHeight() / 2 - getParentHeight() / 2 + yOffset,
				bossWidth, buttonHeight, "Bosses"
		));
		super.getButtons(buttonStartIndex).add(new GuiButton(
				buttonStartIndex + 2,
				getScreenWidth() / 2 - getParentWidth() / 2 + monsterWidth + bossWidth + xOffset + spacing * 2,
				getScreenHeight() / 2 - getParentHeight() / 2 + yOffset,
				passiveWidth, buttonHeight, "Misc"
		));
		super.getButtons(buttonStartIndex).add(new GuiButton(
				buttonStartIndex + 3,
				getScreenWidth() / 2 - getParentWidth() / 2 + monsterWidth + bossWidth + xOffset + spacing * 2 - (showAllWidth - passiveWidth),
				(getScreenHeight() / 2 + getParentHeight() / 2 - 25),
				showAllWidth, buttonHeight, "Show: " + (( GuiEntityScrollingList.getUseFiltered() )? "kills" : "all") 
		));
		return super.getButtons(buttonStartIndex);
	}
	
	public boolean getShowAll() {
		
		return !GuiEntityScrollingList.getUseFiltered();
	}
	
	public void setShowAll( boolean showAll ) {
		
		GuiEntityScrollingList.setUseFiltered(!showAll);
	}
	
	public void setCurrentEntityType( SwordKillsHelper.EntityType entityType ) {
		
		this.activeEntityType = entityType;
		onResize(getScreenWidth(), getScreenHeight());
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