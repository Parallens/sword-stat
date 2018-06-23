package swordstat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import swordstat.init.EntitySorter;
import swordstat.proxy.CommonProxy;


@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class Main {
	
	public static final String MODID = "paras_sword_stat";
	public static final String MODNAME = "Para's Sword Stat";
	public static final String VERSION = "0.1.3";
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);
	
	@Instance(MODID)
	public static Main instance = new Main();
	
	@SidedProxy(
			clientSide = "swordstat.proxy.ClientProxy",
			serverSide = "swordstat.proxy.CommonProxy"
	)
	public static CommonProxy proxy;
    	
	// Called at the beginning of the start routine. It should read the config
    // file, create blocks & items and register them in the GameRegistry.
	@EventHandler
	public void preInit( FMLPreInitializationEvent e ) {
		
		proxy.fmlLifeCycleEvent(e);
	}
	
	// The init Handler is called after the preInit Handler. In this method 
	// we can build up data structures, add Crafting Recipes and register new handler.
	@EventHandler
	public void init( FMLInitializationEvent e ) {
		
		proxy.fmlLifeCycleEvent(e);
	}
	
	// The postInit Handler is called at the very end. Its used to communicate with 
	// other mods and adjust the setup based on this.
	@EventHandler
	public void postInit( FMLPostInitializationEvent e ) {
		
		proxy.fmlLifeCycleEvent(e);
	}
}

