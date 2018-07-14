package swordstat.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import swordstat.gui.page.ISwordPages;
import swordstat.init.EntitySorter.EntitySorting;

public class SwordStatGuiCalledEvent extends Event {
	
	private final ISwordPages swordPages;
	private final ItemStack itemStack;
	private final World clientWorld;
	private final NBTTagCompound itemStackTagCompound;
	private final EntitySorting entitySorting;
	private final EntityPlayer player;
	
	public SwordStatGuiCalledEvent( final ISwordPages swordPages, final ItemStack itemStack,
			final World clientWorld, final NBTTagCompound itemStackTagCompound,
			final EntitySorting entitySorting, final EntityPlayer player ) {
		
		this.swordPages = swordPages;
		this.clientWorld = clientWorld;
		this.itemStack = itemStack;
		this.itemStackTagCompound = itemStackTagCompound;
		this.entitySorting = entitySorting;
		this.player = player;
	}

	public ISwordPages getSwordPages() {
		
		return swordPages;
	}

	public World getClientWorld() {
		
		return clientWorld;
	}
	
	public ItemStack getItemStack() {
		
		return itemStack;
	}
	
	public NBTTagCompound getItemStackTagCompound() {
		
		return itemStackTagCompound;
	}

	public EntitySorting getEntitySorting() {
		
		return entitySorting;
	}
	
	public EntityPlayer getPlayer() {
		
		return player;
	}
}
