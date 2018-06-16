package swordstat.gui;

import java.util.ArrayList;
import java.util.List;

import swordstat.util.StringUtil;
import swordstat.util.swordutil.SwordDataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class PageSword implements IGuiSwordPage {
	
	// x = 12, y = 37
	// width = height = 70
	private int screenWidth, screenHeight;
	private final int parentWidth, parentHeight;
	private final GuiScreen parent;
	private final SwordDataHelper swordDataHelper;
	
	private final String[] titleStringsNextToImage = new String[4];
	private final String[] titleStringsBelowImage = new String[4];
	private final String[] infoStringsNextToImage = new String[4];
	private final String[] infoStringsBelowImage = new String[4];
	
	public PageSword( final GuiScreen parent, final int parentWidth, final int parentHeight,
			final SwordDataHelper swordDataHelper) {
		
		this.parent = parent;
		this.parentWidth = parentWidth;
		this.parentHeight = parentHeight;
		this.swordDataHelper = swordDataHelper;
		
		// Initialise strings next to the rendered sword.
		titleStringsNextToImage[0] = "Master: ";
		titleStringsNextToImage[1] = "Total Kills: ";
		titleStringsNextToImage[2] = "Repair Cost: ";
		titleStringsNextToImage[3] = "Player Kills: ";
		
		infoStringsNextToImage[0] = swordDataHelper.getCurrentMaster();
		infoStringsNextToImage[1] = Integer.toString(swordDataHelper.getTotalKills());
		infoStringsNextToImage[2] = Integer.toString(swordDataHelper.getRepairCost());
		infoStringsNextToImage[3] = Integer.toString(swordDataHelper.getPlayerKills());
		
		// Initialise strings below the rendered sword.
		String discoveredStringTitle, discoveredStringInfo;
		if ( swordDataHelper.getIrlDateCrafted() == null ){
			discoveredStringTitle = "Found irl: ";
			discoveredStringInfo = StringUtil.getNeaterDate(swordDataHelper.getIrlDateFound());
		} else {
			discoveredStringTitle = "Crafted irl: ";
			discoveredStringInfo = StringUtil.getNeaterDate(swordDataHelper.getIrlDateCrafted());
		}
		int inGameAge = (int) (Math.floor(swordDataHelper.getIngameAge()) / 24000);
		titleStringsBelowImage[0] = "Durability: ";
		titleStringsBelowImage[1] = "Sword Type: ";
		titleStringsBelowImage[2] = discoveredStringTitle;
		titleStringsBelowImage[3] = "In Game Age: ";
		
		infoStringsBelowImage[0] = swordDataHelper.getDurability() + "/" + swordDataHelper.getMaxDurability();
		infoStringsBelowImage[1] = swordDataHelper.getSwordType();
		infoStringsBelowImage[2] = discoveredStringInfo;
		infoStringsBelowImage[3] = inGameAge + (( inGameAge == 1 )? " day old" : " days old");
	}
	
	//@Override
	public ItemStack getIconItemStack() {

		return new ItemStack(Items.DIAMOND_AXE);
	}

	//@Override
	public List<GuiButton> getButtons( int buttonStartIndex ) {

		return new ArrayList<GuiButton>();
	}

	//@Override
	public void onResize( int screenWidth, int screenHeight ) {

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	//@Override
	public void drawContents( int mouseX, int mouseY, float partialTicks ) {

		GL11.glPushMatrix();
		
		int widthOffset = screenWidth / 2 - parentWidth / 2;
		int heightOffset = screenHeight / 2 - parentHeight/ 2;
		GL11.glScalef(2F, 2F, 1);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		// TODO dynamic resizing
		// Draw the title
 		String trimmedTitle = fontRenderer.trimStringToWidth(swordDataHelper.getName(), (int) (parentWidth/2 - 5));
		int stringLength = fontRenderer.getStringWidth(trimmedTitle);
        fontRenderer.drawStringWithShadow(
        		trimmedTitle, (widthOffset + (parentWidth - stringLength * 2)/2)/2,
        		(heightOffset + 13)/2, 0xFFFFFF
        ); 
		GL11.glScalef(3/2F, 3/2F, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(
				new ResourceLocation("paras_sword_stat:textures/gui/sword_page.png")
		);
		parent.drawTexturedModalRect(
				(widthOffset + 15) / 3 - 1, (heightOffset + 43) / 3 - 1,
				parentWidth + 1, 0, 18, 18);
		parent.mc.getRenderItem().renderItemAndEffectIntoGUI(swordDataHelper.getSword(),
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
		final int maxBelowImageLength = parentWidth - 5;
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

}
