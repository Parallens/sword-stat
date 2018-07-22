package swordstat.event;

import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import swordstat.SwordStat;
import swordstat.gui.page.ISwordPages;
import swordstat.gui.page.PageEntity;
import swordstat.gui.page.PageSword;
import swordstat.swordinfo.SwordData;
import swordstat.swordinfo.SwordKillsHelper;

public class AddStandardPagesToGuiEventHandler {

	@SubscribeEvent(priority=EventPriority.HIGH)	
	public void onEvent( final SwordStatGuiCalledEvent event ) {

		
		SwordData swordDataHelper = new SwordData(
				event.getItemStack(), event.getItemStackTagCompound().getCompoundTag(SwordStat.MODID),
				event.getPlayer()
		);
		SwordKillsHelper swordKillsHelper = new SwordKillsHelper(
				event.getItemStackTagCompound().getCompoundTag(SwordStat.MODID),
				event.getEntitySorting()
		);

		Set<String> modStrings = swordKillsHelper.getModStrings();		
		ISwordPages pages = event.getSwordPages();
		// Initialise all of the pages which correspond to tabs
		pages.appendPage(new PageSword(swordDataHelper));
		// Add the vanilla tab
		pages.appendPage(new PageEntity(swordKillsHelper.getEntityStringsFromMod(swordKillsHelper.getVanillaString()), swordKillsHelper.getModCreativeTab(swordKillsHelper.getVanillaString()), swordKillsHelper){
				
			@Override
			public ItemStack getIconItemStack() {
			
				//TODO add static cache for these
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
			pages.appendPage(new PageEntity(swordKillsHelper.getEntityStringsFromMod(modString), inferedCreativeTab, swordKillsHelper));
		}
		
	}
}
