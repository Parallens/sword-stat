package swordstat.util;

import swordstat.swordinfo.SwordNBTAttacher;

public final class ServerResourceLocator extends SwordStatResourceLocator {
	
	private SwordNBTAttacher swordNBTAttacher = null;
	
	public SwordNBTAttacher getSwordNBTHelper() {
		
		if ( swordNBTAttacher == null ){
			swordNBTAttacher = new SwordNBTAttacher(getEntitySorting());
		}
		return swordNBTAttacher;
	}
}
