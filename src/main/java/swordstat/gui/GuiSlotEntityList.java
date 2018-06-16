package swordstat.gui;

import java.util.Map;
import java.util.TreeMap;

import swordstat.util.RenderUtil;
import swordstat.util.StringUtil;
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

public class GuiSlotEntityList extends GuiScrollingList {
	
	private GuiSwordMenu parent;
	private Map<String, Class<? extends Entity>> killMapping;
	private int[] kills;
	private float oldMouseX, oldMouseY;
	// Would it be better if these were fibonacci numbers?
	private final int maxWidth = 53;
	private final int maxHeight = 64;

	public GuiSlotEntityList( GuiSwordMenu parent, int listWidth,
			int slotHeight, int[] killsIn,
			Map<String, Class<? extends Entity>> killMappingIn ) {
		
		super(
				parent.getMinecraftInstance(), 
				listWidth,				 // Width of the scrolling list?
				parent.height,			 // Height of the scrolling list
				// Top of the menu?
				parent.height / 2 - parent.getYSize() / 2 + 30,
				// Bottom of the menu?
				parent.height / 2 - parent.getYSize() / 2 + parent.getYSize() - 30,
				// How far to the left in pixels to start?
				parent.width / 2 - parent.getXSize() / 2 + 10,
				slotHeight, 			 // Height of an individual slot?
				parent.getXSize(), 		 // Seems to be width of GUI
				parent.getYSize()		 // Seems to be height of GUI
		);
		this.parent = parent;
		killMapping = new TreeMap<String, Class<? extends Entity>>();
		
		//Filter out the entities with no kills.
		for ( int i = 0; i < killMappingIn.size(); i++ ){
			if ( killsIn[i] != 0 ){
				String str = (String) killMappingIn.keySet().toArray()[i];
				killMapping.put(str, killMappingIn.get(str));
			}
		}
		kills = new int[killMapping.size()];
		for ( int j = 0, k = 0; j < killMappingIn.size(); j++ ){
			if ( killsIn[j] != 0 ){
				kills[k] = killsIn[j];
				k++;
			}
		}
	}

	@Override
	protected int getSize() {

		return kills.length;
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		oldMouseX = (float) mouseX;
        oldMouseY = (float) mouseY;
	}
	
	@Override
	protected void elementClicked( int index, boolean doubleClick ) {
		
	}

	@Override
	protected boolean isSelected(int index) {

		return false;
	}
	
	@Override
	protected void drawBackground() {
		
		//parent.drawDefaultBackground();
	}
	
	@Override
	protected void drawSlot( int slotIdx, int entryRight, int slotTop,
			int slotBuffer, Tessellator tess ) {
		
		String title = (String) killMapping.keySet().toArray()[slotIdx];
		Class<? extends Entity> entityClass = killMapping.get(title);
		FontRenderer font = parent.getFontRenderer();
		int kill = kills[slotIdx];
		GlStateManager.color(1, 1, 1, 1);
	    GlStateManager.pushMatrix();
	    Minecraft.getMinecraft().getTextureManager().bindTexture(
				new ResourceLocation(
						"paras_sword_stat:textures/gui/entity_slot.png"
				)
		);
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
	    // Render the entity:
	    Entity entity = null;
		try {
			entity = entityClass.getConstructor(World.class).
					newInstance(parent.getPlayer().world);
		} catch (Exception e) {
			// Should never prompt an exception as we try to instantiate
			// each entity like this in the entityHandler
			e.printStackTrace();
		}
		int scale = RenderUtil.getScale(
				(EntityLivingBase) entity, maxWidth, maxHeight
		);
		int yRender = RenderUtil.getRenderY(
				(EntityLivingBase) entity, scale, maxWidth, maxHeight
		);
		// (9, 8) , (60, 72) -> average = (34, 40)
		if ( entity instanceof EntityDragon ){
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.popMatrix();
		}
		RenderUtil.drawEntityOnScreen(
				left + 8 + (maxWidth + 1) / 2,
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
	    font.drawString(
	    		entity.getName(), //StringManips.getNeaterName(title),
	    		left + 67 + 3,
	    		slotTop + 15, 0
	    );
	    font.drawString(
	    		"Kills: ",
	    		left + 67 + 3,
	    		slotTop + 35, 0xFFFFFF
	    );
	    font.drawString(
	    		Integer.toString(kill),
	    		left + 67 + 3 + font.getStringWidth("kills: "),
	    		slotTop + 35, StringUtil.getKillColour(kill)
	    );
	    float total = (float)parent.getSwordData().getTotalKills();
	    float percent = ( total > 0 )? (float)kill / total * 100 : 0F;
	    
	    
	    font.drawString(
	    		StringUtil.getTruncFloat(percent) + "%",
	    		left + 67 + 3,
	    		slotTop + 48, 0xDF0000
	    );
	    font.drawString(
	    		" of total",
	    		left + 67 + 3 + font.getStringWidth(
	    				StringUtil.getTruncFloat(percent) + "%"),
	    		slotTop + 48, 0xFFFFFF
	    );
	    
	    GlStateManager.popMatrix();
	}
	
}