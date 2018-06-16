package swordstat.util;

import net.minecraft.client.gui.FontRenderer;

public final class RenderTextUtil {
	
	private RenderTextUtil(){
		
	}
	
	public static void renderTextRight( String text, int maxWidth,
			int endX, int y, FontRenderer fontRenderer, int color ){
		
		int length = fontRenderer.getStringWidth(text);
		if ( maxWidth < length ){
			text = fontRenderer.trimStringToWidth(text, maxWidth - 5);
			length = fontRenderer.getStringWidth(text);
		}
		int renderX = endX - length;
		fontRenderer.drawString(text, renderX, y, color);
	}
}
