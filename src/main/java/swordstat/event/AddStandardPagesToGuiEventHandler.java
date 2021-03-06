package swordstat.event;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import swordstat.SwordStat;
import swordstat.gui.page.ISwordPages;
import swordstat.gui.page.PageEntity;
import swordstat.gui.page.PageSword;
import swordstat.swordinfo.SwordData;
import swordstat.swordinfo.SwordKillsData;

public class AddStandardPagesToGuiEventHandler {

	@SubscribeEvent(priority=EventPriority.HIGH)	
	public void onEvent( final SwordStatGuiCalledEvent event ) {

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
		
		// All entities
		pages.appendPage(new PageEntity(
				"All Entities",
				ForgeRegistries.ENTITIES.getValuesCollection()
				.stream().map(c -> c.getEntityClass())
				.collect(Collectors.toList()),
				event.getEntitySorting(), swordKillsData){
				
			@Override
			public ItemStack getIconItemStack() {
			
				return new ItemStack(Items.BOOK);
			}
			
			@Override
			public String getTitleString() {
				
				return "All Entities";
			}
		});
		
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
		
		// Rest of mods
		for ( String modID: modIDToEntityClassMapping.keySet() ){
			if ( modID.equals("Minecraft") ){
				continue;
			}
			// check if the modID has any entities in the entity sorting
			Set<Class<? extends Entity>> entityClasses = event.getEntitySorting().getAllEntityClassesInSortings();
			entityClasses.retainAll(modIDToEntityClassMapping.get(modID));
			if ( entityClasses.size() > 0 ){
				pages.appendPage(new PageEntity(
						modID, modIDToEntityClassMapping.get(modID),
						event.getEntitySorting(), swordKillsData)
				);
			}

		}
		
	}
}
