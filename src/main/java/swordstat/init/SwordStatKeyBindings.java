package swordstat.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

public class SwordStatKeyBindings {


	public static KeyBinding swordMenu;
	public static KeyBinding swordMenu2;
	
	public static void init() {
		/*
		swordMenu = new KeyBinding(
				"key.swordMenu", Keyboard.KEY_Z, "key.categories.swordstat"
		);*/
		swordMenu2 = new KeyBinding("key.swordMenu2", Keyboard.KEY_X, "key.categories.swordstat");
		
	    //ClientRegistry.registerKeyBinding(swordMenu);
	    ClientRegistry.registerKeyBinding(swordMenu2);
	}
}
