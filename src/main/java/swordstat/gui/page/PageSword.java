package swordstat.gui.page;

import java.util.ArrayList;
import java.util.List;

import swordstat.swordinfo.SwordData;
import swordstat.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Page displaysing information about the sword.
 */
public class PageSword extends AbstractGuiSwordPage {
	
	// x = 12, y = 37
	// width = height = 70
	private final SwordData swordData;
	
	private final String[] titleStringsNextToImage = new String[4];
	private final String[] titleStringsBelowImage = new String[4];
	private final String[] infoStringsNextToImage = new String[4];
	private final String[] infoStringsBelowImage = new String[4];
	
	public PageSword( final SwordData swordData ) {

		super();
		this.swordData = swordData;
		
		// Initialise strings next to the rendered sword.
		titleStringsNextToImage[0] = "Master: ";
		titleStringsNextToImage[1] = "Total Kills: ";
		titleStringsNextToImage[2] = "Repair Cost: ";
		titleStringsNextToImage[3] = "Player Kills: ";
		
		infoStringsNextToImage[0] = swordData.getMasterName();
		infoStringsNextToImage[1] = Integer.toString(swordData.getTotalKills());
		infoStringsNextToImage[2] = Integer.toString(swordData.getRepairCost());
		infoStringsNextToImage[3] = Integer.toString(swordData.getPlayerKills());
		
		// Initialise strings below the rendered sword.
		String discoveredStringTitle;
		if ( swordData.isSwordCrafted() ){
			discoveredStringTitle = "Crafted irl: ";
		} else {
			discoveredStringTitle = "Found irl: ";
		}
		String discoveredStringInfo = StringUtil.getNeaterDate(swordData.getIRLAge()); 
		
		int inGameAge = (int) (Math.floor(swordData.getInGameAge()) / 24000);
		titleStringsBelowImage[0] = "Durability: ";
		titleStringsBelowImage[1] = "Sword Type: ";
		titleStringsBelowImage[2] = discoveredStringTitle;
		titleStringsBelowImage[3] = "In Game Age: ";
		
		infoStringsBelowImage[0] = swordData.getCurrentDurability() +
				"/" + swordData.getMaxDurability();
		infoStringsBelowImage[1] = swordData.getSwordType();
		infoStringsBelowImage[2] = discoveredStringInfo;
		infoStringsBelowImage[3] = inGameAge + (( inGameAge == 1 )? " day old" : " days old");
	}
	
	@Override
	public ItemStack getIconItemStack() {

		return new ItemStack(Items.DIAMOND_AXE);
	}

	@Override
	public List<GuiButton> recreateButtons( int buttonStartIndex ) {

		return new ArrayList<>();
	}

	@Override
	public void onResize( int screenWidth, int screenHeight, int parentWidth, int parentHeight ) {

		super.onResize(screenWidth, screenHeight, parentWidth, parentHeight);
	}

	@Override
	public void drawContents( final GuiScreen parent, int mouseX, int mouseY, float partialTicks ) {

		GL11.glPushMatrix();
		
		int widthOffset = getScreenWidth() / 2 - getParentWidth() / 2;
		int heightOffset = getScreenHeight() / 2 - getParentHeight()/ 2;
		GL11.glScalef(2F, 2F, 1);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		// TODO dynamic resizing
		// Draw the title
 		String trimmedTitle = fontRenderer.trimStringToWidth(swordData.getName(), (int) (getParentWidth()/2 - 5));
		int stringLength = fontRenderer.getStringWidth(trimmedTitle);
        fontRenderer.drawStringWithShadow(
        		trimmedTitle, (widthOffset + (getParentWidth() - stringLength * 2)/2)/2,
        		(heightOffset + 13)/2, 0xFFFFFF
        ); 
		GL11.glScalef(3/2F, 3/2F, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(
				new ResourceLocation("paras_sword_stat:textures/gui/sword_page.png")
		);
		parent.drawTexturedModalRect(
				(widthOffset + 15) / 3 - 1, (heightOffset + 43) / 3 - 1,
				getParentWidth() + 1, 0, 18, 18);
		parent.mc.getRenderItem().renderItemAndEffectIntoGUI(swordData.getSword(),
				(widthOffset + 15) / 3, (heightOffset + 43) / 3);
		
		// Now draw the strings next to the rendered image.
		GL11.glScalef(1/3F, 1/3F, 1);
		final int maxNextToImageLength = 97;
		for ( int i = 0; i < titleStringsNextToImage.length; i++ ){			
			fontRenderer.drawStringWithShadow(
				titleStringsNextToImage[i], widthOffset + 70, heightOffset + 43 + i * 13, 
				( i % 2 == 0 )? 0xA0A0A0 : 0xFFFFFF
			);
			String trim = fontRenderer.trimStringToWidth(infoStringsNextToImage[i], maxNextToImageLength - fontRenderer.getStringWidth(titleStringsNextToImage[i]));
			fontRenderer.drawString(
				trim, widthOffset + 70 + fontRenderer.getStringWidth(titleStringsNextToImage[i]),
				(heightOffset + 43 + i * 13) + 1, ( i % 2 == 0 )? 0x707070 : 0xFFFFFF
			);
		}
		
		// Now draw the strings below the rendered image.
		final int maxBelowImageLength = getParentWidth() - 5;
		for ( int i = 0; i < infoStringsBelowImage.length; i++ ){
			fontRenderer.drawStringWithShadow(
				titleStringsBelowImage[i], widthOffset + 8, heightOffset + 96 + i * 13, 
				( i % 2 == 0 )? 0xA0A0A0 : 0xFFFFFF
			);
			String trim = fontRenderer.trimStringToWidth(infoStringsBelowImage[i], maxBelowImageLength - fontRenderer.getStringWidth(titleStringsBelowImage[i]) - 5);
			fontRenderer.drawString(
				trim, widthOffset + 8 + fontRenderer.getStringWidth(titleStringsBelowImage[i]),
				(heightOffset + 96 + i * 13) + 1, ( i % 2 == 0 )? 0x707070 : 0xFFFFFF
			);
		}
		GL11.glPopMatrix();
	}

	@Override
	public boolean isPageForwardButtonVisible() {
		
		return true;
	}

	@Override
	public boolean isPageBackwardButtonVisible() {

		return true;
	}

	@Override
	public void actionPerformed(GuiButton button) {

		
	}

}
