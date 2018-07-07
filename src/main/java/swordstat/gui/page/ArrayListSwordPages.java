package swordstat.gui.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ArrayListSwordPages implements ISwordPages {

	private final ArrayList<IGuiSwordPage> pages = new ArrayList<>();
	
	@Override
	public Iterator<IGuiSwordPage> iterator() {

		return pages.iterator();
	}

	@Override
	public int size() {

		return pages.size();
	}

	@Override
	public Collection<IGuiSwordPage> getAllPages() {

		return pages;
	}

	@Override
	public void appendPage( IGuiSwordPage page ) {

		pages.add(page);
	}

	@Override
	public void prependPage( IGuiSwordPage page ) {

		pages.add(0, page);
	}

	@Override
	public void addPageAt(IGuiSwordPage page, int index)
			throws IllegalArgumentException {

		try {
			pages.add(index, page);
		}
		catch ( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException("Index is invalid");
		}
	}

	@Override
	public IGuiSwordPage getFirstPage() {

		return pages.get(0);
	}

	@Override
	public IGuiSwordPage getLastPage() {

		return pages.get(pages.size() - 1);
	}

	@Override
	public IGuiSwordPage getPageAt( int index ) throws IllegalArgumentException {

		try {
			return pages.get(index);
		}
		catch ( IndexOutOfBoundsException e ) {
			throw new IllegalArgumentException("Index is invalid");
		}
	}

	@Override
	public boolean removePage( IGuiSwordPage page ) {

		return pages.remove(page);
	}

	
}
