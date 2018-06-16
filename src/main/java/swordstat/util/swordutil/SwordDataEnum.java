package swordstat.util.swordutil;
public enum SwordDataEnum {
	
	IRL_DATE_CRAFTED,
	IRL_DATE_FOUND,
	INGAME_AGE,
	TOTAL_KILLS,
	BOSS_KILLS, BOSS_NAMES,
	MONSTER_KILLS, MONSTER_NAMES,
	PASSIVE_KILLS, PASSIVE_NAMES,
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
