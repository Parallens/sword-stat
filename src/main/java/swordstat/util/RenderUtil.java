package swordstat.util;

import java.io.IOException;

import scala.actors.threadpool.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class RenderUtil {
	
	
	private RenderUtil() {
		
	}
	
	/**
	 * 
	 * @param entity - The entity whose scale is desired(based on width and height).
	 * @return The scale the entity should be rendered at.
	 */
	public static int getScale( EntityLivingBase entity,
			int maxWidth, int maxHeight ) {
		
		// The scale used to render the player on the inventory gui is 30.
		// It's in pixels I think 
		/*
		 * We need to find which of width and height is the limiting factor.
		 */
		int heightScale = (int) Math.floor((maxWidth - 10) / entity.width);
		int widthScale = (int) Math.floor((maxHeight - 10) / entity.height);
		int scalePref = ( heightScale > widthScale )? widthScale : heightScale;
		
		/*
		 * We don't want small entities rendering as big as "large" entities,
		 * I don't like chickens.
		 */
		//System.out.println(entity.toString() + " height: " + entity.height);
		float limiting = ( entity.height > entity.width)? entity.height : entity.width;
		float multiplier = ( limiting < 2 )? limiting - 1 : 1;
		if ( multiplier < 0 ){  // Height < 1
			multiplier = (1 - Math.abs(multiplier)) * (1 - Math.abs(multiplier));
		}
		else if ( multiplier < 0.6 ){  // 1 < Height < 0.6
			multiplier = 0.6F + ((float)Math.pow(multiplier , 3));
		}
		if ( entity instanceof EntityDragon ){
			multiplier += 2;
		}
		float scale = scalePref * multiplier;
		return (int) Math.floor(scale);
	}
	
	public static int getRenderY( EntityLivingBase entity, int scale,
			int maxWidth, int maxHeight ) {
		
		int entityScaledHeight = (int) (entity.height * scale);
		int difference = (maxHeight - entityScaledHeight);
		
		if ( entity instanceof EntityDragon ){
			return entityScaledHeight + (difference / 2) - 10;
		}
		return entityScaledHeight + (difference / 2);
	}
	
	// A little copy and paste from GuiInventory class *shrug* 
	public static void drawEntityOnScreen( int posX, int posY, int scale, 
			float mouseX, float mouseY, EntityLivingBase entity ) {
		
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = entity.renderYawOffset;
        float f1 = entity.rotationYaw;
        float f2 = entity.rotationPitch;
        float f3 = entity.prevRotationYawHead;
        float f4 = entity.rotationYawHead;
        // Bad hack
        if ( entity instanceof EntityDragon ){
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        }
        
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
        entity.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
        entity.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        entity.renderYawOffset = f;
        entity.rotationYaw = f1;
        entity.rotationPitch = f2;
        entity.prevRotationYawHead = f3;
        entity.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	
	/**
	 * 
	 * @param itemStack
	 * @return Average colour of the item stack texture ignoring transparency.
	 */
	public static int getAverageColor( ItemStack itemStack ){
		
		//Perhaps modal colour is more appropriate?
		
		ResourceLocation res = itemStack.getItem().getRegistryName();
		TextureAtlasSprite tas = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack).getParticleTexture();
		//System.out.println(Arrays.deepToString(tas.getFrameTextureData(0)));
		
		int j = 0, total = 0;
		int[] pixels = tas.getFrameTextureData(0)[0];
		for ( int i = 0; i < pixels.length; i++, j++ ){
			if ( pixels[i] != 0 ){
				total += pixels[i];
			}
			else{
				j--;
			}
		}
		/*
		try {
			pixels = TextureUtil.readImageData(Minecraft.getMinecraft().getResourceManager(), res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		return total / j;
	}
}
