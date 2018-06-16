package swordstat.gui;

import java.util.Map;

import swordstat.Main;
import swordstat.util.EntityHelper;
import swordstat.util.RenderUtil;
import swordstat.util.StringUtil;
import swordstat.util.swordutil.SwordDataHelper;
import swordstat.util.swordutil.SwordKillsHelper;
import swordstat.util.swordutil.SwordNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSwordMenu extends GuiScreen {
	
	private final int xSize = 175, ySize = 165;
	private final World worldObj;
	private final EntityPlayer player;
	private final ItemStack sword;
	private final SwordDataHelper swordData;
	private final EntityHelper entityHandler;
	public final SwordKillsHelper swordKillsHelper;
	private SwordPage currentPage = SwordPage.MAIN_PAGE;
	private String currentPageName;
	private GuiSlotEntityList entityScrollable;
	
	private NextPageButton forwardButton;
    private NextPageButton backwardButton;
	
	private enum SwordPage {
		MAIN_PAGE, MONSTER_PAGE, PASSIVE_PAGE, BOSS_PAGE;
	}
	
	public GuiSwordMenu( InventoryPlayer inventory, World world, SwordDataHelper swordData, 
			SwordKillsHelper swordKillsHelper ) {
		
		super();
		worldObj = world;
		player = inventory.player;
		this.swordData = swordData;
		this.swordKillsHelper = swordKillsHelper;
		sword = swordData.getSword();
		entityHandler = EntityHelper.getInstance(world);
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		System.out.println("does this work");
		int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
		forwardButton = new NextPageButton(
				0, k + 145, l + 145,
				18, 10,
				"", true
		);
		backwardButton = new NextPageButton(
				0, k + xSize - 145 - 18, l + 145,
				18, 10,
				"", false
		);
		buttonList.add(forwardButton);
        buttonList.add(backwardButton);
        updatePage();
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		
		if ( currentPage.equals(SwordPage.MAIN_PAGE) ){
			drawMainPage(mouseX, mouseY, partialTicks);
		}
		else {
			drawCurrentPage(mouseX, mouseY, partialTicks);
		}
	}
	
	
	public void drawMainPage( int mouseX, int mouseY, float partialTicks ) {
		
		GL11.glPushMatrix();
		//Set the rendering colour to white.
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//Add our texture!
		mc.renderEngine.bindTexture(
				new ResourceLocation(
						"paras_sword_stat:textures/gui/sword_menu_main.png"
				)
		);
		int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        drawTexturedModalRect(k + 12, l + 37, xSize + 1, 0, 54, 54);
        
        /* Now we add the info to the GUI */
        
        GL11.glScalef(3F, 3F, 1);  // Scale for the sword : *3
        itemRender.renderItemAndEffectIntoGUI(sword, (k + 15)/3, (l + 41)/3);
        GL11.glScalef(2/3F, 2/3F, 1);  // Scale for the sword name: *2
        String trimmedString = 
        		fontRenderer.trimStringToWidth(swordData.getName(), xSize/2 - 5);

        int stringLength = fontRenderer.getStringWidth(trimmedString);
        fontRenderer.drawStringWithShadow(
        		trimmedString,
        		(k + (xSize - stringLength * 2)/2)/2, (l + 13 )/2,
        		0xFFFFFF
        ); 
        // Reset the scale to *1
        GL11.glScalef(0.5F, 0.5F, 1);
        drawStrings();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
	}
	
	public void drawCurrentPage( int mouseX, int mouseY, float partialTicks ) {
		
		GL11.glPushMatrix();
		//Set the rendering colour to white.
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//Add our texture!
		mc.renderEngine.bindTexture(new ResourceLocation(
						"paras_sword_stat:textures/gui/sword_page.png")
		);
		int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        int stringLength = fontRenderer.getStringWidth(currentPageName);
        // 80, 35
        GL11.glScalef(2, 2, 1);
        fontRenderer.drawStringWithShadow(
        		currentPageName,
        		(k + (xSize - stringLength * 2)/2)/2, (l + 10)/2,
        		0xFFFFFF
        );
        GL11.glScalef(0.5F, 0.5F, 1);
		super.drawScreen(mouseX, mouseY, partialTicks);
		entityScrollable.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glPopMatrix();
	}
	
	private void drawStrings() {
		
        // 80, 35
		boolean crafted = ! (swordData.getIrlDateCrafted() == null);
		String[][] nextToImage = {
				{ "Master: ",  swordData.getCurrentMaster() },
				{ "Total Kills: ", Integer.toString(swordData.getTotalKills()) },
				{ "Repair Cost: ",  Integer.toString(swordData.getRepairCost()) },
				{ "Player kills: ",  Integer.toString(swordData.getPlayerKills()) },
		};
		
		int ingameAge = (int) (Math.floor(swordData.getIngameAge()) / 24000);
		String[][] belowImage = {
				{ "Durability: ", swordData.getDurability() + "/" +
						swordData.getMaxDurability() },
				{ "Sword Type: ", swordData.getSwordType() },
				{ ( crafted )? 
						"Crafted irl: " : 
						"Found irl: ",
				  ( crafted )?
						StringUtil.getNeaterDate(swordData.getIrlDateCrafted()) :
						StringUtil.getNeaterDate(swordData.getIrlDateFound()), 
				},
				{ "In game age: ",
				  Integer.toString(ingameAge) + 
				  (( ingameAge == 1 )? " day old": " days old")	  
				},
		};
		int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        
        /* draw the strings next to the image */
        //Perhaps a nested inner class would be better.
        
		for ( int i = 0; i < nextToImage.length; i++ ){
			String title = nextToImage[i][0];
			String info = nextToImage[i][1];
			int v = fontRenderer.getStringWidth(title);
			
			fontRenderer.drawStringWithShadow(
        		title,
        		(k + 70),
        		(l + 42 + i * 13), 
        		( i % 2 == 0 )? 0xA0A0A0 : 0xFFFFFF
        		//RenderUtil.getAverageColor(sword)//
			);
			info = fontRenderer.trimStringToWidth(info, 97 - v);
			fontRenderer.drawString(
				info,
				((k + 70) + fontRenderer.getStringWidth(nextToImage[i][0])),
				(l + 43 + i * 13),
				( i % 2 == 0 )? 0x707070 : 0xFFFFFF
			);
			//GL11.glScalef(7/10F, 7/10F, 1);
			//GL11.glScalef(10/7F, 10/7F, 1);
		}
		
		/* Draw the strings below the image */
		
		int x = RenderUtil.getAverageColor(sword);
		//System.out.println(new Color(x).getRGB());
		for ( int i = 0; i < belowImage.length; i++ ){
			int v = fontRenderer.getStringWidth(belowImage[i][0]);
			fontRenderer.drawStringWithShadow(
				belowImage[i][0],
        		(k + 8),
        		(l + 95 + i * 13),
        		( i % 2 == 0 )? 0xA0A0A0 : 0xFFFFFF
        		//RenderUtil.getAverageColor(sword)//0x0011FF
			);
			/*
			RenderTextHandler.renderTextRight(
				belowImage[i][1],
				169 - fontRenderer.getStringWidth(belowImage[i][0]),
				k + 172, (l + 95 + i * 13),
				fontRenderer,
				0xFFFFFF
			);
			*/
			fontRenderer.drawString(
				fontRenderer.trimStringToWidth(belowImage[i][1], 165 - v),
				(k + 8) + fontRenderer.getStringWidth(belowImage[i][0]),
				(l + 96 + i * 13),
				( i % 2 == 0 )? 0x707070 : 0xFFFFFF
			);
		}
	}
		
	@Override
    protected void actionPerformed( GuiButton button ) {
		
		int index = currentPage.ordinal();
		SwordPage[] values = SwordPage.values();
		int length = values.length;
		
		if ( button == forwardButton ){
			//mc.displayGuiScreen((GuiScreen)null);
			currentPage = values[(index + 1) % length];
		}
		else if ( button == backwardButton ){
			currentPage = values[(index == 0)? length - 1 : (index - 1) % length];
		}
		updatePage();
    }
	
	protected void updatePage() {
		
		int[] kills;
		String listName; //Should add this as a value to the enum
		Map<String, Class<? extends Entity>> killMapping;
		
		if ( currentPage.equals(SwordPage.BOSS_PAGE) ){
			killMapping = entityHandler.getBossMap();
			currentPageName = "Bosses"; kills = swordData.getBossKills();
		}
		else if ( currentPage.equals(SwordPage.MONSTER_PAGE) ){
			killMapping = entityHandler.getMonsterMap();
			currentPageName = "Monsters"; kills = swordData.getMonsterKills();
		}
		else { // if ( currentPage.equals(SwordPage.PASSIVE_PAGE) ){
			killMapping = entityHandler.getPassiveMap();
			currentPageName = "Miscellaneous"; kills = swordData.getPassiveKills();
		}
		entityScrollable = new GuiSlotEntityList(
				this, xSize - 20, 80, kills, killMapping 
		);
		// DEBUG
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	public Minecraft getMinecraftInstance() {
		
		return Minecraft.getMinecraft();
	}
	
	public FontRenderer getFontRenderer() {
        /** The FontRenderer used by GuiScreen */
        return fontRenderer;
    }
	
	public int getXSize() {
		
		return xSize;
	}
	
	public int getYSize() {
		
		return ySize;
	}
	
	private class NextPageButton extends GuiButton {
		
		private final boolean isForward;
		
		public NextPageButton( int buttonId, int x, int y, int widthIn,
				int heightIn, String buttonText, boolean forward ) {
			
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			isForward = forward;
		}
		
		@Override
        public void drawButton( Minecraft mc, int parX, int parY, float partialTicks ) {
			
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(
					new ResourceLocation(
							"minecraft:textures/gui/book.png"
							)
			);
			boolean isHovering = (
					parX >= x &&
					parY >= y &&
					parX < x + width &&
					parY < y + height
			);
	        int textureX = (isHovering)? 26 : 3;
	        int textureY = (isForward)? 194 : 207;
	        drawTexturedModalRect(
	        		x, y, 
	        		textureX, textureY, 
	        		18, 10
	        );
	        GL11.glPopMatrix();
        }
	}
	
	public EntityPlayer getPlayer() {
		
		return player;
	}
	
	public SwordDataHelper getSwordData() {
		
		return swordData;
	}
	
}
