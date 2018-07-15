package swordstat.condition;

import net.minecraft.item.ItemStack;

/**
 * 
 *
 */
public interface ICouldOpenGUICondition {

	/**
	 * Return true if a sword stat GUI should be opened for the given item.
	 *  
	 * @param itemStack itemStack to be checked
	 * @return true if itemstack should be opened, false otherwise
	 */
	boolean openForItemStack( ItemStack itemStack );
}
