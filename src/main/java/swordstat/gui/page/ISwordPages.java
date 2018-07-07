package swordstat.gui.page;

import java.util.Collection;


/**
 * Encapsulates an ordered assortment of IGuiSwordpages. 
 */
public interface ISwordPages extends Iterable<IGuiSwordPage> {

	int size();
	
	Collection<IGuiSwordPage> getAllPages();
	
	/**
	 * Add the specified page to the end of the pages.
	 * 
	 * @param page the page to be added
	 */
	void appendPage( IGuiSwordPage page );
	
	/**
	 * Add the specified page to the start of the pages.
	 * 
	 * @param page the page to be added
	 */
	void prependPage( IGuiSwordPage page );
	
	/**
	 * Add the specified page at the specified index. All pages which are positioned >= index have
	 * their indices incremented. 
	 * 
	 * @param page the page to be added
	 * @throws IllegalArgumentException if the index is invalid (index < 0 || index > pages.size())
	 */
	void addPageAt( IGuiSwordPage page, int index ) throws IllegalArgumentException;
	
	IGuiSwordPage getFirstPage();
	
	IGuiSwordPage getLastPage();

	/**
	 * Get the specified page at the specified index.
	 * 
	 * @param index index of the page
	 * @throws IllegalArgumentException if the index is invalid (index < 0 || index >= pages.size())
	*/
	IGuiSwordPage getPageAt( int index ) throws IllegalArgumentException;
	
	/**
	 * Remove the (first occurrence of) the specified page. Equality determined by .equals().
	 * 
	 * @param page the page to be removed
	 * @return true if removal was successful, false otherwise
	 */
	boolean removePage( IGuiSwordPage page );
}
