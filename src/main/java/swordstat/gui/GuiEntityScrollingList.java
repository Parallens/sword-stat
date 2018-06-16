package swordstat.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.GuiScrollingList;
import swordstat.Main;
import swordstat.util.RenderUtil;
import swordstat.util.StringUtil;
import swordstat.util.swordutil.SwordKillsHelper;

public class GuiEntityScrollingList extends GuiScrollingList {
	
	private static final int SLOT_HEIGHT = 80;
	private static final int MAX_WIDTH = 53;
	private static final int MAX_HEIGHT = 64;
	/** Cache of entities so we aren't creating a new entity on every call.*/
	private static Map<String, Entity> entityCache = new HashMap<>();
	
	private float oldMouseX, oldMouseY;
	private TreeSet<String> entityStrings;
	private TreeSet<String> filteredEntityStrings;
	private final SwordKillsHelper swordKillsHelper;
	// Made static for user convenience
	/** True if only kills are to be shown, false otherwise.*/
	private static boolean useFiltered = true;
	

	public GuiEntityScrollingList( int parentWidth, int parentHeight, int screenWidth, int screenHeight,
			SwordKillsHelper swordKillsHelper, Set<String> entityStrings) {
		
		super(
				Minecraft.getMinecraft(), 
				parentWidth - 20,				 // Width of the scrolling list?
				screenHeight,			 // Height of the scrolling list
				// Top of the menu?
				screenHeight / 2 - parentHeight / 2 + 30,
				// Bottom of the menu?
				screenHeight / 2 - parentHeight / 2 + parentHeight - 30,
				// How far to the left in pixels to start?
				screenWidth / 2 - parentWidth / 2 + 10,
				SLOT_HEIGHT, 		 // Height of an individual slot?
				parentWidth, 		 // Seems to be width of GUI
				parentHeight		 // Seems to be height of GUI
		);
		this.swordKillsHelper = swordKillsHelper;
		this.entityStrings = new TreeSet<String>(entityStrings);
		//Filter out the entities with no kills.
		filteredEntityStrings = new TreeSet<String>();
		for ( String entityString : entityStrings ){
			if ( swordKillsHelper.getEntityKillsFromString(entityString) != 0 ){
				filteredEntityStrings.add(entityString);
			}
		}
	}

	@Override
	protected int getSize() {

		return ( useFiltered )? filteredEntityStrings.size() : entityStrings.size();
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		oldMouseX = (float) mouseX;
        oldMouseY = (float) mouseY;
	}
	
	@Override
	protected boolean isSelected( int index ) {

		return false;
	}
	
	@Override
	protected void drawBackground() {
		
		//parent.drawDefaultBackground();
	}
	
	public static boolean getUseFiltered() {
		
		return GuiEntityScrollingList.useFiltered;
	}
	
	public static void setUseFiltered( boolean useFiltered ) {
		
		GuiEntityScrollingList.useFiltered = useFiltered;
	}
	
	@Override
	protected void drawSlot( int slotIdx, int entryRight, int slotTop,
			int slotBuffer, Tessellator tess ) {
		
		Set<String> stringSet = ( useFiltered )? filteredEntityStrings : entityStrings;
		String entityString = (String) stringSet.toArray()[slotIdx];
		int kill = swordKillsHelper.getEntityKillsFromString(entityString);
		GlStateManager.color(1, 1, 1, 1);
	    GlStateManager.pushMatrix();
	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(
						"paras_sword_stat:textures/gui/entity_slot.png"
		));
	    // Current dimensions are (155, 80)

	    Gui.drawModalRectWithCustomSizedTexture(
	    		left, slotTop,
	    		0, 0,
	    		listWidth, listHeight,
	    		256F, 256F
	    );
	    // Get rid of the black box.
	    Gui.drawModalRectWithCustomSizedTexture(
	    		left, slotTop - 5,
	    		0, 0,
	    		listWidth, 5,
	    		256F, 256F
	    );
	    
	    if ( !entityCache.containsKey(entityString) ){
	    	Class<? extends Entity> entityClass = swordKillsHelper.getEntityClassFromString(entityString);
			try {
				entityCache.put(entityString, entityClass.getConstructor(World.class).
						newInstance(Minecraft.getMinecraft().player.world));
			} catch (Exception e) {
				// Should never happen, each entity is instantiated like this in the entityHandler
				Main.logger.error("Could not initialise entity before rendering it, this should not happen!");
				return;
			}
	    }
	    Entity entity = entityCache.get(entityString);
	    // Render the entity:

		int scale = RenderUtil.getScale(
				(EntityLivingBase) entity, MAX_WIDTH, MAX_HEIGHT
		);
		int yRender = RenderUtil.getRenderY(
				(EntityLivingBase) entity, scale, MAX_WIDTH, MAX_HEIGHT
		);
		// (9, 8) , (60, 72) -> average = (34, 40)
		if ( entity instanceof EntityDragon ){
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.popMatrix();
		}
		RenderUtil.drawEntityOnScreen(
				left + 8 + (MAX_WIDTH + 1) / 2,
				slotTop + 8 + yRender, scale,
				(float) (left + 51) - oldMouseX,
				(float) (slotTop + 75 - 50) - oldMouseY,
				(EntityLivingBase) entity
		);
		if ( entity instanceof EntityDragon ){
			GlStateManager.pushMatrix();
			GlStateManager.rotate(-180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.popMatrix();
		}
	    // Draw some of the additional information
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	    fontRenderer.drawString(
	    		entity.getName(), //StringManips.getNeaterName(title),
	    		left + 67 + 3,
	    		slotTop + 15, 0
	    );
	    fontRenderer.drawString(
	    		"Kills: ",
	    		left + 67 + 3,
	    		slotTop + 35, 0xFFFFFF
	    );
	    fontRenderer.drawString(
	    		Integer.toString(kill),
	    		left + 67 + 3 + fontRenderer.getStringWidth("kills: "),
	    		slotTop + 35, StringUtil.getKillColour(kill)
	    );
	    float total = (float)swordKillsHelper.getTotalKills();
	    float percent = ( total > 0 )? (float)kill / total * 100 : 0F;
	    
	    
	    fontRenderer.drawString(
	    		StringUtil.getTruncFloat(percent) + "%",
	    		left + 67 + 3,
	    		slotTop + 48, 0xDF0000
	    );
	    fontRenderer.drawString(
	    		" of total",
	    		left + 67 + 3 + fontRenderer.getStringWidth(
	    				StringUtil.getTruncFloat(percent) + "%"),
	    		slotTop + 48, 0xFFFFFF
	    );
	    
	    GlStateManager.popMatrix();
	}

	@Override
	protected void elementClicked( int index, boolean doubleClick ) {}
	
}