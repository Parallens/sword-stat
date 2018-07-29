package swordstat.gui.page;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import swordstat.gui.GuiEntityScrollingList;
import swordstat.gui.GuiEntityScrollingList.EntityClassComparator;
import swordstat.init.EntitySorter.EntitySorting;
import swordstat.swordinfo.SwordKillsData;
import swordstat.util.SwordStatResourceLocator;

public class PageEntity extends AbstractGuiSwordPage {
	
	private final String modTitle;
	private final Collection<Class<? extends Entity>> entityClasses;
	private final EntitySorting entitySorting;
	private final Set<Class<? extends Entity>> bossEntityClasses, monsterEntityClasses, passiveEntityClasses;
	private final SwordKillsData swordKillsData;
	//private final CreativeTabs inferedCreativeTab;
	private GuiEntityScrollingList entityScrollingList;
	// static for convenience
	private static EntityType activeEntityType = EntityType.MONSTER;
	/**
	 * Enumeration of entity types used by this page.
	 */
	public enum EntityType {
		
		BOSS, MONSTER, PASSIVE
	}
	
	public PageEntity( final String modTitle, final Collection<Class<? extends Entity>> entityClasses,
			final EntitySorting entitySorting, final SwordKillsData swordKillsData ) {
		
		super();
		this.modTitle = modTitle;
		this.entityClasses = entityClasses;
		this.entitySorting = entitySorting;
		this.swordKillsData = swordKillsData;
		
		// Sort the entity strings into monsters, bosses and passives/misc
		EntityClassComparator entityClassComparator = new EntityClassComparator(swordKillsData);
		bossEntityClasses = new TreeSet<>(entityClassComparator);
		monsterEntityClasses = new TreeSet<>(entityClassComparator);
		passiveEntityClasses = new TreeSet<>(entityClassComparator);

		for ( Class<? extends Entity> entityClass : entityClasses ){
			if ( entitySorting.getSorting(SwordStatResourceLocator.BOSS_STRING).containsValue(entityClass) ){
				bossEntityClasses.add(entityClass);
			}
			else if ( entitySorting.getSorting(SwordStatResourceLocator.PASSIVE_STRING).containsValue(entityClass) ){
				passiveEntityClasses.add(entityClass);
			}
			else if ( entitySorting.getSorting(SwordStatResourceLocator.MONSTER_STRING).containsValue(entityClass) ){
				monsterEntityClasses.add(entityClass);
			}
		}
		
	}
	
	@Override
	public void onResize( int screenWidth, int screenHeight, int parentWidth, int parentHeight ) {
		
		super.onResize(screenWidth, screenHeight, parentWidth, parentHeight);
		Set<Class<? extends Entity>> chosenEntityClassSet = new HashSet<>();
		if ( activeEntityType.equals(EntityType.MONSTER) ){
			chosenEntityClassSet = monsterEntityClasses;
		}
		else if ( activeEntityType.equals(EntityType.BOSS) ){
			chosenEntityClassSet = bossEntityClasses;
		}
		else if ( activeEntityType.equals(EntityType.PASSIVE) ){
			chosenEntityClassSet = passiveEntityClasses;
		}
		entityScrollingList = new GuiEntityScrollingList(
				getParentWidth(), getParentHeight(), screenWidth, screenHeight,
				swordKillsData, chosenEntityClassSet
		);
	}
	
	@Override
	public void drawContents( final GuiScreen parent, int mouseX, int mouseY, float partialTicks ) {

		if ( entityScrollingList == null ){
			entityScrollingList = new GuiEntityScrollingList(
					getParentWidth(), getParentHeight(), getScreenWidth(), getScreenHeight(),
					swordKillsData, entityClasses
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
		
		return modTitle;
	}

	@Override
	public ItemStack getIconItemStack() {

		return new ItemStack(Items.APPLE);
	}
	
	@Override
	public List<GuiButton> recreateButtons( int buttonStartIndex ) {
		
		super.recreateButtons(buttonStartIndex);
		final int buttonHeight = 20;
		final int monsterWidth = 60, bossWidth = 50, passiveWidth = 35, showAllWidth = 60;
		final int xOffset = 9, yOffset = 7;//GuiSwordParent.Y_SIZE - buttonHeight - 5;
		final int spacing = 3;

		// TODO draw shape around currently selected menu
		getButtons().add(new GuiButton(
				buttonStartIndex,
				getScreenWidth() / 2 - getParentWidth() / 2 + xOffset,
				getScreenHeight() / 2 - getParentHeight() / 2 + yOffset,
				monsterWidth, buttonHeight, "Monsters"
		));
		getButtons().add(new GuiButton(
				buttonStartIndex + 1,
				getScreenWidth() / 2 - getParentWidth() / 2 + monsterWidth + xOffset + spacing,
				getScreenHeight() / 2 - getParentHeight() / 2 + yOffset,
				bossWidth, buttonHeight, "Bosses"
		));
		getButtons().add(new GuiButton(
				buttonStartIndex + 2,
				getScreenWidth() / 2 - getParentWidth() / 2 + monsterWidth + bossWidth + xOffset + spacing * 2,
				getScreenHeight() / 2 - getParentHeight() / 2 + yOffset,
				passiveWidth, buttonHeight, "Misc"
		));
		getButtons().add(new GuiButton(
				buttonStartIndex + 3,
				getScreenWidth() / 2 - getParentWidth() / 2 + monsterWidth + bossWidth + xOffset + spacing * 2 - (showAllWidth - passiveWidth),
				(getScreenHeight() / 2 + getParentHeight() / 2 - 25),
				showAllWidth, buttonHeight, "Show: " + (( GuiEntityScrollingList.getUseFiltered() )? "kills" : "all") 
		));
		return getButtons();
	}
	
	public boolean getShowAll() {
		
		return !GuiEntityScrollingList.getUseFiltered();
	}
	
	public void setShowAll( boolean showAll ) {
		
		GuiEntityScrollingList.setUseFiltered(!showAll);
	}
	
	public void setCurrentEntityType( EntityType entityType ) {
		
		this.activeEntityType = entityType;
		onResize(getScreenWidth(), getScreenHeight(), getParentWidth(), getParentHeight());
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
	public void actionPerformed( GuiButton button ) {

		int index = getButtons().indexOf(button);
		if ( index == 0 ){
			// It's the monster button
			setCurrentEntityType(EntityType.MONSTER);
		}
		else if ( index == 1 ){
			// It's the boss button
			setCurrentEntityType(EntityType.BOSS);
		}
		else if ( index == 2 ){
			// It's the passive button 
			setCurrentEntityType(EntityType.PASSIVE);
		}
		else if ( index == 3 ){
			// The show all / only kills button has been toggled
			setShowAll(!getShowAll());
		}
	}
}