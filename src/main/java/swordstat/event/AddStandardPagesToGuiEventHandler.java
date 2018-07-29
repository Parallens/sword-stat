package swordstat.event;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import swordstat.SwordStat;
import swordstat.gui.page.ISwordPages;
import swordstat.gui.page.PageEntity;
import swordstat.gui.page.PageSword;
import swordstat.swordinfo.SwordData;
import swordstat.swordinfo.SwordKillsData;

import com.google.common.collect.Multimap;

public class AddStandardPagesToGuiEventHandler {

	@SubscribeEvent(priority=EventPriority.HIGH)	
	public void onEvent( final SwordStatGuiCalledEvent event ) {

		System.out.println(event.getPlayer().world.isRemote);
		SwordData swordDataHelper = new SwordData(
				event.getItemStack(), event.getItemStackTagCompound().getCompoundTag(SwordStat.MODID),
				event.getPlayer()
		);
		SwordKillsData swordKillsData= new SwordKillsData(
				event.getItemStackTagCompound().getCompoundTag(SwordStat.MODID),
				SwordStat.CLIENT_RESOURCE_LOCATOR.getEntityClassNameToEntityClassMapping()
		);

		Multimap<String, Class<? extends Entity>> modIDToEntityClassMapping =
				SwordStat.CLIENT_RESOURCE_LOCATOR.getModIDToEntityClassMapping();	

		ISwordPages pages = event.getSwordPages();
		// Initialise all of the pages which correspond to tabs
		pages.appendPage(new PageSword(swordDataHelper));
		// Add the vanilla tab
		pages.appendPage(new PageEntity(
				"Minecraft", modIDToEntityClassMapping.get("Minecraft"),
				event.getEntitySorting(), swordKillsData){
				
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
		for ( String modID: modIDToEntityClassMapping.keySet() ){
			if ( modID.equals("Minecraft") ) continue;
			String modName = SwordStat.CLIENT_RESOURCE_LOCATOR.getModNameFromID(modID);
			pages.appendPage(new PageEntity(
					modName, modIDToEntityClassMapping.get(modID),
					event.getEntitySorting(), swordKillsData)
			);
		}
		
	}
}
