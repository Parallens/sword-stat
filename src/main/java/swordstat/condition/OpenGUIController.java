package swordstat.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;

public class OpenGUIController implements Collection<ICouldOpenGUICondition> {

	List<ICouldOpenGUICondition> conditions = new ArrayList<>();
	
	/**
	 * Iterate through all ICouldOpenGUICondition objects held in this object, and
	 * if one (or more) conditions returns true when called with the specified item,
	 * return true, else return false.
	 * 
	 * @param item item to check if a GUI should be opened for (whilst in player hand)
	 * @return true if GUI should be opened, false otherwise
	 */
	public boolean tryItem( ItemStack item ) {
		
		for ( ICouldOpenGUICondition condition : conditions ){
			if ( condition.openForItemStack(item) ){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean add( ICouldOpenGUICondition condition ) {

		return conditions.add(condition);
	}

	@Override
	public boolean addAll( Collection<? extends ICouldOpenGUICondition> collection ) {

		return conditions.addAll(collection);
	}

	@Override
	public void clear() {

		conditions.clear();
	}

	@Override
	public boolean contains( Object obj ) {

		return conditions.contains(obj);
	}

	@Override
	public boolean containsAll( Collection<?> collection ) {

		return conditions.containsAll(collection);
	}

	@Override
	public boolean isEmpty() {

		return conditions.isEmpty();
	}

	@Override
	public Iterator<ICouldOpenGUICondition> iterator() {

		return conditions.iterator();
	}

	@Override
	public boolean remove( Object obj ) {

		return conditions.remove(obj);
	}

	@Override
	public boolean removeAll( Collection<?> collection ) {

		return conditions.removeAll(collection);
	}

	@Override
	public boolean retainAll( Collection<?> collection ) {

		return conditions.removeAll(collection);
	}

	@Override
	public int size() {

		return conditions.size();
	}

	public Object[] toArray() {

		return conditions.toArray();
	}

	public <T> T[] toArray( T[] array ) {

		return conditions.toArray(array);
	}

}
