package swordstat.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class StringUtil {
	
	
	/* Minimum multiplier for string width */
	public static float minMultiplier = 0.7F;
	private static FontRenderer f = Minecraft.getMinecraft().fontRenderer;
	
	/**
	 * 
	 * @param string The sword data to be input onto the gui.
	 * @param pixels The maximum number of horizontal pixels the string can occupy
	 * @param minMul The smallest possible multiplier.
	 * @return
	 */
	public static float getStringMul( String string, int pixels, float minMul ) {
		
		/* 
		 * We first need to set a desired range : m <= mul <= 1
		 */
		
		float mul;
		int width = f.getStringWidth(string);
		if ( width < pixels ){
			return 1F;
		}
		else {
			mul = (((float)pixels) + 3) / ((float)width);
			return (float) Math.max(minMul, mul);
		}
	}
	
	/**
	 * 
	 * @param mobName - String to be processed.
	 * @return A string with some extra spaces and 'Entity' removed
	 */
	public static String getNeaterName( String mobName ) {
		
		StringBuffer buffer = new StringBuffer();
		for ( int i = 0; i < mobName.length(); i++ ){
			if ( Character.isUpperCase(mobName.charAt(i)) && i != 0 ){
				buffer.append(" ");
			}
			buffer.append(mobName.charAt(i));
		}
		String newStr = buffer.toString();
		// Remove "Entity" if its at the beginning of the string.
		String[] words = newStr.split(" ");
		if ( words[0].equals("Entity") ){
			StringBuffer newBuffer = new StringBuffer();
			for ( int i = 1; i < words.length; i++ ){
				newBuffer.append(words[i]);
			}
			newStr = newBuffer.toString();
		}
		return newStr;
	}
	
	/**
	 * @param date A string in from dateObj.toString() where dateObj is an 
	 * 	instance of Date.
	 *  It should be of the form: "Sat Sep 17 21:56:17 BST 2016"
	 * @return A string of the form: "Sat Sep 17 21:56:17"
	 * @throws IllegalArgumentException
	 */
	public static String getNeaterDate( String date )
		throws IllegalArgumentException {
		
		String[] elements = date.split(" ");
		if ( elements.length != 6 ){
			throw new IllegalArgumentException();
		}
		StringBuffer buffer = new StringBuffer();
		for ( int i = 0; i < 4; i++ ){
			buffer.append(elements[i] + " ");
		}
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param fl
	 * @return String of float to 1dp if there's a nonzero decimal expansion.
	 */
	public static String getTruncFloat( float fl ){
		
		String[] spltStr = Float.toString(fl).split("\\.");
	    String percentString;
	    if ( fl % 1 == 0 ){
	    	percentString = spltStr[0];
	    }
	    else {
	    	percentString = spltStr[0] + "." + spltStr[1].substring(0, 1);
	    }
	    return percentString;
	}
	
	public static int getKillColour( int kills ){
		
		if ( kills == 0 ){
			// Dark blue
			return 0x000050;
		}
		else if ( kills < 25 ){
			// Bronze
			return 0x8D7F32;
		}
		else if ( kills < 100 ){
			// Silver
			return 0xC0C0C0;
		}
		else if ( kills < 1000 ){
			// gold
			return 0xFFD700;
		}
		else if ( kills < 10000 ){
			// purple
			return 0xA020F0;
		}
		else {
			// Light blue?
			return 0x0000CF;
		}
	}
}
