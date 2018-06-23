package swordstat.init;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.fml.common.registry.EntityEntry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EntitySorterTest {

	private Class<? extends Entity> 
		cls1 = EntitySheep.class,
		cls2 = EntityCow.class,
		cls3 = EntityZombie.class,
		cls4 = EntityDragon.class;
	@Mock
	private EntitySorter.IEntityGroupSorter s1, s2, s3;
	@Mock
	private EntityEntry en1, en2, en3, en4;

	private Map<String, EntitySorter.IEntityGroupSorter> sorters;
	private List<EntityEntry> entities = new ArrayList<>();
	private EntitySorter entitySorter;

    @Before
    public void setUp() {

    	MockitoAnnotations.initMocks(this);
    	when(s1.isInGroup(any())).thenReturn(true);
    	doReturn(cls1).when(en1).getEntityClass();
    	doReturn(cls2).when(en2).getEntityClass();
    	doReturn(cls3).when(en3).getEntityClass();
    	doReturn(cls4).when(en4).getEntityClass();    	
    	doReturn("en1").when(en1).getName();
    	doReturn("en2").when(en2).getName();
    	doReturn("en3").when(en3).getName();
    	doReturn("en4").when(en4).getName();
    	
    	entities.add(en1); entities.add(en2); entities.add(en3); entities.add(en4);
    	sorters = new HashMap<>();
    	sorters.put("group1", s1);
    	entitySorter = new EntitySorter(entities);
    }
    
    @Test
    public void testAllowAllGroupHasAllGroupsInEntitySort() {
    	
    	EntitySorter.EntitySorting sorting = entitySorter.sort(sorters);
    	Map<String, Class<? extends Entity>> map = sorting.getSorting("group1");
    	assertEquals(entities.size(), map.values().size());
    	assertThat(map.values(), hasItems(cls1, cls2, cls3, cls4));
    }
    
    @Test
    public void testAllowNoneGroupHasNoGroupsInEntitySort() {
    	
    	sorters.remove("group1");
    	sorters.put("group2", s2);
    	EntitySorter.EntitySorting sorting = entitySorter.sort(sorters);
    	Map<String, Class<? extends Entity>> map = sorting.getSorting("group2");
    	assertEquals(0, map.values().size());
    }
    
    @Test
    public void testAllowNoneAndAllowAllGroupsAreCorrectWithBothAddedToSorters() {
    	
    	sorters.put("group2", s2);
    	EntitySorter.EntitySorting sorting = entitySorter.sort(sorters);
    	Map<String, Class<? extends Entity>> map = sorting.getSorting("group1");
    	Map<String, Class<? extends Entity>> map2 = sorting.getSorting("group2");
    	assertThat(map.values(), hasItems(cls1, cls2, cls3, cls4));
    	assertEquals(0, map2.values().size());
    }    
}
