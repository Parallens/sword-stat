package swordstat.swordinfo;
public enum SwordDataEnum {
	
	IRL_DATE_CRAFTED,
	IRL_DATE_FOUND,
	INGAME_AGE,
	TOTAL_KILLS,
	ENTITY_KILLS_ARRAY,
	ENTITY_NAMES_ARRAY,
	PLAYER_KILLS,
	DURABILITY,
	MAX_DURABILITY,
	CHARGE,	//Not used by mods?
	CURRENT_MASTER,
	REPAIR_COST,
	TYPE,
	NAME;
	
	@Override
	public String toString() {
		
		return super.toString().toLowerCase();
	}
}
