package swordstat.util;

import swordstat.util.swordutil.SwordNBTHelper;

public final class ServerResourceLocator extends SwordStatResourceLocator {
	
	private SwordNBTHelper swordNBTHelper = null;
	
	public SwordNBTHelper getSwordNBTHelper() {
		
		if ( swordNBTHelper == null ){
			swordNBTHelper = new SwordNBTHelper(getEntitySorting());
		}
		return swordNBTHelper;
	}
}
