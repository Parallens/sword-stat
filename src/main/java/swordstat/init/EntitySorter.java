package swordstat.init;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class EntitySorter {
	
	private final Collection<EntityEntry> entities;
	
	public EntitySorter( Collection<EntityEntry> entities ) {
		
		this.entities = entities;
	}

	/** Represents a process which sorts entities into group.*/
	public interface IEntityGroupSorter {
		
		/**
		 * Return true if entity is in group defined by this process, false otherwise.
		 * @param entityClass Class object of the entity
		 * @return True if entity in group defined by this process, false otherwise
		 */
		boolean isInGroup( Class<? extends Entity> entityClass );
	}
	
	public EntitySorting sort( Map<String, IEntityGroupSorter> sorters ) {

		Map<String, Map<String, Class<? extends Entity>>> masterMap = new HashMap<>();
		for ( Map.Entry<String, IEntityGroupSorter> sorter : sorters.entrySet() ){
			IEntityGroupSorter sorterFunc = sorter.getValue();
			Map<String, Class<? extends Entity>> mapping = new HashMap<>();
			for ( EntityEntry entityEntry : entities ){
				if ( sorterFunc.isInGroup(entityEntry.getEntityClass()) ){
					mapping.put(entityEntry.getName(), entityEntry.getEntityClass());
				}
			}
			masterMap.put(sorter.getKey(), mapping);
		}
		return new EntitySorting(masterMap);
	}
	
	/**
	 * Class representing the finished product of the entity sorting.
	 */
	public static class EntitySorting {
	
		private final Map<String, Map<String, Class<? extends Entity>>> entitySorts;
		
		public EntitySorting( Map<String, Map<String, Class<? extends Entity>>> entitySorts ) {
			
			if ( entitySorts == null ){
				this.entitySorts = new HashMap<>();
			}
			else {
				this.entitySorts = entitySorts;
			}
		}
		
		/**
		 * Return the entity String to class mapping defined by the given identifier.
		 * 
		 * @param identifier string denoting the given sorting
		 * @return the entity String to class mapping defined by the given identifier if the identifier
		 * exists, else an empty Map.
		 */
		public Map<String, Class<? extends Entity>> getSorting( String identifier ) {
			
			if ( entitySorts.containsKey(identifier) ){
				return entitySorts.get(identifier);
			}
			else {
				return new HashMap<String, Class<? extends Entity>>(); 
			}
		}
		
		public Set<Class<? extends Entity>> getAllEntityClassesInSortings() {
			
			Set<Class<? extends Entity>> entityClasses = new HashSet<>();
			for ( Map<String, Class<? extends Entity>> sort : entitySorts.values() ){
				entityClasses.addAll(sort.values());
			}
			return entityClasses;
		}
		
		public Map<String, Map<String, Class<? extends Entity>>> getInternalMapping() {
			
			return entitySorts;
		}
		
		@Override
		public String toString() {
			
			return entitySorts.toString();
		}
		
		@Override
		public boolean equals( Object o ) {
			
			EntitySorting other;
			if ( o == null ) return false;
			try {
				other = (EntitySorting) o;
			}
			catch ( ClassCastException e ){
				return false;
			}
			return entitySorts.equals(other.getInternalMapping());
		}
		
		@Override
		public int hashCode() {
			
			return entitySorts.hashCode();
		}
	}
}