package swordstat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import swordstat.proxy.CommonProxy;
import swordstat.util.ServerResourceLocator;
import swordstat.util.SwordStatResourceLocator;


@Mod(modid = SwordStat.MODID,
	name = SwordStat.MODNAME,
	version = SwordStat.VERSION,
	acceptedMinecraftVersions = "[1.12.2]",
	dependencies = "after:tconstruct")
public class SwordStat {
	
	public static final String MODID = "paras_sword_stat";
	public static final String MODNAME = "Para's Sword Stat";
	public static final String VERSION = "0.6.0";
	
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	public static final ServerResourceLocator SERVER_RESOURCE_LOCATOR = 
			new ServerResourceLocator();
	public static final SwordStatResourceLocator CLIENT_RESOURCE_LOCATOR =
			new SwordStatResourceLocator();
	
	@Instance(MODID)
	public static SwordStat instance = new SwordStat();
	
	@SidedProxy(
			// Instantiated on physical client
			clientSide = "swordstat.proxy.ClientProxy",
			// Instantiated on dedicated server
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

